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
	List<skipCondition> loopPart;
	List<String> questionTagSkip;
	public QuestionReturn(){
		validate = false;
		gotSkipTo= false;
		questionSkip = "";
		questionTagSkip= new ArrayList<String>();
		questionDisqualifs =new ArrayList<String>();
		loopPart = new ArrayList<skipCondition>();
	}
	public QuestionReturn (boolean val, boolean gotSkip, String q,int questionNum){
		validate = val;
		gotSkipTo=gotSkip;
		if(gotSkipTo){
			questionSkip=q;
		} 
		questionTagSkip= new ArrayList<String>();
		loopPart = new ArrayList<skipCondition>();
		questionDisqualifs =new ArrayList<String>();
	}
	public QuestionReturn (boolean val){
		validate = val;
		gotSkipTo=false;
		questionNumber=-1;
		questionSkip="";
		loopPart = new ArrayList<skipCondition>();
		questionDisqualifs =new ArrayList<String>();
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
