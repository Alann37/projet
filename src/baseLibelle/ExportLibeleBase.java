package baseLibelle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Configuration.Configuration;
/*
 * 
 * 		Iterator rows = sh.rowIterator();
	    XSSFRow row;
		while(rows.hasNext()){
		   row = (XSSFRow)rows.next();
		   listeEntrer.add(new TraitementEntrer());
		   Iterator cells = row.cellIterator();*/
public class ExportLibeleBase {
	public static void exportBaseWithLibelle(List<SawtoothList> list,File file) throws IOException{
		InputStream reader = new FileInputStream(file);
		XSSFWorkbook books = new XSSFWorkbook(reader);
		XSSFSheet sh = books.getSheetAt(0);
		XSSFRow firstRow =sh.getRow(0);
		Iterator rows = sh.rowIterator();
		XSSFCell cell;
	    XSSFRow row;
		while(rows.hasNext()){
			row = (XSSFRow)rows.next();
			Iterator cells = row.cellIterator();
			if(row.getRowNum()==10){
				System.out.println("e");
			}
			while(cells.hasNext()){
				cell=(XSSFCell) cells.next();
				for(int h = 0 ; h < list.size();h++){
					if(firstRow.getCell(cell.getColumnIndex()).getStringCellValue().contains(list.get(h).questionName)){
						if(sh.getRow(row.getRowNum()).getCell(cell.getColumnIndex()) != null ){
							if(sh.getRow(row.getRowNum()).getCell(cell.getColumnIndex()).getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
								if(sh.getRow(row.getRowNum()).getCell(cell.getColumnIndex()).getNumericCellValue()==0){
									sh.getRow(row.getRowNum()).getCell(cell.getColumnIndex()).setCellValue(" ");
									h=list.size();
								} else {
									int temp =(int) sh.getRow(row.getRowNum()).getCell(cell.getColumnIndex()).getNumericCellValue();
									String enteteName ="";
									if(firstRow.getCell(cell.getColumnIndex()).getStringCellValue().contains("_")){
										enteteName = firstRow.getCell(cell.getColumnIndex()).getStringCellValue().split("_")[0];
										if(firstRow.getCell(cell.getColumnIndex()).getStringCellValue().split("_")[0].equals(list.get(h).questionName)&& list.get(h).isUsed()){
											if(temp==1 && !list.get(h).isRadioButton){
												if(sh.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue().contains("_")){
													if(sh.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue().contains("_c") && sh.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue().contains("_r")&& sh.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue().split("_").length<=3){
														if(list.get(h).isColList){
															String sTemp = sh.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue().split("_c")[1];
															if(sTemp.contains(".")){
																sTemp = sTemp.split("\\.")[0];
															}
															sTemp=sTemp.replaceAll("[^\\d.]", "");
															if(!sTemp.isEmpty()){
																temp=Integer.valueOf(sTemp);
															}
														} else if( list.get(h).isRowList){
															String sTemp = sh.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue().split("_r")[1];
															if(sTemp.contains("_")){
																sTemp= sTemp.split("_")[0];
															}
															if(sTemp.contains(".")){
																sTemp = sTemp.split("\\.")[0];
															}
															sTemp=sTemp.replaceAll("[^\\d.]", "");
															if(!sTemp.isEmpty()){
																temp=Integer.valueOf(sTemp);
															}
														} 
													} else {
														String sTemp = sh.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue().split("_")[sh.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue().split("_").length-1];
														if(sTemp.contains(".")){
															sTemp = sTemp.split("\\.")[0];
														}
														sTemp=sTemp.replaceAll("[^\\d.]", "");
														if(!sTemp.isEmpty()){
															temp=Integer.valueOf(sTemp);
														}
													}
												}
											}
										}
									} else {
										if(firstRow.getCell(cell.getColumnIndex()).getStringCellValue().contains(".")){
											enteteName =sh.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue().split("\\.")[0];
										} else {
											enteteName =sh.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue();
										}
									}
									if(enteteName.equals(list.get(h).questionName) && list.get(h).isUsed()){
										if(temp<=list.get(h).listItem.size()&& temp>=1){
											sh.getRow(row.getRowNum()).getCell(cell.getColumnIndex()).setCellValue(list.get(h).listItem.get(temp-1));
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
