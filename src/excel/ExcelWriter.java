package excel;

import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelWriter {
	
	private HSSFWorkbook workbook;
	private HSSFSheet sheet;
	private int rowNum;
	
	public ExcelWriter() {
		workbook = new HSSFWorkbook();
        sheet = workbook.createSheet();
        rowNum = 0;
	}
	
	/*
	 * Adds a Title row to the Excel file
	 * PARAMETER: String title - the title to be added
	 * */
	public void addTitle(String title) {
		//add an empty row
		sheet.createRow(rowNum++);
		
		//create the title
		HSSFRow row = sheet.createRow(rowNum++);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(title);
	}
	
	/*
	 * Adds a header row to the Excel file
	 * PARAMETER: String[] header - contains the different items to go into the header
	 */
	public void addHeader(String[] header) {
		HSSFRow row = sheet.createRow(rowNum++);
		
		//loop through the header, putting in the items in the header
		for (int i = 0; i < header.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(header[i]);
		}
	}
	
	
	/*
	 * Adds a Map containing information about violators in the Excel file
	 * PARAMETER: Map<String, Map<String, String>> violators - Map of Maps containing violators
	 * */
	public void addMap(Map<String, Map<String, String>> violators) {
		//get an Iterator and iterate over the violators
		Iterator<String> phones = violators.keySet().iterator();
        while (phones.hasNext()) {
        	Map<String, String> violator = violators.get(phones.next());
        	
        	//create the row and add the data to the cells
        	HSSFRow row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(violator.get("missionaries"));
            row.createCell(1).setCellValue(Double.parseDouble(violator.get("count")));
        }
	}
	
	/*
	 * Adds a List containing information about violators in the Excel file
	 * PARAMETER: List<Map<String, String>> violators - List of Maps containing violators
	 * */
	public void addList(List<Map<String, String>> violators) {
		//loop through the List, putting data into the Excel file
		for (int i = 0; i < violators.size(); i++) {
			Map<String, String> violator = violators.get(i);
			
			//create the row and add the data to the cells
			HSSFRow row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(violator.get("caller"));
			row.createCell(1).setCellValue(violator.get("receiver"));
			row.createCell(2).setCellValue(violator.get("date"));
		}
	}
	
	/*
	 * Writes the Excel file to the specified file name
	 * PARAMETER: String fileName - the name of the file to write to
	 * */
	public void write(String fileName) {
		try {
            FileOutputStream out = new FileOutputStream(fileName);
            workbook.write(out);
            out.close();
        }
		catch(Exception e) { 
			e.printStackTrace();
		}
	}
}
