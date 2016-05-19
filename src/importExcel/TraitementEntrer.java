package importExcel;

import java.util.ArrayList;
import java.util.List;

public class TraitementEntrer {
	private List<Reponse> reponses;
	private String numEntrer;
	private boolean disqualif;
	private List<String> questionDisqualif;
	private List<skipCondition> notToBeAnswer;
	public TraitementEntrer(){
		disqualif = false;
		numEntrer ="";
		reponses = new ArrayList<Reponse>();
		notToBeAnswer = new ArrayList<skipCondition>();
		questionDisqualif = new ArrayList<String>();
		
	}
	public void addNotToBe(skipCondition quest){
		notToBeAnswer.add(quest);
	}

	public List<skipCondition> getNotToBeAnswer(){
		return notToBeAnswer; 
	}
	public List<Reponse> getReponses() {
		return reponses;
	}

	public void setReponses(List<Reponse> reponses) {
		this.reponses = reponses;
	}

	public String getNumEntrer() {
		return numEntrer;
	}
	
	public int[] getDisqualifCell(){
		int[] iRet = new int[questionDisqualif.size()];
		int j = 0;
		if(questionDisqualif.size()>0){
			for(int i =0 ; i < reponses.size();i++){
				if(reponses.get(i).disqualif){
					iRet[j] = reponses.get(i).cellPosition; 
				}
			}
		}
		return iRet;
	}

	public void setNumEntrer(String numEntrer) {
		this.numEntrer = numEntrer;
	}

	public boolean isDisqualif() {
		return disqualif;
	}

	public void setDisqualif(boolean disqualif) {
		this.disqualif = disqualif;
	}

	public List<String> getQuestionDisqualif() {
		return questionDisqualif;
	}

	public void setQuestionDisqualif(String questionDisqualif) {
	
		this.questionDisqualif.add(questionDisqualif);
	}

	@Override
	public String toString() {
		String sRet = "";
		for(int i = 0 ; i < reponses.size(); i ++){
			sRet+=reponses.get(i).toString();
			sRet+="\n";
		}
		return sRet;
	}
	
	public void setQuestionName(List<Question> questions){
		for(int i = 0 ; i < reponses.size(); i ++){
			for(int j = 0 ; j< questions.size();j++){
				String s = questions.get(j).name;
				s = s.replaceAll("\\s+", "");
				if(reponses.get(i).questionTag.contains(s) ){
					reponses.get(i).questionName=questions.get(j).name;
				}
			}
		}
	}
}
