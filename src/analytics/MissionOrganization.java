package analytics;

import java.io.IOException;
import java.util.Map;

import excel.ExcelReader;

public class MissionOrganization {
	
	//this Map of Maps will contain all the areas in the mission
	//the key is the area's phone number, and the value is a Map of the area's data
	private Map<String, Map<String, String>> mission;
	
	MissionOrganization(String filePath) throws IOException {
		mission = ExcelReader.getMissionOrganization(filePath);
	}
	
	/*
	 * Determines whether or not the two phone numbers passed in belong to the same 
	 * Zone.
	 * PARAMETER: String phone1 - first phone number
	 * PARAMETER: String phone2 - second phone number
	 * RETURN VALUE: boolean - true = same zone, false = not same zone
	 * THROWS: IllegalArgumentException if a non missionary number is passed in
	 * */
	public boolean isSameZone(String phone1, String phone2) {
		if (!mission.containsKey(phone1) || !mission.containsKey(phone2)) {
			throw new IllegalArgumentException("One of the phone numbers passed into isSameZone is not a missionary number.");
		}
		
		String zone1 = mission.get(phone1).get("zone");
		String zone2 = mission.get(phone2).get("zone");
		
		return zone1.equals(zone2);
	}
	
	/*
	 * Determines whether or not the phone number passed in is a missionary phone number
	 * PARAMETER: String phone - the phone number to test
	 * RETURN VALUE: boolean - true = missionary number, false = not missionary number
	 * */
	public boolean isMissionaryNumber(String phone) {
		return mission.containsKey(phone);
	}
	
	/*
	 * Returns the companionship type of the companionship associated with the phone number
	 * passed into the method
	 * PARAMETER: String phone - the phone number of the companionship
	 * RETURN VALUE: String - the companionship type of the companionship
	 * THROWS: IllegalArgumentException if a non missionary number is passed in
	 * */
	public String getCompanionshipType(String phone) {
		if (!mission.containsKey(phone)) 
			throw new IllegalArgumentException("The number passed into getCompanionshipType is not a missionary number.");
		return mission.get(phone).get("type");
	}
	
	/*
	 * Returns a String representing the area
	 * PARAMETER: String phone - the phone number of the companionship
	 * RETURN VALUE: String - a String with basic information about the companionship
	 * THROWS: IllegalArgumentException if a non missionary number is passed in
	 * */
	public String getAreaString(String phone) {
		if (!mission.containsKey(phone))
			throw new IllegalArgumentException("The number passed into getAreaString is not a missionary number.");
		
		Map<String, String> area = mission.get(phone);
		return area.get("area") + ": " + area.get("missionaries");
	}

}
