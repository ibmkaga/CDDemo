package com.ibm.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonObject;

import example.nosql.CloudantClientMgr;

/**
 * Simplified RESTful API to the Orders database.
 */
public class Orders extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String ORDERS="orders";
	public static String ID="order_id";
	public static String STATUS="order_status";
	public static String NUM_ITEMS="order_num_items";
	public static String AMOUNT="order_amount";
	public static String DESCRIPTION="order_description";
	public static String DATE="order_date";
	
       
    public Orders() {
        super();
    }
    
    private String getServiceUrl(){
    	String vcapApp = System.getenv().get("VCAP_APPLICATION");
    	if( vcapApp == null || vcapApp.isEmpty() ) vcapApp = "{}";
    	JSONObject app = new JSONObject(vcapApp);


    	String serviceName = "cloudantNoSQLDB";
    	String vcapJSONString = System.getenv("VCAP_SERVICES");
    	JSONObject json = new JSONObject(vcapJSONString);
    	String key;
    	JSONArray serviceArray =null;

    	for (Object k: json.keySet())
    	{
    		key = (String )k;            
    		if (key.startsWith(serviceName))
    		{
    			serviceArray = (JSONArray)json.get(key);
    			System.out.println("Cloudant service found!");
    			break;
    		}                       
    	}
    	if (serviceArray== null){
    		System.out.println("Could not connect: I was not able to find the Cloudant service!");
    		System.out.println("This is your VCAP services content");
    		System.out.println(vcapJSONString);
    		return null;
    	}

    	JSONObject twsService = (JSONObject)serviceArray.get(0); 
    	JSONObject twsServiceCreds = (JSONObject)twsService.get("credentials");
    	String serviceURL = (String) twsServiceCreds.get("url");
    	return serviceURL;
    }

    /**
     * Returns the list of current orders in JSON format.
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        
        com.cloudant.client.api.Database db = CloudantClientMgr.getDB();
		String id=ORDERS;

		HashMap<String, Object> obj = null;
		
		try{
			obj=db.find(HashMap.class, id);
		} catch (org.lightcouch.NoDocumentException e){
			
		}

		
		if (obj == null) {
			//Return null?
		} 
		
		List<Map<String, Object>> orders=(List<Map<String, Object>>) obj.get(ORDERS);
		if(orders==null){
			//Return null?
		}
		
				
        

	    try {
            JSONArray ordersArray = new JSONArray(orders);
            out.println( ordersArray.toString() );
            response.setContentType( "application/json" );
	    }
	    catch( Throwable t ) {
            out.println("got exception: " + t);
            t.printStackTrace(out);
            
            response.setStatus( 503 ); // Service unavailable
	    }
	}

	/**
	 * Adds an order to the database. Returns a JSON object containing the order id:
	 * 
	 * {
	 *     id: 42
	 * }
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int num_items = Integer.parseInt( request.getParameter( "num_items" ), 10 );
		double amount = Double.parseDouble( request.getParameter( "amount" ));
		String description = request.getParameter( "description" );

		com.cloudant.client.api.Database db = CloudantClientMgr.getDB();
		PrintWriter out = response.getWriter();
		String id=ORDERS;
		boolean alreadyPresent=false;

		// check if document exist
		HashMap<String, Object> obj = null;
		
		try{
			obj=db.find(HashMap.class, id);
		} catch (org.lightcouch.NoDocumentException e){
			
		}
		
		if (obj == null) {
			alreadyPresent=true;
			obj= new HashMap<String, Object>();
			obj.put("_id",id);
		} 
		
		List<Map<String, Object>> orders=(List<Map<String, Object>>) obj.get(ORDERS);
		if(orders==null){
			orders=new ArrayList<Map<String, Object>>();
			obj.put(ORDERS, orders);
			
		}
		
		Map<String, Object> order = new HashMap<String, Object>();
		String orderId=Long.toString(System.currentTimeMillis());
		order.put(ID, orderId);
		order.put(STATUS, "S");
		order.put(NUM_ITEMS, num_items);
		order.put(AMOUNT, amount);
		order.put(DESCRIPTION, description);
		order.put(DATE, new Date().toString());
		
		orders.add(order);
		
		if(alreadyPresent){
			db.save(obj);
		} else {
			db.update(obj);
		}
		out.println( "{ \"id\": \"" + orderId + "\" }" );
		//            
		//            response.setContentType( "application/json" );

		//        Connection conn = null;
		//
		//        try {
		//            int num_items = Integer.parseInt( request.getParameter( "num_items" ), 10 );
		//            int amount = Integer.parseInt( request.getParameter( "amount" ), 10 );
		//            String description = request.getParameter( "description" );
		//            
		//            conn = Database.open();
		//            
		//            // Insert the order
		//            StringBuffer sql = new StringBuffer("insert into twa_orders (order_status,order_num_items,order_amount,order_description,order_date) values (");
		//            
		//            sql.append( "'S'," ); // Status
		//            sql.append( num_items + "," ); // Num. items
		//            sql.append( amount + "," ); // Amount
		//            sql.append( "'" + description + "'," ); // Description
		//            sql.append( "now()" ); // Date
		//            sql.append( ");" ); 
		//                    
		//            Statement stmt = conn.createStatement();
		//            
		//            stmt.executeUpdate(sql.toString());
		//
		//            // Retrieve the order id (automatically generated)
		//            int order_id = -1;
		//            
		//            ResultSet rs = stmt.getGeneratedKeys();
		//            
		//            if( rs.next() ) {
		//                order_id = rs.getInt( 1 );
		//            }
		//            
		//            out.println( "{ \"id\": " + order_id + " }" );
		//            
		//            response.setContentType( "application/json" );
		//        }
		//        catch( Throwable t ) {
		//            out.println("got exception: " + t);
		//            t.printStackTrace(out);
		//            
		//            response.setStatus( 503 ); // Service unavailable
		//        }
		//        finally {
		//            Database.close( conn );
		//        }
	}
}
