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
	double naValue;
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
	public void setNa(){
		for(int i = 0 ; i < conditions.size(); i ++){
			if(conditions.get(i).isNa){
				naValue = conditions.get(i).eq;
			}
		}
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
			}
			this.name = name.split("@")[0];
			naValue = -999999;
		}
	}
	

	private QuestionReturn gestionConditionNotEmpty(QuestionReturn option, Condition c){
		QuestionReturn qRet = option;
		if(c.conditionSens==0){
			for(int i = 0 ; i < reponses.size(); i ++){
				for(int j = i+1; j< reponses.size(); j ++){
					if(reponses.get(i).questionTag.split("_").length>=3 && reponses.get(j).questionTag.split("_").length>=3){
						if(reponses.get(i).questionTag.split("_")[1].equals(reponses.get(j).questionTag.split("_")[1]) && !reponses.get(i).questionTag.split("_")[2].equals(reponses.get(j).questionTag.split("_")[2]) ){
							if(reponses.get(i).isEmpty && !reponses.get(j).isEmpty){
								qRet.questionDisqualifs.add(reponses.get(i).questionTag);
								qRet.questionDisqualifs.add(reponses.get(j).questionTag);
								qRet.validate=false;
								reponses.get(i).disqualif=true;
								reponses.get(j).disqualif=true;
							}
							if(reponses.get(i).reponseNumeric == 0 &&( reponses.get(j).reponseNumeric!=0 && !reponses.get(j).isEmpty)){
								qRet.questionDisqualifs.add(reponses.get(i).questionTag);
								qRet.questionDisqualifs.add(reponses.get(j).questionTag);
								qRet.validate=false;
								reponses.get(i).disqualif=true;
								reponses.get(j).disqualif=true;
							}
						}
					}
				}
			}
		} else {
			for(int i = 0 ; i < reponses.size(); i ++){
				for(int j = i+1; j< reponses.size(); j ++){
					if(reponses.get(i).questionTag.split("_").length>=3 && reponses.get(j).questionTag.split("_").length>=3){
						if(reponses.get(i).questionTag.split("_")[2].equals(reponses.get(j).questionTag.split("_")[2])){
							if(reponses.get(i).isEmpty && !reponses.get(j).isEmpty){
								qRet.questionDisqualifs.add(reponses.get(i).questionTag);
								qRet.questionDisqualifs.add(reponses.get(j).questionTag);
								qRet.validate=false;
								reponses.get(i).disqualif=true;
								reponses.get(j).disqualif=true;
							}
							if(reponses.get(i).reponseNumeric == 0 &&( reponses.get(j).reponseNumeric!=0 && !reponses.get(j).isEmpty)){	qRet.questionDisqualifs.add(reponses.get(i).questionTag);
								qRet.questionDisqualifs.add(reponses.get(j).questionTag);
								qRet.validate=false;
								reponses.get(i).disqualif=true;
								reponses.get(j).disqualif=true;
							}
						}
					}
				}
			}
		}
		return qRet;
	}

	private QuestionReturn gestionTypeCondition(QuestionReturn option,Condition c,Reponse answer){
		QuestionReturn qRet = new QuestionReturn();
		qRet=option;
		for(int h = 0 ; h < c.type.length;h++){
		if(c.type[h]==0){
			if( answer.reponseNumeric>=(double)c.sup && answer.reponseNumeric!=naValue && !answer.shouldBeEmpty  ){
				if(c.skip && !c.doubleSkip){
					qRet.gotSkipTo=true;
					qRet.questionSkip = c.questionSkip;
					qRet.validate=true;
					qRet.questionSkip = qRet.questionSkip.replaceAll(" ", "");
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
			if( answer.reponseNumeric<=(double)c.inf && answer.reponseNumeric !=naValue && !answer.shouldBeEmpty   ){
				if(c.skip && !c.doubleSkip){
					qRet.gotSkipTo=true;
					qRet.questionSkip = c.questionSkip;
					qRet.validate=true;
					qRet.setQuestionNumber();
					qRet.questionSkip = qRet.questionSkip.replaceAll(" ", "");
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
			if(answer.reponseNumeric>(double)c.inf && answer.reponseNumeric !=naValue && !answer.shouldBeEmpty && c.multiple){
				if(answer.questionTag.contains(".")){
					qRet.conditions.add(new MultipleCondition(c.questionSkip,answer.questionTag.split("\\.")[1]));	
				}else {
					qRet.conditions.add(new MultipleCondition(c.questionSkip));	
				}
			}
			
		}
		if(c.type[h]==2){
			if( answer.reponseNumeric!=(double)c.eq && answer.reponseNumeric !=naValue && !answer.shouldBeEmpty && !c.multiple && !c.skip && !c.doubleSkip){
					qRet.validate = false;
					answer.disqualif=true;
					qRet.questionDisqualifs.add(answer.questionTag);
				
			}else if (answer.reponseNumeric == c.eq &&answer.reponseNumeric !=naValue && !answer.shouldBeEmpty){
				if(c.skip && !c.doubleSkip){
					qRet.gotSkipTo=true;
					qRet.questionSkip = c.questionSkip;
					qRet.validate=true;
					qRet.questionSkip = qRet.questionSkip.replaceAll(" ", "");
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
			if( answer.reponseNumeric==(double)c.neq && answer.reponseNumeric !=naValue && !answer.shouldBeEmpty && !c.multiple && !c.skip && !c.doubleSkip){
			
					qRet.validate = false;
					answer.disqualif=true;
					qRet.questionDisqualifs.add(answer.questionTag);
				
				
			} else if (answer.reponseNumeric!=(double)c.neq && answer.reponseNumeric !=naValue && !answer.shouldBeEmpty ){
				if(c.skip && !c.doubleSkip){
					qRet.gotSkipTo=true;
					qRet.questionSkip = qRet.questionSkip.replaceAll(" ", "");
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
				int testNum=-1;
				if(answer.reponseNumeric == 1){
				String temp = answer.questionTag;
				temp = temp.split("_")[temp.split("_").length-1];
				if(temp.contains(".")){
					temp = temp.split("\\.")[0];
				}
				 temp = temp.replaceAll("[^\\d.]", "");
				 testNum = Integer.valueOf(temp);
				}
				if( answer.reponseNumeric == c.checkbox[k] &&  !answer.shouldBeEmpty ){
					if(c.skip && !c.doubleSkip && !c.multiple){
						qRet.gotSkipTo=true;
						qRet.questionSkip = c.questionSkip;
						qRet.validate=true;
						qRet.questionSkip = qRet.questionSkip.replaceAll(" ", "");
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
			if( (answer.reponseNumeric>(double)c.max ||answer.reponseNumeric<(double)c.min )&&  answer.reponseNumeric !=naValue && !answer.shouldBeEmpty  ){
				if(c.skip && !c.doubleSkip){
					qRet.gotSkipTo=true;
					qRet.questionSkip = c.questionSkip;
					qRet.validate=true;
					qRet.questionSkip = qRet.questionSkip.replaceAll(" ", "");
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
						qRet.questionSkip = qRet.questionSkip.replaceAll(" ", "");
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
					qRet.isConstSum=true;
			if(answer.questionTag.contains(".")){
				qRet.isLoop=true;
						if(qRet.loopNumber==""){
							qRet.loopNumber=answer.questionTag.split("\\.")[1];
							qRet.sum=0;
							if(answer.reponseNumeric != naValue && !answer.questionTag.contains("NA")){
								qRet.sum+=answer.reponseNumeric;
							}
							if(!answer.questionTag.contains("NA"))
							{
							qRet.questionTagSum.add(answer.questionTag);
							}
						} else if (qRet.loopNumber.equals(answer.questionTag.split("\\.")[1])){
							if(answer.reponseNumeric != naValue && !answer.questionTag.contains("NA")){
								qRet.sum+= answer.reponseNumeric;
							}
							if(!answer.questionTag.contains("NA"))
							{
							qRet.questionTagSum.add(answer.questionTag);
							}
						} else {
							if(qRet.sum!= c.constSumRes){
								qRet.validate = false;
								answer.disqualif=true;
								qRet.questionDisqualifs.addAll(qRet.questionTagSum);
								
							}	
							qRet.questionTagSum.clear();
							qRet.sum=0;
							qRet.loopNumber = answer.questionTag.split("\\.")[1];
							if(answer.reponseNumeric != naValue){
								qRet.sum+=answer.reponseNumeric;
							}
						}
					} else {
						if(answer.reponseNumeric != naValue && !answer.questionTag.contains("NA")){
							qRet.sum+=answer.reponseNumeric;
						}
			}
		}
		if(c.type[h]==8){
			
			for(int k = 0 ; k < c.checkbox.length;k++){
				int testNum=0;
				if(answer.reponseNumeric == 1){
				String temp = answer.questionTag;
				temp = temp.split("_")[temp.split("_").length-1];
				if(temp.contains(".")){
					temp = temp.split("\\.")[0];
				}
				 temp = temp.replaceAll("[^\\d.]", "");
				 testNum = Integer.valueOf(temp);
				}
				if( testNum == c.checkbox[k] &&  !answer.shouldBeEmpty ){
					if(c.skip && !c.doubleSkip && !c.multiple){
						qRet.gotSkipTo=true;
						qRet.questionSkip = c.questionSkip;
						qRet.validate=true;
						qRet.questionSkip = qRet.questionSkip.replaceAll(" ", "");
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
	}
	
		return qRet;
	}
	
	private QuestionReturn gestionTypeConditionDate(QuestionReturn option,Condition c,Reponse answer){
		QuestionReturn qRet = new QuestionReturn();
		qRet=option;
		for(int h = 0 ; h < c.type.length;h++){
		if(c.type[h]==0){
			if(answer.reponseDate!=null){
				if( answer.reponseDate.get(Calendar.YEAR)>=c.sup && answer.reponseNumeric!=naValue && !answer.shouldBeEmpty  ){
					if(c.skip && !c.doubleSkip){
						qRet.gotSkipTo=true;
						qRet.questionSkip = c.questionSkip;
						qRet.validate=true;
						qRet.questionSkip = qRet.questionSkip.replaceAll(" ", "");
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
		if(c.type[h]==1){
			if(answer.reponseDate!=null){
				if( answer.reponseDate.get(Calendar.YEAR)<=c.inf && answer.reponseNumeric !=naValue && !answer.shouldBeEmpty   ){
					if(c.skip && !c.doubleSkip){
						qRet.gotSkipTo=true;
						qRet.questionSkip = c.questionSkip;
						qRet.validate=true;
						qRet.setQuestionNumber();
						qRet.questionSkip = qRet.questionSkip.replaceAll(" ", "");
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
				if(answer.reponseDate.get(Calendar.YEAR)>c.inf && answer.reponseNumeric !=naValue && !answer.shouldBeEmpty && c.multiple){
					if(answer.questionTag.contains(".")){
						qRet.conditions.add(new MultipleCondition(c.questionSkip,answer.questionTag.split("\\.")[1]));	
					}else {
						qRet.conditions.add(new MultipleCondition(c.questionSkip));	
					}
				}
			}
		}
		if(c.type[h]==2){
			if(answer.reponseDate!=null){
				if( answer.reponseDate.get(Calendar.YEAR)!=c.eq && answer.reponseNumeric !=naValue && !answer.shouldBeEmpty && !c.multiple && !c.skip && !c.doubleSkip){
						qRet.validate = false;
						answer.disqualif=true;
						qRet.questionDisqualifs.add(answer.questionTag);
					
				}else if (answer.reponseDate.get(Calendar.YEAR) == c.eq &&answer.reponseNumeric !=naValue && !answer.shouldBeEmpty){
					if(c.skip && !c.doubleSkip){
						qRet.gotSkipTo=true;
						qRet.questionSkip = c.questionSkip;
						qRet.validate=true;
						qRet.questionSkip = qRet.questionSkip.replaceAll(" ", "");
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
		}
		if(c.type[h]==3){
			if(answer.reponseDate!=null){
				if( answer.reponseDate.get(Calendar.YEAR)==c.neq && answer.reponseNumeric !=naValue && !answer.shouldBeEmpty && !c.multiple && !c.skip && !c.doubleSkip){
				
						qRet.validate = false;
						answer.disqualif=true;
						qRet.questionDisqualifs.add(answer.questionTag);
					
					
				} else if (answer.reponseDate.get(Calendar.YEAR)!=c.neq && answer.reponseNumeric !=naValue && !answer.shouldBeEmpty ){
					if(c.skip && !c.doubleSkip){
						qRet.gotSkipTo=true;
						qRet.questionSkip = qRet.questionSkip.replaceAll(" ", "");
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
		}
		if(c.type[h]==5){
			if(answer.reponseDate!=null){
			 if( answer.reponseDate.get(Calendar.YEAR)>c.max ||answer.reponseDate.get(Calendar.YEAR)<c.min && !answer.shouldBeEmpty  ){
				 if(c.skip && !c.doubleSkip){
						qRet.gotSkipTo=true;
						qRet.questionSkip = c.questionSkip;
						qRet.questionSkip = qRet.questionSkip.replaceAll(" ", "");
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
	}
	
		return qRet;
	}
	private QuestionReturn tryCondition(Condition c ,Reponse answer,QuestionReturn option)
	{
		QuestionReturn qRet=new QuestionReturn();
		qRet=option;
		if(c.questionValue){
			if(c.countryTag.isEmpty()){
				qRet.conditions.add(new MultipleCondition(c.questionSkip,answer.reponseNumeric, answer.reponseTexte));
			} else{
				qRet.conditions.add(new MultipleCondition(c.questionSkip,answer.reponseNumeric, answer.reponseTexte,c.countryTag));
			}
		}else {
			if(c.notEmptyCondition){
	
				qRet = gestionConditionNotEmpty(qRet,c);
			}else {
				if(c.isDate){
					if(c.tag!=null ){
						if(!c.countryTag.isEmpty()){
							if(answer.questionTag.contains(c.tag) && qRet.etudename.contains(c.countryTag)){
									qRet = gestionTypeConditionDate(qRet,c,answer);
							}
						} else if(answer.questionTag.contains(c.tag)){
							qRet = gestionTypeConditionDate(qRet,c,answer);
						}
					}else {
						if(c.countryTag.isEmpty()){
							qRet = gestionTypeConditionDate(qRet,c,answer);
						} else if(qRet.etudename.contains(c.countryTag)){
							qRet = gestionTypeConditionDate(qRet,c,answer); 
						}
					}
				} else {
					if(c.tag!=null ){
						if(!c.countryTag.isEmpty()){
							if(answer.questionTag.contains(c.tag) && qRet.etudename.contains(c.countryTag)){
									qRet = gestionTypeCondition(qRet,c,answer);
							}
						} else if(answer.questionTag.contains(c.tag)){
							qRet = gestionTypeCondition(qRet,c,answer);
						}
					} else {
						if(c.countryTag.isEmpty()){
							qRet = gestionTypeCondition(qRet,c,answer);
						} else if(qRet.etudename.contains(c.countryTag)){
							qRet = gestionTypeCondition(qRet,c,answer);
						}
					}
				}	
			}
		}
		return qRet;
}
	public QuestionReturn questionTreatement(QuestionReturn option){

		QuestionReturn qRet = new QuestionReturn();
		qRet.etudename = option.etudename;
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
				if(!option.questionSkip.equals(this.name) && !option.doubleSkip ){
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
				} else {
					option.gotSkipTo = false;
					option.questionSkip="";
				}
				
			}
			if(option.conditions.size()>0){
				for(int i = 0 ; i < option.conditions.size();i++ ){
					boolean passage=false;
					for(int j = 0; j < reponses.size();j++){
						if(!reponses.get(j).questionTag.contains("other") && !reponses.get(j).questionTag.contains("NA")){
							if(reponses.get(j).partOfLoop){
								if(reponses.get(j).questionName.equals(option.conditions.get(i).questionName) && reponses.get(j).questionTag.split("\\.")[1].equals(option.conditions.get(i).partOfLoop)){
									if(option.conditions.get(i).secondCondition!=null){
										qRet = tryCondition(option.conditions.get(i).secondCondition,reponses.get(j),qRet);
										passage=true;
									}
								}
							}else {
								if(reponses.get(j).questionName.equals(option.conditions.get(i).questionName)){
									if(option.conditions.get(i).secondCondition!=null){
										qRet = tryCondition(option.conditions.get(i).secondCondition,reponses.get(j),qRet);
										passage = true;
									}
								}
							}
						}
					}
					if(passage){
						option.conditions.remove(i);
						i--;
					}
				}
			}

			qRet.isAnswer=true;
			if(shouldBeAnswer){
				for(int i = 0 ; i < reponses.size(); i ++){
					for(int j = 0 ; j < conditions.size(); j ++){
						if(!reponses.get(i).questionTag.contains("other") && ! reponses.get(i).questionTag.contains("NA")){
							if(conditions.get(j)!=null){
								qRet = tryCondition(conditions.get(j),reponses.get(i), qRet);
							}
				
						}
					}
				}
				if(qRet.isConstSum && !qRet.isLoop){
					for(int h=0; h<conditions.size(); h++){
						for(int j=0; j!=conditions.get(h).type.length;j++){
							if(conditions.get(h).type[j]==7){
								if(qRet.sum!= conditions.get(h).constSumRes){
									for(int k=0;k!=reponses.size();k++)
									{
										if(qRet.isLoop==false)
										{
											if(!reponses.get(k).questionTag.contains("NA"))		
											{
												reponses.get(k).disqualif=true;
												qRet.questionDisqualifs.add(reponses.get(k).questionTag);
											}
										}
										
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
			if(option.gotSkipTo){
				if(option.questionSkip.equals(name) && !option.doubleSkip){
					option.gotSkipTo=false;
					option.questionSkip="";
				}
			}
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
