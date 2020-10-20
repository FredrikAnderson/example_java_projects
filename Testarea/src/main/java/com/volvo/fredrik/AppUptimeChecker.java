package com.volvo.fredrik;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.swing.JOptionPane;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Helper servlet to show current log levels and apply new.
 */
public class AppUptimeChecker {


	private static List<String> urlList = new ArrayList<>();

	private static String fullStatus = "";
	private static boolean urlStatus = true;

	private static boolean running = true;

	static TrayIcon trayIcon = null;

	public static void main(String[] args) {

		addUrls();

		try {

			SystemTray tray = SystemTray.getSystemTray();
			trayIcon = new TrayIcon(getStatusImage());

			PopupMenu popup = new PopupMenu();
			MenuItem infoMi = new MenuItem("Info");
			infoMi.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(null, fullStatus);
				}
			});
			popup.add(infoMi);
			MenuItem exitMi = new MenuItem("Exit");
			exitMi.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					running = false;
					System.exit(0);
				}
			});
			popup.add(exitMi);
			trayIcon.setPopupMenu(popup);
			tray.add(trayIcon);

			while (running) {

//				trayIcon.displayMessage("Checking uptime", "Time to check uptime for URLs", MessageType.INFO);
				String checkStatusUp = checkStatusUp();

				trayIcon.setImage(getStatusImage());
				trayIcon.setToolTip(checkStatusUp);

				Thread.sleep(300 * 1000); // 5 min
			}

		} catch (AWTException e) {
			e.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	private static void addUrls() {

		String urlFile = "urls_to_check.txt";
		try (BufferedReader br = new BufferedReader(new FileReader(urlFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				// process the line.
				if (!line.startsWith("//") && !line.trim().isEmpty()) {
					urlList.add(line);					
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Image getStatusImage() {

		Color statusColor = Color.GREEN;
		if (!urlStatus) {
			statusColor = Color.RED;
		}

		// Create the image
		BufferedImage bi = new BufferedImage(40, 40, ColorSpace.TYPE_RGB);
		Graphics2D graphics = bi.createGraphics();

		// Fill the background with gray color
		graphics.setBackground(statusColor);
		graphics.setColor(statusColor);
		graphics.fillRect(0, 0, bi.getWidth(), bi.getHeight());

		return bi;
	}

	private static String checkStatusUp() {
		StringBuilder buf = new StringBuilder();
		boolean roundStatus = true;
		fullStatus = "" + LocalDateTime.now() + ": \n";
		info("Checking URLs at: " + LocalDateTime.now());
		
		for (String url : urlList) {

			boolean currentUrl = true;
			Integer status = null;
			try {
				status = requestURL(url);
				info("URL: " + url + " got " + status);
				
				if (status != 200) {
					currentUrl = false;
				}

				if (!currentUrl) {
					buf.append(url + ": " + status + "\n");
					roundStatus = false;
				}
			} catch (IOException e) {
				e.printStackTrace();

				roundStatus = false;
			} finally {
				fullStatus += url + ": " + status + "\n";

			}
		}

		urlStatus = roundStatus;

		String string = buf.toString();
		if (string.isEmpty()) {
			string = "Everything OK.\n";
		}
		return string;
	}

	private static int requestURL(String url) throws IOException {

		int toret = 0;

//        info("Requesting HTTP get: " + url);

		CloseableHttpClient httpclient = null;
		CloseableHttpResponse getResponse = null;
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true)
					.build();

			// CloseableHttpClient client = HttpClients.custom()
			// .setSSLContext(sslContext)
			// .setSSLHostnameVerifier(new NoopHostnameVerifier())
			// .build();

			// CloseableHttpClient httpclient = HttpClients.createDefault();

			httpclient = HttpClients.custom().setSSLContext(sslContext)
					.setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

			HttpGet httpGet = new HttpGet(url);

//            info(httpGet.toString());

			getResponse = httpclient.execute(httpGet);
			// The underlying HTTP connection is still held by the response object
			// to allow the response content to be streamed directly from the network
			// socket.
			// In order to ensure correct deallocation of system resources
			// the user MUST call CloseableHttpResponse#close() from a finally clause.
			// Please note that if response content is not fully consumed the underlying
			// connection cannot be safely re-used and will be shut down and discarded
			// by the connection manager.

			toret = getResponse.getStatusLine().getStatusCode();

//            Header contentTypeHeader = getResponse.getFirstHeader("Content-Type");
//            info("HTTP response, statusLine: " + getResponse.getStatusLine() + ", " + contentTypeHeader);
//            HttpEntity entity1 = getResponse.getEntity();
//
//            // Headers
//            // Header[] headers = postResponse.getAllHeaders();
//            // for (Header h : headers) {
//            // info("addHeader: " + h.getName() + " - " + h.getValue());
//            // }
//            String entityContents = EntityUtils.toString(getResponse.getEntity());
//            ObjectMapper jsonmapper = new ObjectMapper();
//            JsonNode json = jsonmapper.readTree(entityContents);
//            info(json.get("Version-Number").asText());
//
//            // info("Content: " + entityContents);
//            // markup.append(entityContents);
//
//            // do something useful with the response body
//            // and ensure it is fully consumed
//            EntityUtils.consume(entity1);

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} finally {
			getResponse.close();
			httpclient.close();
		}

		return toret;
	}

	private static void info(String string) {

		System.out.println("INFO: " + string);
	}

	private Object toHTML(Map<String, String> logLevelsMap) {
		StringBuilder sb = new StringBuilder();

		sb.append("<table>");
		for (String keyStr : logLevelsMap.keySet()) {
			sb.append("<tr>");
			sb.append("<td>" + keyStr + "</td>");
			sb.append("<td>" + logLevelsMap.get(keyStr) + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");

		return sb.toString();
	}

	private void printApplyLogLevelForm(StringBuilder markup) {
		// Apply log level form
		markup.append("<h3>Apply new loglevel for a logger:</h3>");
		markup.append("<form method='post' action='logging'><table><tr>");
		markup.append("<td>Username:</td> <td><input type='text' name='username' id='usernameId' size='40'/></td>");
		markup.append("</tr><tr>");
		markup.append("<td>Log level:</td> <td><input type='text' name='password' id='passwordId' size='40'/></td>");
		markup.append("</tr></table>");
		markup.append("<button type='submit'>Apply loglevel</button></form>");
	}

	private void printCurrentDateAndTime(StringBuilder markup) {
		// Current date and time
		markup.append("<br>");

		Date today = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh.mm.ss");
		String dateAndTimeWithFormat = formatter.format(today);

		markup.append(dateAndTimeWithFormat);
	}

}
