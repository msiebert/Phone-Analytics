package analytics;

public class PhoneAnalytics {
	
	private static MissionOrganization mission;
	
	public static void main(String[] main) {
		mission = new MissionOrganization("MissionOrganization.xls");
		
		System.out.println("0972-377-672: " + mission.getAreaString("0972-377-672")); 
		System.out.println("0972-263-507: " + mission.getAreaString("0972-263-507")); 
		//System.out.println("0972-263-666: " + mission.getAreaString("0972-263-666"));
		System.out.println("0972-377-683: " + mission.getAreaString("0972-377-683"));
		
		//System.out.println("0972-377-672 and 0972-263-500: " + mission.isSameZone("0972-377-672", "0972-263-500"));
		//System.out.println("0972-377-672 and 0972-263-507: " + mission.isSameZone("0972-377-672", "0972-263-507"));		
	}
}
