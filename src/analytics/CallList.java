package analytics;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import excel.ExcelReader;

public class CallList {
	
	//this List will contain all the phone calls
	//each phone call is represented by a Map with the various pieces of data
	private List<String[]> calls;
	
	//these numbers represent the indices that the particular data is stored in
	public static int NUM_DATA_POINTS = 4;
	public static int CALLER = 0;
	public static int RECEIVER = 1;
	public static int START = 2;
	public static int END = 3;
	
	public CallList(String filePath) throws IOException {
		calls = ExcelReader.getCallList(filePath);
	}
}
