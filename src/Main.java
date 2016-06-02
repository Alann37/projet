

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
    public static void main(String[] args) throws IOException, ParseException, PropertyVetoException, InterruptedException, InvalidFormatException {
    	MainView m = new MainView();
    	m.visible(true);
    	
    }
}
