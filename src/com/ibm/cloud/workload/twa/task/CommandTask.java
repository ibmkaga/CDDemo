package com.ibm.cloud.workload.twa.task;

import org.json.JSONException;
import org.json.JSONObject;

public class CommandTask extends TwaTask {
	private String m_command;
	
	public CommandTask( String command ) {
	    super("command");
		m_command = command;
	}

    @Override
    protected JSONObject getTaskAsJson() throws JSONException {
        JSONObject o = new JSONObject();
        
        o.put("command", m_command);
        
        return o;
    }
}
