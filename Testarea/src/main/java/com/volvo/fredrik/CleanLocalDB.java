package com.volvo.fredrik;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Properties;

public class CleanLocalDB {

    public static void main(String[] args) {

        try {
            // create our mysql database connection
            String myDriver = "org.postgresql.Driver";
            String myUrl = "jdbc:postgresql://127.0.0.1:5432/postgres";
            
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, "postgres", "postgrespw");

            // our SQL SELECT query.
            // if you only need a few columns, specify them by name instead of using "*"
            String query = "DROP schema public CASCADE; CREATE SCHEMA public;";

            // create the java statement
            Statement st = conn.createStatement();

            // execute the query, and get a java resultset
            boolean execute = st.execute(query);

            System.out.println("Local Postgres DB with URL: " + myUrl + " has been cleaned.");
//            System.out.println("ResultSet: " + execute);
            
//            // iterate through the java resultset
//            while (rs.next()) {
//                System.out.println("" + rs.getString(1));
//                
//            }
            
            st.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    
    private static void info(String string) {
        System.out.println(string);
    }

}
