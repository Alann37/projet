package importExcel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.synth.SynthSpinnerUI;
import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;

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
	
	
		QuestionReturn qRet = new QuestionReturn(true, this.etudename);		
		Question qu = new Question("");

		List<SpecificCondition> cs = new ArrayList<SpecificCondition>();
		List<SpecificCondition> temp = new ArrayList<SpecificCondition>();
		for(int i = 0 ; i < specificC.size();i++){
			if(specificC.get(i).countryTag.isEmpty()){
				qRet = traitement(qRet,qu,i,cs,temp);
			}else{
				if(this.etudename.contains(specificC.get(i).countryTag)){
					qRet = traitement(qRet,qu,i,cs,temp);
				}
			}
			
		}		
	}
	private QuestionReturn traitement(QuestionReturn qRet,Question qu,int i,List<SpecificCondition> cs,List<SpecificCondition> temp ){
		boolean init = false;
		boolean disqu=false;
		for(int n = 0 ;n < specificC.get(i).conditions.get(0).answers.size();n++){
			qRet.validate=true;
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
				cs.get(cs.size()-1).indice=j;
				boolean type = false;
				if(j+1<specificC.get(i).conditions.size()){
					if(j>0){
						if((specificC.get(i).conditions.get(j).braketPlace== (specificC.get(i).conditions.get(j-1).braketPlace-1)) &&( specificC.get(i).conditions.get(j).braketPlace== (specificC.get(i).conditions.get(j+1).braketPlace-1)))
						{
							init = true;
							for(int n = 0 ; n < specificC.get(i).conditions.get(j-1).answers.size() && n < specificC.get(i).conditions.get(j+1).answers.size();n++){
								
								qRet.validate=true;
								if(specificC.get(i).conditions.get(j-1).c.type[0]==8){
									qRet = qu.gestionCheckBox(qRet, specificC.get(i).conditions.get(j-1).answers, specificC.get(i).conditions.get(j-1).c);
								}else {
									qRet = qu.gestionTypeCondition(qRet, specificC.get(i).conditions.get(j-1).c, specificC.get(i).conditions.get(j-1).answers.get(n));
								}
								if(specificC.get(i).conditions.get(j-1).answers.get(n).isPartOfLoop()){
									specificC.get(i).conditions.get(j-1).loop.add(new ValidationBoucle(qRet.validate,specificC.get(i).conditions.get(j-1).answers.get(n).questionTag.split("\\.")[1]));
									specificC.get(i).conditions.get(j-1).inLoop=true;
								}
								if(specificC.get(i).conditions.get(j).link.contains("OR")){
									type=false;
								} else {
									type = true;
								}
								AndCondition an = new AndCondition(specificC.get(i).conditions.get(j-1).answers.get(n),qRet.validate, specificC.get(i).conditions.get(j+1), type);
								qRet.validate=true;
								if(specificC.get(i).conditions.get(j+1).c.type[0]==8){
									qRet = qu.gestionAndOrCheckBox(qRet, specificC.get(i).conditions.get(j+1).answers, an);
								}else {
									qRet= qu.gestionAndOr(qRet, specificC.get(i).conditions.get(j+1).answers.get(n), an);
								}
								if(specificC.get(i).conditions.get(j+1).answers.get(n).isPartOfLoop()){
									specificC.get(i).conditions.get(j+1).loop.add(new ValidationBoucle(qRet.validate,specificC.get(i).conditions.get(j+1).answers.get(n).questionTag.split("\\.")[1]));
									specificC.get(i).conditions.get(j+1).inLoop=true;
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
								if(!disqu){
									for(int m = cs.size()-2;m>0;m--){
										if(cs.get(m).braketPlace<cs.get(cs.size()-1).braketPlace){
											break;
										}
										if(cs.get(cs.size()-1).braketPlace==cs.get(m).braketPlace){
											if(cs.get(m).link.contains("AND")){
												cs.get(m).satisfied=cs.get(cs.size()-1).satisfied;
											}
										}
										
									}
								}
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
		if(!specificC.get(i).conditions.get(specificC.get(i).conditions.size()-1).treated){
			if(!cs.get(cs.size()-1).equals(specificC.get(i).conditions.get(specificC.get(i).conditions.size()-1))){
				for(int n = 0 ;n < specificC.get(i).conditions.get(specificC.get(i).conditions.size()-1).answers.size();n++){
					qRet.validate=true;
					qRet = qu.gestionTypeCondition(qRet, specificC.get(i).conditions.get(specificC.get(i).conditions.size()-1).c, specificC.get(i).conditions.get(specificC.get(i).conditions.size()-1).answers.get(n));
					if(specificC.get(i).conditions.get(specificC.get(i).conditions.size()-1).answers.get(n).isPartOfLoop()){
						specificC.get(i).conditions.get(specificC.get(i).conditions.size()-1).loop.add(new ValidationBoucle(qRet.validate,specificC.get(i).conditions.get(specificC.get(i).conditions.size()-1).answers.get(n).questionTag.split("\\.")[1]));
						specificC.get(i).conditions.get(specificC.get(i).conditions.size()-1).inLoop=true;
						
					}
					
				}
				specificC.get(i).conditions.get(specificC.get(i).conditions.size()-1).satisfied=qRet.validate;
				specificC.get(i).conditions.get(specificC.get(i).conditions.size()-1).treated=true;
				cs.add(specificC.get(i).conditions.get(specificC.get(i).conditions.size()-1));
				cs.get(cs.size()-1).indice=specificC.get(i).conditions.size()-1;
			}
		}
		for(int o=0;o<cs.size();o++){
			if((o+1)<cs.size()){
				if(!cs.get(o).treated && !cs.get(o+1).treated){
					if(cs.get(o).braketPlace>cs.get(o+1).braketPlace){
						boolean add=false;
						for(int p= cs.get(o).indice ; p< cs.get(o+1).indice;p++){
							if(specificC.get(i).conditions.get(p).braketPlace==(cs.get(o).braketPlace)+1){
								if(specificC.get(i).conditions.get(p).endBraket==cs.get(o).endBraket ){
									if(!add){
										if(!specificC.get(i).conditions.get(p).treated){
											for(int n = 0 ;n < specificC.get(i).conditions.get(p).answers.size();n++){
												qRet.validate=true;
												qRet = qu.gestionTypeCondition(qRet, specificC.get(i).conditions.get(specificC.get(i).conditions.size()-1).c, specificC.get(i).conditions.get(p).answers.get(n));
												if(specificC.get(i).conditions.get(p).answers.get(n).isPartOfLoop()){
													specificC.get(i).conditions.get(p).loop.add(new ValidationBoucle(qRet.validate,specificC.get(i).conditions.get(p).answers.get(n).questionTag.split("\\.")[1]));
													specificC.get(i).conditions.get(p).inLoop=true;
													
												}
												
											}
											specificC.get(i).conditions.get(p).satisfied=qRet.validate;
											specificC.get(i).conditions.get(p).treated=true;
										}
										cs.add(o+1, specificC.get(i).conditions.get(p));
										cs.get(o+1).indice=o+1;
										add =true;
									}else {
										cs.set(o+1, specificC.get(i).conditions.get(p));
									}
									
								} 
							}
						}
					}
				}
			}
			
		}
		int up=-2;
		int nbrTreated=0;
		int passageDoWhile=0;
	
	
		do{
			
			passageDoWhile++;
			nbrTreated=0;
		
		
			for(int j= 0 ; j < cs.size();j++){
				if(cs.get(j).treated){
					nbrTreated++;
				}else if(cs.get(j).braketPlace==0){
					nbrTreated++;
				}
				if(cs.get(j).braketPlace>up && !cs.get(j).treated){
					temp.clear();
				
					up = cs.get(j).braketPlace;
					temp.add(cs.get(j));
				}
			
				if(cs.get(j).braketPlace==up+1 && cs.get(j).treated && cs.get(j).endBraket == temp.get(0).endBraket ){
					boolean  add=true;
					for(int k=0;k<temp.size();k++){
						if(temp.get(k).indice==cs.get(j).indice){
							add=false;
						}
					}
					if(add){
						temp.add(cs.get(j));
					}
				}
				if(temp.size()==3){
			
					if(!temp.get(1).inLoop){
						if(temp.get(0).link.contains("OR")){
							if(temp.get(1).satisfied ||temp.get(2).satisfied){
								specificC.get(i).conditions.get(temp.get(0).indice).satisfied=true;
							}
						}else {
							if(temp.get(1).satisfied &&temp.get(2).satisfied){
								specificC.get(i).conditions.get(temp.get(0).indice).satisfied=true;
							}	
						}
					}else {
						for(int k=0;k < temp.get(1).loop.size() && k < temp.get(2).loop.size();k++){
							if(temp.get(0).link.contains("OR")){
								if(temp.get(0).inLoop){
									if(temp.get(0).loop.get(k).loopPart.equals(temp.get(2).loop.get(k).loopPart) &&temp.get(1).loop.get(k).loopPart.equals(temp.get(2).loop.get(k).loopPart)){
										if(temp.get(1).loop.get(k).satisfied ||temp.get(2).loop.get(k).satisfied){
											specificC.get(i).conditions.get(temp.get(0).indice).loop.get(k).satisfied=true;
										}
									}
								} else {
									if(temp.get(1).loop.get(k).satisfied ||temp.get(2).loop.get(k).satisfied){
										specificC.get(i).conditions.get(temp.get(0).indice).loop.add(new ValidationBoucle(true,temp.get(1).loop.get(k).loopPart));
										
									}
								}
							}else {
								if(temp.get(0).inLoop){
									if(temp.get(0).loop.get(k).loopPart.equals(temp.get(2).loop.get(k).loopPart) &&temp.get(1).loop.get(k).loopPart.equals(temp.get(2).loop.get(k).loopPart)){
										if(temp.get(1).loop.get(k).satisfied &&temp.get(2).loop.get(k).satisfied){
											specificC.get(i).conditions.get(temp.get(0).indice).loop.get(k).satisfied=true;
										}
										else {
											specificC.get(i).conditions.get(temp.get(0).indice).loop.get(k).satisfied=false;
										}
									}
								}  else {
									if(temp.get(1).loop.get(k).satisfied &&temp.get(2).loop.get(k).satisfied){
										specificC.get(i).conditions.get(temp.get(0).indice).loop.add(new ValidationBoucle(true,temp.get(1).loop.get(k).loopPart));
										
									} else {
										specificC.get(i).conditions.get(temp.get(0).indice).loop.add(temp.get(2).loop.get(k));
										specificC.get(i).conditions.get(temp.get(0).indice).loop.get(specificC.get(i).conditions.get(temp.get(0).indice).loop.size()-1).satisfied=false;
										
									}
								}
							}
						}
					}
					if(temp.get(1).inLoop || temp.get(2).inLoop){
						temp.get(0).inLoop=true;
					}
					specificC.get(i).conditions.get(temp.get(0).indice).treated=true;
					temp.clear();
		
					up=-2;
				}
				
			}
			
			if(passageDoWhile==320){
				System.out.println("SORTIE NON VOULUS POUR "+ this.etudename);
				break;
			}
		}while(nbrTreated!=cs.size());
		SpecificCondition pillier=null;
		SpecificCondition lastPrevious =null;
		SpecificCondition lastNext=null;
		boolean treatZeroBraket=false;
		for(int j = 0 ; j < cs.size();j++){
			if(pillier == null){
				if(cs.get(j).braketPlace==1){
					lastPrevious=cs.get(j);
				}
			}
			if(pillier !=null && cs.get(j).braketPlace==1){
				lastNext=cs.get(j);
			}
			if(!treatZeroBraket && cs.get(j).braketPlace==0){
				pillier=cs.get(j);
				treatZeroBraket=true;
			}
			if(pillier !=null && cs.get(j).braketPlace==0){
				if(lastNext!= null && lastPrevious!=null){
					if(pillier.link.contains("OR")){
						if(lastNext.inLoop||lastPrevious.inLoop){
							pillier.inLoop=true;
							for(int o = 0 ; o < lastNext.loop.size()&& o<lastPrevious.loop.size();o++){
								if(lastNext.loop.get(o).satisfied || lastPrevious.loop.get(o).satisfied){
									pillier.loop.add(new ValidationBoucle(true,lastNext.loop.get(o).loopPart));
								}else {
									pillier.loop.add(new ValidationBoucle(false,lastNext.loop.get(o).loopPart));
								}
							}
						}else {
							if(lastNext.satisfied || lastPrevious.satisfied){
								pillier.satisfied=true;
							}
						}
					}else{
						if(lastNext.inLoop||lastPrevious.inLoop){
							pillier.inLoop=true;
							for(int o = 0 ; o < lastNext.loop.size()&& o<lastPrevious.loop.size();o++){
								if(lastNext.loop.get(o).satisfied && lastPrevious.loop.get(o).satisfied){
									pillier.loop.add(new ValidationBoucle(true,lastNext.loop.get(o).loopPart));
								}else {
									pillier.loop.add(new ValidationBoucle(false,lastNext.loop.get(o).loopPart));
								}
							}
						} else {
							if(lastNext.satisfied && lastPrevious.satisfied){
								pillier.satisfied=true;
							}
						}
					}
					pillier.treated=true;
					lastPrevious=pillier;
					pillier=cs.get(j);
				}
			}
		}
		if(lastNext!= null && lastPrevious!=null){
			if(pillier.link.contains("OR")){
				if(lastNext.inLoop||lastPrevious.inLoop){
					pillier.inLoop=true;
					for(int o = 0 ; o < lastNext.loop.size()&& o<lastPrevious.loop.size();o++){
						if(lastNext.loop.get(o).satisfied || lastPrevious.loop.get(o).satisfied){
							pillier.loop.add(new ValidationBoucle(true,lastNext.loop.get(o).loopPart));
						}else {
							pillier.loop.add(new ValidationBoucle(false,lastNext.loop.get(o).loopPart));
						}
					}
				}else {
					if(lastNext.satisfied || lastPrevious.satisfied){
						pillier.satisfied=true;
					}
				}
			}else{
				if(lastNext.inLoop||lastPrevious.inLoop){
					pillier.inLoop=true;
					for(int o = 0 ; o < lastNext.loop.size()&& o<lastPrevious.loop.size();o++){
						if(lastNext.loop.get(o).satisfied && lastPrevious.loop.get(o).satisfied){
							pillier.loop.add(new ValidationBoucle(true,lastNext.loop.get(o).loopPart));
						}else {
							pillier.loop.add(new ValidationBoucle(false,lastNext.loop.get(o).loopPart));
						}
					}
				} else {
					if(lastNext.satisfied && lastPrevious.satisfied){
						pillier.satisfied=true;
					}
				}
			}
			pillier.treated=true;
		}
		int lower = 5;
		int ind=-1;
		for(int j = 0 ; j < cs.size();j++){
			if(cs.get(j).braketPlace<lower){
				ind=j;
				lower = cs.get(j).braketPlace;
			}
			if(cs.get(j).braketPlace==0 && pillier !=null){
				if(cs.get(j).isLink){
					if(cs.get(j).link.contains("OR")){
						if(pillier.satisfied || cs.get(j).satisfied){
							pillier.satisfied=true;
						}
					}else {
						if(pillier.satisfied && cs.get(j).satisfied){
							pillier.satisfied=true;
						}
					}
					
				}
			}
			if(cs.get(j).braketPlace==0 && pillier == null){
				pillier = cs.get(j);					
			}
			
		}
		if(pillier==null && ind>=0 && ind < cs.size()){
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
		return qRet;
	}
	public void setQuestionSkip(String questionSkip) {
		this.questionSkip = questionSkip;
	}
}
