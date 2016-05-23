package importExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import Configuration.Configuration;

public class ReadExcel {

	/**
	 * 
	 * @param file
	 * @param macroName
	 * 
	 */
	public static void callExcelMacro(File file, String macroName) {
		ComThread.InitSTA();
		final ActiveXComponent excel = new ActiveXComponent("Excel.Application");
		try {
			final Dispatch workbooks = excel.getProperty("Workbooks").toDispatch();
			final Dispatch workBook = Dispatch.call(workbooks, "Open", file.getAbsolutePath()).toDispatch();
			final Variant result = Dispatch.call(excel, "Run", new Variant("\'" + file.getName() + "\'" + macroName));
			com.jacob.com.Variant f = new com.jacob.com.Variant(true);
			Dispatch.call(workBook, "Close", f);

		} catch (Exception e) {
			e.printStackTrace();
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
							} else {
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
		for (int i = 0; i < questions.size(); i++) {
			questions.get(i).makeCondition();
		}
		//exportControlToExcel(questions, file.toString().replace(".docx", ""));
		return questions;
	}

/*	private static void exportControlToExcel(List<Question> list, String name) throws IOException {
		try {
			// name.replace("\\E0", "\\Condition\\E0");
			FileOutputStream fileOut = new FileOutputStream(name + "Condition.xls");

			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet worksheet = workbook.createSheet();

			HSSFRow row1 = worksheet.createRow(0);
			List<HSSFRow> listRow = new ArrayList<HSSFRow>();
			listRow.add(row1);
			for (int i = 0; i < list.size(); i++) {
				HSSFCell cell = row1.createCell(i);
				cell.setCellValue(list.get(i).name);
				if (listRow.size() - 1 < list.get(i).conditions.size()) {
					int max = list.get(i).conditions.size() + 1;
					for (int j = listRow.size(); j < max; j++) {
						listRow.add(worksheet.createRow(j));
					}
				}
				for (int j = 0; j < list.get(i).conditions.size(); j++) {
					HSSFCell temp = listRow.get(j + 1).createCell(i);
					temp.setCellValue(list.get(i).conditions.get(j).toString());
				}
			}
			workbook.write(fileOut);
			workbook.close();
			fileOut.flush();
			fileOut.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * fonction pour lire la bdd non traitée
	 * 
	 */
	public static List<TraitementEntrer> readExcelDocument(File file) throws IOException, ParseException {
		InputStream reader = new FileInputStream(file);
		XSSFWorkbook books = new XSSFWorkbook(reader);
		XSSFSheet sh = books.getSheetAt(0);
		XSSFCell cell;
		List<TraitementEntrer> listeEntrer = new ArrayList<TraitementEntrer>();
		for(int j = 1; j <= sh.getLastRowNum();j++){
			listeEntrer.add(new TraitementEntrer());
			for(int i = 0 ; i < sh.getRow(j).getPhysicalNumberOfCells(); i++) {
				cell = sh.getRow(j).getCell(i);
				listeEntrer.get(j-1).getReponses().add(new Reponse(cell,sh.getRow(0).getCell(i)));			
			}
		}
		books.close();
		return listeEntrer;
	}

	
	public static void applyDisqualif(File file, List<TraitementEntrer> list) throws IOException{
		InputStream reader = new FileInputStream(file);
		XSSFWorkbook books = new XSSFWorkbook(reader);
		XSSFRow row;
		XSSFSheet sh = books.getSheetAt(0);
		XSSFCell cell;
		Iterator rows = sh.rowIterator();
		
		while (rows.hasNext()) {
			
			row = (XSSFRow) rows.next();
			Iterator cells = row.cellIterator();
			System.out.println("rowNum = "+row.getRowNum());
			if(row.getRowNum()>0){

					while (cells.hasNext()) {
						cell = (XSSFCell) cells.next();
						if(list.get(row.getRowNum()-1)!=null){
							for(int i = 0 ; i < list.get(row.getRowNum()-1).getQuestionDisqualif().size(); i++){
								String temp = list.get(row.getRowNum()-1).getQuestionDisqualif().get(i);
								if((sh.getRow(0).getCell(cell.getColumnIndex() ).getStringCellValue() ).equals(temp) ){
									CellStyle style = books.createCellStyle();
									style.cloneStyleFrom(cell.getCellStyle());
									style.setFillBackgroundColor(IndexedColors.RED.getIndex());
									style.setFillForegroundColor(IndexedColors.RED.getIndex());
									style.setFillPattern(CellStyle.SOLID_FOREGROUND);
									cell.setCellStyle(style);
								}
									
							}
							int size = list.get(row.getRowNum()-1).getNotToBeAnswer().size();
							String temp = sh.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue();
							for(int i = 0 ; i <size ;i++){							
								if(temp.equals(list.get(row.getRowNum()-1).getNotToBeAnswer().get(i)) ){
									CellStyle style = books.createCellStyle();
									style.cloneStyleFrom(cell.getCellStyle());
									style.setFillBackgroundColor(IndexedColors.PINK.getIndex());
									style.setFillForegroundColor(IndexedColors.PINK.getIndex());
									style.setFillPattern(CellStyle.SOLID_FOREGROUND);
									cell.setCellStyle(style);
									list.get(row.getRowNum()-1).getNotToBeAnswer().remove(i);
									i=size;
								} 
							}
						}
					}

					

			}
		}
		System.out.println("FINIII!");
		OutputStream writer = new FileOutputStream(Configuration.importConfig().get(1)+"\\"+file.getName().replaceAll(" Base brute", " Base qualif"));
		books.write(writer);
		books.close();
		System.out.println("close done");
	}
	
}
