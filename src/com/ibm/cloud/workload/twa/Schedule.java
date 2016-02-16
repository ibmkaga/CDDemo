package com.ibm.cloud.workload.twa;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.cloud.workload.IJsonizable;
import com.ibm.cloud.workload.ISchedule;

public class Schedule implements IJsonizable, ISchedule {
    public static final int Minutely = 60; // Seconds in a minute
	public static final int Hourly = 60*60; // Seconds in a hour
	public static final int Daily = Hourly*24; // Seconds in a day
	public static final int Weekly = Daily*7; // Seconds in a week
	
	public static final int Monday = 1 << 0;
	public static final int Tuesday = 1 << 1;
	public static final int Wednesday = 1 << 2;
	public static final int Thursday = 1 << 3;
	public static final int Friday = 1 << 4;
	public static final int Saturday = 1 << 5;
	public static final int Sunday = 1 << 6;
	public static final int Workday = Monday + Tuesday + Wednesday + Thursday + Friday;
	public static final int Weekend = Saturday + Sunday;
	public static final int Day = Workday + Weekend;
	
	private interface Entry extends IJsonizable {
	}
	
	private class ScheduleOn implements Entry {
		private Date m_date;
		
		ScheduleOn( Date d ) {
			m_date = d;
		}

		@Override
		public JSONObject toJson() throws JSONException {
			JSONObject o = entryToJson( "on" );
			o.put( "date", dateToJson(m_date) );
			return o;
		}
	}
	
	private class ScheduleRepeat implements Entry {
		private Date m_from;
		private Date m_to;
		private int m_interval; // Seconds
		
		ScheduleRepeat( Date from, Date to, int interval ) {
			m_from = from;
			m_to = to;
			m_interval = interval;
		}

		@Override
		public JSONObject toJson() throws JSONException {
			JSONObject o = entryToJson( "repeat" );
			o.put( "from", dateToJson(m_from) );
			o.put( "to", dateToJson(m_to) );
			o.put( "interval", m_interval );
			return o;
		}
	}
	
	private class ScheduleEvery implements Entry {
		private int m_day;
		private int m_at_hh;
		private int m_at_mm;
		
		ScheduleEvery( int day, int hh, int mm ) {
			m_day = day;
			m_at_hh = hh;
			m_at_mm = mm;
		}

		@Override
		public JSONObject toJson() throws JSONException {
			JSONObject o = entryToJson( "every" );
			o.put( "unit", "day" );
			o.put( "mask", m_day );
			o.put( "at_hh", m_at_hh );
			o.put( "at_mm", m_at_mm );
			return o;
		}
	}
	
	private LinkedList<Entry> m_items = new LinkedList<Entry> ();
	
	public Schedule() {
	}

	public Schedule( String s ) {
	}

	@Override
	public JSONObject toJson() throws JSONException {
		JSONObject o = new JSONObject();
		JSONArray items = new JSONArray();
		
		for( Entry e: m_items ) {
			items.put( e.toJson() );
		}
		
		try {
			o.put("items", items);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return o;
	}

	private Schedule add(Entry e) {
		m_items.add( e );
		
		return this;
	}
	
	public static Schedule on(int y, int m, int d, int hh, int mm) {
		Schedule s = new Schedule();
		s.addDate( y, m, d, hh, mm );
		return s;
	}

    public static Schedule repeat( int interval ) {
        Schedule s = new Schedule();
        s.addRepeat( null, null, interval );
        return s;
    }

	public static Schedule repeat( Date from, Date to, int interval ) {
		Schedule s = new Schedule();
		s.addRepeat( from, to, interval );
		return s;
	}

	public static Schedule every( int day, int hh, int mm ) {
		Schedule s = new Schedule();
		s.addEvery( day, hh, mm );
		return s;
	}
	
	public void addDate(int y, int m, int d, int hh, int mm) {
		Calendar c = new GregorianCalendar();
		
		c.clear();
		c.set(Calendar.YEAR, y);
		c.set(Calendar.MONTH, m);
		c.set(Calendar.DATE, d);
		c.set(Calendar.HOUR, hh);
		c.set(Calendar.MINUTE, mm);
		c.set(Calendar.SECOND, 0);
		c.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		add( new ScheduleOn( c.getTime() ) );
	}
	
	public void addRepeat( Date from, Date to, int interval ) {
		add( new ScheduleRepeat( from, to, interval ) );
	}

	public void addRepeat( Date from, int count, int interval ) {
		long to = from.getTime();
		for( int i=0; i<count; i++ ) {
			to += 1000 * interval;
		}
		add( new ScheduleRepeat( from, new Date(to), interval ) );
	}

	public void addEvery( int day, int hh, int mm ) {
		add( new ScheduleEvery( day, hh, mm ) );
	}

	static private JSONObject entryToJson( String type ) {
		JSONObject o = new JSONObject();
		
		try {
			o.put( "type", type );
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return o;
	}

	static public JSONObject dateToJson( Date d ) {
	    if( d != null ) {
    		Calendar c = new GregorianCalendar();
    		c.setTimeZone( TimeZone.getTimeZone("UTC") );
    		c.setTime( d );
    		return calendarToJson( c );
	    }
	    
	    return null;
	}
	
	static public JSONObject calendarToJson( Calendar c ) {
		JSONObject o = new JSONObject();
		
		try {
			// Note: we must call isSet() before getting the other fields, otherwise it will always return true (how broken is that?!)
			if( c.isSet(Calendar.HOUR) || c.isSet(Calendar.MINUTE) || c.isSet(Calendar.SECOND) ) {
				o.put( "hh", c.get( Calendar.HOUR ) );
				o.put( "mm", c.get( Calendar.MINUTE ) );
				o.put( "ss", c.get( Calendar.SECOND ) );
			}
			
			o.put( "y", c.get( Calendar.YEAR ) );
			o.put( "m", c.get( Calendar.MONTH ) );
			o.put( "d", c.get( Calendar.DATE ) );
			
			if( c.getTimeZone() != null ) {
				o.put( "tz", c.getTimeZone().getID() );
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return o;
	}
}

	