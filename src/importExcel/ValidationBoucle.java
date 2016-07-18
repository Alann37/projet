package importExcel;

public class ValidationBoucle {
	boolean satisfied;
	String loopPart;
	boolean empty;
	public ValidationBoucle(){
		satisfied=false;
		loopPart="";
		empty=false;
	}
	public ValidationBoucle(boolean b, String s){
		loopPart=s;
		satisfied=b;
		empty = false;
	}
}
