package baseLibelle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Configuration.Configuration;

public class ExportLibeleBase {
	public static void exportBaseWithLibelle(List<SawtoothList> list,File file) throws IOException{
		InputStream reader = new FileInputStream(file);
		XSSFWorkbook books = new XSSFWorkbook(reader);
		XSSFSheet sh = books.getSheetAt(0);
		XSSFRow firstRow =sh.getRow(0);
		for(int i = 1 ; i <= sh.getLastRowNum(); i++){
			for(int j = 0; j < sh.getRow(i).getPhysicalNumberOfCells();j++){
				for(int h = 0 ; h < list.size();h++){
					if(firstRow.getCell(j).getStringCellValue().contains(list.get(h).questionName)){
						if(sh.getRow(i).getCell(j) != null ){
							if(sh.getRow(i).getCell(j).getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
								if(sh.getRow(i).getCell(j).getNumericCellValue()==0){
									sh.getRow(i).getCell(j).setCellValue(" ");
									h=list.size();
								} else {
									if(firstRow.getCell(j).getStringCellValue().split("_")[0].equals(list.get(h).questionName)&& list.get(h).isUsed()){
										int temp =(int) sh.getRow(i).getCell(j).getNumericCellValue();
										if(temp==1){
											if(sh.getRow(0).getCell(j).getStringCellValue().split("_").length>=1){
												String sTemp = sh.getRow(0).getCell(j).getStringCellValue().split("_")[sh.getRow(0).getCell(j).getStringCellValue().split("_").length-1];
												if(sTemp.contains(".")){
													sTemp = sTemp.split("\\.")[0];
												}
												sTemp=sTemp.replaceAll("[^\\d.]", "");
												if(!sTemp.isEmpty()){
													temp=Integer.valueOf(sTemp);
												}
											}
										}
										if(temp<=list.get(h).listItem.size()&& temp>=1){
											sh.getRow(i).getCell(j).setCellValue(list.get(h).listItem.get(temp-1));
											h=list.size();
										}
									}
								}
							}
						} 
					}
				}
			}
		}
		String pathTreated = Configuration.importConfig().get(3) +"\\"+ file.getName().replaceAll("- Base brute", "- Base libellé");
		OutputStream writer = new FileOutputStream(pathTreated);		
		books.write(writer);
		books.close();
	}
}
