package importExcel;

public class AndCondition {
	boolean firstPart;
	Condition secondPart;
	Reponse firstAnswer;
	String qName;
	String loopPart;
	AndCondition previous;
	boolean andOr; // true si AND , false si OR
	public AndCondition(Reponse a,boolean val , String secondPart,boolean b){
		firstPart=val;
		loopPart ="";
		firstAnswer=a;
		if(secondPart.startsWith(" ")){
			secondPart = secondPart.substring(1);
		}
		qName = "";
		if(secondPart.contains(" ")){
			qName = secondPart.split(" ")[0];
			secondPart = secondPart.replaceAll(qName,"");
			
		}
		if(firstAnswer.questionTag.contains(".")){
			loopPart = firstAnswer.questionTag.split("\\.")[1];
		}
		this.secondPart = new Condition(secondPart);
		previous=null;
		andOr = b;
	}
	public AndCondition(Reponse a,boolean val , SpecificCondition secondPart,boolean b){
		firstPart=val;
		loopPart ="";
		firstAnswer=a;
		if(firstAnswer.questionTag.contains(".")){
			loopPart = firstAnswer.questionTag.split("\\.")[1];
		}
		qName = secondPart.link;


		this.secondPart = secondPart.c;
		previous=null;
		andOr = b;
	}
	public AndCondition(Reponse a,boolean val , String secondPart,AndCondition p,boolean b){
		firstPart=val;
		previous=p;
		loopPart ="";
		andOr=b;
		firstAnswer=a;
		if(secondPart.startsWith(" ")){
			secondPart = secondPart.substring(1);
		}
		qName = "";
		if(secondPart.contains(" ")){
			qName = secondPart.split(" ")[0];
			secondPart = secondPart.replaceAll(qName,"");
			
		}
		if(firstAnswer.questionTag.contains(".")){
			loopPart = firstAnswer.questionTag.split("\\.")[1];
		}
		this.secondPart = new Condition(secondPart);
	}
}
