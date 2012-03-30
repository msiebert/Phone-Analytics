package excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import analytics.CallList;

/*
 * This class simply contains a static method that will read the Mission Organization or Call List out of 
 * an Excel file
 * */

public class ExcelReader {
		
	/*This method reads the file MissionOrganization.xls and gets the mission's organization
	 * from the file.
	 * PARAMTER: String filePath = String containing the file path to the Excel file
	 * RETURN VALUE: Set = a set of sets which represent zones holding maps which represent areas
	 * */
	public static Map<String, Map<String, String>> getMissionOrganization(String filePath) throws IOException {
		if (filePath == null) 
			throw new IllegalArgumentException();
		
		//initialize the Map which will contain the mission
		Map<String, Map<String, String>> mission = new HashMap<String, Map<String, String>>(200);
		
		Iterator<Row> rows = getRows(filePath);
			
		//loop through the rows of the work sheet
		rows.next();//skip the title row
		while (rows.hasNext()) {
			Row row = rows.next();

			//get an Iterator to iterate over the cells of the row
			Iterator<Cell> cells = row.cellIterator();

			//create the Map that will contain the area's data
			Map<String, String> area = new HashMap<String, String>();

			//get all the data out of the row and put it into a Map
			area.put("zone", cells.next().getStringCellValue());
			area.put("area", cells.next().getStringCellValue());
			area.put("missionaries", cells.next().getStringCellValue());
			area.put("phone", cells.next().getStringCellValue().replaceAll("-", ""));
			area.put("type", cells.next().getStringCellValue());

			mission.put(area.get("phone"), area);
		}

		//remove the entry that's there because of the blank rows in the file
		mission.remove("");
			
		return mission;
	}
	
	/*
	 * Returns the Call List represented as a List of Map containing call data as read from an Excel file
	 * PARAMETER: String filePath = the location of the call list Excel file
	 * RETURN VALUE: List<Map<String, String>> = Each Map within the List will represent an individual
	 * 	call, and each piece of data is represented by a String
	 * */
	public static List<String[]> getCallList(String filePath) throws IOException {
		if (filePath == null)
			throw new IllegalArgumentException();
		
		//initialize the List that will represent the call list
		List<String[]> calls = new ArrayList<String[]>();
		
		Iterator<Row> rows = getRows(filePath);
		
		//loop through the rows of the work sheet
		rows.next();//skip the title row
		while (rows.hasNext()) {
			Row row = rows.next();

			//get an Iterator to iterate over the cells of the row
			Iterator<Cell> cells = row.cellIterator();
			
			//create the Map that will contain the call data
			String[] call = new String[CallList.NUM_DATA_POINTS];
			
			call[CallList.CALLER] = cells.next().getStringCellValue().trim();
			
			//get the date of the call (omit year and month to make the string smaller for memory)
			String date = cells.next().getStringCellValue();
			date = date.substring(5);
			
			//add the starting time
			String startTime = cells.next().getStringCellValue();
			startTime = startTime.substring(0,2) + ":" + startTime.substring(2,4) + ":" + startTime.substring(4);
			call[CallList.START] = date + " " + startTime;
			
			//if there's nothing in the next cell, it's an internet charge, skip it
			if (!cells.hasNext())
				continue;
			
			//add the ending time
			String endTime = cells.next().getStringCellValue();
			endTime = endTime.substring(0,2) + ":" + endTime.substring(2,4) + ":" + endTime.substring(4);
			call[CallList.END] = date + " " + endTime;
			
			//add the receiver's number
			call[CallList.RECEIVER] = cells.next().getStringCellValue();
						
			calls.add(call);
		}
		
		return calls;
	}
	
	private static Iterator<Row> getRows(String filePath) throws IOException {
		Iterator<Row> rows = null;
		
		//try to create an InputString from the mission organization Excel file
		InputStream stream = new FileInputStream(filePath);

		//try to read the Excel file
		POIFSFileSystem fileSystem = null;
		
		fileSystem = new POIFSFileSystem(stream);

		//get an Iterator to iterate over the rows of the workbook
		HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
		HSSFSheet sheet = workbook.getSheetAt(0);
		rows = sheet.rowIterator();
		
		return rows;
	}
	
}
