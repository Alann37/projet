package importExcel;

import java.util.ArrayList;
import java.util.List;

public class QuestionReturn {
	boolean validate;
	boolean gotSkipTo;
	String questionSkip;
	List<String> questionDisqualifs;
	int questionNumber;
	boolean isAnswer;
	List<SkipCondition> loopPart;
	List<String> questionTagSkip;
	List<MultipleCondition> conditions;
	double sum;
	boolean isConstSum;
	boolean isLoop;
	String loopNumber;
	String etudename;
	
	
	//mise en place doubleSkip
	boolean doubleSkip;
	String beginSkip;
	String endSkip;
	
	public QuestionReturn(){
		validate = true;
		gotSkipTo= false;
		questionSkip = "";
		questionTagSkip= new ArrayList<String>();
		questionDisqualifs =new ArrayList<String>();
		loopPart = new ArrayList<SkipCondition>();
		conditions = new ArrayList<MultipleCondition>();
		loopNumber="";
	}
	public QuestionReturn (boolean val, boolean gotSkip, String q,int questionNum,String etudename){
		validate = val;
		gotSkipTo=gotSkip;
		if(gotSkipTo){
			questionSkip=q;
		} 
		questionTagSkip= new ArrayList<String>();
		loopPart = new ArrayList<SkipCondition>();
		questionDisqualifs =new ArrayList<String>();
		conditions = new ArrayList<MultipleCondition>();
		this.etudename = etudename;
		loopNumber="";
	}
	public QuestionReturn (boolean val,String etudename){
		validate = val;
		gotSkipTo=false;
		questionNumber=-1;
		questionSkip="";
		loopPart = new ArrayList<SkipCondition>();
		questionDisqualifs =new ArrayList<String>();
		conditions = new ArrayList<MultipleCondition>();
		this.etudename = etudename;
		loopNumber="";
	}
	public void setQuestionNumber(){
		String temp = questionSkip.split("@")[0];
		temp = temp.replaceAll("[^\\d.]", "");
		if(!temp.isEmpty()){
			questionNumber = Integer.valueOf(temp);
		}
		questionTagSkip= new ArrayList<String>();
	}
	public boolean isValidate() {
		return validate;
	}
	public void addToTag(String tag)
	{
		questionTagSkip.add(tag);
	}
	public void setValidate(boolean validate) {
		this.validate = validate;
	}
	public boolean isGotSkipTo() {
		return gotSkipTo;
	}
	public void setGotSkipTo(boolean gotSkipTo) {
		this.gotSkipTo = gotSkipTo;
	}
	public String getQuestionSkip() {
		return questionSkip;
	}
	public void setQuestionSkip(String questionSkip) {
		this.questionSkip = questionSkip;
	}
}
