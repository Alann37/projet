package traitement;
/**
 * 
 * @author dbinet
 *
 *classe permettant la gestion des skipto
 *
 */
public class SkipCondition {
	String loopNumber;
	String questionName;
	String questionTo;
	public SkipCondition( String questionName, String loopNumber) {
	
		this.loopNumber = loopNumber;
		this.questionName = questionName;
	}
	public SkipCondition (String question ,String to , boolean b){
		questionName=question;
		questionTo=to;
	}
	public SkipCondition( String questionName,String questionto, String loopNumber) {
		
		this.loopNumber = loopNumber;
		this.questionName = questionName;
		this.questionTo = questionto;
	}
	
}
