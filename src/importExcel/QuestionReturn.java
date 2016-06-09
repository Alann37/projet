package importExcel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.synth.SynthSpinnerUI;

public class QuestionReturn {
	boolean validate;
	boolean gotSkipTo;
	String questionSkip;
	
	int questionNumber;
	boolean isAnswer;
	List<String> questionDisqualifs;
	List<SkipCondition> loopPart;
	List<String> questionTagSkip;
	List<MultipleCondition> conditions;
	List<AndCondition> andConditions;
	List<SpecificList> specificC;
	double sum;
	boolean isConstSum;
	boolean isLoop;
	String loopNumber;
	List<String> questionTagSum;
	String etudename;
	
	
	//mise en place doubleSkip
	boolean doubleSkip;
	String beginSkip;
	String endSkip;
	
	public QuestionReturn(){
		validate = true;
		gotSkipTo= false;
		questionSkip = "";
		questionTagSkip= new ArrayList<String>();
		questionDisqualifs =new ArrayList<String>();
		loopPart = new ArrayList<SkipCondition>();
		conditions = new ArrayList<MultipleCondition>();
		questionTagSum =  new ArrayList<String>();
		andConditions = new ArrayList<AndCondition>();
		specificC = new ArrayList<SpecificList>();
		
		loopNumber="";
	}
	public QuestionReturn (boolean val, boolean gotSkip, String q,int questionNum,String etudename){
		validate = val;
		gotSkipTo=gotSkip;
		if(gotSkipTo){
			questionSkip=q;
		} 
		questionTagSum =  new ArrayList<String>();
		questionTagSkip= new ArrayList<String>();
		loopPart = new ArrayList<SkipCondition>();
		questionDisqualifs =new ArrayList<String>();
		conditions = new ArrayList<MultipleCondition>();
		this.etudename = etudename;
		loopNumber="";
		specificC = new ArrayList<SpecificList>();
		andConditions = new ArrayList<AndCondition>();
	}
	public QuestionReturn (boolean val,String etudename){
		validate = val;
		gotSkipTo=false;
		questionNumber=-1;
		questionTagSum =  new ArrayList<String>();
		questionSkip="";
		loopPart = new ArrayList<SkipCondition>();
		questionDisqualifs =new ArrayList<String>();
		conditions = new ArrayList<MultipleCondition>();
		this.etudename = etudename;
		loopNumber="";
		andConditions = new ArrayList<AndCondition>();
		specificC = new ArrayList<SpecificList>();
	}
	public void setQuestionNumber(){
		String temp = questionSkip.split("@")[0];
		temp = temp.replaceAll("[^\\d.]", "");
		if(!temp.isEmpty()){
			questionNumber = Integer.valueOf(temp);
		}
		questionTagSkip= new ArrayList<String>();
	}
	public boolean isValidate() {
		return validate;
	}
	public void addToTag(String tag)
	{
		questionTagSkip.add(tag);
	}
	public void setValidate(boolean validate) {
		this.validate = validate;
	}
	public boolean isGotSkipTo() {
		return gotSkipTo;
	}
	public void setGotSkipTo(boolean gotSkipTo) {
		this.gotSkipTo = gotSkipTo;
	}
	public String getQuestionSkip() {
		return questionSkip;
	}
	
