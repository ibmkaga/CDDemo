package com.ibm.cloud.workload;

import org.json.JSONException;
import org.json.JSONObject;

public interface IJsonizable {
	public JSONObject toJson() throws JSONException;
}
