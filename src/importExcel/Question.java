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
	private QuestionReturn tryCondition(Condition c ,Reponse answer,QuestionReturn option, String loopNumber, double sum, boolean isConstSum)
	{
		QuestionReturn qRet=new QuestionReturn();
		qRet=option;
		
			for(int h=0; h!=c.type.length; h++)
			{
			if(c.tag!=null){
				if(answer.questionTag.contains(c.tag)){
					if(c.type[h]==0){
						if( answer.reponseNumeric>(double)c.sup && answer.reponseNumeric!=-1 && !answer.shouldBeEmpty){
							if(c.skip && !c.doubleSkip){
								qRet.gotSkipTo=true;
								qRet.questionSkip = c.questionSkip;
								qRet.validate=true;
								qRet.setQuestionNumber();
								if(answer.partOfLoop){
									qRet.loopPart.add(new SkipCondition(answer.questionName,answer.questionTag.split("\\.")[1]));
								}
							}else if(c.skip && c.doubleSkip) {
								qRet.doubleSkip = true;
								qRet.beginSkip = c.questionSkip;
								qRet.endSkip = c.questionSkipTo;
								qRet.validate=true;
								qRet.gotSkipTo=true;
								if(answer.partOfLoop){
									qRet.loopPart.add(new SkipCondition(c.questionSkip,c.questionSkipTo,answer.questionTag.split("\\.")[1]));
								
								}
							}else {
								qRet.validate = false;
								qRet.questionDisqualifs.add(answer.questionTag);
								answer.disqualif=true;
							}
						}									
					}
					if(c.type[h]==1){
						if( answer.reponseNumeric>(double)c.sup && answer.reponseNumeric!=-1 && !answer.shouldBeEmpty){
							if(c.skip && !c.doubleSkip){
								qRet.gotSkipTo=true;
								qRet.questionSkip = c.questionSkip;
								qRet.validate=true;
								qRet.setQuestionNumber();
								if(answer.partOfLoop){
									qRet.loopPart.add(new SkipCondition(answer.questionName,answer.questionTag.split("\\.")[1]));
								}
							}else if(c.skip && c.doubleSkip) {
								qRet.doubleSkip = true;
								qRet.beginSkip = c.questionSkip;
								qRet.endSkip = c.questionSkipTo;
								qRet.validate=true;
								qRet.gotSkipTo=true;
								if(answer.partOfLoop){
									qRet.loopPart.add(new SkipCondition(c.questionSkip,c.questionSkipTo,answer.questionTag.split("\\.")[1]));
								
								}
							}else {
								qRet.validate = false;
								qRet.questionDisqualifs.add(answer.questionTag);
								answer.disqualif=true;
							}
						}
					}
					if(c.type[h]==2){
						if( answer.reponseNumeric!=(double)c.eq && answer.reponseNumeric !=-1 && !answer.shouldBeEmpty){
							if(c.skip && !c.doubleSkip){
								qRet.gotSkipTo=true;
								qRet.questionSkip = c.questionSkip;
								qRet.validate=true;
								qRet.setQuestionNumber();
								if(answer.partOfLoop){
									qRet.loopPart.add(new SkipCondition(answer.questionName,answer.questionTag.split("\\.")[1]));
								}
							}else if(c.skip && c.doubleSkip) {
								qRet.doubleSkip = true;
								qRet.beginSkip = c.questionSkip;
								qRet.endSkip = c.questionSkipTo;
								qRet.validate=true;
								qRet.gotSkipTo=true;
								if(answer.partOfLoop){
									qRet.loopPart.add(new SkipCondition(c.questionSkip,c.questionSkipTo,answer.questionTag.split("\\.")[1]));
								
								}
							}else {
								qRet.validate = false;
								answer.disqualif=true;
								qRet.questionDisqualifs.add(answer.questionTag);
							}
						}
					}
					if( c.type[h]==3){
						if( answer.reponseNumeric==(double)c.neq && answer.reponseNumeric !=-1 && !answer.shouldBeEmpty){
							if(c.skip && !c.doubleSkip){
								qRet.gotSkipTo=true;
								qRet.questionSkip = c.questionSkip;
								qRet.validate=true;
								qRet.setQuestionNumber();
								if(answer.partOfLoop){
									qRet.loopPart.add(new SkipCondition(answer.questionName,answer.questionTag.split("\\.")[1]));
								}
							}else if(c.skip && c.doubleSkip) {
								qRet.doubleSkip = true;
								qRet.beginSkip = c.questionSkip;
								qRet.endSkip = c.questionSkipTo;
								qRet.validate=true;
								qRet.gotSkipTo=true;
								if(answer.partOfLoop){
									qRet.loopPart.add(new SkipCondition(c.questionSkip,c.questionSkipTo,answer.questionTag.split("\\.")[1]));
								
								}
							}else {
								qRet.validate = false;
								qRet.questionDisqualifs.add(answer.questionTag);
								answer.disqualif=true;
							}
						}
					}
					if(c.type[h]==4){
						for(int k = 0 ; k < c.checkbox.length;k++){
							if( answer.reponseNumeric == c.checkbox[k] && !answer.shouldBeEmpty){
								if(c.skip && !c.doubleSkip){
									qRet.gotSkipTo=true;
									qRet.questionSkip = c.questionSkip;
									qRet.validate=true;
									qRet.setQuestionNumber();
									if(answer.partOfLoop){
										qRet.loopPart.add(new SkipCondition(answer.questionName,answer.questionTag.split("\\.")[1]));
									}
									
								}else if(c.skip && c.doubleSkip) {
									qRet.doubleSkip = true;
									qRet.beginSkip = c.questionSkip;
									qRet.endSkip = c.questionSkipTo;
									qRet.validate=true;
									qRet.gotSkipTo=true;
									if(answer.partOfLoop){
										qRet.loopPart.add(new SkipCondition(c.questionSkip,c.questionSkipTo,answer.questionTag.split("\\.")[1]));
									
									}
								}else {
									qRet.validate = false;
									qRet.questionDisqualifs.add(answer.questionTag);
									answer.disqualif=true;
									
								}
							}
						}
					}
					if(c.type[h]==5){
						if( (answer.reponseNumeric>(double)c.max ||answer.reponseNumeric<(double)c.min )&&  answer.reponseNumeric !=-1 && !answer.shouldBeEmpty){
							if(c.skip && !c.doubleSkip){
								qRet.gotSkipTo=true;
								qRet.questionSkip = c.questionSkip;
								qRet.validate=true;
								qRet.setQuestionNumber();
								if(answer.partOfLoop){
									qRet.loopPart.add(new SkipCondition(answer.questionName,answer.questionTag.split("\\.")[1]));
								}
							}else if(c.skip && c.doubleSkip) {
								qRet.doubleSkip = true;
								qRet.beginSkip = c.questionSkip;
								qRet.endSkip = c.questionSkipTo;
								qRet.validate=true;
								qRet.gotSkipTo=true;
								if(answer.partOfLoop){
									qRet.loopPart.add(new SkipCondition(c.questionSkip,c.questionSkipTo,answer.questionTag.split("\\.")[1]));
								
								}
							}else {
								qRet.validate = false;
								qRet.questionDisqualifs.add(answer.questionTag);
								answer.disqualif=true;
								
							}
						}
					}
					if(c.type[h]==6){
						if( answer.reponseDate !=null){
							if( answer.reponseDate.get(Calendar.YEAR)>c.max ||answer.reponseDate.get(Calendar.YEAR)<c.min && !answer.shouldBeEmpty){
								if(c.skip && !c.doubleSkip){
									qRet.gotSkipTo=true;
									qRet.questionSkip = c.questionSkip;
									qRet.validate=true;
									qRet.setQuestionNumber();
									if(answer.partOfLoop){
										qRet.loopPart.add(new SkipCondition(answer.questionName,answer.questionTag.split("\\.")[1]));
									}
								}else if(c.skip && c.doubleSkip) {
									qRet.doubleSkip = true;
									qRet.beginSkip = c.questionSkip;
									qRet.endSkip = c.questionSkipTo;
									qRet.validate=true;
									qRet.gotSkipTo=true;
									if(answer.partOfLoop){
										qRet.loopPart.add(new SkipCondition(c.questionSkip,c.questionSkipTo,answer.questionTag.split("\\.")[1]));
									
									}
								}else if(c.multiple) {
									qRet.conditions.add(new MultipleCondition(c.questionSkip));								
								}else {
									qRet.validate = false;
									answer.disqualif=true;
									qRet.questionDisqualifs.add(answer.questionTag);
								}
							}
						}
					}
					if(c.type[h]==7){
						if(answer.questionTag.contains(".")){
							if(qRet.loopNumber.equals(answer.questionTag.split("\\.")[1])){
								if(answer.reponseNumeric != -1){
									qRet.sum+= answer.reponseNumeric;
								}
							} else {
								if(qRet.loopNumber==""){
									qRet.loopNumber=answer.questionTag.split("\\.")[1];
									if(answer.reponseNumeric != -1){
										qRet.sum+=answer.reponseNumeric;
									}
								} else {
									if(qRet.sum!= c.constSumRes){
										qRet.validate = false;
										answer.disqualif=true;

										qRet.questionDisqualifs.add(answer.questionTag);
									}												
									qRet.sum=0;
									
								}
							}
						}
					}
				}
			} else {
				if(c.type[h]==0){
					if( answer.reponseNumeric>(double)c.sup && answer.reponseNumeric!=-1 && !answer.shouldBeEmpty  ){
						if(c.skip && !c.doubleSkip){
							qRet.gotSkipTo=true;
							qRet.questionSkip = c.questionSkip;
							qRet.validate=true;
							qRet.setQuestionNumber();
							if(answer.partOfLoop){
								qRet.loopPart.add(new SkipCondition(answer.questionName,answer.questionTag.split("\\.")[1]));
							}
						}else if(c.skip && c.doubleSkip) {
							qRet.doubleSkip = true;
							qRet.beginSkip = c.questionSkip;
							qRet.endSkip = c.questionSkipTo;
							qRet.validate=true;
							qRet.gotSkipTo=true;
							if(answer.partOfLoop){
								qRet.loopPart.add(new SkipCondition(c.questionSkip,c.questionSkipTo,answer.questionTag.split("\\.")[1]));
							
							}
						} else if(c.multiple){
							 if(answer.questionTag.contains(".")){
									qRet.conditions.add(new MultipleCondition(c.questionSkip,answer.questionTag.split("\\.")[1]));	
								}else {
									qRet.conditions.add(new MultipleCondition(c.questionSkip));	
								}
						 }else {
							qRet.validate = false;
							answer.disqualif=true;
							qRet.questionDisqualifs.add(answer.questionTag);
						}
					}
					
					
				}
				if(c.type[h]==1){
					if( answer.reponseNumeric<(double)c.inf && answer.reponseNumeric !=-1 && !answer.shouldBeEmpty   ){
						if(c.skip && !c.doubleSkip){
							qRet.gotSkipTo=true;
							qRet.questionSkip = c.questionSkip;
							qRet.validate=true;
							qRet.setQuestionNumber();
							if(answer.partOfLoop){
								qRet.loopPart.add(new SkipCondition(answer.questionName,answer.questionTag.split("\\.")[1]));
							}
						}else if(c.skip && c.doubleSkip) {
							qRet.doubleSkip = true;
							qRet.beginSkip = c.questionSkip;
							qRet.endSkip = c.questionSkipTo;
							qRet.validate=true;
							qRet.gotSkipTo=true;
							if(answer.partOfLoop){
								qRet.loopPart.add(new SkipCondition(c.questionSkip,c.questionSkipTo,answer.questionTag.split("\\.")[1]));
							
							}
						} else if(c.multiple){
							 if(answer.questionTag.contains(".")){
									qRet.conditions.add(new MultipleCondition(c.questionSkip,answer.questionTag.split("\\.")[1]));	
								}else {
									qRet.conditions.add(new MultipleCondition(c.questionSkip));	
								}
						 }else {
							qRet.validate = false;
							answer.disqualif=true;
							qRet.questionDisqualifs.add(answer.questionTag);
						}
					}
					if(answer.reponseNumeric>(double)c.inf && answer.reponseNumeric !=-1 && !answer.shouldBeEmpty && c.multiple){
						if(answer.questionTag.contains(".")){
							qRet.conditions.add(new MultipleCondition(c.questionSkip,answer.questionTag.split("\\.")[1]));	
						}else {
							qRet.conditions.add(new MultipleCondition(c.questionSkip));	
						}
					}
					
				}
				if(c.type[h]==2){
					if( answer.reponseNumeric!=(double)c.eq && answer.reponseNumeric !=-1 && !answer.shouldBeEmpty && !c.multiple && !c.skip && !c.doubleSkip){
							qRet.validate = false;
							answer.disqualif=true;
							qRet.questionDisqualifs.add(answer.questionTag);
						
					}else if (answer.reponseNumeric == c.eq &&answer.reponseNumeric !=-1 && !answer.shouldBeEmpty){
						if(c.skip && !c.doubleSkip){
							qRet.gotSkipTo=true;
							qRet.questionSkip = c.questionSkip;
							qRet.validate=true;
							qRet.setQuestionNumber();
							if(answer.partOfLoop){
								qRet.loopPart.add(new SkipCondition(answer.questionName,answer.questionTag.split("\\.")[1]));
							}
						}else if(c.skip && c.doubleSkip) {
							qRet.doubleSkip = true;
							qRet.beginSkip = c.questionSkip;
							qRet.endSkip = c.questionSkipTo;
							qRet.validate=true;
							qRet.gotSkipTo=true;
							if(answer.partOfLoop){
								qRet.loopPart.add(new SkipCondition(c.questionSkip,c.questionSkipTo,answer.questionTag.split("\\.")[1]));
							
							}
						}else if(c.multiple){
							if(answer.questionTag.contains(".")){
								qRet.conditions.add(new MultipleCondition(c.questionSkip,answer.questionTag.split("\\.")[1]));	
							}else {
								qRet.conditions.add(new MultipleCondition(c.questionSkip));	
							}
						}
					}
					
				}
				if(c.type[h]==3){
					if( answer.reponseNumeric==(double)c.neq && answer.reponseNumeric !=-1 && !answer.shouldBeEmpty && !c.multiple && !c.skip && !c.doubleSkip){
					
							qRet.validate = false;
							answer.disqualif=true;
							qRet.questionDisqualifs.add(answer.questionTag);
						
						
					} else if (answer.reponseNumeric!=(double)c.neq && answer.reponseNumeric !=-1 && !answer.shouldBeEmpty ){
						if(c.skip && !c.doubleSkip){
							qRet.gotSkipTo=true;
							qRet.questionSkip = c.questionSkip;
							qRet.validate=true;
							qRet.setQuestionNumber();
							if(answer.partOfLoop){
								qRet.loopPart.add(new SkipCondition(answer.questionName,answer.questionTag.split("\\.")[1]));
							}
						}else if(c.skip && c.doubleSkip) {
							qRet.doubleSkip = true;
							qRet.beginSkip = c.questionSkip;
							qRet.endSkip = c.questionSkipTo;
							qRet.validate=true;
							qRet.gotSkipTo=true;
							if(answer.partOfLoop){
								qRet.loopPart.add(new SkipCondition(c.questionSkip,c.questionSkipTo,answer.questionTag.split("\\.")[1]));
							
							}
						}else if(c.multiple) {
							qRet.conditions.add(new MultipleCondition(c.questionSkip));								
						}
					}
					
				}
				if(c.type[h]==4){
				
					for(int k = 0 ; k < c.checkbox.length;k++){
						if( answer.reponseNumeric == c.checkbox[k] &&  !answer.shouldBeEmpty ){
							if(c.skip && !c.doubleSkip && !c.multiple){
								qRet.gotSkipTo=true;
								qRet.questionSkip = c.questionSkip;
								qRet.validate=true;
								qRet.setQuestionNumber();
								if(answer.partOfLoop){
									qRet.loopPart.add(new SkipCondition(answer.questionName,answer.questionTag.split("\\.")[1]));
								}
								break;
							}else if(c.skip && c.doubleSkip && !c.multiple) {
								qRet.doubleSkip = true;
								qRet.beginSkip = c.questionSkip;
								qRet.endSkip = c.questionSkipTo;
								qRet.validate=true;
								qRet.gotSkipTo=true;
								if(answer.partOfLoop){
									qRet.loopPart.add(new SkipCondition(c.questionSkip,c.questionSkipTo,answer.questionTag.split("\\.")[1]));
								
								}
							}else if(c.multiple) {
								if(answer.questionTag.contains(".")){
									qRet.conditions.add(new MultipleCondition(c.questionSkip,answer.questionTag.split("\\.")[1]));	
								}else {
									qRet.conditions.add(new MultipleCondition(c.questionSkip));	
								}							
							}else {
								qRet.validate = false;
								answer.disqualif=true;
								qRet.questionDisqualifs.add(answer.questionTag);
								break;
							}
						}
					}
				}
				if(c.type[h]==5){
					if( (answer.reponseNumeric>(double)c.max ||answer.reponseNumeric<(double)c.min )&&  answer.reponseNumeric !=-1 && !answer.shouldBeEmpty  ){
						if(c.skip && !c.doubleSkip){
							qRet.gotSkipTo=true;
							qRet.questionSkip = c.questionSkip;
							qRet.validate=true;
							qRet.setQuestionNumber();
							if(answer.partOfLoop){
								qRet.loopPart.add(new SkipCondition(answer.questionName,answer.questionTag.split("\\.")[1]));
							}
						}else if(c.skip && c.doubleSkip) {
							qRet.doubleSkip = true;
							qRet.beginSkip = c.questionSkip;
							qRet.endSkip = c.questionSkipTo;
							qRet.validate=true;
							qRet.gotSkipTo=true;
							if(answer.partOfLoop){
								qRet.loopPart.add(new SkipCondition(c.questionSkip,c.questionSkipTo,answer.questionTag.split("\\.")[1]));
							
							}
						}else if(c.multiple) {
							qRet.conditions.add(new MultipleCondition(c.questionSkip));								
						}else {
							qRet.validate = false;
							answer.disqualif=true;
							qRet.questionDisqualifs.add(answer.questionTag);
						}
					}
					
					
				}
				if(c.type[h]==6){
					if(answer.reponseDate!=null){
					 if( answer.reponseDate.get(Calendar.YEAR)>c.max ||answer.reponseDate.get(Calendar.YEAR)<c.min && !answer.shouldBeEmpty  ){
						 if(c.skip && !c.doubleSkip){
								qRet.gotSkipTo=true;
								qRet.questionSkip = c.questionSkip;
								qRet.validate=true;
								qRet.setQuestionNumber();
								if(answer.partOfLoop){
									qRet.loopPart.add(new SkipCondition(answer.questionName,answer.questionTag.split("\\.")[1]));
								}
							}else if(c.skip && c.doubleSkip) {
								qRet.doubleSkip = true;
								qRet.beginSkip = c.questionSkip;
								qRet.endSkip = c.questionSkipTo;
								qRet.validate=true;
								qRet.gotSkipTo=true;
								if(answer.partOfLoop){
									qRet.loopPart.add(new SkipCondition(c.questionSkip,c.questionSkipTo,answer.questionTag.split("\\.")[1]));
								
								}
							} else if(c.multiple){
								 if(answer.questionTag.contains(".")){
										qRet.conditions.add(new MultipleCondition(c.questionSkip,answer.questionTag.split("\\.")[1]));	
									}else {
										qRet.conditions.add(new MultipleCondition(c.questionSkip));	
									}
							 }else {
								qRet.validate = false;
								answer.disqualif=true;
								qRet.questionDisqualifs.add(answer.questionTag);
							}
					 }
					
					}
					 
				}
				
				if(c.type[h]==7){
					if(answer.questionTag.contains(".")){
						qRet.isLoop=true;
						if(qRet.loopNumber.equals(answer.questionTag.split("\\.")[1])){
							if(answer.reponseNumeric != -1){
								qRet.sum+= answer.reponseNumeric;
							}
						} else {
							if(qRet.loopNumber==""){
								qRet.loopNumber=answer.questionTag.split("\\.")[1];
								if(answer.reponseNumeric != -1){
									qRet.sum+=answer.reponseNumeric;
								}
							} else {
								if(qRet.sum!= c.constSumRes){
									qRet.validate = false;
									answer.disqualif=true;
								}												
								qRet.sum=0;
								
							}
						}
					} else {
						qRet.isConstSum=true;
						if(answer.reponseNumeric != -1){
							qRet.sum+=answer.reponseNumeric;
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
		boolean alreadyDoubleSkip=false;
		
		if(!reponses.isEmpty()){
			if(option.gotSkipTo){ // futur par am
				if(option.doubleSkip){
					alreadyDoubleSkip=true;
				}
				if(option.doubleSkip && option.beginSkip.equals(name) ){
					option.questionSkip=option.endSkip;
					option.setQuestionNumber();
					option.doubleSkip=false;
					alreadyDoubleSkip=false;
				}
				if(option.questionNumber>this.questionNumber&& !option.doubleSkip ){
					for(int i = 0 ; i < reponses.size(); i ++){
						if(reponses.get(i).partOfLoop){
							for(int j = 0 ; j < option.loopPart.size();j++){
								if(reponses.get(i).questionTag.split("\\.")[1].equals(option.loopPart.get(j).loopNumber)){
									reponses.get(i).shouldBeEmpty=true;
								}
							}
						} else {
							reponses.get(i).shouldBeEmpty=true;
						}
					}
					qRet = option;
				}
				
			}
			if(option.conditions.size()>0){
				for(int i = 0 ; i < option.conditions.size();i++ ){
					for(int j = 0; j < reponses.size();j++){
						if(reponses.get(j).partOfLoop){
							if(reponses.get(j).questionName.equals(option.conditions.get(i).questionName) && reponses.get(j).questionTag.split("\\.")[1].equals(option.conditions.get(i).partOfLoop)){
								qRet = tryCondition(option.conditions.get(i).secondCondition,reponses.get(j),qRet,"",sum,isConstSum);
							}
						}else {
							if(reponses.get(j).questionName.equals(option.conditions.get(i).questionName)){
								qRet = tryCondition(option.conditions.get(i).secondCondition,reponses.get(j),qRet,"",sum,isConstSum);
							}
						}
					}
				}
			}
			this.isAnswer=true;
			qRet.isAnswer=true;
			if(shouldBeAnswer){
				for(int i = 0 ; i < reponses.size(); i ++){
					for(int j = 0 ; j < conditions.size(); j ++){
					qRet = tryCondition(conditions.get(j),reponses.get(i), qRet, "", sum, isConstSum);
					}
				}
				if(qRet.isConstSum){
					for(int h=0; h<conditions.size(); h++){
						for(int j=0; j!=conditions.get(h).type.length;j++){
							if(conditions.get(h).type[j]==7){
								if(qRet.sum!= conditions.get(h).constSumRes){
									if(qRet.isLoop==false)
									{
										for(int k=0;k!=reponses.size();k++)
										{
											if(!reponses.get(k).questionTag.contains("NA"))		
											{
												reponses.get(k).disqualif=true;
												qRet.questionDisqualifs.add(reponses.get(k).questionTag);
											}
										}
									}
									else{
										;
									}
									qRet.validate = false;
									
									//erreur a faire
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
		if(alreadyDoubleSkip){
			qRet.doubleSkip=true;
			qRet.beginSkip = option.beginSkip;
			qRet.endSkip = option.endSkip;
			qRet.gotSkipTo=true;
			for(int i = 0 ; i < option.loopPart.size();i++){
				qRet.loopPart.add(option.loopPart.get(i));
			}
		}
		if(option.conditions.size()>0){
			qRet.conditions.addAll(option.conditions);
		}
		return qRet;
	}
	
}
