package analytics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import gui.AnalyticsFrame;

public class PhoneAnalytics {
	
	private MissionOrganization mission;
	private CallList calls;
	
	public PhoneAnalytics() throws IOException {
		mission = null;
		calls = null;
		new AnalyticsFrame(this);
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
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/*
	 * This method does all the phone analysis and will write to an Excel file
	 * RETURN VALUE: boolean - true = operation succeeded, false = operation failed
	 * */
	public boolean runAnalysis() {
		if (mission == null || calls == null)
			return false;
		return true;
	}
	
	public static void main(String[] main) throws IOException, ParseException {
		
		//TODO add code to handle exceptions
		PhoneAnalytics analytics = new PhoneAnalytics();
		
		/*Iterator<String[]> iterator = calls.getOverNineMinutes();
		
		int j = 0;
		while (iterator.hasNext()) {
			String[] current = iterator.next();
			for (int i = 0; i < current.length; i++)
				System.out.print(current[i] + " ");
			System.out.println(j++);
		}*/
		
		//System.out.println(CallList.withinFiveMinutes("2011/05/31 01:00:10", "2011/05/31 01:05:10"));
	}
}
