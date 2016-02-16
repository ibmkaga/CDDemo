package com.ibm.cloud.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class Cloud {
    /**
     * Looks for a specified service. 
     */
    public static JSONObject getServiceByName(String name) {
        JSONObject result = null;
        
        try {
            String vcapServices = System.getenv().get("VCAP_SERVICES");
            JSONObject services = new JSONObject(vcapServices);
            for( Object serviceType: services.keySet() ) {
                JSONArray serviceTypeInstances = services.getJSONArray((String)serviceType);
                for( int i=0; i<serviceTypeInstances.length(); i++ ) {
                    JSONObject service = serviceTypeInstances.getJSONObject( i );
                    if( name.equals(service.getString("name")) ) {
                        result = service;
                        break;
                    }
                }
            }
        }
        catch(Exception e) {
            System.out.println("Got an exception: " + e.getMessage());
            e.printStackTrace(System.out);
        }
        
        return result;
    }
}
