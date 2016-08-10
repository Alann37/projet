package traitement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import javax.swing.SingleSelectionModel;

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
	Date reponseDate;
	boolean disqualif;
	boolean shouldBeEmpty;
	boolean isAerDisq;
	boolean isEmpty;
	int reponseType;
	boolean isSetOnQuestion;
	int columnPosition;
	boolean isValueDisqu;
	boolean isDate;
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
		isDate = r.isDate;
		shouldBeEmpty = r.shouldBeEmpty;
		isSetOnQuestion= r.isSetOnQuestion;
		isValueDisqu=r.isValueDisqu;
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
		isDate=false;
		isValueDisqu=false;
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
	public Date getReponseDate() {
		return reponseDate;
	}
	public void setReponseDate(Date reponseDate) {
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
		isValueDisqu=false;
		shouldBeEmpty= false;
		isDate = false;

		if(answer != null){
			reponseTexte=answer;
			reponseType = type;
			if(type == 1) {// cas int
				
				answer = answer.replaceAll("[^\\d.]", "");
				reponseNumeric = Double.parseDouble(answer) ;
				if (questionTag.contains("date") ){
					reponseDate = HSSFDateUtil.getJavaDate(reponseNumeric);
				
				}
			} else {
				reponseTexte = answer;
			
				reponseNumeric=-1;
				if (questionTag.contains("date") && !answer.contains("undef") && !answer.isEmpty() && answer.length()>4 ){
					
					isDate = true;
					DateFormat d = new SimpleDateFormat("dd/mm/yy");

					 reponseDate = d.parse(reponseTexte);
					 reponseDate.setMonth(Integer.valueOf(answer.split("\\/")[1])-1);
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
