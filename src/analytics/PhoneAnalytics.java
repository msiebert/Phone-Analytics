package analytics;

import excel.ExcelReader;

public class PhoneAnalytics {
	public static void main(String[] main) {
		ExcelReader.getMissionOrganization("MissionOrganization.xls");
		System.out.println("Done");
	}
}
