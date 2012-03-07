package excel;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/*
 * This class simply contains a static method that will read the Mission Organization out of 
 * an Excel file
 * */

public class ExcelReader {
	
	/*This method reads the file MissionOrganization.xls and gets the mission's organization
	 * from the file.
	 * PARAMTER: String filePath = String containing the file path to the Excel file
	 * RETURN VALUE: Set = a set of sets which represent zones holding maps which represent areas
	 * */
	public static Map<String, Map<String, String>> getMissionOrganization(String filePath) {
		if (filePath == null) 
			throw new IllegalArgumentException();
		
		//initialize the Map which will contain the mission
		Map<String, Map<String, String>> mission = null;
		
		//try to create an InputString from the mission organization Excel file
		InputStream stream =  null;
		try {
			stream = new FileInputStream(filePath);
		}
		catch(FileNotFoundException e) {
			System.out.println("Error: Unable to find MissionOrganization.xls");
			e.printStackTrace();
		}
		
		//try to read the Excel file
		POIFSFileSystem fileSystem = null;
		try {
			fileSystem = new POIFSFileSystem(stream);
			
			//get an Iterator to iterate over the rows of the workbook
			HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
			HSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rows = sheet.rowIterator();
			
			mission = new HashMap<String, Map<String, String>>(200);
			
			//loop through the rows of the worksheet
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
				area.put("phone", cells.next().getStringCellValue());
				area.put("type", cells.next().getStringCellValue());
				
				mission.put(area.get("phone"), area);
			}
			
			//remove the entry that's there because of the blank rows in the file
			mission.remove("");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return mission;
	}
	
}
