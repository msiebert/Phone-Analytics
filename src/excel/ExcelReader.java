package excel;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
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
	public static Set getMissionOrganization(String filePath) {
		if (filePath == null) 
			throw new IllegalArgumentException();
		
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
			
			//create a Set that will contain sets representing zones
			//Set<Set> zones = new HashSet<Set>(200);
			
			//loop through the rows of the worksheet
			while (rows.hasNext()) {
				Row row = rows.next();
				
				//get an Iterator to iterate over the cells of the row
				Iterator<Cell> cells = row.cellIterator();
				
				//loop through the cells of the row, pulling out information about the areas
				while (cells.hasNext()) {
					Cell cell = cells.next();
					RichTextString string = cell.getRichStringCellValue();
					System.out.println(string.getString());
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
