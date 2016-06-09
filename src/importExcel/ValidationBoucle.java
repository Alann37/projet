package importExcel;

public class ValidationBoucle {
	boolean satisfied;
	String loopPart;
	public ValidationBoucle(){
		satisfied=false;
		loopPart="";
		
	}
	public ValidationBoucle(boolean b, String s){
		loopPart=s;
		satisfied=b;
	}
}
