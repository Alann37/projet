package importExcel;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import ErrorLog.Error;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import Configuration.Configuration;

import importMSQLServer.ConnectURL;
public class TraitementEtude extends Thread {
	private String etudeName;
	private List<Question> questions;
	private List<TraitementEntrer> etudes;
	private boolean isTreated;
	private boolean onTreatment;
	private boolean haveBeenWrite;
	public String getValidationGuide() {
		return validationGuide;
	}
	public void setValidationGuide(String validationGuide) {
		this.validationGuide = validationGuide;
	}
	private String language ;
	private String serveur;
	private String validationGuide;
	public String getServeur() {
		return serveur;
	}
	public void setServeur(String serveur) {
		this.serveur = serveur;
	}
	private String base;
	public void setBaseAndLanguage(String l, String b){
		language=l;
		base = b;
	}
	public String getEtudeName() {
		return etudeName;
	}
	public void setEtudeName(String etudeName) {
		this.etudeName = etudeName;
	}
	public List<Question> getQuestion() {
		return questions;
	}
	public void setQuestion(List<Question> question) {
		this.questions = question;
	}
	@Override
	public void run(){
		System.out.println("début de "+etudeName);
		ConnectURL connectionWithDB = new ConnectURL();
		try {
			etudes = connectionWithDB.importDatabase(base, language,serveur);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println("xfin import bdd pour "+ etudeName);
		onTreatment=true;
		
		
		checkEtude();
		System.out.println("isTreated " + etudeName);
		do{
			File f2 = null;
			try {
				String sPath = "";
				sPath = Configuration.getConf(1);
				
				f2 = new File(sPath+this.getEtudeName()+" base qualif.xlsx");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				Error er = new Error(e1.getMessage());
				try {
					er.printError();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				try {
					if(f2!=null){
						this.setHaveBeenWrite(ReadExcel.exportBaseExcel(f2,this));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Error er = new Error(e.getMessage());
					try {
						er.printError();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (InvalidFormatException e) {
					// TODO Auto-generated catch block
					Error er = new Error(e.getMessage());
					try {
						er.printError();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

		} while(!this.haveBeenWrite);
		
	
	}
	@Override
	public String toString() {
		String sRet = etudeName;
		for(int i = 0 ; i < questions.size(); i ++){
			sRet+="\n";
			sRet+=questions.get(i).name;
			for(int j = 0 ; j < questions.get(i).conditions.size();j++){
				sRet+="\n";
				sRet+="   " + questions.get(i).conditions.get(j);
				
			}
		}
		return sRet;
	}
	public TraitementEtude(String etudeName, List<Question> question) {
		this.etudeName = etudeName;
		this.questions = question;
		etudes= new ArrayList<TraitementEntrer>();
		
	}
	public List<TraitementEntrer> getEtudes() {
		return etudes;
	}
	public void setEtudes(List<TraitementEntrer> etudes) {
		this.etudes = etudes;
	}
	public void cleanAnswer(){

	}
	public void checkEtude(){

		for(int i = 0 ; i < etudes.size(); i ++){
			if(i%200==0){
				System.out.println("passage pour i = " + i +"/"+etudes.size()+ " et études : "+ etudeName);
			}
			TraitementEntrer temp = etudes.get(i);
			
			if(questions.size()>0){
				temp.setQuestionName(questions);
				for(int j = 0 ; j < temp.getReponses().size();j++){
					Reponse rTemp = temp.getReponses().get(j);
					if(rTemp.questionName!=null){
						for(int h = 0 ; h < questions.size() ; h ++){
							if(questions.get(h).name.equals(rTemp.questionName)){
								questions.get(h).reponses.add(rTemp);
								//h=questions.size();
							}
							questions.get(h).setNa();
						}
					}
				}
				
				QuestionReturn skipTo= new QuestionReturn(true,false,"",-1,etudeName) ;
				for(int t = 0 ; t < questions.size(); t++){
					skipTo.validate=true;
					//System.out.println("originalQuestion "+ questions.get(t).name + " with replace "+questions.get(t).questionNumber);
						QuestionReturn returnQuest = questions.get(t).questionTreatement(skipTo);
						if(!returnQuest.validate){
							temp.setDisqualif(true);
						}
						skipTo = returnQuest;
				} 
				for(int o = 0 ; o <questions.size() ; o ++){
					questions.get(o).reponses.clear();
				}

 				skipTo.setSpecific();
				for(int m = 0 ; m < skipTo.specificC.size();m++){
					for(int n = 0 ; n < 	skipTo.specificC.get(m).conditions.size();n++){
						skipTo.specificC.get(m).conditions.get(n).answers.clear();
						skipTo.specificC.get(m).conditions.get(n).loop.clear();
						if(!skipTo.specificC.get(m).conditions.get(n).isLink){
							skipTo.specificC.get(m).conditions.get(n).c.valueCondition.clear();
						}
						skipTo.specificC.get(m).conditions.get(n).inLoop=false;
					}
				}
				skipTo.specificC.clear();
				this.etudes.set(i, temp);
			}
		}		
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public boolean isOnTreatment(){
		return onTreatment;
	}
	public boolean isTreated(){
		return isTreated;
	}
	public TraitementEtude(){
		super();
		onTreatment=false;
		haveBeenWrite=false;
		isTreated=false;
		questions= new ArrayList<Question>();
		etudes= new ArrayList<TraitementEntrer>();
		serveur = "";
		language="";
		base="";
		
	}
	public boolean isHaveBeenWrite() {
		return haveBeenWrite;
	}
	public void setHaveBeenWrite(boolean haveBeenWrite) {
		this.haveBeenWrite = haveBeenWrite;
	}
	
}
