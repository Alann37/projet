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
	private QuestionReturn aziz(int i,QuestionReturn option, String loopNumber, double sum, boolean isConstSum)
	{
		QuestionReturn qRet=new QuestionReturn();
		qRet=option;
		for(int j = 0 ; j < conditions.size(); j ++){
			for(int h=0; h!=conditions.get(j).type.length; h++)
			{
			if(conditions.get(j).tag!=null){
				if(reponses.get(i).questionTag.contains(conditions.get(j).tag)){
					if(conditions.get(j).type[h]==0){
						if( reponses.get(i).reponseNumeric>(double)conditions.get(j).sup && reponses.get(i).reponseNumeric!=-1 && !reponses.get(i).shouldBeEmpty){
							if(conditions.get(j).skip){
								qRet.gotSkipTo=true;
								qRet.questionSkip = conditions.get(j).questionSkip;
								qRet.validate=true;
								qRet.setQuestionNumber();
								if(reponses.get(i).partOfLoop){
									qRet.loopPart.add(new skipCondition(reponses.get(i).questionName,reponses.get(i).questionTag.split("\\.")[1]));
								}
							}else {
								qRet.validate = false;
								qRet.questionDisqualifs.add(reponses.get(i).questionTag);
								reponses.get(i).disqualif=true;
							}
						}									
					}
					if(conditions.get(j).type[h]==1){
						if(conditions.get(j).skip){
							qRet.gotSkipTo=true;
							qRet.questionSkip = conditions.get(j).questionSkip;
							qRet.validate=true;
							qRet.setQuestionNumber();
							if(reponses.get(i).partOfLoop){
								qRet.loopPart.add(new skipCondition(reponses.get(i).questionName,reponses.get(i).questionTag.split("\\.")[1]));
							}
						}else {
							qRet.validate = false;
							qRet.questionDisqualifs.add(reponses.get(i).questionTag);
							reponses.get(i).disqualif=true;
						}
						
					}
					if(conditions.get(j).type[h]==2){
						
					}
					if( conditions.get(j).type[h]==3){
						if(conditions.get(j).skip){
							qRet.gotSkipTo=true;
							qRet.questionSkip = conditions.get(j).questionSkip;
							qRet.validate=true;
							qRet.setQuestionNumber();
							if(reponses.get(i).partOfLoop){
								qRet.loopPart.add(new skipCondition(reponses.get(i).questionName,reponses.get(i).questionTag.split("\\.")[1]));
							}
						}else {
							qRet.validate = false;
							qRet.questionDisqualifs.add(reponses.get(i).questionTag);
							reponses.get(i).disqualif=true;
						}
						
					}
					if(conditions.get(j).type[h]==4){
						for(int k = 0 ; i < conditions.get(j).checkbox.length;k++){
							if( reponses.get(i).reponseNumeric == conditions.get(j).checkbox[k] && !reponses.get(i).shouldBeEmpty){
								if(conditions.get(j).skip){
									qRet.gotSkipTo=true;
									qRet.questionSkip = conditions.get(j).questionSkip;
									qRet.validate=true;
									qRet.setQuestionNumber();
									if(reponses.get(i).partOfLoop){
										qRet.loopPart.add(new skipCondition(reponses.get(i).questionName,reponses.get(i).questionTag.split("\\.")[1]));
									}
									
								}else {
									qRet.validate = false;
									qRet.questionDisqualifs.add(reponses.get(i).questionTag);
									reponses.get(i).disqualif=true;
									
								}
							}
						}
					}
					if(conditions.get(j).type[h]==5){
						if( (reponses.get(i).reponseNumeric>(double)conditions.get(j).max ||reponses.get(i).reponseNumeric<(double)conditions.get(j).min )&&  reponses.get(i).reponseNumeric !=-1 && !reponses.get(i).shouldBeEmpty){
							if(conditions.get(j).skip){
								qRet.gotSkipTo=true;
								qRet.questionSkip = conditions.get(j).questionSkip;
								qRet.validate=true;
								qRet.setQuestionNumber();
								if(reponses.get(i).partOfLoop){
									qRet.loopPart.add(new skipCondition(reponses.get(i).questionName,reponses.get(i).questionTag.split("\\.")[1]));
								}
							}else {
								qRet.validate = false;
								qRet.questionDisqualifs.add(reponses.get(i).questionTag);
								reponses.get(i).disqualif=true;
								
							}
						}
					}
					if(conditions.get(j).type[h]==6){
						if( reponses.get(i).reponseDate !=null){
							if(conditions.get(j).skip){
								qRet.gotSkipTo=true;
								qRet.questionSkip = conditions.get(j).questionSkip;
								qRet.validate=true;
								qRet.setQuestionNumber();
								if(reponses.get(i).partOfLoop){
									qRet.loopPart.add(new skipCondition(reponses.get(i).questionName,reponses.get(i).questionTag.split("\\.")[1]));
								}
							}else {
								qRet.validate = false;
								reponses.get(i).disqualif=true;
								qRet.questionDisqualifs.add(reponses.get(i).questionTag);
							}
							
						}
					}
					if(conditions.get(j).type[h]==7){
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

										qRet.questionDisqualifs.add(reponses.get(i).questionTag);
									}												
									sum=0;
									
								}
							}
						}
					}
				}
			} else {
				if(conditions.get(j).type[h]==0){
					if( reponses.get(i).reponseNumeric>(double)conditions.get(j).sup && reponses.get(i).reponseNumeric!=-1 && !reponses.get(i).shouldBeEmpty){
						if(conditions.get(j).skip){
							qRet.gotSkipTo=true;
							qRet.questionSkip = conditions.get(j).questionSkip;
							qRet.validate=true;
							qRet.setQuestionNumber();
							if(reponses.get(i).partOfLoop){
								qRet.loopPart.add(new skipCondition(reponses.get(i).questionName,reponses.get(i).questionTag.split("\\.")[1]));
							}
						}else {
							qRet.validate = false;
							reponses.get(i).disqualif=true;
							qRet.questionDisqualifs.add(reponses.get(i).questionTag);
						}
					}
					
				}
				if(conditions.get(j).type[h]==1){
					if( reponses.get(i).reponseNumeric<(double)conditions.get(j).inf && reponses.get(i).reponseNumeric !=-1 && !reponses.get(i).shouldBeEmpty ){
						if(conditions.get(j).skip){
							qRet.gotSkipTo=true;
							qRet.questionSkip = conditions.get(j).questionSkip;
							qRet.validate=true;
							qRet.setQuestionNumber();
							if(reponses.get(i).partOfLoop){
								qRet.loopPart.add(new skipCondition(reponses.get(i).questionName,reponses.get(i).questionTag.split("\\.")[1]));
							}
						}else {
							qRet.validate = false;
							reponses.get(i).disqualif=true;
							qRet.questionDisqualifs.add(reponses.get(i).questionTag);
						}
					}
					
				}
				if(conditions.get(j).type[h]==2){
					if( reponses.get(i).reponseNumeric!=(double)conditions.get(j).eq && reponses.get(i).reponseNumeric !=-1 && !reponses.get(i).shouldBeEmpty){
						if(conditions.get(j).skip){
							qRet.gotSkipTo=true;
							qRet.questionSkip = conditions.get(j).questionSkip;
							qRet.validate=true;
							qRet.setQuestionNumber();
							if(reponses.get(i).partOfLoop){
								qRet.loopPart.add(new skipCondition(reponses.get(i).questionName,reponses.get(i).questionTag.split("\\.")[1]));
							}
						}else {
							qRet.validate = false;
							reponses.get(i).disqualif=true;
							qRet.questionDisqualifs.add(reponses.get(i).questionTag);
						}
					}
					
				}
				if(conditions.get(j).type[h]==3){
					if( reponses.get(i).reponseNumeric==(double)conditions.get(j).neq && reponses.get(i).reponseNumeric !=-1 && !reponses.get(i).shouldBeEmpty){
						if(conditions.get(j).skip){
							qRet.gotSkipTo=true;
							qRet.questionSkip = conditions.get(j).questionSkip;
							qRet.validate=true;
							qRet.setQuestionNumber();
							if(reponses.get(i).partOfLoop){
								qRet.loopPart.add(new skipCondition(reponses.get(i).questionName,reponses.get(i).questionTag.split("\\.")[1]));
							}
						}else {
							qRet.validate = false;
							reponses.get(i).disqualif=true;
							qRet.questionDisqualifs.add(reponses.get(i).questionTag);
						}
					}
					
				}
				if(conditions.get(j).type[h]==4){
				
					for(int k = 0 ; k < conditions.get(j).checkbox.length;k++){
						if( reponses.get(i).reponseNumeric == conditions.get(j).checkbox[k] &&  !reponses.get(i).shouldBeEmpty){
							if(conditions.get(j).skip){
								qRet.gotSkipTo=true;
								qRet.questionSkip = conditions.get(j).questionSkip;
								qRet.validate=true;
								qRet.setQuestionNumber();
								if(reponses.get(i).partOfLoop){
									qRet.loopPart.add(new skipCondition(reponses.get(i).questionName,reponses.get(i).questionTag.split("\\.")[1]));
								}
								break;
							}else {
								qRet.validate = false;
								reponses.get(i).disqualif=true;
								qRet.questionDisqualifs.add(reponses.get(i).questionTag);
								break;
							}
						}
						
						
					}
				}
				if(conditions.get(j).type[h]==5){
					if( (reponses.get(i).reponseNumeric>(double)conditions.get(j).max ||reponses.get(i).reponseNumeric<(double)conditions.get(j).min )&&  reponses.get(i).reponseNumeric !=-1 && !reponses.get(i).shouldBeEmpty){
						if(conditions.get(j).skip){
							qRet.gotSkipTo=true;
							qRet.questionSkip = conditions.get(j).questionSkip;
							qRet.validate=true;
							qRet.setQuestionNumber();
							if(reponses.get(i).partOfLoop){
								qRet.loopPart.add(new skipCondition(reponses.get(i).questionName,reponses.get(i).questionTag.split("\\.")[1]));
							}
						}else {
							qRet.validate = false;
							reponses.get(i).disqualif=true;
							qRet.questionDisqualifs.add(reponses.get(i).questionTag);
						}
					}
					
				}
				if(conditions.get(j).type[h]==6){
					 if( reponses.get(i).reponseDate.get(Calendar.YEAR)>conditions.get(j).max ||reponses.get(i).reponseDate.get(Calendar.YEAR)<conditions.get(j).min && !reponses.get(i).shouldBeEmpty){
						 if(conditions.get(j).skip){
								qRet.gotSkipTo=true;
								qRet.questionSkip = conditions.get(j).questionSkip;
								qRet.validate=true;
								qRet.setQuestionNumber();
								if(reponses.get(i).partOfLoop){
									qRet.loopPart.add(new skipCondition(reponses.get(i).questionName,reponses.get(i).questionTag.split("\\.")[1]));
								}
							}else {
								qRet.validate = false;
								reponses.get(i).disqualif=true;
								qRet.questionDisqualifs.add(reponses.get(i).questionTag);
							}
					}
					 
				}
				
				if(conditions.get(j).type[h]==7){
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
		return qRet;
}
	public QuestionReturn applyCondition(QuestionReturn option){
		boolean isConstSum = false;
		double sum=0;
		QuestionReturn qRet = new QuestionReturn(true);
		shouldBeAnswer= true;
		
		
		if(!reponses.isEmpty()){
			if(option.gotSkipTo){ // futur param
				if(option.questionNumber>this.questionNumber ){
					for(int i = 0 ; i < reponses.size(); i ++){
						if(reponses.get(i).partOfLoop){
							for(int j = 0 ; j < option.loopPart.size();j++){
								if(reponses.get(i).questionTag.contains(option.loopPart.get(j).loopNumber)){
									reponses.get(i).shouldBeEmpty=true;
								}
							}
						}
					}

				}
				
			}
			this.isAnswer=true;
			qRet.isAnswer=true;
			if(shouldBeAnswer){
				for(int i = 0 ; i < reponses.size(); i ++){
					qRet = aziz(i, qRet, "", sum, isConstSum);
				}
				if(isConstSum){
					for(int h=0; h<conditions.size(); h++){
						for(int j=0; j!=conditions.get(h).type.length;j++){
							if(conditions.get(h).type[j]==7){
								if(sum!= conditions.get(h).constSumRes){
									qRet.validate = false;
									reponses.get(h).disqualif=true;
								}
							}
						}
					}
				}
			}	
		} else {
			this.isAnswer=false;
			qRet=option;	
		}
		return qRet;
	}
	
}
