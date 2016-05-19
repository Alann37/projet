package importExcel;

public class QuestionReturn {
	boolean validate;
	boolean gotSkipTo;
	String questionSkip;
	int questionNumber;
	boolean isAnswer;
	public QuestionReturn(){
		validate = false;
		gotSkipTo= false;
		questionSkip = "";
	}
	public QuestionReturn (boolean val, boolean gotSkip, String q,int questionNum){
		validate = val;
		gotSkipTo=gotSkip;
		if(gotSkipTo){
			questionSkip=q;
		} 
	}
	public QuestionReturn (boolean val){
		validate = val;
		gotSkipTo=false;
		questionNumber=-1;
		questionSkip="";
	}
	public void setQuestionNumber(){
		String temp = questionSkip.split("@")[0];
		temp = temp.replaceAll("[^\\d.]", "");
		if(!temp.isEmpty()){
			questionNumber = Integer.valueOf(temp);
		}
		System.out.println("NUMBER "+questionNumber);
	}
	public boolean isValidate() {
		return validate;
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
