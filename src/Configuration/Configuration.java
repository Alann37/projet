package Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Configuration {
	public static List<String> importConfig() throws IOException{
		File config = new File("config.xlsx");
		List<String> configs = new ArrayList<String>();
		InputStream reader = new FileInputStream(config);
		XSSFWorkbook books = new XSSFWorkbook(reader);
		XSSFSheet sh = books.getSheetAt(0);
		XSSFCell cell;
		for(int j = 0; j <= sh.getLastRowNum();j++){
			for(int i = 0 ; i < sh.getRow(j).getPhysicalNumberOfCells(); i++) {
				cell = sh.getRow(j).getCell(i);	
				String temp = cell.getStringCellValue();
				configs.add(temp);
			}
		}
		books.close();
		return configs;
	}
	
	public static void setConfig(int confPos,String newConf) throws IOException{
		File config = new File("config.xlsx");
		InputStream reader = new FileInputStream(config);
		XSSFWorkbook books = new XSSFWorkbook(reader);
		XSSFSheet sh = books.getSheetAt(0);
		sh.getRow(0).getCell(confPos).setCellValue(newConf);
		OutputStream writer = new FileOutputStream(config);
		books.write(writer);
		books.close();
	}
	
	public static String getConf(int pos) throws IOException{
		
		File config = new File("config.xlsx");
		InputStream reader = new FileInputStream(config);
		XSSFWorkbook books = new XSSFWorkbook(reader);
		XSSFSheet sh = books.getSheetAt(0);
		
		return sh.getRow(0).getCell(pos).getStringCellValue();
	}
}
