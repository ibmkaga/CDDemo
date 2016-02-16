<%--
    Homepage with dev information.  
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="org.json.*" %>
<%@ page import="com.ibm.cloud.utils.*" %>

<%
    String vcapApp = System.getenv().get("VCAP_APPLICATION");
    if( vcapApp == null || vcapApp.isEmpty() ) vcapApp = "{}";
    JSONObject app = new JSONObject(vcapApp);
    
    String requestURL = request.getRequestURL().toString();
    
//     JSONObject twsService = Cloud.getServiceByName("GuitarShopScheduler");
//    JSONObject twsServiceCreds = twsService != null ? twsService.getJSONObject("credentials") : new JSONObject("{'url':'N/A'}");
	String workloadServiceName = "WorkloadScheduler";
 	String vcapJSONString = System.getenv("VCAP_SERVICES");
    //Object jsonObject = JSON.parse(vcapJSONString);    
    //JSONObject json = (JSONObject)jsonObject;
    JSONObject json = new JSONObject(vcapJSONString);
    String key;
    JSONArray twaServiceArray =null;
         
    System.out.println("Looking for Workload Automation Service...");


   for (Object k: json.keySet())
   {
       key = (String )k;            
       if (key.startsWith(workloadServiceName))
       {
        	twaServiceArray = (JSONArray)json.get(key);
            System.out.println("Workload Automation service found!");
            break;
       }                       
   }
      if (twaServiceArray== null){
      	   System.out.println("Could not connect: I was not able to find the Workload Automation service!");
      	   System.out.println("This is your VCAP services content");
           System.out.println(vcapJSONString);
          return;
       }
         
       JSONObject twsService = (JSONObject)twaServiceArray.get(0); 
       JSONObject twsServiceCreds = (JSONObject)twsService.get("credentials");
       String twsURL = (String) twsServiceCreds.get("url");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Hello</title>
</head>
<body>

<p>
<a href="<%= requestURL %>html/demo/shop.html">Online beer shop</a>

<p>
<a href="<%= requestURL %>html/demo/orders.html">Check order status</a>

<p>
<a href="<%= requestURL %>Orders">GET /Orders</a>

<h2>Application</h2>

Name: <%= app.getString("name") %><br/>
Host: <%= app.getString("host") %><br/>
Port: <%= app.getInt("port") %><br/>
<p>
Request URL: <%= requestURL %>

<h2>Scheduling service (TWS)</h2>
URL: <%= twsServiceCreds.getString("url") %><br/>
<p>

<h2>Environment variables</h2>

<%  
    Map<String,String> env = System.getenv();
    
    List<Map.Entry<String,String>> entries = new ArrayList<Map.Entry<String,String>>( env.entrySet() );
    
    Collections.sort( entries, new Comparator<Map.Entry<String,String>>() {
        public int compare( Map.Entry<String,String> e1, Map.Entry<String,String> e2 ) {
            return e1.getKey().compareTo( e2.getKey() );
        }
    } );
    
    for( Map.Entry e: entries ) {
%>
    <%= e.getKey() %> = <%= e.getValue() %><br/>
<%  
    }
%>

</body>
</html>
