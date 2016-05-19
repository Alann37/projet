package importExcel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Question {
	public String name;
	public List<Condition> conditions;
	public String type;
	public List<Reponse> reponses;
	int questionNumber;
	boolean shouldBeAnswer;
	boolean isAnswer;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Condition> getCondition() {
		return conditions;
	}
	public void setCondition(List<Condition> condition) {
		this.conditions = condition;
	}
	public Question(String name) {
		this.type=null;
		this.name = name;
		this.conditions = new ArrayList<Condition>();
		this.reponses = new ArrayList<Reponse>();
		if(this.name != ""){
			String temp = name.split("@")[0];
			temp = temp.replaceAll(". ", "");
			temp=temp.replaceAll("[^\\d.]", "");
			if(!temp.isEmpty()){
				questionNumber = Integer.valueOf(temp);
				System.out.println("NUMBER "+ questionNumber);
			}
		}
	}
	public void makeCondition(){
		// REVOIR COMMENT SPLIT NAME  pour le moment @
		this.name = name.split("@")[0];
	}
	
	public QuestionReturn applyCondition(QuestionReturn option){
		boolean isConstSum = false;
		double sum=0;
		QuestionReturn qRet = new QuestionReturn(true);
		shouldBeAnswer= true;
		String loopNumber="";
		
		if(!reponses.isEmpty()){
			if(option.gotSkipTo){ // futur param
				if(option.questionNumber>this.questionNumber ){
					this.shouldBeAnswer= false;

				}
				
			}
			this.isAnswer=true;
			qRet.isAnswer=true;
			if(shouldBeAnswer){
				for(int i = 0 ; i < reponses.size(); i ++){
					for(int j = 0 ; j < conditions.size(); j ++){
						if(conditions.get(j).tag!=null){
							if(reponses.get(i).questionTag.contains(conditions.get(j).tag)){			
								if(conditions.get(j).type==0){
									if(!conditions.get(j).skip && reponses.get(i).reponseNumeric>(double)conditions.get(j).sup && reponses.get(i).reponseNumeric!=-1){
										qRet.validate = false;
										reponses.get(i).disqualif=true;
									}
									
								}
								if(conditions.get(j).type==1){
									if(!conditions.get(j).skip && reponses.get(i).reponseNumeric<(double)conditions.get(j).inf && reponses.get(i).reponseNumeric !=-1){
										qRet.validate = false;
										reponses.get(i).disqualif=true;
									}
								}
								if(conditions.get(j).type==2){
									if(!conditions.get(j).skip && reponses.get(i).reponseNumeric!=(double)conditions.get(j).eq && reponses.get(i).reponseNumeric !=-1){
										qRet.validate = false;
										reponses.get(i).disqualif=true;
									}
								}
								if(!conditions.get(j).skip && conditions.get(j).type==3){
									if(!conditions.get(j).skip && reponses.get(i).reponseNumeric==(double)conditions.get(j).neq && reponses.get(i).reponseNumeric !=-1){
										qRet.validate = false;
										reponses.get(i).disqualif=true;
									}
								}
								if(conditions.get(j).type==4){
									for(int h = 0 ; i < conditions.get(j).checkbox.length;h++){
										if(!conditions.get(j).skip && reponses.get(i).reponseNumeric == conditions.get(j).checkbox[h]){
											qRet.validate = false;	
											reponses.get(i).disqualif=true;
										}
										
									}
								}
								if(conditions.get(j).type==5){
									if(!conditions.get(j).skip && (reponses.get(i).reponseNumeric>(double)conditions.get(j).max ||reponses.get(i).reponseNumeric<(double)conditions.get(j).min )&&  reponses.get(i).reponseNumeric !=-1){
										qRet.validate = false;
										reponses.get(i).disqualif=true;
									}
								}
								if(conditions.get(j).type==6){
									if( reponses.get(i).reponseDate !=null){
										if(!conditions.get(j).skip && reponses.get(i).reponseDate.get(Calendar.YEAR)>conditions.get(j).max ||reponses.get(i).reponseDate.get(Calendar.YEAR)<conditions.get(j).min){
											qRet.validate = false;
											reponses.get(i).disqualif=true;
										}
									}
								}
								if(conditions.get(j).type==7){
									if(reponses.get(i).questionTag.contains(".")){
										if(loopNumber.equals(reponses.get(i).questionTag.split("\\.")[1])){
											if(reponses.get(i).reponseNumeric != -1){
												sum+= reponses.get(i).reponseNumeric;
											}
										} else {
											if(loopNumber==""){
												loopNumber=reponses.get(i).questionTag.split("\\.")[1];
												if(reponses.get(i).reponseNumeric != -1){
													sum+=reponses.get(i).reponseNumeric;
												}
											} else {
												if(sum!= conditions.get(i).constSumRes){
													qRet.validate = false;
													reponses.get(i).disqualif=true;
												}												
												sum=0;
												
											}
										}
									}
								}
							}
						} else {
							if(conditions.get(j).type==0){
								if(!conditions.get(j).skip && reponses.get(i).reponseNumeric>(double)conditions.get(j).sup && reponses.get(i).reponseNumeric!=-1){
									qRet.validate = false;
									reponses.get(i).disqualif=true;
								}
							}
							if(conditions.get(j).type==1){
								if(!conditions.get(j).skip && reponses.get(i).reponseNumeric<(double)conditions.get(j).inf && reponses.get(i).reponseNumeric !=-1){
									qRet.validate = false;
									reponses.get(i).disqualif=true;
								}
							}
							if(conditions.get(j).type==2){
								if(!conditions.get(j).skip && reponses.get(i).reponseNumeric!=(double)conditions.get(j).eq && reponses.get(i).reponseNumeric !=-1){
									qRet.validate = false;
									reponses.get(i).disqualif=true;
								}
							}
							if(conditions.get(j).type==3){
								if(!conditions.get(j).skip && reponses.get(i).reponseNumeric==(double)conditions.get(j).neq && reponses.get(i).reponseNumeric !=-1){
									qRet.validate = false;
									reponses.get(i).disqualif=true;
								}
							}
							if(conditions.get(j).type==4){
							
								for(int h = 0 ; h < conditions.get(j).checkbox.length;h++){
									if(!conditions.get(j).skip && reponses.get(i).reponseNumeric == conditions.get(j).checkbox[h] && !conditions.get(j).skip){
										qRet.validate = false;	
										reponses.get(i).disqualif=true;
										break;
									}
									
								}
							}
							if(conditions.get(j).type==5){
								if(!conditions.get(j).skip && (reponses.get(i).reponseNumeric>(double)conditions.get(j).max ||reponses.get(i).reponseNumeric<(double)conditions.get(j).min )&&  reponses.get(i).reponseNumeric !=-1){
									qRet.validate = false;
									reponses.get(i).disqualif=true;
								}
							}
							if(conditions.get(j).type==6){
								 if(!conditions.get(j).skip && reponses.get(i).reponseDate.get(Calendar.YEAR)>conditions.get(j).max ||reponses.get(i).reponseDate.get(Calendar.YEAR)<conditions.get(j).min){
									qRet.validate = false;
									reponses.get(i).disqualif=true;
								}
							}
							if(conditions.get(j).type==7){
								if(reponses.get(i).questionTag.contains(".")){
									if(loopNumber.equals(reponses.get(i).questionTag.split("\\.")[1])){
										if(reponses.get(i).reponseNumeric != -1){
											sum+= reponses.get(i).reponseNumeric;
										}
									} else {
										if(loopNumber==""){
											loopNumber=reponses.get(i).questionTag.split("\\.")[1];
											if(reponses.get(i).reponseNumeric != -1){
												sum+=reponses.get(i).reponseNumeric;
											}
										} else {
											if(sum!= conditions.get(i).constSumRes){
												qRet.validate = false;
												reponses.get(i).disqualif=true;
											}												
											sum=0;
											
										}
									}
								} else {
									isConstSum=true;
									if(reponses.get(i).reponseNumeric != -1){
										sum+=reponses.get(i).reponseNumeric;
									}
																	
								}
							}
						}
					}
				}
				if(isConstSum){
					for(int h=0; h<conditions.size(); h++){
						if(conditions.get(h).type==7){
							if(sum!= conditions.get(h).constSumRes){
								qRet.validate = false;
								reponses.get(h).disqualif=true;
							}
						}
					}
				}
			}
		} else {
			this.isAnswer=false;
			qRet=option;
			
		}
		if(!qRet.isValidate()){
			System.out.println("toto");
		}
		return qRet;
	}
	
}
