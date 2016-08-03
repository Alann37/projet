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
import org.apache.poi.openxml4j.opc.OPCPackage;
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
import baseLibelle.InfoQuota;
import baseLibelle.StudyQuotas;
import importMSQLServer.ConnectURL;
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
	public static void exportQuota(List<StudyQuotas> listQuotas,File file) throws IOException{
		XSSFWorkbook books = new XSSFWorkbook();
		for(int h = 0 ; h < listQuotas.size();h++){
			List<InfoQuota> quotas = listQuotas.get(h).getQuotas();
			XSSFSheet sh = books.createSheet(listQuotas.get(h).getLanguage());
			XSSFRow row; 
			XSSFCell cell;
			row = sh.createRow(0);
			for(int i = 0 ;i < 50; i +=3){
				cell = row.createCell(i);
				if(i==3){
					i-=2;
					cell = row.createCell(i);
				}
				if(i==0){
					cell.setCellValue("Nom  quota");
				} else {
					cell.setCellValue("Name");
					cell = row.createCell(i+1);
					cell.setCellValue("Value");
					cell = row.createCell(i+2);
					cell.setCellValue("Limit");
				}
			}
			for(int i = 0 ; i < quotas.size();i++){
				row =sh.createRow(i+1);
				cell = row.createCell(0);
				cell.setCellValue(quotas.get(i).getName());
				int limite = quotas.get(i).getPosibility().size();
				limite--;
				limite *=2;
				limite ++;
				int cellIterator = 1;
				for(int j =0; j<quotas.get(i).getPosibility().size();j++){
					
					if(j==0){
						cellIterator=1;
					}else {
						cellIterator=(j*3) +1;
					}
					cell = row.createCell(cellIterator);
					cell.setCellValue(quotas.get(i).getPosibility().get(j).getName());
					cell = row.createCell(cellIterator+1);
					cell.setCellValue(quotas.get(i).getPosibility().get(j).getValue());
					cell = row.createCell(cellIterator+2);
					cell.setCellValue(quotas.get(i).getPosibility().get(j).getLimite());
				}
			}
		}
		FileOutputStream writer = new FileOutputStream(file);
		POIXMLProperties xmlProps = books.getProperties();    
		POIXMLProperties.CoreProperties coreProps =  xmlProps.getCoreProperties();
		coreProps.setCreator("A+A");
		books.write(writer);
		books.close();

	}
	
	private static void errorLog(String error) throws InvalidFormatException, IOException{
		File file = new File("debug.xlsx");
		XSSFWorkbook books = new XSSFWorkbook();
		XSSFSheet sh = books.createSheet();
		
	    XSSFRow row= sh.createRow(0);
	    XSSFCell cell= row.createCell(0);
	    cell.setCellValue(error);
	    FileOutputStream writer = new FileOutputStream(file);
		POIXMLProperties xmlProps = books.getProperties();    
		POIXMLProperties.CoreProperties coreProps =  xmlProps.getCoreProperties();
		coreProps.setCreator("A+A");
		books.write(writer);
		books.close();
		
			   
		
	}
	public static void callExcelMacro() throws InvalidFormatException, IOException {
		ComThread.InitSTA();
		File file = new File("general_update.xlsm");
		String macro = "!Module1.test";
		System.out.println(file.getName());
		final ActiveXComponent excel = new ActiveXComponent("Excel.Application");
		try {
			
			final Dispatch workbooks = excel.getProperty("Workbooks").toDispatch();
// TODO PROBLEME HERE
			final Dispatch workBook = Dispatch.call(workbooks, "Open", file.getAbsolutePath()).toDispatch();
			
			final Variant result = Dispatch.call(excel, "Run", new Variant("\'" + file.getName() + "\'" + macro));
			com.jacob.com.Variant f = new com.jacob.com.Variant(true);
			Dispatch.call(workBook, "Close", f);

		} catch (Exception e) {
			
			ReadExcel.errorLog(e.getMessage());
			
		} finally {

			excel.invoke("Quit", new Variant[0]);
			ComThread.Release();
		}

	}
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
								if(!para.getText().equals(" ")){
									questions.get(j).conditions.add(new Condition(para.getText()));
								}
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
		File file = new File("Import Database.xlsx");
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
			   		}else if (cell.getColumnIndex()>3) {
			   			if(!cell.getStringCellValue().isEmpty()){
			   				lRet.get(lRet.size()-1).getLangues().add(cell.getStringCellValue());
			   			}
			   		} else if (cell.getColumnIndex()==2){
			   			lRet.get(lRet.size()-1).setServeur(cell.getStringCellValue());
			   		} else if (cell.getColumnIndex() == 3){
			   			lRet.get(lRet.size()-1).setValidationGuideName(cell.getStringCellValue());
			   		}
			   	}
			   }
		}
		for(int i = 0 ; i < lRet.size();i++){
			if(lRet.get(i).getServeur()==null || lRet.get(i).getLangues().size()==0 || lRet.get(i).getBase()==null){
				lRet.remove(i);
				i--;
			}

		}
		
		
		return lRet;
	}
	

	private static  XSSFWorkbook testExcel(List<String> s) throws IOException{
		XSSFWorkbook books = new XSSFWorkbook();
		List<XSSFSheet> shs= new ArrayList<XSSFSheet>();
		shs.add(books.createSheet("Page 0"));
		int shPage=0;
		shs.get(0).createRow(0);
		int cellIndex;
		cellIndex= 0;
		for(int i = 0 ; i < s.size();i++){

			if(cellIndex%16384==0 && cellIndex!=0){
				shPage++;
				shs.add(books.createSheet("Page "+shPage));
				shs.get(shPage).createRow(0);
				shs.get(shPage).getRow(0).createCell(0);
				shs.get(shPage).getRow(0).getCell(0).setCellValue(s.get(0));
				cellIndex=1;
				
			}
			shs.get(shPage).getRow(0).createCell(cellIndex);
			shs.get(shPage).getRow(0).getCell(cellIndex).setCellValue(s.get(i));
			cellIndex++;
		}
		return books;
	}
	public static boolean exportBaseExcel(File file,TraitementEtude study) throws IOException, InvalidFormatException {
		//System.out.println("test acquire for " + file.getName()  + " " +sem.tryAcquire());

				
			try {
				sem.acquire();
				System.out.println("sem acquire for "+ file.getName());
				List<TraitementEntrer> list = study.getEtudes();
			//	Go_Chrono();
				ConnectURL con = new ConnectURL();
				XSSFWorkbook books;
				if(!study.getBase().isEmpty() && !study.getLanguage().isEmpty() && !study.getServeur().isEmpty()){
					books= ReadExcel.testExcel(con.getColumnLabel(study.getBase(), study.getLanguage(),study.getServeur()));
				} else {
					books = new XSSFWorkbook(file);
				}
				List<XSSFSheet> shs= new ArrayList<XSSFSheet>();
				for(int i =0 ; i < books.getNumberOfSheets();i++){
					shs.add(books.getSheetAt(i));
				
				}
				int shPage=0;
				//XSSFSheet shDisqu= books.createSheet();
				//shDisqu.createRow(0);
				boolean disqu=false;
				boolean aerDisqu = false;
			//	System.out.println("passage pour " + file.getName());
				CellStyle styleDisqu = books.createCellStyle();
				CellStyle styleSkip = books.createCellStyle();
				CellStyle styleAer = books.createCellStyle();
				CellStyle styleValueDiqu = books.createCellStyle();
				if(shs.get(0) == null){
					shs.add(books.createSheet());
				}
				if(shs.get(0).getRow(0)==null){
					shs.get(0).createRow(0);
				}
				if(shs.get(0).getRow(0).getCell(0)==null){
					shs.get(0).getRow(0).createCell(0);
				}
				styleDisqu.cloneStyleFrom(shs.get(0).getRow(0).getCell(0).getCellStyle());
				styleDisqu.setFillBackgroundColor(IndexedColors.RED.getIndex());
				styleDisqu.setFillForegroundColor(IndexedColors.RED.getIndex());
				styleDisqu.setFillPattern(CellStyle.SOLID_FOREGROUND);

				styleValueDiqu.cloneStyleFrom(shs.get(0).getRow(0).getCell(0).getCellStyle());
				styleValueDiqu.setFillBackgroundColor(IndexedColors.ORANGE.getIndex());
				styleValueDiqu.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
				styleValueDiqu.setFillPattern(CellStyle.SOLID_FOREGROUND);
				
				styleSkip.cloneStyleFrom(shs.get(0).getRow(0).getCell(0).getCellStyle());
				styleSkip.setFillBackgroundColor(IndexedColors.PINK.getIndex());
				styleSkip.setFillForegroundColor(IndexedColors.PINK.getIndex());
				styleSkip.setFillPattern(CellStyle.SOLID_FOREGROUND);
				
				styleAer.cloneStyleFrom(shs.get(0).getRow(0).getCell(0).getCellStyle());
				styleAer.setFillBackgroundColor(IndexedColors.BLUE_GREY.getIndex());
				styleAer.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
				styleAer.setFillPattern(CellStyle.SOLID_FOREGROUND);
				
				File f2 = file;
			
				for(int i = 1 ; i <((list.size())+1);i++){
					int rowNum = shs.get(0).getLastRowNum()+1;
					
					//System.out.println("passage pour i = " + i + " et size = " + list.size());
					for(int j= 0 ; j<shs.size();j++){
						shs.get(j).createRow(rowNum);
					}
					shPage=0;
					disqu= false;
				
					for(int j = 0 ; j < list.get(i-1).getReponses().size();j++){
						
						if(j>=16384){
							shPage++;							
						} 
							if(!list.get(i-1).getReponses().get(j).isEmpty){
								
								//	System.out.println("passage p  = " + p + " et question tag = " + list.get(i-1).getReponses().get(j).questionTag);
										int columnIndex = list.get(i-1).getReponses().get(j).columnPosition;
										if(columnIndex>=16384){
											columnIndex++;
											columnIndex %=16384;
											if(columnIndex==0){
												columnIndex++;
											}
											
										}
										shs.get(shPage).getRow(rowNum).createCell(columnIndex);
										if(list.get(i-1).getReponses().get(j).reponseType==1){
											shs.get(shPage).getRow(rowNum).getCell(columnIndex).setCellValue(list.get(i-1).getReponses().get(j).reponseNumeric);
										} else {
											shs.get(shPage).getRow(rowNum).getCell(columnIndex).setCellValue(list.get(i-1).getReponses().get(j).reponseTexte);
										}
										if(list.get(i-1).getReponses().get(j).disqualif){
											disqu = true;
											if(list.get(i-1).getReponses().get(j).isAerDisq){
												aerDisqu=true;
												shs.get(shPage).getRow(rowNum).getCell(columnIndex).setCellStyle(styleAer);
											}else {
												shs.get(shPage).getRow(rowNum).getCell(columnIndex).setCellStyle(styleDisqu);
											}
										}
										if(list.get(i-1).getReponses().get(j).shouldBeEmpty){

											shs.get(shPage).getRow(rowNum).getCell(columnIndex).setCellStyle(styleSkip);
										}
										if(list.get(i-1).getReponses().get(j).isValueDisqu){
											shs.get(shPage).getRow(rowNum).getCell(columnIndex).setCellStyle(styleValueDiqu);
										}
										list.get(i-1).getReponses().remove(j);
										j--;
							
				 					}
								
							
							
							
						
					}
					if(disqu){
						if(aerDisqu){
							shs.get(shPage).getRow(rowNum).getCell(0).setCellStyle(styleAer);
						}else {
							shs.get(shPage).getRow(rowNum).getCell(0).setCellStyle(styleDisqu);
						}
					}
					disqu = false;
					aerDisqu=false;
					list.remove(i-1);
					i--;
				}
				if(books.getNumberOfSheets()>1){
					int rowFirstPage =books.getSheetAt(0).getLastRowNum();
					for(int i = 1; i < books.getNumberOfSheets();i++){
						if(books.getSheetAt(i).getLastRowNum()<rowFirstPage){
							for(int j = books.getSheetAt(i).getLastRowNum()+1; j< rowFirstPage;j++){
								books.getSheetAt(i).createRow(j);
							}
						}
						for(int j = 1 ; j <= books.getSheetAt(i).getLastRowNum();j++){
							books.getSheetAt(i).getRow(j).createCell(0);
							if(list.size()>j){
								if(list.get(j-1).getReponses().size()>0){
									books.getSheetAt(i).getRow(j).getCell(0).setCellValue(list.get(j-1).getReponses().get(0).reponseNumeric);
								}
							}
						}
					}
				}
				int fileNumber =0;
				boolean isWrite = false;
				
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
				
				FileOutputStream writer = new FileOutputStream(file);

				books.write(writer);
				books.close();
				System.out.println("Sem release " + file.getPath());
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
