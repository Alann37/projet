package importExcel;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hwpf.model.types.SEPAbstractType;


public class QuestionReturn {
	boolean validate;
	boolean gotSkipTo;
	String questionSkip;
	
	int questionNumber;
	boolean isAnswer;
	List<SkipCondition> loopPart;
	List<String> questionTagSkip;
	List<MultipleCondition> conditions;
	List<AndCondition> andConditions;
	List<SpecificList> specificC;
	List<SkipCondition> skipTo;
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
		skipTo = new ArrayList<SkipCondition>();
		validate = true;
		gotSkipTo= false;
		questionSkip = "";
		questionTagSkip= new ArrayList<String>();
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
		skipTo = new ArrayList<SkipCondition>();
		if(gotSkipTo){
			questionSkip=q;
		} 
		questionTagSum =  new ArrayList<String>();
		questionTagSkip= new ArrayList<String>();
		loopPart = new ArrayList<SkipCondition>();
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
		skipTo = new ArrayList<SkipCondition>();
		questionTagSum =  new ArrayList<String>();
		questionSkip="";
		loopPart = new ArrayList<SkipCondition>();
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
	
		//Sytem.out.println("debut setspecific");
		QuestionReturn qRet = new QuestionReturn(true, this.etudename);		
		Question qu = new Question("");
		List<SpecificCondition> cs = new ArrayList<SpecificCondition>();
		List<SpecificCondition> temp = new ArrayList<SpecificCondition>();
		for(int i = 0 ; i < specificC.size();i++){
			if(specificC.get(i).countryTag.isEmpty()){
				validationConditionSpecific(qRet,i);
			}else{
				if(this.etudename.contains(specificC.get(i).countryTag)){
					validationConditionSpecific(qRet,i);
				}
			}
		}
		//Sytem.out.println("fin setSpecific");
	}
	
	private void validationConditionSpecific(QuestionReturn qRet, int i){
		List<String> listLoop = new ArrayList<String>();
		String loopPart="";
		//Mise en place de toute les itérations de la boucle
		for(int j= 0 ; j < specificC.get(i).conditions.size();j++){
			for(int h = 0 ;h < specificC.get(i).conditions.get(j).answers.size();h++){
				boolean add = true;
				if(specificC.get(i).conditions.get(j).answers.get(h).isPartOfLoop()){
					for(int k = 0 ; k < listLoop.size();k++){
						if(listLoop.get(k).equals(specificC.get(i).conditions.get(j).answers.get(h).questionTag.split("\\.")[1])){
							add =false;
						}
					}
					if(add){
						listLoop.add(specificC.get(i).conditions.get(j).answers.get(h).questionTag.split("\\.")[1]);
					}
				}
				
			}
		}
		
		for(int j = 0 ;j  < listLoop.size();j++){
			for(int h = j+1; h < listLoop.size();h++){
				if(Integer.valueOf(listLoop.get(j))>Integer.valueOf(listLoop.get(h))){
					String temp = listLoop.get(j);
					listLoop.set(j,listLoop.get(h));
					listLoop.set(h,temp);
				}
			}
		}
		// traitement des premieres conditions simple
		for(int j = 0 ; j < specificC.get(i).conditions.size();j++){
			
			loopPart ="";
			if(!specificC.get(i).conditions.get(j).isLink){
				
				Question qu = new Question("");
				for(int h = 0 ; h < specificC.get(i).conditions.get(j).answers.size();h++){
					boolean treat = true;
					qRet.validate=true;
					if(specificC.get(i).conditions.get(j).isValue){
						if(!specificC.get(i).conditions.get(j).answers.get(h).isPartOfLoop()){
							if(!specificC.get(i).conditions.get(j).c.valueCondition.isEmpty()){
								specificC.get(i).conditions.get(j).c.setCondition(String.valueOf(specificC.get(i).conditions.get(j).c.valueCondition.get(0).reponseNumeric));
							}else {
								treat = false;
								System.out.println("treat = false for " + specificC.get(i).conditions.get(j).c.questionSkip);
							}
						}else {
							for(int k = 0 ; k < specificC.get(i).conditions.get(j).c.valueCondition.size();k++){
								if(specificC.get(i).conditions.get(j).c.valueCondition.get(k).questionTag.split("\\.")[1].equals(specificC.get(i).conditions.get(j).answers.get(h).questionTag.split("\\.")[1])){
									specificC.get(i).conditions.get(j).c.setCondition(String.valueOf(specificC.get(i).conditions.get(j).c.valueCondition.get(k).reponseNumeric));
									break;
								}
							}
						}
					}
				//	if(treat){
						if(specificC.get(i).conditions.get(j).c.type[0]<8){
							qRet = qu.tryCondition(specificC.get(i).conditions.get(j).c, specificC.get(i).conditions.get(j).answers.get(h),qRet,true);
						} else if (specificC.get(i).conditions.get(j).c.type[0]==8 ){
							qRet = qu.gestionCheckBox(qRet, specificC.get(i).conditions.get(j).answers, specificC.get(i).conditions.get(j).c);
						} else if (specificC.get(i).conditions.get(j).c.type[0]==9){
							if(specificC.get(i).conditions.get(j).answers.get(h).isPartOfLoop()&& !loopPart.equals(specificC.get(i).conditions.get(j).answers.get(h).questionTag.split("\\.")[1]) ){
								
								qRet = qu.gestionNbrChecked(qRet, specificC.get(i).conditions.get(j).c,specificC.get(i).conditions.get(j).answers, specificC.get(i).conditions.get(j).answers.get(h).questionTag.split("\\.")[1]);
							}else if (!specificC.get(i).conditions.get(j).answers.get(h).isPartOfLoop()) {
								qRet = qu.gestionNbrChecked(qRet, specificC.get(i).conditions.get(j).c,specificC.get(i).conditions.get(j).answers, "");
							}
						} else if(specificC.get(i).conditions.get(j).c.type[0]==10){
							if(specificC.get(i).conditions.get(j).answers.get(h).isPartOfLoop()){
								qRet = qu.gestionNbrChecked(qRet, specificC.get(i).conditions.get(j).c, specificC.get(i).conditions.get(j).answers,specificC.get(i).conditions.get(j).answers.get(h).questionTag.split("\\.")[1] );
							} else {
								qRet = qu.gestionNbrChecked(qRet, specificC.get(i).conditions.get(j).c, specificC.get(i).conditions.get(j).answers,"" );
							}
						//	qRet = qu.gestionNbrChecked(qRet, specificC.get(i).conditions.get(j).c, specificC.get(i).conditions.get(j).answers, )
						}
					//}
				//	qRet = qu.tryCondition(specificC.get(i).conditions.get(j).c, specificC.get(i).conditions.get(j).answers.get(h), qRet);
					if(specificC.get(i).conditions.get(j).answers.get(h).isPartOfLoop()){
						if(!specificC.get(i).conditions.get(j).answers.get(h).questionTag.split("\\.")[1].equals(loopPart)){
							loopPart = specificC.get(i).conditions.get(j).answers.get(h).questionTag.split("\\.")[1];
							specificC.get(i).conditions.get(j).loop.add(new ValidationBoucle(qRet.validate,specificC.get(i).conditions.get(j).answers.get(h).questionTag.split("\\.")[1]));
							specificC.get(i).conditions.get(j).inLoop = true;
						}
					} else {
						specificC.get(i).conditions.get(j).satisfied=qRet.validate;
					}
					
				}
				specificC.get(i).conditions.get(j).treated=true;
				if(specificC.get(i).conditions.get(j).loop.size()<listLoop.size()){
					for(int l = 0; l < listLoop.size(); l++){
						boolean add=true;
						for(int k =0 ; k < specificC.get(i).conditions.get(j).loop.size();k++){
							if(listLoop.get(l).equals(specificC.get(i).conditions.get(j).loop.get(k).loopPart)){
								add =false;
							}
						}
						if(add){
							specificC.get(i).conditions.get(j).loop.add(new ValidationBoucle(true,listLoop.get(l)));
							specificC.get(i).conditions.get(j).loop.get(specificC.get(i).conditions.get(j).loop.size()-1).empty=true;
						}
					}
				}
				
			}
		}
		//boucle pour remettre les loop number dans le bon ordre
		for(int j = 0 ;j  < specificC.get(i).conditions.size();j++){
			if(specificC.get(i).conditions.get(j).loop.size()>0){
				for(int h = 0 ; h < specificC.get(i).conditions.get(j).loop.size();h++){
					for(int p = 0;p<specificC.get(i).conditions.get(j).loop.size(); p++){
						if(Integer.parseInt(specificC.get(i).conditions.get(j).loop.get(h).loopPart)<Integer.parseInt(specificC.get(i).conditions.get(j).loop.get(p).loopPart)){
							ValidationBoucle temp = specificC.get(i).conditions.get(j).loop.get(h);
							ValidationBoucle temp2 = specificC.get(i).conditions.get(j).loop.get(p);
							specificC.get(i).conditions.get(j).loop.set(h,temp2);
							specificC.get(i).conditions.get(j).loop.set(p,temp);
						}
					}
				}
			}
		}
		for(int j = 0 ; j < specificC.get(i).conditions.size();j++){
			if(specificC.get(i).conditions.get(j).isLink){
				if(specificC.get(i).conditions.get(j).loop.size()<listLoop.size()){
					for(int h = 0 ; h < listLoop.size();h++){
						boolean add = true;
						for(int k = 0 ; k< specificC.get(i).conditions.get(j).loop.size();k++){
							if(specificC.get(i).conditions.get(j).loop.get(k).loopPart.equals(listLoop.get(h))){
								add = false;
							}
						}
						if(add){
							specificC.get(i).conditions.get(j).loop.add(new ValidationBoucle(listLoop.get(h)));
						}
					}
				}
			}
		}
		int nbrTreat = 0;
		List<SpecificCondition> temp = new ArrayList<SpecificCondition>();
		int up = -2;
		int start=-1;
		int passage =0;
		int end=-1;
		//traitement des conditions de type 2 1 2 (par rapport au braketPlace)
		do{
			passage ++;
			nbrTreat=0;
		for(int j = 0 ; j < specificC.get(i).conditions.size();j++){
			if(specificC.get(i).conditions.get(j).treated){
				nbrTreat++;
			}else if ( specificC.get(i).conditions.get(j).braketPlace==0 && passage >10){
				nbrTreat++;
			
			}
			if(specificC.get(i).conditions.get(j).braketPlace>up && specificC.get(i).conditions.get(j).braketPlace>0 && !specificC.get(i).conditions.get(j).treated){
				temp.clear();
				up = specificC.get(i).conditions.get(j).braketPlace;
				temp.add(specificC.get(i).conditions.get(j));
				if(j>0){
					if(!specificC.get(i).conditions.get(j-1).isLink && specificC.get(i).conditions.get(j-1).treated && specificC.get(i).conditions.get(j-1).braketPlace==(up+1) ){
						temp.add(specificC.get(i).conditions.get(j-1));
					}
				}
			}
			if(temp.size()>0){
				if(specificC.get(i).conditions.get(j).endBraket == temp.get(0).endBraket && specificC.get(i).conditions.get(j).treated){
					if(specificC.get(i).conditions.get(j).braketPlace==(up+1)){
						boolean add = true;
						for(int h = 0 ; h < temp.size();h++){
							if(temp.get(h).equals(specificC.get(i).conditions.get(j))){
								add = false;
							}
						}
						if(add){
							temp.add(specificC.get(i).conditions.get(j));
						}
					}
				}
				if(temp.size()==3){
					if(!temp.get(1).inLoop){
						if(temp.get(0).link.contains("AND")){
							if(temp.get(1).satisfied ||temp.get(2).satisfied){
								temp.get(0).satisfied=true;
							}
						}else {
							if(temp.get(1).satisfied &&temp.get(2).satisfied){
								temp.get(0).satisfied=true;
							}	
						}
					}else {
						if(temp.get(1).loop.size()>=1 && temp.get(2).loop.size()>=1){	
							for(int k=0;k < temp.get(1).loop.size() && k < temp.get(2).loop.size();k++){
								boolean satis = false;
								if(temp.get(0).link.contains("AND")){
									if(temp.get(0).inLoop){
										if(temp.get(0).loop.get(k).loopPart.equals(temp.get(2).loop.get(k).loopPart) &&temp.get(1).loop.get(k).loopPart.equals(temp.get(2).loop.get(k).loopPart)){
											if(temp.get(1).loop.get(k).satisfied ||temp.get(2).loop.get(k).satisfied){
												satis =true;
											}
										}
									} else {
										if(temp.get(1).loop.get(k).satisfied ||temp.get(2).loop.get(k).satisfied){
											satis = true;
											
										
										}
									}
									
								}else {
									if(temp.get(0).inLoop){
										if(temp.get(0).loop.get(k).loopPart.equals(temp.get(2).loop.get(k).loopPart) &&temp.get(1).loop.get(k).loopPart.equals(temp.get(2).loop.get(k).loopPart)){
											if(temp.get(1).loop.get(k).satisfied &&temp.get(2).loop.get(k).satisfied){
												satis = true;
											}
											
										}
									}  else {
										if(temp.get(1).loop.get(k).satisfied &&temp.get(2).loop.get(k).satisfied){
												satis = true;
										}
									}
								
									
								}
								temp.get(0).loop.get(k).satisfied = satis ;//(new ValidationBoucle(satis,temp.get(1).loop.get(k).loopPart));
								if(temp.get(1).loop.get(k).empty || temp.get(2).loop.get(k).empty){
									temp.get(0).loop.get(k).empty=true;									
								}
							}
						} else {
						
						}
					}
					if(temp.get(1).inLoop || temp.get(2).inLoop){
						temp.get(0).inLoop=true;
					}
					temp.get(0).treated=true;
					temp.clear();
		
					up=-2;
				}
			}
		}
		}while(nbrTreat < specificC.get(i).conditions.size());
	
		SpecificCondition link = null;
		int endBraket=-2;
		for(int j=0 ; j < specificC.get(i).conditions.size();j++){
			if(specificC.get(i).conditions.get(j).isLink){
				if(specificC.get(i).conditions.get(j).endBraket!= endBraket){
					link = null;
				}
				if(specificC.get(i).conditions.get(j).endBraket==endBraket  && specificC.get(i).conditions.get(j).braketPlace>0){
					if(link != null){ 	
						if(!link.inLoop){
							if(specificC.get(i).conditions.get(j).link.contains("OR")){
								if(!link.satisfied ||!specificC.get(i).conditions.get(j).satisfied){
									specificC.get(i).conditions.get(j).satisfied=false; 
								}
							} else {
								if(!link.satisfied && !specificC.get(i).conditions.get(j).satisfied){
									specificC.get(i).conditions.get(j).satisfied=false;
								}
							}
						}else {
							for(int h = 0 ; h < specificC.get(i).conditions.get(j).loop.size();h++){
								if(specificC.get(i).conditions.get(j).link.contains("OR")){
									if(!link.loop.get(h).satisfied ||!specificC.get(i).conditions.get(j).loop.get(h).satisfied){
										if(!link.loop.get(h).empty && !specificC.get(i).conditions.get(j).loop.get(h).empty){
											specificC.get(i).conditions.get(j).loop.get(h).satisfied=false;
										}
										 
									}
								} else {
									if(!link.loop.get(h).satisfied &&!specificC.get(i).conditions.get(j).loop.get(h).satisfied){
										if(!link.loop.get(h).empty && !specificC.get(i).conditions.get(j).loop.get(h).empty){
											specificC.get(i).conditions.get(j).loop.get(h).satisfied=false;
										}
									}
								}
							}
						}
						link = specificC.get(i).conditions.get(j);
					}
				}
				if(link == null && specificC.get(i).conditions.get(j).braketPlace>0){
					link = specificC.get(i).conditions.get(j);
					endBraket = link.endBraket;
				}
			}
		}
		SpecificCondition lastPrevious=null;
		SpecificCondition lastNext = null;
		SpecificCondition pillier = null;
		for(int j = 0 ; j < specificC.get(i).conditions.size();j++){
			if(pillier ==null && specificC.get(i).conditions.get(j).braketPlace==1){
				lastPrevious = specificC.get(i).conditions.get(j);
			}
			if(specificC.get(i).conditions.get(j).braketPlace==0){
				if(pillier == null ){
					pillier = specificC.get(i).conditions.get(j);
				} else {
					if(lastPrevious!= null && lastNext != null) {
						if(lastPrevious.inLoop){
							for(int h = 0 ; h < listLoop.size();h++){
								boolean satis = true;
								if(pillier.link.contains("OR")){
									if(!lastPrevious.loop.get(h).satisfied || !lastNext.loop.get(h).satisfied){
										satis = false;
									}
								} else {
									if(!lastPrevious.loop.get(h).satisfied && !lastNext.loop.get(h).satisfied){
										satis = false;
									}
								}
								pillier.loop.get(h).satisfied=satis;
								
								pillier.treated=true;
								pillier.inLoop=true;
								if(lastPrevious.loop.get(h).empty ||lastNext.loop.get(h).empty){
									pillier.loop.get(h).empty=true;
								}
							}
							
						} else {
							boolean satis = true;
							if(pillier.link.contains("OR")){
								if(!lastPrevious.satisfied || !lastNext.satisfied){
									satis = false;
								}
							} else {
								if(!lastPrevious.satisfied && !lastNext.satisfied){
									satis = false;
								}
							}
							pillier.satisfied=satis;
							pillier.treated=true;
						}
					}
					lastPrevious = pillier;
					pillier = specificC.get(i).conditions.get(j);
				}
			}
			if(pillier != null && specificC.get(i).conditions.get(j).braketPlace==1){
				lastNext = specificC.get(i).conditions.get(j);
			}
		}
		
		if(lastPrevious!= null && lastNext!= null && pillier!= null){
				if(lastPrevious.inLoop){
					pillier.inLoop=true;
					for(int h = 0 ; h < listLoop.size();h++){
						boolean satis = true;
						if(pillier.link.contains("OR")){
							if(!lastPrevious.loop.get(h).satisfied || !lastNext.loop.get(h).satisfied){
								satis = false;
							}
						} else {
							if(!lastPrevious.loop.get(h).satisfied && !lastNext.loop.get(h).satisfied){
								satis = false;
							}
						}
						
						pillier.loop.get(h).satisfied=satis;
						if(lastPrevious.loop.get(h).empty ||lastNext.loop.get(h).empty){
							pillier.loop.get(h).empty=true;
						}
						pillier.treated=true;
					}
				} else {
					boolean satis = true;
					if(pillier.link.contains("OR")){
						if(!lastPrevious.satisfied || !lastNext.satisfied){
							satis = false;
						}
					} else {
						if(!lastPrevious.satisfied && !lastNext.satisfied){
							satis = false;
						}
					}
					pillier.satisfied=satis;
					pillier.treated=true;
				}
		}
	
		for(int j = 0 ;j  < specificC.get(i).conditions.size();j++){
			if(specificC.get(i).conditions.get(j).loop.size()>0){
				for(int h = 0 ; h < specificC.get(i).conditions.get(j).loop.size();h++){
					for(int p = 0;p<specificC.get(i).conditions.get(j).loop.size(); p++){
						if(Integer.parseInt(specificC.get(i).conditions.get(j).loop.get(h).loopPart)<Integer.parseInt(specificC.get(i).conditions.get(j).loop.get(p).loopPart)){
							ValidationBoucle temp1 = specificC.get(i).conditions.get(j).loop.get(h);
							ValidationBoucle temp2 = specificC.get(i).conditions.get(j).loop.get(p);
							specificC.get(i).conditions.get(j).loop.set(h,temp2);
							specificC.get(i).conditions.get(j).loop.set(p,temp1);
						}
					}
				}
			}
		}
		if(pillier == null ){
			if(lastPrevious !=null){
				if(lastPrevious.treated){
					pillier = lastPrevious;
				}
			}
			if(lastNext !=null){
				if(lastNext.treated){
					pillier = lastNext;
				}
			}
		}
			
		for(int j = 0 ; j < specificC.get(i).conditions.size();j++){
			if(pillier != null){
				for(int h = 0; h < specificC.get(i).conditions.get(j).answers.size();h++){
					if(!pillier.inLoop){
						if(!pillier.satisfied){
							specificC.get(i).conditions.get(j).answers.get(h).disqualif = true;
							if(specificC.get(i).isAer ){
								specificC.get(i).conditions.get(j).answers.get(h).isAerDisq=true;
							}
						}
					} else {
						for(int p = 0 ; p < listLoop.size();p++){
							if( !pillier.loop.get(p).empty&&specificC.get(i).conditions.get(j).answers.get(h).questionTag.split("\\.")[1].contains(pillier.loop.get(p).loopPart)){
								if(!pillier.loop.get(p).satisfied){
									specificC.get(i).conditions.get(j).answers.get(h).disqualif = true;
									if(specificC.get(i).isAer){
										specificC.get(i).conditions.get(j).answers.get(h).isAerDisq= true;
									}
								}
							}
						}
					}
				}
				if(!specificC.get(i).conditions.get(j).isLink){
					if(specificC.get(i).conditions.get(j).c.questionValue){
						if(!pillier.satisfied){
							if(!pillier.inLoop){
									for(int k = 0 ; k < specificC.get(i).conditions.get(j).c.valueCondition.size();k++){
										specificC.get(i).conditions.get(j).c.valueCondition.get(k).isValueDisqu=true;
									}
							} else {
								for(int p = 0 ; p < listLoop.size();p++){
									for(int h = 0 ; h <specificC.get(i).conditions.get(j).c.valueCondition.size();h++ ){
										if( !pillier.loop.get(p).empty&&specificC.get(i).conditions.get(j).c.valueCondition.get(h).questionTag.split("\\.")[1].contains(pillier.loop.get(p).loopPart)){
											if(!pillier.loop.get(p).satisfied){
												specificC.get(i).conditions.get(j).c.valueCondition.get(h).isValueDisqu = true;
											}
										}
									}
								}
							}
						}
					}
				}
			}
			if(!specificC.get(i).conditions.get(j).equals(pillier)){
				specificC.get(i).conditions.get(j).satisfied=false;
				specificC.get(i).conditions.get(j).treated=false;
			}
			//System.out.println("condition sur : " + specificC.get(i).conditions.get(j).link +" braketPlace = " + specificC.get(i).conditions.get(j).braketPlace+  " treated = " + specificC.get(i).conditions.get(j).treated + " satisfied = "+ specificC.get(i).conditions.get(j).satisfied) ;
		}
		pillier.satisfied=false;
		pillier.treated=false;
		temp.clear();
		//System.out.println();
	}
	
	
	
	public void setQuestionSkip(String questionSkip) {
		this.questionSkip = questionSkip;
	}
}
