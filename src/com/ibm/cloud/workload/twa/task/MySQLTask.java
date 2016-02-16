package com.ibm.cloud.workload.twa.task;

import org.json.JSONException;
import org.json.JSONObject;

public class MySQLTask extends TwaTask {
	private String m_host;
	private int m_port;
	private String m_database;
	private String m_user;
	private String m_password;
	private String m_sql;
	
	public MySQLTask(String host, int port, String database, String user, String password, String sql) {
		super("MySQL");
		
		m_host = host;
		m_port = port;
        m_database = database;
		m_user = user;
		m_password = password;
		m_sql = sql;
	}

	@Override
	protected JSONObject getTaskAsJson() throws JSONException {
		JSONObject o = new JSONObject();
		
		o.put("host", m_host);
		o.put("port", m_port);
		o.put("name", m_database);
		o.put("username", m_user);
		o.put("password", m_password);
		o.put("sql", m_sql);
		
		return o;
	}
}