	public void setSpecific(){
	
		boolean init = false;
		boolean disqu=false;
		QuestionReturn qRet = new QuestionReturn(true, this.etudename);
		Question q= new Question("");
		
		Question qu = new Question("");

		List<SpecificCondition> cs = new ArrayList<SpecificCondition>();
		List<SpecificCondition> temp = new ArrayList<SpecificCondition>();
		for(int i = 0 ; i < specificC.size();i++){
		
			for(int n = 0 ;n < specificC.get(i).conditions.get(0).answers.size();n++){
				qRet = qu.gestionTypeCondition(qRet, specificC.get(i).conditions.get(0).c, specificC.get(i).conditions.get(0).answers.get(n));
				if(specificC.get(i).conditions.get(0).answers.get(n).isPartOfLoop()){
					specificC.get(i).conditions.get(0).loop.add(new ValidationBoucle(qRet.validate,specificC.get(i).conditions.get(0).answers.get(n).questionTag.split("\\.")[1]));
					specificC.get(i).conditions.get(0).inLoop=true;
				}
			}	
				specificC.get(i).conditions.get(0).satisfied=qRet.validate;
				
				cs.add(specificC.get(i).conditions.get(0));
				
				cs.get(0).indice=0;
				for(int j = 1 ; j < specificC.get(i).conditions.size();j++){
				disqu = false;
				init = false;
	
				
				if(specificC.get(i).conditions.get(j).isLink){
					cs.add(specificC.get(i).conditions.get(j));
					cs.get(cs.size()-1).indice=cs.size()-1;
					boolean type = false;
					if(j+1<specificC.get(i).conditions.size()){
						if(j>0){
							if((specificC.get(i).conditions.get(j).braketPlace== (specificC.get(i).conditions.get(j-1).braketPlace-1)) &&( specificC.get(i).conditions.get(j).braketPlace== (specificC.get(i).conditions.get(j+1).braketPlace-1))&& (!specificC.get(i).conditions.get(j-1).treated && !specificC.get(i).conditions.get(j+1).treated))
							{
								init = true;
								for(int n = 0 ; n < specificC.get(i).conditions.get(j-1).answers.size() && n < specificC.get(i).conditions.get(j+1).answers.size();n++){
									
									qRet.validate=true;
									qRet = qu.gestionTypeCondition(qRet, specificC.get(i).conditions.get(j-1).c, specificC.get(i).conditions.get(j-1).answers.get(n));
									if(specificC.get(i).conditions.get(j-1).answers.get(n).isPartOfLoop()){
										specificC.get(i).conditions.get(j-1).loop.add(new ValidationBoucle(qRet.validate,specificC.get(i).conditions.get(j-1).answers.get(n).questionTag.split("\\.")[1]));
									}
									if(specificC.get(i).conditions.get(j).link.contains("OR")){
										type=false;
									} else {
										type = true;
									}
									AndCondition an = new AndCondition(specificC.get(i).conditions.get(j-1).answers.get(n),qRet.validate, specificC.get(i).conditions.get(j+1), type);
									qRet.validate=true;
									qRet= qu.gestionAndOr(qRet, specificC.get(i).conditions.get(j+1).answers.get(n), an);
									
									if(specificC.get(i).conditions.get(j+1).answers.get(n).isPartOfLoop()){
										specificC.get(i).conditions.get(j+1).loop.add(new ValidationBoucle(qRet.validate,specificC.get(i).conditions.get(j+1).answers.get(n).questionTag.split("\\.")[1]));
									}
									disqu = qRet.validate;
									
								}
						
						}
							
						}
						if(init){
							if(specificC.get(i).conditions.get(j-1).loop.size()==0){
								if(type){
									
									specificC.get(i).conditions.get(j).satisfied=disqu;
									specificC.get(i).conditions.get(j).treated=true;
									specificC.get(i).conditions.get(j-1).treated=true;
									specificC.get(i).conditions.get(j+1).treated=true;
								}else {
									specificC.get(i).conditions.get(j).satisfied=disqu;
									specificC.get(i).conditions.get(j).treated=true;
									specificC.get(i).conditions.get(j-1).treated=true;
									specificC.get(i).conditions.get(j+1).treated=true;
								}
							} else {
								for(int t = 0 ; t < specificC.get(i).conditions.get(j+1).loop.size();t++){
									specificC.get(i).conditions.get(j).loop.add(specificC.get(i).conditions.get(j+1).loop.get(t));
								}
								specificC.get(i).conditions.get(j).inLoop=true;
								specificC.get(i).conditions.get(j).treated=true;
								specificC.get(i).conditions.get(j-1).treated=true;
								specificC.get(i).conditions.get(j+1).treated=true;
								specificC.get(i).conditions.get(j-1).inLoop=true;
								specificC.get(i).conditions.get(j+1).inLoop=true;
							}
						}
					}
				}
			}
				cs.get(0).treated=true;
				int up=-2;
			int nbrTreated=0;
			do{
				
				nbrTreated=0;
				for(int j= 0 ; j < cs.size();j++){
					if(cs.get(j).treated){
						nbrTreated++;
					}
					if(cs.get(j).braketPlace>up && !cs.get(j).treated){
						temp.clear();
						up = cs.get(j).braketPlace;
						temp.add(cs.get(j));
					}
					if(cs.get(j).braketPlace==up+1 && cs.get(j).treated){
						temp.add(cs.get(j));
					}
					if(temp.size()==3){
						if(!temp.get(1).inLoop){
							if(temp.get(0).link.contains("OR")){
								if(temp.get(1).satisfied ||temp.get(2).satisfied){
									cs.get(temp.get(0).indice).satisfied=true;
								}
							}else {
								if(temp.get(1).satisfied &&temp.get(2).satisfied){
									cs.get(temp.get(0).indice).satisfied=true;
								}	
							}
						}else {
							for(int k=0;k < temp.get(1).loop.size() && k < temp.get(2).loop.size();k++){
								if(temp.get(0).link.contains("OR")){
									if(temp.get(0).inLoop){
										if(temp.get(0).loop.get(k).loopPart.equals(temp.get(2).loop.get(k).loopPart) &&temp.get(1).loop.get(k).loopPart.equals(temp.get(2).loop.get(k).loopPart)){
											if(temp.get(1).loop.get(k).satisfied ||temp.get(2).loop.get(k).satisfied){
												cs.get(temp.get(0).indice).loop.get(k).satisfied=true;
											}
										}
									} else {
										if(temp.get(1).loop.get(k).satisfied ||temp.get(2).loop.get(k).satisfied){
											cs.get(temp.get(0).indice).loop.add(temp.get(2).loop.get(k));
										}
									}
								}else {
									if(temp.get(0).inLoop){
										if(temp.get(0).loop.get(k).loopPart.equals(temp.get(2).loop.get(k).loopPart) &&temp.get(1).loop.get(k).loopPart.equals(temp.get(2).loop.get(k).loopPart)){
											if(temp.get(1).loop.get(k).satisfied &&temp.get(2).loop.get(k).satisfied){
												cs.get(temp.get(0).indice).loop.get(k).satisfied=true;
											}
											else {
												cs.get(temp.get(0).indice).loop.get(k).satisfied=false;
											}
										}
									}  else {
										if(temp.get(1).loop.get(k).satisfied &&temp.get(2).loop.get(k).satisfied){
											cs.get(temp.get(0).indice).loop.add(temp.get(2).loop.get(k));
										} else {
											cs.get(temp.get(0).indice).loop.add(temp.get(2).loop.get(k));
											cs.get(temp.get(0).indice).loop.get(cs.get(temp.get(0).indice).loop.size()-1).satisfied=false;
										}
									}
								}
							}
						}
						cs.get(temp.get(0).indice).treated=true;
						temp.clear();
						up=-2;
					}
					
				}

			}while(nbrTreated!=cs.size());
			SpecificCondition pillier=null;
			int lower = 5;
			int ind=-1;
			for(int j = 0 ; j < cs.size();j++){
				if(cs.get(j).braketPlace<lower){
					ind=j;
					lower = cs.get(j).braketPlace;
				}
				if(cs.get(j).braketPlace==0){
					pillier = cs.get(j);
					break;
					
				}
			}
			if(pillier==null && ind>0 && ind < cs.size()){
				pillier = cs.get(ind);
			}

			for(int j = 0 ; j < specificC.get(i).conditions.size();j++){
				for(int n = 0 ; n < specificC.get(i).conditions.get(j).answers.size();n++){
					if(!pillier.loop.isEmpty()){
						for(int m = 0 ; m < pillier.loop.size();m++){
							if(pillier.loop.get(m).loopPart.equals(specificC.get(i).conditions.get(j).answers.get(n).questionTag.split("\\.")[1])){
								specificC.get(i).conditions.get(j).answers.get(n).disqualif = ! pillier.loop.get(m).satisfied;
							}
						}
					}else {
						specificC.get(i).conditions.get(j).answers.get(n).disqualif = !pillier.satisfied;
					}
				}
				if(!specificC.get(i).conditions.get(j).equals(pillier)){
					specificC.get(i).conditions.get(j).satisfied=false;
					specificC.get(i).conditions.get(j).treated=false;
				}
				
			}
			pillier.satisfied=false;
			pillier.treated=false;
			cs.clear();
			
		}		
	}
	public void setQuestionSkip(String questionSkip) {
		this.questionSkip = questionSkip;
	}
}
