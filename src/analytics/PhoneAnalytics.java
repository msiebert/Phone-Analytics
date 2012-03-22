package analytics;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import gui.AnalyticsFrame;

public class PhoneAnalytics {
	
	private MissionOrganization mission;
	private CallList calls;
	private AnalyticsFrame gui;
	
	public PhoneAnalytics() throws IOException {
		mission = null;
		calls = null;
		gui = new AnalyticsFrame(this);
	}
	
	/*
	 * This method initializes the mission organization from the file passed in
	 * PARAMETER: String fileName= the file to use
	 * RETURN VALUE: boolean - true = operation succeeded, false = operation failed
	 * */
	public boolean initOrganization(String fileName) {
		try {
			mission = new MissionOrganization(fileName);
		}
		catch (IOException e) {
			gui.setError("Error: Something went wrong loading the mission organization file. Make sure " +
					"you have the right file or check the log file to see the cause of the problem.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/*
	 * This method initializes the call list from the file passed in
	 * PARAMETER: String fileName = the file to use
	 * PARAMETER: String year = the year that the data in the file is from
	 * RETURN VALUE: boolean - true = the operation succeeded, false = operation failed
	 * */
	public boolean initCallList(String fileName, String year) {
		try {
			calls = new CallList(fileName, year);
		}
		catch(IOException e) {
			gui.setError("Error: Something went wrong loading the call list file. Make sure " +
					"you have the right file or check the log file to see the cause of the problem.");
			e.printStackTrace();
			return false;
		}
		catch (OutOfMemoryError e) {
			gui.setError("Error: Ran out of memory trying to load the call list.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/*
	 * This method does all the phone analysis and will write to an Excel file
	 * RETURN VALUE: boolean - true = operation succeeded, false = operation failed
	 * */
	public boolean runAnalysis() throws ParseException {
		if (mission == null || calls == null)
			return false;
		
		//System.out.println(checkNightCalls());
		//System.out.println(checkFiveMinuteCalls());
		//System.out.println(checkNineMinuteCalls());
		
		List<Map<String, String>> map = this.checkOutOfZone();
		for (int i = 0; i < map.size(); i++) {
			System.out.println(map.get(i));
		}
		return true;
	}
	
	/*
	 * Checks for calls between 10:30pm and 6:30 am
	 * RETURN VALUE: List of Maps containing information about violators
	 * */
	private Map<String, Map<String, String>> checkNightCalls() {
		//create a List to contain the violators
		Map<String, Map<String, String>> violators = new HashMap<String, Map<String, String>>();
				
		//loop through the calls in the call list
		Iterator<String[]> callList = calls.iterator();
		while (callList.hasNext()) {
			String[] call = callList.next();
			
			//get the start and end times for the call
			String start = call[CallList.START].substring(6, 11);
			String end = call[CallList.END].substring(6, 11);
			
			//check that the start time wasn't too early or the end time wasn't too late and that the caller wasn't a special number
			if ((start.compareTo("06:30") < 0 || end.compareTo("22:30") > 0) && !mission.isSpecialNumber(call[CallList.CALLER])) {
				//only add a new Map if there isn't already one for the companionship
				if (!violators.containsKey(call[CallList.CALLER])) {
					Map<String, String> violator = new HashMap<String, String>();
					violator.put("missionaries", mission.getAreaString(call[CallList.CALLER]));
					violator.put("count", "1");
					violators.put(call[CallList.CALLER], violator);
				}
				else {
					Map<String, String> violator = violators.get(call[CallList.CALLER]);
					int count = Integer.parseInt(violator.get("count")) + 1;
					violator.put("count", count + "");
				}
			}
		}
		
		return violators;
	}
	
	/*
	 * Checks for calls to investigators that are longer than 5 minutes
	 * RETURN VALUE: Map of Maps containing information about violators
	 * */
	private Map<String, Map<String, String>> checkFiveMinuteCalls() throws ParseException {
		//create a List to contain the violators
		Map<String, Map<String, String>> violators = new HashMap<String, Map<String, String>>();
		
		//get the list of calls over 5 minutes
		Iterator<String[]> callList = calls.getOverFiveMinutes();
		while (callList.hasNext()) {
			String[] call = callList.next();
			//if it's a missionary number, they have up to 9 minutes and if the caller or receiver is a special number, no limit
			if (!mission.isMissionaryNumber(call[CallList.RECEIVER]) && !(mission.isSpecialNumber(call[CallList.CALLER]) || mission.isSpecialNumber(call[CallList.RECEIVER]))) {
				//only add a new Map if there isn't already one for the companionship
				if (!violators.containsKey(call[CallList.CALLER])) {
					Map<String, String> violator = new HashMap<String, String>();
					violator.put("missionaries", mission.getAreaString(call[CallList.CALLER]));
					violator.put("count", "1");
					violators.put(call[CallList.CALLER], violator);
				}
				else {
					Map<String, String> violator = violators.get(call[CallList.CALLER]);
					int count = Integer.parseInt(violator.get("count")) + 1;
					violator.put("count", count + "");
				}
			}
		}
		return violators;
	}
	
	/*
	 * Checks for calls to other missionaries that are over 9 minutes
	 * RETURN VALUE: Map of Maps containing information about violators
	 * */
	private Map<String, Map<String, String>> checkNineMinuteCalls() throws ParseException {
		//create a List to contain the violators
		Map<String, Map<String, String>> violators = new HashMap<String, Map<String, String>>();

		//get the list of calls over 9 minutes
		Iterator<String[]> callList = calls.getOverNineMinutes();
		while (callList.hasNext()) {
			String[] call = callList.next();
			//make sure the caller or receiver is not a special number or a non missionary (which is caught in over 5 minutes)
			if (mission.isMissionaryNumber(call[CallList.RECEIVER]) && !(mission.isSpecialNumber(call[CallList.CALLER]) || mission.isSpecialNumber(call[CallList.RECEIVER]))) {
				//only add a new Map if there isn't already one for the companionship
				if (!violators.containsKey(call[CallList.CALLER])) {
					Map<String, String> violator = new HashMap<String, String>();
					violator.put("missionaries", mission.getAreaString(call[CallList.CALLER]));
					violator.put("count", "1");
					violators.put(call[CallList.CALLER], violator);
				}
				//if the number's already in there, just add one to the counter on the companionship
				else {
					Map<String, String> violator = violators.get(call[CallList.CALLER]);
					int count = Integer.parseInt(violator.get("count")) + 1;
					violator.put("count", count + "");
				}
			}
		}
		return violators;
	}
	
	/*
	 * Check for calls where missionaries call missionaries outside of their zone
	 * RETURN VALUE: List of Maps containing information about violations
	 * */
	private List<Map<String, String>> checkOutOfZone() {
		List<Map<String, String>> violators = new ArrayList<Map<String, String>>();
		
		//loop through all the phone calls looking for calls outside of the zone
		Iterator<String[]> callList = calls.iterator();
		while (callList.hasNext()) {
			String[] call = callList.next();
			
			//only do the check if the receiver is a missionary or the caller or receiver wasn't special number
			if (mission.isMissionaryNumber(call[CallList.RECEIVER]) && !(mission.isSpecialNumber(call[CallList.CALLER]) || mission.isSpecialNumber(call[CallList.RECEIVER]))) {
				if (!mission.isSameZone(call[CallList.CALLER], call[CallList.RECEIVER])) {
					Map<String, String> violator = new HashMap<String, String>();
					
					//add the data to the violator
					violator.put("caller", mission.getAreaString(call[CallList.CALLER]));
					violator.put("receiver", mission.getAreaString(call[CallList.RECEIVER]));
					
					violators.add(violator);
				}
			}
		}
		return violators;
	}
	
	public static void main(String[] main) throws IOException, ParseException {
		
		//TODO add code to handle exceptions
		PhoneAnalytics analytics = new PhoneAnalytics();
	}
}
