package traitement;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author dbinet
 *
 *classe représentant un répondant
 *
 */
public class TraitementEntrer {
	private List<Reponse> reponses;
	private boolean disqualif;
	public TraitementEntrer(){
		disqualif = false;
		reponses = new ArrayList<Reponse>();	
	}
	public List<Reponse> getReponses() {
		return reponses;
	}
	public void setReponses(List<Reponse> reponses) {
		this.reponses = reponses;
	}
	public boolean isDisqualif() {
		return disqualif;
	}
	public void setDisqualif(boolean disqualif) {
		this.disqualif = disqualif;
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
	/*
	 *  fonction de mise en place des réponses dans les différentes questions détectée
	 * */
	public void setQuestionName(List<Question> questions){
		for(int i = 0 ; i < reponses.size(); i ++){
			String temp = reponses.get(i).getQuestionTag();
			
			if(temp.contains(".")){
				temp = temp.split("\\.")[0];
			}
			if(temp.contains("_") && !temp.contains("date")){
				temp = temp.split("_")[0];
			}
			for(int j = 0 ; j< questions.size();j++){
				String s = questions.get(j).name;
				s = s.replaceAll("\\s+", "");
				s=s.replaceAll(" ", "");
				if(reponses.get(i).questionTag.equals(s)){
					reponses.get(i).questionName=questions.get(j).name;
					reponses.get(i).isSetOnQuestion=true;
				}else if(temp.equals(s) && !reponses.get(i).isSetOnQuestion ){
					reponses.get(i).questionName=s;
					questions.get(j).setName(s);
					//System.out.println("test questiontag " + reponses.get(i).questionTag + " et question name "+reponses.get(i).questionName);
					reponses.get(i).isSetOnQuestion=true;
				}
			}
		}
	}
}
