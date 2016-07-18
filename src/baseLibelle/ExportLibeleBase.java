package baseLibelle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import ErrorLog.Error;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

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
		String pathTreated = Configuration.importConfig().get(3) +"\\"+ file.getName().replaceAll("base qualif", "base libellé");
		OutputStream writer = new FileOutputStream(pathTreated);		
		books.write(writer);
		books.close();
	}


	public static void setMasterWithPrintStudy(List<String> list,String name) throws IOException{
		File master = new File("Word Validation Guide.docx");
		XWPFDocument doc;
	
		try {
			FileInputStream in = new FileInputStream(master);
			doc = new XWPFDocument(in);
			XWPFParagraph temp;
			XWPFParagraph temp2;
			XWPFParagraph temp3;
			int count = 0;
			for(int i = 0 ; i < list.size(); i++){
				
				temp = null;
					doc.createParagraph();
					temp = doc.getLastParagraph();
					temp.createRun();
					changeText(temp,list.get(i),false);
					temp.setStyle("QuestionName");
				if(temp!=null){
					doc.setParagraph(temp, count);
				}
					count ++;
					doc.createParagraph();
					temp2 = doc.getLastParagraph();
					temp2.createRun();
					
					changeText(temp2,"errorW if : ",true);
					if(temp2!=null){
						doc.setParagraph(temp2,count);
					}
					count++;
					doc.createParagraph();
					temp3 = doc.getLastParagraph();
					temp3.createRun();
					temp3.setStyle("QuestionInformation");
					changeText(temp3," ",false);
					if(temp3!=null){
						doc.setParagraph(temp3, count);
					}
					count++;
					temp = null;
					doc.createParagraph();
					temp = doc.getLastParagraph();
					temp.createRun();
					changeText(temp, "errorS if :", true);
					if(temp!=null){
						doc.setParagraph(temp, count);
					}
					count++;
					temp = null;
					doc.createParagraph();
					temp = doc.getLastParagraph();
					temp.setStyle("statCondition");
					temp.createRun();
					changeText(temp," ", false);
					if(temp!=null){
						doc.setParagraph(temp, count);
					}
					count++;
			}
			
			temp = doc.getLastParagraph();
			temp.createRun();
			changeText(temp,"",true);
			temp.setStyle("QuestionName");
		if(temp!=null){
			doc.setParagraph(temp, list.size());
		}
			File out = new File(System.getProperty("user.dir")+"\\ValidationGuideBrut\\"+name+".docx");
			FileOutputStream f = new FileOutputStream(out);
			doc.write(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Error er = new Error(e.getMessage());
			er.printError();
		}
		
	}
	public static void changeText(XWPFParagraph p, String newText,boolean b) {
		   List<XWPFRun> runs = p.getRuns();
		  
		   for(int i = runs.size() - 1; i > 0; i--) {
		      p.removeRun(i);
		   }
		   if(runs.size()>0){
		   XWPFRun run = runs.get(0);
		   
		   run.setItalic(b);
		   
		   run.setText(newText, 0);
		   }
		}

}
