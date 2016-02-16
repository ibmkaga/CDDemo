package com.ibm.cloud.workload.twa.task;

import org.json.JSONException;
import org.json.JSONObject;

public class HadoopTask extends TwaTask {
	private String m_host;
	private String m_user;
	private String m_password;
	private String m_jar;
	private String [] m_args;
	
	public HadoopTask(String host, String user, String password, String jar, String [] args ) {
		super("Hadoop");
		
		m_host = host;
		m_user = user;
		m_password = password;
		m_jar = jar;
		m_args = args;
	}

	@Override
	protected JSONObject getTaskAsJson() throws JSONException {
		JSONObject o = new JSONObject();
		
		o.put("host", m_host);
		o.put("username", m_user);
		o.put("password", m_password);
		o.put("jar", m_jar);
		o.put("args", m_args);
		
		return o;
	}
}
