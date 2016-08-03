

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import Configuration.Configuration;
import ErrorLog.Error;
import Graphic.MainView;
import baseLibelle.ExportLibeleBase;
import baseLibelle.ImportTxt;
import baseLibelle.StudyQuotas;
import importExcel.Filter;
import importExcel.ReadExcel;

import importExcel.TraitementEtude;
import importMSQLServer.InformationBDD;


public class Main {
	private static long chrono;
	static void Go_Chrono() { 
		chrono = java.lang.System.currentTimeMillis() ; 
		} 
	static void Stop_Chrono() { 
		long chrono2 = java.lang.System.currentTimeMillis() ; 
		long temps = chrono2 - chrono ; 
		System.out.println("Temps ecoule = " + temps + " ms") ; 
		} 
	private static void basesQualif() throws IOException, InvalidFormatException, InterruptedException{

		List<TraitementEtude> list = new ArrayList<TraitementEtude>();
    	List<InformationBDD> listBdd = new ArrayList<InformationBDD>();
    	listBdd = ReadExcel.importListBases();
    	int size = 0;
    	System.out.println("début");
    	for(int i = 0 ; i < listBdd.size();i++){
    		size+=listBdd.get(i).getLangues().size();
    	}
    	for(int i = 0 ; i <size; i ++){
    		list.add(new TraitementEtude());
    	}
    	int listIndice = 0;
    	for(int i = 0 ; i < listBdd.size();i++){
    		for(int j = 0 ; j < listBdd.get(i).getLangues().size();j++){
    			list.get(listIndice).setEtudeName(listBdd.get(i).getBase()+listBdd.get(i).getLangues().get(j));
    			list.get(listIndice).setBaseAndLanguage(listBdd.get(i).getLangues().get(j), listBdd.get(i).getBase());
    			list.get(listIndice).setServeur(listBdd.get(i).getServeur());
    			list.get(listIndice).setValidationGuide(listBdd.get(i).getValidationGuideName());
    			listIndice++;
    		}
    	}
    //	System.out.println("mise en place des masters");
    
  //  	File[] masters = filterMaster.finder(System.getProperty("user.dir")+"\\ValidationGuide",".docx");
    	for(int i = 0 ; i < list.size();i++){
    		if(list.get(i).getValidationGuide()!=null){
    			File f = new File(System.getProperty("user.dir")+"\\ValidationGuide\\"+list.get(i).getValidationGuide());
    			list.get(i).setQuestion(ReadExcel.importConditionFromWord(f));
    			System.out.println("passage pour " + f.getName());
    		}
    	}
		int threadCount = 0;
		System.out.println("size = " +list.size());
		
		do{
			threadCount = 0;
			for(int i = 0 ; i < list.size(); i ++){
			//	System.out.println(i + "  " + list.get(i).getState());
				if(list.get(i).getState()==Thread.State.TERMINATED){
					list.remove(i);
					i--;
			
				}else if(list.get(i).getState()==Thread.State.RUNNABLE){
					threadCount ++;
				}
			}
			if(list.size()==0){
				break;
			}
		
			//lProgress.setText(endCount+"%");
	
			if(threadCount != 2 ){
				for(int i = 0 ; i < list.size();i++){
				//	System.out.println(i + "  " + list.get(i).getState());
					if(list.get(i).getState()==Thread.State.NEW && threadCount<2){
						threadCount++;
						Thread.currentThread();
						Thread.sleep(5000);
			//			System.out.println("throw new thread " + threadCount);
						list.get(i).start();
						
					}
					
				}
			}
		}while(list.size()!=0);
	}
	public static void createMaster() throws IOException{
		Filter printStudyFilter = new Filter ();
		File[] printStudys=null;
    	
			printStudys = printStudyFilter.finder(System.getProperty("user.dir")+"\\PrintStudy",".txt");
		
		List<String> exportMaster = new ArrayList<String>();
		for(int i = 0 ; i < printStudys.length;i++){
			try {
				exportMaster= ImportTxt.getQuestionList(printStudys[i]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Error er = new Error(e.getMessage()); er.printError();
			}
			String temp =printStudys[i].getName();
		/*	temp=temp.replaceAll("Print","");
			temp=temp.replaceAll(".txt","");
			temp= temp.replaceAll("[^\\d.]", "");*/
			ExportLibeleBase.setMasterWithPrintStudy(exportMaster,"E"+temp+"-");
		}
	}
	public static void exportQuotas(){
		try {
			Filter a = new Filter();
			File[] printStudys = a.finder(System.getProperty("user.dir")+"\\PrintStudy", ".txt");
			List<StudyQuotas> list = new ArrayList<StudyQuotas>();
			for(int i =0 ; i < printStudys.length;i++){
				String temp = printStudys[i].getName();
				temp =temp.replaceAll("[^\\d.]", "");
				temp = "Quota" + temp+"xlsx"; 
				
				list.add(ImportTxt.getQuota(printStudys[i]));
				
			}
			String name="";
			for(int i = 0 ; i < list.size();i++){
				name = list.get(i).getName();
				List<StudyQuotas> temp = new ArrayList<StudyQuotas>();
				temp.add(list.get(i));
				for(int j = i+1 ; j < list.size();j++){
					if(list.get(j).getName().equals(name)){
						temp.add(list.get(j));
					}
				}
				String tempName = list.get(i).getPrintStudyName();
				tempName=tempName.replaceAll("Print","");
				String fileName="";
				boolean fill = true;
				for(int k = 0 ; k < tempName.length();k++){
		
					if(Character.isDigit(tempName.charAt(k)) && fill){
						fileName+=String.valueOf(tempName.charAt(k));
					} else {
						fill = false;
					}
					if(!fill){
						break;
					}
				}
				
				fileName = "Quota" + fileName + ".xlsx";
				File f2 = new File(System.getProperty("user.dir")+"\\QuotasBruts\\"+fileName);
				System.out.println("ecriture de " + f2.getPath());
				ReadExcel.exportQuota(temp,f2);
				int  remove = 0;
				for(int j = i ; j <list.size();j++){
					if(list.get(j).getName().equals(name)){
						list.remove(j);
						j--;
						remove ++;
					}
				}
				if(remove>0){
					i--;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
    public static void main(String[] args) throws IOException, InvalidFormatException, InterruptedException, PropertyVetoException  {
    	Go_Chrono();
    	/*MainView m = new MainView();
    	m.visible(true);//*/
    	String sPath = "";
    	
    
    	  	
    	sPath = Configuration.getConf(0);
	
		  Calendar cal = Calendar.getInstance();
	 	  int month = cal.get(Calendar.MONTH)+1;
	 	  String sMonth ="";
	 	  if(month<10){
	 		  sMonth = "0"+String.valueOf(month);
	 	  } else {
	 		 sMonth = String.valueOf(month);
	 	  }
	 	  String sDate = ""+cal.get(Calendar.DAY_OF_MONTH)+"." + sMonth +"."+ cal.get(Calendar.YEAR);
	 	  sPath+="\\"+sDate+"\\";			
	 	  if(!Files.isDirectory(Paths.get(sPath), LinkOption.NOFOLLOW_LINKS)){
			File folder = new File(sPath);
			folder.mkdir();	
	 	  } 

	 	  Configuration.setConfig(1,sPath);

				
		  basesQualif();
	   
    /*	System.out.println("fin qualif debut macro vba");
    	ReadExcel.callExcelMacro();
    	System.out.println("fin");*/
    	// */
    	//exportQuotas();*/
				
			//exportQuotas();
			createMaster();
			
    	//ReadExcel.callExcelMacro();
			Stop_Chrono();
    }
}
