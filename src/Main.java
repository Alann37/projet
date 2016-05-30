

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import Configuration.Configuration;
import Graphic.MainView;
import Graphic.OptionView;
import importExcel.Filter;
import importExcel.ReadExcel;
import importExcel.TraitementEntrer;
import importExcel.TraitementEtude;
import importMSQLServer.InformationBDD;
import importMSQLServer.ConnectURL;

public class Main {
	static long chrono = 0 ;
	int thread=0;
	private static void tryForExcel(TraitementEtude t) throws IOException, InvalidFormatException{
		File f2 = new File("C:\\Users\\dbinet.APLUSA\\Desktop\\Projet\\TraitementBDD\\BasesQualif\\"+t.getEtudeName()+" base qualif.xlsx");
		boolean ret = false;
		
			ret = ReadExcel.test(f2,t.getEtudes());
			t.setHaveBeenWrite(ret);
	}
			
	static void Go_Chrono() { 
		chrono = java.lang.System.currentTimeMillis() ; 
		System.out.println(chrono);
		} 
	static long Stop_Chrono() { 
		long chrono2 = java.lang.System.currentTimeMillis() ;
		
		long temps = chrono2 - chrono ; 
		System.out.println("Temps ecoule = " + temps + " ms") ; 
		return temps;
		} 
    public static void main(String[] args) throws IOException, ParseException, PropertyVetoException, InterruptedException, InvalidFormatException {
    	MainView m = new MainView();
    	m.visible(true);
    }
}
