package com.volvo.drs.svncommitters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.volvo.drs.ldap.LdapUtil;
import com.volvo.drs.ldap.PersonInfo;

public class SvnCommitters {

    private static boolean debug = false;

    public static void main(String[] args) {
        Set<String> committersList = null;
        if (args.length > 1 && !args[0].equals("")) {
            committersList = readCommittersList(args[0]);
//            printCommitters(committersList);
        }
        if (args[1] == null && args[2] == null) {
            printOut("No LDAP request user info given as argument 2 (VCN userid) and 3 (VCN user passwd). ");
        }
        createGitAuthorsFile(committersList, args[1], args[2]);
        
    }

    private static void createGitAuthorsFile(Set<String> committersList, 
            String userNameForLdapReq, String passwdForLdapReq) {
        
        LdapUtil ldapUtil = new LdapUtil();
        String gitAuthorsFileName = "git_authors.txt";
        try {
            PrintWriter writer = new PrintWriter(gitAuthorsFileName, "UTF-8");
            
            for (String vcnId : committersList) {
                
                PersonInfo userInfo = null;
                if (userNameForLdapReq != null) {
                    printOut("Looking up user in LDAP: " + vcnId);
                    userInfo = ldapUtil.getUserInfo(vcnId, userNameForLdapReq, passwdForLdapReq);
                }
                if (userInfo != null) {
                    writer.println(vcnId + " = " + userInfo.getFirstName() + " " + userInfo.getLastName() 
                                   + " <" + userInfo.getEmail() + ">");
                } else {
                    writer.println(vcnId + " = " + vcnId + " <" + vcnId + "@volvo.com>");
                }
            }
            writer.close();            
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }
        printOut("Wrote Git authors file: " + System.getProperty("user.dir") + File.separator + gitAuthorsFileName);
    }

    private static void printCommitters(Set<String> committersList) {
        
        for (String userId : committersList) {
            System.out.println("Committer: " + userId);
        }        

        System.out.println("Size of set: " + committersList.size());
    }

    private static Set<String> readCommittersList(String fileName) {
        Set<String> userIds = new HashSet<String>();
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            printOut("Current dir: " + System.getProperty("user.dir") + ". Reading SVN log, file: " + fileName);
            
            String line;
            while ((line = br.readLine()) != null) {
                debug(line);
                
                // -------
                if (line.startsWith("------")) {
                    continue;
                }
                int fstLine = line.indexOf("|");
                fstLine++;
                int sndLine = line.indexOf("|", fstLine);
                String string = line.substring(fstLine, sndLine);
                String userId = string.trim();

                debug("User ID: " + userId); 
                userIds.add(userId);

                final Pattern pattern = Pattern.compile("\\|(.+?)\\|");
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
//                    String userId = matcher.group(1);
//                    System.out.println("User ID: " + userId); 
                    userIds.add(userId);
                }
            }
            br.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        printOut("Found " + userIds.size() + " unique committers.");
        
        return userIds;
    }

    public static String test_script() {
        String toret = "";
        String execDir = "C:\\DRS_Java\\svn\\JVS4\\trunk\\";

        String cmd = "C:\\Program Files\\TortoiseSVN\\bin\\svn log -q";
        String arg1 = "";
        String arg2 = "";

        try {
            File f = new File(execDir);
            boolean directory = f.isDirectory();
            System.out.println("Dir: " + directory);

            String line;
            ProcessBuilder pb = new ProcessBuilder(cmd);
            Map<String, String> env = pb.environment();
            env.put("VAR1", arg1);
            // env.put("VAR2", arg2);
            // pb.directory(new File(execDir));
            pb.redirectErrorStream(true);
            Process p = pb.start();
            p.waitFor();
            BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while ((line = bri.readLine()) != null) {
                toret += line;
            }

            System.out.println("Done. " + toret);

        } catch (Exception err) {
            err.printStackTrace();
        }
        return toret;
    }

    private static void printOut(String string) {
        System.out.println(string);
    }
    
    private static void debug(String string) {
        if (debug ) {
            System.out.println(string);
        }
    }

}
