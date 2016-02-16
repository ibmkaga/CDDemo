package com.ibm.cloud.workload.twa.task;

import org.json.JSONException;
import org.json.JSONObject;

public class CognosTask extends TwaTask {
	private String m_ref;
	private String m_report;
	private String [] m_args;
	
	public CognosTask(String ref, String report, String [] args) {
		super("Cognos");
		
		m_ref = ref;
		m_report = report;
		m_args = args;
	}

	@Override
	protected JSONObject getTaskAsJson() throws JSONException {
		JSONObject o = new JSONObject();
		
		o.put("ref", m_ref);
		o.put("report", m_report);
		o.put("args", m_args);
		
		return o;
	}
}
