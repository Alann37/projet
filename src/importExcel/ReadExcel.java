package importExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.apache.poi.POIXMLProperties;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetDimension;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import Configuration.Configuration;
import importMSQLServer.InformationBDD;

public class ReadExcel {
	static long chrono;
	static void Go_Chrono() { 
		chrono = java.lang.System.currentTimeMillis() ; 
		System.out.println(chrono);
		} 
	static long Stop_Chrono() { 
		long chrono2 = java.lang.System.currentTimeMillis() ;
		
		long temps = chrono2 - chrono ; 
		System.out.println("Temps ecoule = " + temps + " ms") ; 
		return temps;
		} 
	static Semaphore sem = new Semaphore(1,true);


	public static List<Question> importConditionFromWord(File file) throws IOException {
		int j = -1;
		List<Question> questions = new ArrayList<Question>();
		try {
			FileInputStream in = new FileInputStream(file);
			XWPFDocument doc;
			List<XWPFParagraph> paragraphList;
			try {
				doc = new XWPFDocument(in);
				paragraphList = doc.getParagraphs();

				for (XWPFParagraph para : paragraphList) {
					if (para.getStyle() != null) {
						if (para.getStyle().contains("Question") && !para.getText().toString().isEmpty()) {
							if (para.getStyle().equals("QuestionName")) {
								j++;
								questions.add(new Question(para.getText()));
							} else if (!para.getText().isEmpty()) {
								questions.get(j).conditions.add(new Condition(para.getText()));
							}
						}
					}
				}
				doc.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//exportControlToExcel(questions, file.toString().replace(".docx", ""));
		return questions;
	}

	public static List<TraitementEntrer> readExcelDocument(File file) throws IOException, ParseException {
		InputStream reader = new FileInputStream(file);
		XSSFWorkbook books = new XSSFWorkbook(reader);
		XSSFSheet sh = books.getSheetAt(0);
		XSSFCell cell;
		List<TraitementEntrer> listeEntrer = new ArrayList<TraitementEntrer>();
		Iterator rows = sh.rowIterator();
	    XSSFRow row;
		while(rows.hasNext()){
		   row = (XSSFRow)rows.next();
		   listeEntrer.add(new TraitementEntrer());
		   Iterator cells = row.cellIterator();
		   if(row.getRowNum()>0){
			   while(cells.hasNext()){
				   cell=(XSSFCell) cells.next();
				   listeEntrer.get(row.getRowNum()-1).getReponses().add(new Reponse(cell,sh.getRow(0).getCell(cell.getColumnIndex()))); 
			   }
		    
		   	}
		  }
		books.close();
		return listeEntrer;
	}


	public static List<InformationBDD> importListBases() throws InvalidFormatException, IOException{
	    List<InformationBDD> lRet = new ArrayList<InformationBDD>();
		File file = new File("Base � importer.xlsx");
		XSSFWorkbook books = new XSSFWorkbook(file);
		XSSFSheet sh = books.getSheetAt(0);
		Iterator rows = sh.rowIterator();
	    XSSFRow row;
	    XSSFCell cell;

		while(rows.hasNext()){
			   row = (XSSFRow)rows.next();
			   if(row.getRowNum()>0){
			   Iterator cells = row.cellIterator();
			   	while(cells.hasNext()){
			   		cell = (XSSFCell)cells.next();
			   		if(cell.getColumnIndex()==0){
			   			lRet.add(new InformationBDD(cell.getStringCellValue()));
			   		}else if (cell.getColumnIndex()>1) {
			   			if(!cell.getStringCellValue().isEmpty()){
			   				lRet.get(lRet.size()-1).getLangues().add(cell.getStringCellValue());
			   			}
			   		}
			   	}
			   }
		}
		
		
		
		return lRet;
	}
	
	
	public static boolean exportBaseExcel(File file,List<TraitementEntrer> list) throws IOException {
		//System.out.println("test acquire for " + file.getName()  + " " +sem.tryAcquire());

				
			try {
				sem.acquire();
				System.out.println("sem acquire for "+ file.getName());

			//	Go_Chrono();
				XSSFWorkbook books = new XSSFWorkbook();
				List<XSSFSheet> shs= new ArrayList<XSSFSheet>();
				shs.add(books.createSheet("Page 0"));
				int shPage=0;
				//XSSFSheet shDisqu= books.createSheet();
				//shDisqu.createRow(0);
				boolean disqu=false;
			//	System.out.println("passage pour " + file.getName());
				CellStyle styleDisqu = books.createCellStyle();
				CellStyle styleSkip = books.createCellStyle();
				for(int i = 1 ; i < list.size()+1;i++){
					if(i==1){
						shs.get(shPage).createRow(0);
						shs.get(shPage).createRow(i);
					} else {
						for(int j= 0 ; j<shs.size();j++){
							shs.get(j).createRow(i);
						}
					}
					
					shPage=0;
					disqu= false;
					if(i==2){
						styleDisqu.cloneStyleFrom(shs.get(0).getRow(0).getCell(0).getCellStyle());
						styleDisqu.setFillBackgroundColor(IndexedColors.RED.getIndex());
						styleDisqu.setFillForegroundColor(IndexedColors.RED.getIndex());
						styleDisqu.setFillPattern(CellStyle.SOLID_FOREGROUND);

						styleSkip.cloneStyleFrom(shs.get(0).getRow(0).getCell(0).getCellStyle());
						styleSkip.setFillBackgroundColor(IndexedColors.PINK.getIndex());
						styleSkip.setFillForegroundColor(IndexedColors.PINK.getIndex());
						styleSkip.setFillPattern(CellStyle.SOLID_FOREGROUND);
					}
					for(int j = 0 ; j < list.get(i-1).getReponses().size();j++){
						
						if(j%16384==0 && j !=0&& i==1){
							shPage++;
							shs.add(books.createSheet("Page"+shPage));
							
							
							shs.get(shPage).createRow(0);
							shs.get(shPage).createRow(i);
							
						} else if (j%16384==0 && j !=0){
							shPage++;
						}
						if(i == 1){
							shs.get(shPage).getRow(0).createCell(j%16384);
							//shDisqu.getRow(0).createCell(j);
							shs.get(shPage).getRow(0).getCell(j%16384).setCellValue(list.get(i-1).getReponses().get(j).questionTag);
							//shDisqu.getRow(i).getCell(j).setCellValue(list.get(i).getReponses().get(j).questionTag);
						} 
							if(!list.get(i-1).getReponses().get(j).isEmpty){
						
								if(j==16384){
									shs.get(shPage).getRow(i).createCell(0);
									shs.get(shPage).getRow(i).getCell(0).setCellValue(shs.get(0).getRow(i).getCell(0).getStringCellValue());
									j++;
								}else if(j>16384){
									j++;
								}
								if(shs.get(shPage).getRow(i)==null){
									System.out.println(" i = "+i+" j= "+j + " et j%="+j%16384);
								}
								shs.get(shPage).getRow(i).createCell(j%16384);
								if(list.get(i-1).getReponses().get(j).reponseType==1){
									shs.get(shPage).getRow(i).getCell(j%16384).setCellValue(list.get(i-1).getReponses().get(j).reponseNumeric);
								} else {
									shs.get(shPage).getRow(i).getCell(j%16384).setCellValue(list.get(i-1).getReponses().get(j).reponseTexte);
								}
								if(list.get(i-1).getReponses().get(j).disqualif){
									disqu = true;
					
									shs.get(shPage).getRow(i).getCell(j%16384).setCellStyle(styleDisqu);
								}
								if(list.get(i-1).getReponses().get(j).shouldBeEmpty){

									shs.get(shPage).getRow(i).getCell(j%16384).setCellStyle(styleSkip);
								}
								if(j>16384){
									j--;
								}
							}
							
						
					}
					if(disqu){
					
						shs.get(shPage).getRow(i).getCell(0).setCellStyle(styleDisqu);
					}
				}
				int fileNumber =0;
				boolean isWrite = false;
				File f2 = file;
				String temp = file.getAbsolutePath();
				temp = temp.split("base qualif")[0] + fileNumber + " base qualif" + temp.split("base qualif")[1];
				f2= new File(temp);
				do{
					if(Files.exists(Paths.get(f2.getAbsolutePath()), LinkOption.NOFOLLOW_LINKS)){
						temp = file.getAbsolutePath();
						temp = temp.split("base qualif")[0] + fileNumber + " base qualif" + temp.split("base qualif")[1];
						f2= new File(temp);
						fileNumber++;
					}
					else {
						isWrite=true;
					}
				}while(!isWrite);
				
				FileOutputStream writer = new FileOutputStream(f2);
				POIXMLProperties xmlProps = books.getProperties();    
				POIXMLProperties.CoreProperties coreProps =  xmlProps.getCoreProperties();
				coreProps.setCreator("A+A");
				books.write(writer);
				books.close();
				sem.release();
				return true;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("passage catch ");
				return false;
			} 
	}
	
	
	
	
	
}
