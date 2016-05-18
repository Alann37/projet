package importExcel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class Reponse {
	String reponseTexte ;
	double reponseNumeric;
	int cellPosition;
	boolean partOfQuestion;
	boolean partOfLoop;
	String questionTag;
	String questionName;
	Calendar reponseDate;
	boolean disqualif;
	boolean shouldBeEmpty;
	boolean isEmpty;

	public Reponse (Reponse r){
		reponseTexte = r.reponseTexte;
		reponseNumeric= r.reponseNumeric;
		cellPosition=r.cellPosition;
		partOfQuestion = r.partOfQuestion;
		partOfLoop = r.partOfLoop;
		questionName = r.questionName;
		questionTag = r.questionTag;
		reponseDate = r.reponseDate;
		disqualif = r.disqualif;
		shouldBeEmpty = r.shouldBeEmpty;
	}
	public Reponse (){
		shouldBeEmpty=false;
		questionName="";
		reponseTexte="";
		reponseNumeric=-2;
		cellPosition = -2;
		reponseDate = null;
		disqualif = false;
	}
	
	public Reponse(XSSFCell cell,XSSFCell question) throws ParseException{
		disqualif = false;
		cellPosition = question.getColumnIndex();
		questionTag= question.getStringCellValue();
		reponseDate= null;
		shouldBeEmpty= false;
		if(cell!=null){
			if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC){
				reponseNumeric = cell.getNumericCellValue();
				reponseTexte = "";
				if (questionTag.contains("date") ){
					reponseDate= Calendar.getInstance();
					Date da = HSSFDateUtil.getJavaDate(reponseNumeric);
					reponseDate.setTime(da);
				}
			} 
			if(cell.getCellType() == XSSFCell.CELL_TYPE_STRING){
				reponseTexte = cell.getStringCellValue();
				reponseNumeric=-1;
				if (questionTag.contains("date") && !cell.getStringCellValue().contains("undef") && !cell.getStringCellValue().isEmpty() && cell.getStringCellValue().length()>4 ){
					reponseDate= Calendar.getInstance();
					DateFormat d = new SimpleDateFormat("dd/mm/yy");
					Date da = d.parse(reponseTexte);
					reponseDate.setTime(da);
				}
			} 
			if(question.getStringCellValue().contains("_")){
		    	partOfQuestion=true;
		    }
		    if(question.getStringCellValue().contains(".")){
		    	partOfLoop=true;
		    }
		    isEmpty =false;
		} else {
			reponseTexte="";
			reponseNumeric = -1;
			reponseDate = null;
			if(question.getStringCellValue().contains("_")){
		    	partOfQuestion=true;
		    }
		    if(question.getStringCellValue().contains(".")){
		    	partOfLoop=true;
		    }
			isEmpty=true;
		}
	    
	}

	@Override
	public String toString() {
		if(questionName !=null){
		return "Reponse [questionName = "+ questionName +" ,questionTag= "+questionTag+" ,reponseTexte= " + reponseTexte + ", reponseNumeric=" + reponseNumeric + ", cellPosition="
				+ cellPosition + ", partOfQuestion=" + partOfQuestion + ", partOfLoop=" + partOfLoop + "]";
	}else {
		return "";
	}
	}
	
}
