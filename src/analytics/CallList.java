package analytics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import excel.ExcelReader;

public class CallList {
	
	//this List will contain all the phone calls
	//each phone call is represented by a Map with the various pieces of data
	private List<String[]> calls;
	
	//the year is supplied by the user to avoid running out of heap space
	String year;
	
	//these numbers represent the indices that the particular data is stored in
	public static int NUM_DATA_POINTS = 4;
	public static int CALLER = 0;
	public static int RECEIVER = 1;
	public static int START = 2;
	public static int END = 3;
	private static int fiveMinutes = 300;
	private static int nineMinutes = 530;
	
	public CallList(String filePath, String year) throws IOException {
		calls = ExcelReader.getCallList(filePath);
		this.year = year;
	}
	
	/*
	 * Returns an Iterator that will iterate over all the calls
	 * */
	public Iterator<String[]> iterator() {
		return new CallIterator<String[]>(calls);
	}
	
	/*
	 * Returns an Iterator that will iterate over calls that are over five minutes long
	 * */
	public Iterator<String[]> getOverFiveMinutes() throws ParseException {
		List<String[]> overFive = new ArrayList<String[]>();
		
		//loop through the calls, adding all calls over 5 minutes to the new List
		for(int i = 0; i < calls.size(); i++) {
			String[] current = calls.get(i);
			String start = year + "/" + current[START];
			String end = year + "/" + current[END];
			if (!withinFiveMinutes(start, end))
				overFive.add(current);
		}
		
		return new CallIterator<String[]>(overFive);
	}
	
	/*
	 * Returns an Iterator that will iterate over calls that are over nine minutes long
	 * */
	public Iterator<String[]> getOverNineMinutes() throws ParseException {
		List<String[]> overNine = new ArrayList<String[]>();
		
		//loop through the calls, adding all calls over 9 minutes to the new List
		for(int i = 0; i < calls.size(); i++) {
			String[] current = calls.get(i);
			String start = year + "/" + current[START];
			String end = year + "/" + current[END];
			if (!withinNineMinutes(start, end))
				overNine.add(current);
		}
		
		return new CallIterator<String[]>(overNine);
	}
	
	/*
	 * Determines if the phone call is less than 5 minutes
	 * PARAMETER: String start = represents the call's start time (yyyy/MM/dd kk:mm:ss)
	 * PARAMETER: String end = represents the call's end time (yyyy/MM/dd kk:mm:ss)
	 * RETURN VALUE: boolean = true - less than 5 minutes, false - longer than 5 minutes
	 * */
	private boolean withinFiveMinutes(String start, String end) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
		long seconds = getDifferenceInSeconds(format.parse(start), format.parse(end));
		return (seconds <= fiveMinutes);
	}
	
	/*
	 * Determines if the phone call is less than 9 minutes
	 * PARAMETER: String start = represents the call's start time (yyyy/MM/dd kk:mm:ss)
	 * PARAMETER: String end = represents the call's end time (yyyy/MM/dd kk:mm:ss)
	 * RETURN VALUE: boolean = true - less than 9 minutes, false - longer than 9 minutes
	 * */
	private boolean withinNineMinutes(String start, String end) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
		long seconds = getDifferenceInSeconds(format.parse(start), format.parse(end));
		return (seconds <= nineMinutes);
	}
	
	/*
	 * Determines if a given phone number is a cell phone, assuming cell phone numbers are 09 numbers
	 * PARAMETER: String phone = the phone number to test
	 * RETURN VALUE: boolean = true - is a cell phone, false - is a home phone
	 * */
	public boolean isCellPhone(String phone) {
		return phone.startsWith("09");
	}
	
	/*
	 * Determines whether or not a date is a Friday (yyyy/MM/dd). This is for determining if calls were made 
	 * 	the wrong times
	 * PARAMETER: String date - the date to test
	 * RETURN VALUE: boolean = true - is Friday, false - not Friday
	 * */
	public static boolean isFriday(String date) {
		int year = Integer.parseInt(date.substring(0,4));
		int month = Integer.parseInt(date.substring(5,7));
		int day = Integer.parseInt(date.substring(8,10));
		
		Calendar calendar = new GregorianCalendar(year, month - 1, day);
		return (calendar.get(Calendar.DAY_OF_WEEK) == 6);
	}
	
	/*
	 * Returns the absolute value of the difference between two Dates in seconds
	 * PARAMETER: Date date1 = the first date to use (yyyy/MM/dd kk:mm:ss)
	 * PARAMETER: Date date2 = the second date to use (yyyy/MM/dd kk:mm:ss)
	 * RETURN VALUE: long = the time difference between the two dates in seconds
	 * */
	private static long getDifferenceInSeconds(Date date1, Date date2) {
		long milis1 = date1.getTime();
		long milis2 = date2.getTime();
		
		long diff = Math.abs(milis2 - milis1);
		
		return (diff / 1000);
	}
	
	/*
	 * Gets the total minutes of each phone number
	 * RETURN VALUE: Map of Maps containing phone number and total number of used minutes
	 * */
	public Map<String, Map<String, String>> getTotalMinutes() throws ParseException {
		Map<String, Map<String, String>> phones = new HashMap<String, Map<String, String>>();
		
		//iterate through the calls in the call list, adding up the time
		Iterator<String[]> callList = this.iterator();
		while (callList.hasNext()) {
			String[] call = callList.next();
			
			//if the phone number is already in the Map, just add on the seconds
			if (phones.containsKey(call[CallList.CALLER])) {
				Map<String, String> phone = phones.get(call[CallList.CALLER]);
				double totalSeconds = Double.parseDouble(phone.get("time"));
				
				//parse the dates and get the difference in seconds
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
				totalSeconds += getDifferenceInSeconds(format.parse(year + "/" + call[CallList.START]), format.parse(year + "/" + call[CallList.END]));
				
				//put the time back into the Map
				phone.put("time", totalSeconds + "");
			}
			//if the phone number isn't already in the Map, create a Map to represent the phone number
			else {
				Map<String, String> phone = new HashMap<String, String>();
				phone.put("phone", call[CallList.CALLER]);
				
				//parse the dates and get the difference in seconds
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
				double totalSeconds = getDifferenceInSeconds(format.parse(year + "/" + call[CallList.START]), format.parse(year + "/" + call[CallList.END]));
				
				phone.put("time", "" + totalSeconds);
				phones.put(call[CallList.CALLER], phone);
			}
		}
		
		//Iterate over all the phones and change the time value into minutes
		Iterator<String> phoneKeys = phones.keySet().iterator();
		while (phoneKeys.hasNext()) {
			Map<String, String> phone = phones.get(phoneKeys.next());
			double time = Double.parseDouble(phone.get("time"));
			time = time / 60; //convert to minutes
			phone.put("time", "" + Math.round(time));
		}
		
		return phones;
	}
	
	/*This nested class will provide an iterator for the CallList*/
	private class CallIterator<T> implements Iterator<T> {
		private List<String[]> calls;
		private int i;//keep track of current index
		
		public CallIterator(List<String[]> calls) {
			this.calls = calls;
			i = 0;
		}

		public boolean hasNext() {
			if (i < calls.size())
				return true;
			return false;
		}

		@SuppressWarnings("unchecked")
		public T next() {
			return (T) calls.get(i++);
		}
		
		/*Removing a call is an unsupported method*/
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
