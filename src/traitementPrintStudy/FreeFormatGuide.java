package traitementPrintStudy;
/**
 * 
 * @author dbinet
 *
 *classe servant au traitement des free format lors de la cr�ation des validationguide dans ImportTxt
 */
public class FreeFormatGuide {
	String questionName;
	String variableName;
	public FreeFormatGuide(String s){
		questionName=s;
	}
	public FreeFormatGuide(String s,String n){
		questionName=s;
		variableName=n;
	}
}
