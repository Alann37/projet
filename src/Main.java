

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.text.ParseException;

import Graphic.MainView;
import importMSQLServer.connectURL;

public class Main {
	static long chrono = 0 ;
	static void Go_Chrono() { 
		chrono = java.lang.System.currentTimeMillis() ; 
		} 
	static void Stop_Chrono() { 
		long chrono2 = java.lang.System.currentTimeMillis() ; 
		long temps = chrono2 - chrono ; 
		System.out.println("Temps ecoule = " + temps + " ms") ; 
		} 
    public static void main(String[] args) throws IOException, ParseException, PropertyVetoException {
    	/*Go_Chrono();
    	connectURL.test();
    	Stop_Chrono();*/
    	MainView b = new MainView();
    	b.visible(true);
    }
}
