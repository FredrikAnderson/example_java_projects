package com.volvo.fredrik;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import javax.net.ssl.SSLContext;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.MQTopic;
import com.ibm.mq.constants.CMQC;

import jcifs.CIFSContext;
import jcifs.CIFSException;
import jcifs.CloseableIterator;
import jcifs.SmbResource;
import jcifs.context.SingletonContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;

public class TestingArea {

	public static void main(String[] args) {
		cmdLineArgsToStdout(args);

		if (args.length == 1 && args[0].toLowerCase().equals("toldm")) {
			publishMessageToMqTopic();
		}

		if (args.length >= 1 && args[0].toLowerCase().equals("ebngwq")) {
			publishMessageToMqTopicToEBNGWQ();
		}

		try {
			doTokenExchange();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//    	convertSQL();

//    	printJavaProperties(args);        

//    	replaceUnicodeZeros();

//    	trySmbAccess();

//    	compareDates();

//        compareIntLists();

//        compareLists();

//        comparePersonLists();

//        Long long1 = new Long(2);
//        Long long2 = new Long(2);
//
//        if (long1 == long2) {
//            System.out.println("long1 and long2 ==");
//        }
//        if (long1.equals(long2)) {
//            System.out.println("long1 and long2 equals");
//        }
//

//        generateInsertStmt(args);

//        try {
//
//            testPingFederateAuth();
////            testHttpPostWithBasicAuth();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        printJavaProperties(args);        
	}

	private static void doTokenExchange() throws IOException {

        // info("Requesting version using HTTP get: " + httpGet.toString());

        CloseableHttpClient httpclient = null;
        CloseableHttpResponse postResponse = null;
        try {
            SSLContext sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, (certificate, authType) -> true).build();

            // CloseableHttpClient client = HttpClients.custom()
            // .setSSLContext(sslContext)
            // .setSSLHostnameVerifier(new NoopHostnameVerifier())
            // .build();

            // CloseableHttpClient httpclient = HttpClients.createDefault();

             httpclient = HttpClients.custom()
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

            HttpPost httpPost = new HttpPost("https://wice-qa.srv.volvo.com/m2m/v1/auth/login");
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Accept", "application/vnd.se.alkit.wice.authservice-v2+json");
            
//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("login", "cs-ws-s-ldm-devops"));
//            params.add(new BasicNameValuePair("password", "@MkGqkW7S6~T(d^H"));
//            httpPost.setEntity(new UrlEncodedFormEntity(params));

            httpPost.setEntity(new StringEntity("{ \"login\": \"cs-ws-s-ldm-devops\", \"password\": \"@MkGqkW7S6~T(d^H\" }"));

            info(httpPost.toString());
            
            postResponse = httpclient.execute(httpPost);
            // The underlying HTTP connection is still held by the response object
            // to allow the response content to be streamed directly from the network socket.
            // In order to ensure correct deallocation of system resources
            // the user MUST call CloseableHttpResponse#close() from a finally clause.
            // Please note that if response content is not fully consumed the underlying
            // connection cannot be safely re-used and will be shut down and discarded
            // by the connection manager.

            int toret = postResponse.getStatusLine().getStatusCode();
            Header contentTypeHeader = postResponse.getFirstHeader("Content-Type");
            info("HTTP response, statusLine: " + postResponse.getStatusLine() + ", " + contentTypeHeader);
            HttpEntity entity1 = postResponse.getEntity();

            // Headers
            // Header[] headers = postResponse.getAllHeaders();
            // for (Header h : headers) {
            // info("addHeader: " + h.getName() + " - " + h.getValue());
            // }
            String entityContents = EntityUtils.toString(postResponse.getEntity());
//            ObjectMapper jsonmapper = new ObjectMapper();
//            JsonNode json = jsonmapper.readTree(entityContents);
//            info(json.get("Version-Number").asText());

             info("Content: " + entityContents);
            // markup.append(entityContents);

            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity1);
            
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
            postResponse.close();
            httpclient.close();
        }

		
	}

	private static void publishMessageToMqTopicToEBNGWQ() {

		MQQueueManager _qMgr = null;
		MQTopic publisher = null;
		MQMessage mqMsg = null;
		String qMgrName = "EBNGWQ";
		String topicString = "WICE/FA-TEST";
		String topicObject = "LDM.WICE.ROOT.TOPIC";
		String mqHostname = "ebngwq.srv.volvo.com";
		int mqPort = 1417;
		String srvChannel = "LDM.SRV01Q";
		String mqUser = "cs-ws-s-WICE";
		String mqPass = "9J777&&Ia&999J8";

		try {
			System.setProperty("javax.net.ssl.keyStore", "C:\\Dev\\RDM\\Admin\\WICE\\key.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "TB050");
			System.setProperty("javax.net.ssl.trustStore", "C:\\Dev\\RDM\\Admin\\WICE\\key.jks");
			System.setProperty("javax.net.ssl.trustStorePassword", "TB050");

			System.setProperty("com.ibm.mq.cfg.useIBMCipherMappings", "false");

			MQEnvironment.hostname = mqHostname;
			MQEnvironment.port = mqPort;
			MQEnvironment.channel = srvChannel;
			MQEnvironment.userID = mqUser;
			MQEnvironment.password = mqPass;
			MQEnvironment.sslCipherSuite = "TLS_RSA_WITH_AES_256_CBC_SHA256";

			_qMgr = new MQQueueManager(qMgrName);
			log("connected to queue manager: " + qMgrName);

			int openOutputOptions = CMQC.MQOO_OUTPUT + CMQC.MQOO_FAIL_IF_QUIESCING;
			publisher = _qMgr.accessTopic(topicString, topicObject, CMQC.MQTOPIC_OPEN_AS_PUBLICATION,
					openOutputOptions);
			log("opened topic: " + publisher.getName());

			String line = "This is a test message from WICE.";
			mqMsg = new MQMessage();
			mqMsg.messageId = CMQC.MQMI_NONE;
			mqMsg.correlationId = CMQC.MQCI_NONE;
			mqMsg.writeString(line);
			mqMsg.setStringProperty("SenderId", "WICE"); // SenderID shall always be WICE from your end
			mqMsg.setStringProperty("MsgType", "ebngwq-dtc"); // MsgType, could de some of: dtc, dm1, area5, mlog, iclog, a7log
														// etc
			mqMsg.setStringProperty("Guid", "123-456-78"); // Guid, unique id. Could be generated from your end, using
															// Java could do: UUID uuid = UUID.randomUUID();
			mqMsg.setStringProperty("TcpXRefKey", ""); // If you have a related TCPx ref key shuold be added here

			MQPutMessageOptions pmo = new MQPutMessageOptions();
			publisher.put(mqMsg, pmo);

			log("message published: " + line);

		} catch (MQException e) {
			log("MQException CC=" + e.completionCode + " : RC=" + e.reasonCode);
			e.printStackTrace();
		} catch (IOException e) {
			log("IOException " + e.getLocalizedMessage());
		} finally {
			try {
				if (publisher != null)
					publisher.close();
			} catch (MQException e) {
				log("MQException CC=" + e.completionCode + " : RC=" + e.reasonCode);
			}

			try {
				if (_qMgr != null)
					_qMgr.disconnect();
			} catch (MQException e) {
				log("MQException CC=" + e.completionCode + " : RC=" + e.reasonCode);
			}
		}

	}

	private static void cmdLineArgsToStdout(String[] args) {

		System.out.println("Command-line arguments:");
		for (String arg : args) {
			System.out.println(arg);
		}

	}

	private static void publishMessageToMqTopic() {

		MQQueueManager _qMgr = null;
		MQTopic publisher = null;
		MQMessage mqMsg = null;
		String qMgrName = "GIMLI_A4";
		String topicString = "FA-TEST";
		String topicObject = "LDM.WICE.ROOT.TOPIC";
		String mqHostname = "gimli-a4.it.volvo.net";
		int mqPort = 1443;
		String srvChannel = "LDM.SRV01";
		String mqUser = "cs-ws-s-WICE";
		String mqPass = "9J777&&Ia&999J8";

		try {
			File inFile = new File("C:\\Dev\\RDM\\Workarea\\toLDM\\in.json");
			if (inFile.exists()) {
				String jsonInput = Files.readString(inFile.toPath());

				MQEnvironment.hostname = mqHostname;
				MQEnvironment.port = mqPort;
				MQEnvironment.channel = srvChannel;
				MQEnvironment.userID = mqUser;
				MQEnvironment.password = mqPass;

				_qMgr = new MQQueueManager(qMgrName);
				log("connected to queue manager: " + qMgrName);

				int openOutputOptions = CMQC.MQOO_OUTPUT + CMQC.MQOO_FAIL_IF_QUIESCING;
				publisher = _qMgr.accessTopic(topicString, topicObject, CMQC.MQTOPIC_OPEN_AS_PUBLICATION,
						openOutputOptions);
				log("opened topic: " + publisher.getName());

				String line = "This is a test message from WICE.";
				mqMsg = new MQMessage();
				mqMsg.messageId = CMQC.MQMI_NONE;
				mqMsg.correlationId = CMQC.MQCI_NONE;
				mqMsg.writeString(line);
				mqMsg.setStringProperty("SenderId", "WICE"); // SenderID shall always be WICE from your end
				mqMsg.setStringProperty("MsgType", "dtc"); // MsgType, could de some of: dtc, dm1, area5, mlog, iclog,
															// a7log etc
				mqMsg.setStringProperty("Guid", "123-456-78"); // Guid, unique id. Could be generated from your end,
																// using Java could do: UUID uuid = UUID.randomUUID();
				mqMsg.setStringProperty("TcpXRefKey", ""); // If you have a related TCPx ref key shuold be added here

				MQPutMessageOptions pmo = new MQPutMessageOptions();
				publisher.put(mqMsg, pmo);

				log("message published: " + line);

			} else {

				System.out.println("No input file for LDM found: " + inFile.getName());
			}

		} catch (MQException e) {
			log("MQException CC=" + e.completionCode + " : RC=" + e.reasonCode);
		} catch (IOException e) {
			log("IOException " + e.getLocalizedMessage());
		} finally {
			try {
				if (publisher != null)
					publisher.close();
			} catch (MQException e) {
				log("MQException CC=" + e.completionCode + " : RC=" + e.reasonCode);
			}

			try {
				if (_qMgr != null)
					_qMgr.disconnect();
			} catch (MQException e) {
				log("MQException CC=" + e.completionCode + " : RC=" + e.reasonCode);
			}
		}
	}

	private static void log(String string) {
		System.out.println(string);
	}

	private static void convertSQL() {

		File inFile = new File("C:\\Dev\\RDM\\Workarea\\tmp\\dispatcher_test.sql");
		File toFile = new File("C:\\Dev\\RDM\\Workarea\\tmp\\dispatcher_conv.sql");

		try {
			List<String> allLines = Files.readAllLines(inFile.toPath());
			List<String> toLines = new ArrayList<>();

			for (String string : allLines) {
				String toStr = string;

				int xStart = string.indexOf(", X");
//				if (string.contains(", X'")) {
				if (xStart != -1) {
//					System.out.println("Got line: " + string);
					int commaStop = string.indexOf(", ", xStart + 3);

					// replace ', X' => ', hex('
					// replace ', ' => ', 'hex'),'

					toStr = string.substring(0, commaStop) + ", 'hex'), " + string.substring(commaStop + 2);

					toStr = toStr.replace(", X", ", decode(");
					System.out.println("To line: " + toStr);
				}

				toLines.add(toStr);
			}

			Files.write(toFile.toPath(), toLines, StandardOpenOption.CREATE_NEW, StandardOpenOption.APPEND);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void replaceUnicodeZeros() {

		String str = "abc\\u0000\\u0000\\u0000defafterwards\\\\u0000\\\\u0000";

		System.out.println("String is" + str);

		String got = str.replace("\\u0000", "");

		System.out.println("Got: " + got);

	}

	private static void compareDates() {

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime twoHoursAgo = LocalDateTime.now().minusHours(2);
		LocalDateTime twoHoursInFuture = LocalDateTime.now().plusHours(2);

		List<LocalDateTime> list = Arrays.asList(now, twoHoursAgo, twoHoursInFuture);

		list.sort(new Comparator<LocalDateTime>() {

			@Override
			public int compare(LocalDateTime o1, LocalDateTime o2) {
				return o1.compareTo(o2);
			}

		});

		System.out.println("Sorted list: " + list);

		Collections.sort(list, Collections.reverseOrder());

		System.out.println("Reversed list: " + list);

	}

	private static void trySmbAccess() {

		NtlmPasswordAuthenticator ntlmPasswordAuthenticator = new NtlmPasswordAuthenticator("VCN", "cs-ws-s-RDMADM",
				"L%&&9&K788Jc-9H");
		CIFSContext context = SingletonContext.getInstance().withCredentials(ntlmPasswordAuthenticator);
		SmbResource resource;
		String dir = "vashare1.ess.volvo.net/va_confined1/P2745_system_verification/Vehicle_Logs/";
		try {
			resource = context.get("smb://" + dir);
			boolean canRead = resource.canRead();
			System.out.println("Read access to: " + dir + ", " + canRead);

			CloseableIterator<SmbResource> children = resource.children();

			children.forEachRemaining(child -> System.out.println("child:" + child.getName()));

			SmbFile smbFile = new SmbFile("smb://" + dir, context);

			CloseableIterator<SmbResource> children2 = smbFile.children();
			children2.forEachRemaining(child -> System.out.println("child:" + child.getName()));

		} catch (CIFSException | MalformedURLException e) {
			e.printStackTrace();
		}

	}

	private static void compareIntLists() {

		ArrayList<Integer> list1 = new ArrayList<Integer>();

		// Add values in ArrayList
		list1.add(new Integer(32));
		list1.add(new Integer(33));
		list1.add(new Integer(35));

		// print list 1
		System.out.println("List1: " + list1);

		// Create ArrayList list2
		ArrayList<Integer> list2 = new ArrayList<Integer>();
		// Add values in ArrayList
		list2.add(new Integer(44));
		list2.add(new Integer(33));
		list2.add(new Integer(35));

		// Create ArrayList list3
		ArrayList<Integer> list3 = new ArrayList<Integer>();
		// Add values in ArrayList
		list3.add(new Integer(32));
		list3.add(new Integer(33));
		list3.add(new Integer(35));

//        list2.get(1).setAge(44);
////        list2.get(2).setLastName("Pettersson");
//        list2.get(2).setFirstName("Fredda");

		// print list 2
		System.out.println("List2: " + list2);

		System.out.println("List1 and list2 same: " + Arrays.deepEquals(list1.toArray(), list2.toArray()));

		System.out.println("List1 and list3 same: " + Arrays.deepEquals(list1.toArray(), list3.toArray()));

		list3.add(new Integer(3));

		System.out.println("List1 and list3 same: " + Arrays.deepEquals(list1.toArray(), list3.toArray()));

	}

	private static void comparePersonLists() {

		// create ArrayList list1
		ArrayList<Person> list1 = new ArrayList<Person>();

		// Add values in ArrayList
		list1.add(new Person("Fred", "And", 32));
		list1.add(new Person("Petter", "Nilson", 33));
		list1.add(new Person("Fredrik", "Anderson", 35));

		// print list 1
		System.out.println("List1: " + list1);

		// Create ArrayList list2
		ArrayList<Person> list2 = new ArrayList<Person>();

		// Add values in ArrayList
		list2.add(new Person("Fred", "And", 44));
		list2.add(new Person("Petter", "Nilson", 33));
		list2.add(new Person("Fredda", "Anderson", 35));

//           list2.get(1).setAge(44);
////           list2.get(2).setLastName("Pettersson");
//           list2.get(2).setFirstName("Fredda");

		// print list 2
		System.out.println("List2: " + list2);

		System.out.println("List1: " + list1);

//           ArrayList<Person> common = new ArrayList<>();
//           common.addAll(list1);
//           
//           // Find the common elements 
//           common.retainAll(list2); 
//           // print list 1 
//           System.out.println("Common elements: " + common); 

//           // Now we should have only unique items from list1, ie elements that was removed from list2
//           list1.removeAll(common);
//           System.out.println("Removed elements: " + list1); 
//
//           // Now we should have only unique items from list2, ie elements that was added to list2
//           list2.removeAll(common);
//           System.out.println("Added elements: " + list2); 
//

//           Collections
//           Predicate<Person> equals = 
//           System.out.println("Age over 33:");
//           List<Person> rest = common.stream().filter(item -> item.getAge() >= 33).collect(Collectors.toList());
//           rest.forEach(System.out::println);

		PersonFirstEquator equator = new PersonFirstEquator();
		Collection<Person> retainAll = CollectionUtils.retainAll(list1, list2, Person.FIRSTNAME_EQUATOR);

		System.out.println("Equator:");
		retainAll.forEach(System.out::println);

	}

	private static void compareLists() {

		// create ArrayList list1
		ArrayList<String> list1 = new ArrayList<String>();

		// Add values in ArrayList
		list1.add("Hii");
		list1.add("Geeks");
		list1.add("for");
		list1.add("Geeks");

		// print list 1
		System.out.println("List1: " + list1);

		// Create ArrayList list2
		ArrayList<String> list2 = new ArrayList<String>();

		// Add values in ArrayList
		list2.add("Hii");
		list2.add("Geeks");
		list2.add("Gaurav");

		// print list 2
		System.out.println("List2: " + list2);

		ArrayList<String> common = new ArrayList<>();
		common.addAll(list1);

		// Find the common elements
		common.retainAll(list2);

		// print list 1
		System.out.println("Common elements: " + common);

		// Now we should have only unique items from list1, ie elements that was removed
		// from list2
		list1.removeAll(common);

		System.out.println("List1 elements: " + list1);

		// Now we should have only unique items from list2, ie elements that was added
		// to list2
		list2.removeAll(common);

		System.out.println("List2 elements: " + list2);

	}

	private static void generateInsertStmt(String[] args) {

		try {
			File file = new File("C:\\temp\\input.txt");
			Scanner scanner = new Scanner(file);
			boolean firstLine = true;
			String[] columns = new String[20];
			ArrayList<String> valueLines = new ArrayList<>();
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
//                System.out.println("Line: " + line);
				if (firstLine) {
					columns = line.split("\t");
					firstLine = false;
				} else {
					valueLines.add(line);
				}
			}
			scanner.close();

			int value = 0;
			for (String str : valueLines) {
				value++;
				System.out.println("<insert tableName=\"" + args[0] + "\">");
				String[] values = str.split("\t");
				int column = 0;
				System.out.println("   <column name=\"id\" value=\"" + value + "\"/>");
				System.out.println("   <column name=\"description\" value=\"\"/>");
				for (String string : values) {
					System.out
							.println("   <column name=\"" + columns[column] + "\" value=\"" + forXml(string) + "\"/>");
					column++;
				}
				System.out.println("</insert>");

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private static String forXml(String string) {
		return string.replaceAll("&", "&amp;");
	}

	private static void testPingFederateAuth() throws ClientProtocolException, IOException {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(
				"https://federate-qa.volvo.com/as/authorization.oauth2?client_id=tm-uiservice&response_type=token&redirect_uri=https://localhost");
		// Basic Auth - tm-uiservice
		//
//        String auth = "tm-uiservice:secret";
//        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.ISO_8859_1));
//        String authHeader = "Basic " + new String(encodedAuth);
//        httpGet.setHeader(HttpHeaders.AUTHORIZATION, authHeader);

		httpGet.addHeader("Upgrade-Insecure-Requests", "1");
		httpGet.addHeader("Connection", "keep-alive");
		httpGet.addHeader("Pragma", "no-cache");
		httpGet.addHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		httpGet.addHeader("Accept-Encoding", "gzip, deflate, br");
		httpGet.addHeader("Accept-Language", "en-US,en;q=0.9");
		httpGet.addHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");

		info("HTTP Get: " + httpGet.toString());

		CloseableHttpResponse getResponse = httpclient.execute(httpGet);
		// The underlying HTTP connection is still held by the response object
		// to allow the response content to be streamed directly from the network
		// socket.
		// In order to ensure correct deallocation of system resources
		// the user MUST call CloseableHttpResponse#close() from a finally clause.
		// Please note that if response content is not fully consumed the underlying
		// connection cannot be safely re-used and will be shut down and discarded
		// by the connection manager.
		try {
			info("StatusLine: " + getResponse.getStatusLine());
			HttpEntity entity1 = getResponse.getEntity();

			// Statusode
			info("" + getResponse.getStatusLine().getStatusCode());

			// Headers
			Header[] headers = getResponse.getAllHeaders();
			for (Header h : headers) {
//                LOGGER.info("Header:" + h.getName() + ": " + h.getValue());
				info("addHeader: " + h.getName() + " - " + h.getValue());
			}

			String entityContents = EntityUtils.toString(getResponse.getEntity());

//            InputStream inputStream = entity1.getContent();
//            BufferedReader buf = new BufferedInputStream(inputStream)

			info("Content: " + entityContents);

			// do something useful with the response body
			// and ensure it is fully consumed
			EntityUtils.consume(entity1);
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			getResponse.close();
		}
	}

	private static void testHttpPostWithBasicAuth() throws ClientProtocolException, IOException {

		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("tm-uiservice", "secret");
		provider.setCredentials(AuthScope.ANY, credentials);

//        CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(
				"http://localhost:8090/oauth/token?grant_type=password&username=testleader&password=testleader");
		// Basic Auth - tm-uiservice
		//
		String auth = "tm-uiservice:secret";
		String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.ISO_8859_1));
		String authHeader = "Basic " + new String(encodedAuth);
		httpPost.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
//        httpPost.addHeader("accept", "*/*");
//        httpPost.addHeader("host", "localhost:8090");
//        httpPost.addHeader("cache-control", "no-cache");

		Header[] headers = httpPost.getAllHeaders();
		for (Header h : headers) {
//            LOGGER.info("Header:" + h.getName() + ": " + h.getValue());
			info("addHeader: " + h.getName() + " - " + h.getValue());
		}

//        String encoding = Base64.getEncoder().encodeToString("tm-uiservice:secret".getBytes());
//        httpPost.addHeader("Authorization", "Basic " + encoding);
//        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");

		info("HTTP Post: " + httpPost.toString());

		CloseableHttpResponse postResponse = httpclient.execute(httpPost);
		// The underlying HTTP connection is still held by the response object
		// to allow the response content to be streamed directly from the network
		// socket.
		// In order to ensure correct deallocation of system resources
		// the user MUST call CloseableHttpResponse#close() from a finally clause.
		// Please note that if response content is not fully consumed the underlying
		// connection cannot be safely re-used and will be shut down and discarded
		// by the connection manager.
		try {
			info("StatusLine: " + postResponse.getStatusLine());
			HttpEntity entity1 = postResponse.getEntity();

			// Statusode
			info("" + postResponse.getStatusLine().getStatusCode());

			// Headers
			headers = postResponse.getAllHeaders();
			for (Header h : headers) {
//                LOGGER.info("Header:" + h.getName() + ": " + h.getValue());
				info("addHeader: " + h.getName() + " - " + h.getValue());
			}

//            // Content-type and length
//            response.setContentType(response1.getEntity().getContentType().getValue());
//            response.setContentLength((int) response1.getEntity().getContentLength());
//            
//            // Content
//            IOUtils.copy(entity1.getContent(), response.getOutputStream());

			String entityContents = EntityUtils.toString(postResponse.getEntity());

//            InputStream inputStream = entity1.getContent();
//            BufferedReader buf = new BufferedInputStream(inputStream)

			info("Content: " + entityContents);

			// do something useful with the response body
			// and ensure it is fully consumed
			EntityUtils.consume(entity1);
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			postResponse.close();
		}
	}

	private static void info(String string) {
		System.out.println(string);
	}

	private static void printJavaProperties(String[] args) {
		System.out.println("Command-line arguments:");
		for (String arg : args) {
			System.out.println(arg);
		}

		Properties p = System.getProperties();
		Enumeration keys = p.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value = (String) p.get(key);
			System.out.println(key + ": " + value);
		}
	}

}
