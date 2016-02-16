package com.ibm.app;

import org.json.JSONObject;

import com.ibm.cloud.utils.Cloud;
import com.ibm.cloud.workload.twa.Schedule;
import com.ibm.cloud.workload.twa.TWSService;
import com.ibm.cloud.workload.twa.Workflow;
import com.ibm.cloud.workload.twa.task.CommandTask;
import com.ibm.cloud.workload.twa.task.MySQLTask;

/*
 * Creates a simple workflow that processes the shop orders.
 */
public class AppWorkflow {
    static Workflow createWorkflow() {
        Workflow wk = new Workflow();
        
        wk.setTrigger( Schedule.repeat(Schedule.Minutely) );
        
        wk.addTask( new MySQLTask(
            Database.getHost(),
            Database.getPort(),
            Database.getDatabase(),
            Database.getUsername(),
            Database.getPassword(),
            "update twa_orders set order_status='P' where order_status='S';" ) );
        
        wk.addTask( new MySQLTask(
            Database.getHost(),
            Database.getPort(),
            Database.getDatabase(),
            Database.getUsername(),
            Database.getPassword(),
            "select order_id,order_description from twa_orders where order_status='P';" ) );
        
        wk.addTask( new CommandTask( "c:\\cloudoe\\bin\\processorders.cmd" ) ); // TODO: remove path!
            
        wk.addTask( new MySQLTask(
            Database.getHost(),
            Database.getPort(),
            Database.getDatabase(),
            Database.getUsername(),
            Database.getPassword(),
            "update twa_orders set order_status='C' where order_status='P';" ) );
            
        return wk;
    }
    
    static TWSService getTwsService() {
        JSONObject twsService = Cloud.getServiceByName("GuitarShopScheduler");
        JSONObject twsServiceCreds = twsService.getJSONObject("credentials");
        String url = twsServiceCreds.getString("url");
        
        return TWSService.getInstance(url);    
    }
}
