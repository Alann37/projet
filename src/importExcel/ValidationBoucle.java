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
	public ValidationBoucle(String s){
		loopPart=s;
		empty=false;
	}
}
