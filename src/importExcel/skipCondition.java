package importExcel;

public class SkipCondition {
	String loopNumber;
	String questionName;
	String questionTo;
	public SkipCondition( String questionName, String loopNumber) {
	
		this.loopNumber = loopNumber;
		this.questionName = questionName;
	}
	public SkipCondition( String questionName,String questionto, String loopNumber) {
		
		this.loopNumber = loopNumber;
		this.questionName = questionName;
		this.questionTo = questionto;
	}
	
}
