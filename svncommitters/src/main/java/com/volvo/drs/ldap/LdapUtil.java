package com.volvo.drs.ldap;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class LdapUtil {


    public PersonInfo getUserInfo(String userName, 
            String userNameForLdapReq, String passwdForLdapReq) {
        
        PersonInfo toret = null;
        LdapUtil ldapUtil = new LdapUtil();
        
        LdapContext ldapContext = ldapUtil.getLdapContext(userNameForLdapReq, passwdForLdapReq);
        NamingEnumeration userBasicAttributes = ldapUtil.getUserBasicAttributes(userName, ldapContext);
        toret = ldapUtil.convertToPersonInfo(userBasicAttributes);
        
        return toret;
    }    
    
    public static void main(String args[]) {
        LdapUtil ldapUtil = new LdapUtil();
        
        PersonInfo userInfo = ldapUtil.getUserInfo("yt52878", "yt52878", "IawDRS45");        
        System.out.println("Person found: " + userInfo);
        
//        LdapContext ldapContext = ldapUtil.getLdapContext("yt52878", "IawDRS45");
//        NamingEnumeration userBasicAttributes = ldapUtil.getUserBasicAttributes("yt52878", ldapContext);
//        ldapUtil.displayLdapInfo(userBasicAttributes);
    }
    
    public LdapContext getLdapContext(String userNameReq, String passwdReq) {
        LdapContext ctx = null;
        try {
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.SECURITY_AUTHENTICATION, "Simple");
            // OU=IT,DC=vcn,DC=ds,DC=volvo,DC=net
//            env.put(Context.SECURITY_PRINCIPAL, "CN=cs-ws-s-CONDOR-TEST,OU=Service Accounts,OU=WINSRV,OU=CS,DC=vcn,DC=ds,DC=volvo,DC=net");
//            env.put(Context.SECURITY_PRINCIPAL, "CN=yt52878,OU=GOT,OU=Users,OU=VolvoGroup,DC=vcn,DC=ds,DC=volvo,DC=net");
            env.put(Context.SECURITY_PRINCIPAL, "VCN\\" + userNameReq);
            env.put(Context.SECURITY_CREDENTIALS, passwdReq);
            env.put(Context.PROVIDER_URL, "ldap://vcn.ds.volvo.net:389");
            ctx = new InitialLdapContext(env, null);
//            System.out.println("Connection Successful.");
            
        } catch (NamingException nex) {
            System.out.println("LDAP Connection: FAILED");
            nex.printStackTrace();
        }
//        System.out.println("Connected to AD: " + ctx.toString());
        return ctx;
    }

    public NamingEnumeration getUserBasicAttributes(String username, LdapContext ctx) {
        NamingEnumeration answer = null;
        try {
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String[] attrIDs = {"distinguishedName", "sn", "givenname", "mail", "telephonenumber"};
            constraints.setReturningAttributes(attrIDs);
            answer = ctx.search("DC=vcn,DC=ds,DC=volvo,DC=net", "cn=" + username, constraints);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return answer;
    }

    public PersonInfo convertToPersonInfo(NamingEnumeration answer) {
        PersonInfo toret = new PersonInfo();
        
        try {
            if (answer.hasMore()) {
                Attributes attrs = ((SearchResult) answer.next()).getAttributes();
                if (attrs.get("distinguishedName").get() != null) {
                    toret.setDistinguishedName(attrs.get("distinguishedName").get().toString());
                }
                if (attrs.get("givenname").get() != null) {
                    toret.setFirstName(attrs.get("givenname").get().toString());
                }
                if (attrs.get("sn").get() != null) {
                    toret.setLastName(attrs.get("sn").get().toString());
                }
                if (attrs.get("mail") != null && attrs.get("mail").get() != null) {
                    toret.setEmail(attrs.get("mail").get().toString());
                }
                if (attrs.get("telephonenumber") != null && attrs.get("telephonenumber").get() != null) {
                    toret.setTelephoneNumber(attrs.get("telephonenumber").get().toString());
                }
                
            } else {
                System.out.println("Invalid User");
            }
        } catch (NamingException ex) {
            System.out.println("LDAP search failed. LdapUtil error: " + ex.toString());
            return null;
        }
        return toret;
    }

    public void displayLdapInfo(NamingEnumeration answer) {
        try {
            if (answer.hasMore()) {
                Attributes attrs = ((SearchResult) answer.next()).getAttributes();
                System.out.println(attrs.get("distinguishedName"));
                System.out.println(attrs.get("givenname"));
                System.out.println(attrs.get("sn"));
                System.out.println(attrs.get("mail"));
                System.out.println(attrs.get("telephonenumber"));
            } else {
                System.out.println("Invalid User");
            }
        } catch (NamingException ex) {
            System.out.println("GenericLdapUtilTest error" + ex.toString());
        }

    }
}
