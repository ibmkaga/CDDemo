package com.ibm.cloud.workload.twa;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.ibm.cloud.workload.IWorkflow;

public class TWSService {
    String m_url;
    
	private TWSService(String url) {
	    m_url = url;
	}
	
    // Simple Base64 encoder/decoder
    static final String Alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    static final char [] CodeToChar = Alphabet.toCharArray();
    
    static char [] base64_encode( byte [] data ) {
        char [] result = new char [ ((data.length+2) / 3) * 4 ];
        int off_result = 0;
        int off_data = 0;
        
        while( off_data < data.length ) {
            int p = 2; // Index of final padding sequence
            int v = data[off_data++] << 16;
            if( off_data < data.length ) { v |= (data[off_data++] << 8) & 0x00FF00; p++; }
            if( off_data < data.length ) { v |= data[off_data++] & 0xFF; p++; }
            
            for( int i=0; i<4; i++ ) {
                result[ off_result++ ] = i < p ? CodeToChar[(v >> 18) & 0x3F] : '=';
                v <<= 6;
            }
        }
        
        return result;
    }
    
    private HttpURLConnection openConnection(String method) throws Exception {
        URL url = new URL(m_url); 
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();           
        conn.setRequestMethod(method);
        
        System.out.println("[TWS.activate.request] url=" + url );
        System.out.println("[TWS.activate.request] url.userInfo=" + url.getUserInfo() );
        
        if( url.getUserInfo() != null ) {
            char [] userInfoBase64 = base64_encode( url.getUserInfo().getBytes() );
            String basicAuth = "Basic " + new String(userInfoBase64);
            System.out.println("[TWS.activate.request] auth=" + basicAuth );
            conn.setRequestProperty("Authorization", basicAuth);
        }
        
        return conn;
    }
    
	public void activate( IWorkflow workflow ) throws Exception {
        HttpURLConnection conn = openConnection("POST");           
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());

        System.out.println("[TWS.activate.request] " + workflow.toJson().toString());
        
        out.write( workflow.toJson().toString() );
        out.flush();
        
        String line;
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        System.out.println("[TWS.activate.response] " + conn.getResponseCode());
        
        while( (line = in.readLine()) != null ) {
            System.out.println("[TWS.activate.response] " + line);
        }
        
        in.close();
        out.close();
        conn.disconnect();    
	}
	
	public void deactivate( IWorkflow workflow ) throws Exception {
        HttpURLConnection conn = openConnection("DELETE");
        conn.connect();
        
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
        
        System.out.println("[TWS.activate.request] " + workflow.toJson().toString());
        
        out.write( workflow.toJson().toString() );
        out.flush();
        out.close();
        
        conn.disconnect();    
    }
	
	static public TWSService getInstance(String url) {
		return new TWSService(url);
	}
}
