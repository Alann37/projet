package importExcel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Configuration.Configuration;
public class TraitementEtude extends Thread {
	private String etudeName;
	private List<Question> questions;
	private List<TraitementEntrer> etudes;
	private boolean isTreated;
	private boolean onTreatment;
	private boolean haveBeenWrite;
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
		/*String temp = etudeName.substring(etudeName.length()-2);
		connectURL t = new connectURL();
		etudes = t.test(temp);*/
		onTreatment=true;
		System.out.println("début de "+etudeName);
		
		checkEtude();
		System.out.println("isTreated " + etudeName);
		do{
			File f2 = null;
			try {
				f2 = new File(Configuration.getConf(1)+"\\"+this.getEtudeName()+" base qualif.xlsx");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				try {
					if(f2!=null){
						this.setHaveBeenWrite(ReadExcel.test(f2,this.getEtudes()));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		/*int[] success = new int[etudes.size()];
		int numberFail = 0;
		int unfail = 0;
		*/
		for(int i = 0 ; i < etudes.size(); i ++){
			if(i%200==0){
				System.out.println("passage pour i = " + i + " et études : "+ etudeName);
			}
			TraitementEntrer temp = etudes.get(i);*
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
						//numberFail++;
						temp.setQuestionDisqualif(returnQuest.questionDisqualifs);
						
					}
					skipTo = returnQuest;
					   
				
				} 
				for(int o = 0 ; o <questions.size() ; o ++){
					questions.get(o).reponses.clear();
				}
				for(int p = 0 ; p<temp.getReponses().size();p++){
					if( temp.getReponses().get(p).shouldBeEmpty){
						temp.addNotToBe(temp.getReponses().get(p).questionTag);
					}
				}
				this.etudes.set(i, temp);
			}
		}
	/*	for(int i = 0; i<etudes.size();i++){
			if(!etudes.get(i).isDisqualif()){
				success[unfail]=i;
				unfail++;
			}
		}
		System.out.println("Number of disqualif : "+numberFail +" on " + etudes.size() + " study");
		for(int i = 0 ; i < unfail; i++){
			//System.out.println("complete study is number : " + success[i]);
		}*/
		
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
		
	}
	public boolean isHaveBeenWrite() {
		return haveBeenWrite;
	}
	public void setHaveBeenWrite(boolean haveBeenWrite) {
		this.haveBeenWrite = haveBeenWrite;
	}
	
}
