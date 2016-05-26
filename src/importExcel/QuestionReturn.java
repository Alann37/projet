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
	List<String> questionTagSum;
	
	
	//mise en place doubleSkip
	boolean doubleSkip;
	String beginSkip;
	String endSkip;
	
	public QuestionReturn(){
		validate = false;
		gotSkipTo= false;
		questionSkip = "";
		questionTagSkip= new ArrayList<String>();
		questionDisqualifs =new ArrayList<String>();
		loopPart = new ArrayList<SkipCondition>();
		conditions = new ArrayList<MultipleCondition>();
		questionTagSum =  new ArrayList<String>();
		loopNumber="";
	}
	public QuestionReturn (boolean val, boolean gotSkip, String q,int questionNum){
		validate = val;
		gotSkipTo=gotSkip;
		if(gotSkipTo){
			questionSkip=q;
		} 
		questionTagSum =  new ArrayList<String>();
		questionTagSkip= new ArrayList<String>();
		loopPart = new ArrayList<SkipCondition>();
		questionDisqualifs =new ArrayList<String>();
		conditions = new ArrayList<MultipleCondition>();
		loopNumber="";
	}
	public QuestionReturn (boolean val){
		validate = val;
		gotSkipTo=false;
		questionNumber=-1;
		questionTagSum =  new ArrayList<String>();
		questionSkip="";
		loopPart = new ArrayList<SkipCondition>();
		questionDisqualifs =new ArrayList<String>();
		conditions = new ArrayList<MultipleCondition>();
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
