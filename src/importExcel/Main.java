package importExcel;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.text.ParseException;

import Graphic.MainView;

public class Main {
	public void getLibeleBase(){
		
	}
    public static void main(String[] args) throws IOException, ParseException, PropertyVetoException {
    	
    	//LANCEMENT MACRO
    	
        //File excelFile = new File("verif base ext + guide.xlsm");
        //ReadExcel.callExcelMacro(excelFile, "!md_selenium.Extract_Site");
    
    	//LANCEMENT TRAITEMENT BASE
    	MainView b = new MainView();
    	b.visible(true);
    	
    	// IMPORT MULTIPLE BASES LIBELLE
    	
    	
    }
}
