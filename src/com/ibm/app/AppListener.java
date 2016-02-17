package com.ibm.app;

import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ibm.cloud.workload.twa.TWSService;
import com.ibm.cloud.workload.twa.Workflow;

/**
 * Initialization and deinitialization methods.
 */
public class AppListener implements ServletContextListener {
    Workflow wk;
    
    /*
     * Activates the workflow that manages the backend of this application.
     */
    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("[AppListener] init");
        
        try {
//            // Create database table if it doesn't exist
//            Connection conn = Database.open();
//
//            try {
//                Statement stmt = conn.createStatement();
//                
//                stmt.execute( "create table if not exists twa_orders ( order_id int not null auto_increment primary key, order_status char(1), order_num_items int, order_amount int, order_description varchar(255), order_date datetime, order_phone varchar(40) );" );
//                
//                System.out.println("[AppListener] created database" );
//            }
//            finally {
//                Database.close( conn );
//            }
//            
//            // Create workflow for backend
//            wk = AppWorkflow.createWorkflow();
//            
//            TWSService tws = AppWorkflow.getTwsService();
//            
//            tws.activate( wk );
//            
//            System.out.println("[AppListener] activated workflow" );
        }
        catch( Throwable t ) {
            System.out.println("[AppListener] got exception: " + t);
            t.printStackTrace(System.out);
        }
    }

    /*
     * Deactivates the backend workflow.
     */
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        System.out.println("[AppListener] destroy");
        
/*        try {
            TWSService tws = AppWorkflow.getTwsService();
            
            tws.deactivate( wk );
            
            System.out.println("[AppListener] deactivated (stopped) workflow" );
        }
        catch( Throwable t ) {
            System.out.println("[AppListener] got exception: " + t);
            t.printStackTrace(System.out);
        }*/
    }
}
