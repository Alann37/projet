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
	boolean isAerDisq;
	boolean isEmpty;
	int reponseType;
	boolean isSetOnQuestion;
	int columnPosition;
	public int getColumnPosition(){
		return columnPosition;
	}
	public Reponse (Reponse r){
		isAerDisq=r.isAerDisq;
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
		isSetOnQuestion= r.isSetOnQuestion;
	}
	public Reponse (){
		isAerDisq=false;
		shouldBeEmpty=false;
		questionName="";
		reponseTexte="";
		reponseNumeric=-2;
		cellPosition = -2;
		reponseDate = null;
		disqualif = false;
		isSetOnQuestion=false;
	}
	public String getReponseTexte() {
		return reponseTexte;
	}
	public void setReponseTexte(String reponseTexte) {
		this.reponseTexte = reponseTexte;
	}
	public double getReponseNumeric() {
		return reponseNumeric;
	}
	public void setReponseNumeric(double reponseNumeric) {
		this.reponseNumeric = reponseNumeric;
	}
	public int getCellPosition() {
		return cellPosition;
	}
	public void setCellPosition(int cellPosition) {
		this.cellPosition = cellPosition;
	}
	public boolean isPartOfQuestion() {
		return partOfQuestion;
	}
	public void setPartOfQuestion(boolean partOfQuestion) {
		this.partOfQuestion = partOfQuestion;
	}
	public boolean isPartOfLoop() {
		return partOfLoop;
	}
	public void setPartOfLoop(boolean partOfLoop) {
		this.partOfLoop = partOfLoop;
	}
	public String getQuestionTag() {
		return questionTag;
	}
	public void setQuestionTag(String questionTag) {
		this.questionTag = questionTag;
	}
	public String getQuestionName() {
		return questionName;
	}
	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}
	public Calendar getReponseDate() {
		return reponseDate;
	}
	public void setReponseDate(Calendar reponseDate) {
		this.reponseDate = reponseDate;
	}
	public boolean isDisqualif() {
		return disqualif;
	}
	public void setDisqualif(boolean disqualif) {
		this.disqualif = disqualif;
	}
	public boolean isShouldBeEmpty() {
		return shouldBeEmpty;
	}
	public void setShouldBeEmpty(boolean shouldBeEmpty) {
		this.shouldBeEmpty = shouldBeEmpty;
	}
	public boolean isEmpty() {
		return isEmpty;
	}
	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}
	public Reponse(String answer,int type , String questionLabel,int place) throws ParseException{
		columnPosition=place;
		disqualif = false;
		questionTag= questionLabel;
		reponseDate= null;
		isSetOnQuestion=false;
		shouldBeEmpty= false;
		if(answer != null){
			reponseTexte=answer;
			reponseType = type;
			if(type == 1) {// cas int
				
				answer = answer.replaceAll("[^\\d.]", "");
				reponseNumeric = Double.parseDouble(answer) ;
				if (questionTag.contains("date") ){
					reponseDate= Calendar.getInstance();
					Date da = HSSFDateUtil.getJavaDate(reponseNumeric);
					reponseDate.setTime(da);
				}
			} else {
				reponseTexte = answer;
				reponseNumeric=-1;
				if (questionTag.contains("date") && !answer.contains("undef") && !answer.isEmpty() && answer.length()>4 ){
					reponseDate= Calendar.getInstance();
					DateFormat d = new SimpleDateFormat("dd/mm/yy");
					Date da = d.parse(reponseTexte);
					reponseDate.setTime(da);
				}
			}
			if(questionLabel.contains("_")){
		    	partOfQuestion=true;
		    }
		    if(questionLabel.contains(".")){
		    	partOfLoop=true;
		    }
		    isEmpty =false;
		}else {
			reponseTexte="";
			reponseNumeric = -1;
			reponseDate = null;
			if(questionLabel.contains("_")){
		    	partOfQuestion=true;
		    }
		    if(questionLabel.contains(".")){
		    	partOfLoop=true;
		    }
			isEmpty=true;
		}   
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
