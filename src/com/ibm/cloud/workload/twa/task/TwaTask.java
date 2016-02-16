package com.ibm.cloud.workload.twa.task;

import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.cloud.workload.ITask;

public abstract class TwaTask implements ITask {
	private String m_type;
	
	TwaTask( String type ) {
		m_type = type;
	}
	
	protected abstract JSONObject getTaskAsJson() throws JSONException;

	@Override
	public JSONObject toJson() throws JSONException {
		JSONObject o = new JSONObject();
		
		try {
			o.put("type", m_type);
			o.put("task", getTaskAsJson());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return o;
	}
}
