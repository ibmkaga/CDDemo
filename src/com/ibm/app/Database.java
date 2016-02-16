package com.ibm.app;

import java.sql.Connection;
import java.sql.DriverManager;

import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.cloud.utils.Cloud;

/**
 * Connects to the database service. 
 */
public class Database {
    static String m_jdbcURL = null;
    static String m_jdbcHost = null;
    static int m_jdbcPort = 0;
    static String m_jdbcDatabase = null;
    static String m_jdbcUsername = null;
    static String m_jdbcPassword = null;
    
    static void log( String msg ) {
        System.out.println("[DB] " + msg);
    }
    
//    static private void init() throws JSONException, ClassNotFoundException {
//        if( m_jdbcURL == null ) {
//            log( "init()" );
//            
//            // Get MySQL service
//            JSONObject jsonMySql = Cloud.getServiceByName( "GuitarShopDatabase" );
//            JSONObject jsonMySqlCredentials = jsonMySql.getJSONObject( "credentials" );
//            
//            log( "Name: " + jsonMySql.getString( "name" ) );
//            log( "Host: " + jsonMySqlCredentials.getString("host") + ":" + jsonMySqlCredentials.getInt( "port" ) );
//            log( "User: " + jsonMySqlCredentials.getString("user") );
//            log( "Auth: " + jsonMySqlCredentials.getString("username") + " / " + jsonMySqlCredentials.getString("password" ) );
//            
//            // Establish JDBC connection
//            String jdbcConnURL = "jdbc:mysql://" + 
//                jsonMySqlCredentials.getString("host") + 
//                ":" +
//                jsonMySqlCredentials.getInt( "port" ) +
//                "/" +
//                jsonMySqlCredentials.getString( "name" );
//            
//            log( "JDBC URL: " + jdbcConnURL );
//
//            m_jdbcURL = jdbcConnURL;
//            m_jdbcHost = jsonMySqlCredentials.getString("host");
//            m_jdbcPort = jsonMySqlCredentials.getInt( "port" );
//            m_jdbcDatabase = jsonMySqlCredentials.getString( "name" );
//            m_jdbcUsername = jsonMySqlCredentials.getString("username");
//            m_jdbcPassword = jsonMySqlCredentials.getString("password" );
//            
//            Class.forName("org.mariadb.jdbc.Driver");
//        }
//}
    
//    static private void safeInit() {
//        try {
//            init();
//        }
//        catch( Throwable t ) {
//            log("got exception: " + t);
//            t.printStackTrace(System.out);
//        }
//    }
    
    static public Connection open() {
        log( "open()" );
//        
        Connection conn = null;
//        
//        try {
//            init();
//            
//            conn = DriverManager.getConnection(m_jdbcURL, m_jdbcUsername, m_jdbcPassword);
//        }
//        catch( Throwable t ) {
//            log("got exception: " + t);
//            t.printStackTrace(System.out);
//        }
        
        return conn;
    }
    
    static public void close( Connection conn ) {
        log( "close()" );
//        
//        try {
//            if( conn != null ) {
//                conn.close();
//            }
//        }
//        catch( Throwable t ) {
//            log("got exception: " + t);
//            t.printStackTrace(System.out);
//        }
    }
    
    static public String getHost() {
//        safeInit();
        return m_jdbcHost;
    }
    
    static public int getPort() {
//        safeInit();
        return m_jdbcPort;
    }
    
    static public String getDatabase() {
//        safeInit();
        return m_jdbcDatabase;
    }
    
    static public String getUsername() {
//        safeInit();
        return m_jdbcUsername;
    }
    
    static public String getPassword() {
//        safeInit();
        return m_jdbcPassword;
    }
}
