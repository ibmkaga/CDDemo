package com.ibm.cloud.workload.twa;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.cloud.workload.ISchedule;
import com.ibm.cloud.workload.ITask;
import com.ibm.cloud.workload.IWorkflow;

public class Workflow implements IWorkflow {
	private List<ITask> m_tasks = new LinkedList<ITask>();
	private ISchedule m_schedule;
	
	public void schedule( ISchedule s ) {
		m_schedule = s;
	}
	
	public void add( ITask t ) {
		m_tasks.add( t );
	}
	
	public void setTrigger( ISchedule s ) {
		schedule( s );
	}
	
	public void addTask( ITask t ) {
		add( t );
	}

	@Override
	public JSONObject toJson() {
		JSONObject o = new JSONObject();
		JSONArray tasks = new JSONArray();
		
		try {
			for( ITask t: m_tasks ) {
				tasks.put( t.toJson() );
			}
			
			if( m_schedule != null ) {
				o.put( "schedule", m_schedule.toJson() );
			}
			
			o.put("tasks", tasks);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return o;
	}
	
	public void print() {
		String o = "?";
		
		try {
			o = toJson().toString( 4 );
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		System.out.println( "Workflow: " + o );
	}
}
