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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Condition> getCondition() {
		return conditions;
	}
	public void checkIfMultiple(){
		for(int i = 0 ; i < conditions.size();i++){
			if(conditions.get(i).type == 4){
				this.type = "checkbox";
			}
		}
	}
	public void setCondition(List<Condition> condition) {
		this.conditions = condition;
	}
	public Question(String name) {
		this.type=null;
		this.name = name;
		this.conditions = new ArrayList<Condition>();
		this.reponses = new ArrayList<Reponse>();
		/*if(this.name != ""){
			String temp = name.split("@")[0];
			temp = temp.replaceAll(". ", "");
			if(!temp.isEmpty()){
				questionNumber = Integer.valueOf(temp);
				System.out.println("NUMBER "+ questionNumber);
			}
		}*/
	}
	public void makeCondition(){
		// REVOIR COMMENT SPLIT NAME  pour le moment @
		this.name = name.split("@")[0];
	}
	
	public boolean applyCondition(){
		boolean bRet = true;
		checkIfMultiple();
		if(!reponses.isEmpty()){
			for(int i = 0 ; i < reponses.size(); i ++){
				for(int j = 0 ; j < conditions.size(); j ++){
					if(conditions.get(j).tag!=null){
						if(reponses.get(i).questionTag.contains(conditions.get(j).tag)){			
							if(conditions.get(j).type==0){
								if(reponses.get(i).reponseNumeric>(double)conditions.get(j).sup && reponses.get(i).reponseNumeric!=-1){
									bRet= false;
									reponses.get(i).disqualif=true;
								}
							}
							if(conditions.get(j).type==1){
								if(reponses.get(i).reponseNumeric<(double)conditions.get(j).inf && reponses.get(i).reponseNumeric !=-1){
									bRet= false;
									reponses.get(i).disqualif=true;
								}
							}
							if(conditions.get(j).type==2){
								if(reponses.get(i).reponseNumeric!=(double)conditions.get(j).eq && reponses.get(i).reponseNumeric !=-1){
									bRet= false;
									reponses.get(i).disqualif=true;
								}
							}
							if(conditions.get(j).type==3){
								if(reponses.get(i).reponseNumeric==(double)conditions.get(j).neq && reponses.get(i).reponseNumeric !=-1){
									bRet= false;
									reponses.get(i).disqualif=true;
								}
							}
							if(conditions.get(j).type==4){
								for(int h = 0 ; i < conditions.get(j).checkbox.length;h++){
									if(reponses.get(i).reponseNumeric == conditions.get(j).checkbox[h]){
										bRet = false;	
										reponses.get(i).disqualif=true;
									}
								}
							}
							if(conditions.get(j).type==5){
								if((reponses.get(i).reponseNumeric>(double)conditions.get(j).max ||reponses.get(i).reponseNumeric<(double)conditions.get(j).min )&&  reponses.get(i).reponseNumeric !=-1){
									bRet= false;
									reponses.get(i).disqualif=true;
								}
							}
							if(conditions.get(j).type==6){
								if(reponses.get(i).reponseDate !=null){
									if(reponses.get(i).reponseDate.get(Calendar.YEAR)>conditions.get(j).max ||reponses.get(i).reponseDate.get(Calendar.YEAR)<conditions.get(j).min){
										bRet= false;
										reponses.get(i).disqualif=true;
									}
								}
							}
						}
					} else {
						if(conditions.get(j).type==0){
							if(reponses.get(i).reponseNumeric>(double)conditions.get(j).sup && reponses.get(i).reponseNumeric!=-1){
								bRet= false;
								reponses.get(i).disqualif=true;
							}
						}
						if(conditions.get(j).type==1){
							if(reponses.get(i).reponseNumeric<(double)conditions.get(j).inf && reponses.get(i).reponseNumeric !=-1){
								bRet= false;
								reponses.get(i).disqualif=true;
							}
						}
						if(conditions.get(j).type==2){
							if(reponses.get(i).reponseNumeric!=(double)conditions.get(j).eq && reponses.get(i).reponseNumeric !=-1){
								bRet= false;
								reponses.get(i).disqualif=true;
							}
						}
						if(conditions.get(j).type==3){
							if(reponses.get(i).reponseNumeric==(double)conditions.get(j).neq && reponses.get(i).reponseNumeric !=-1){
								bRet= false;
								reponses.get(i).disqualif=true;
							}
						}
						if(conditions.get(j).type==4){
						
							for(int h = 0 ; h < conditions.get(j).checkbox.length;h++){
								if(reponses.get(i).reponseNumeric == conditions.get(j).checkbox[h]){
									bRet = false;	
									reponses.get(i).disqualif=true;
									break;
								}
							}
						}
						if(conditions.get(j).type==5){
							if((reponses.get(i).reponseNumeric>(double)conditions.get(j).max ||reponses.get(i).reponseNumeric<(double)conditions.get(j).min )&&  reponses.get(i).reponseNumeric !=-1){
								bRet= false;
								reponses.get(i).disqualif=true;
							}
						}
						if(conditions.get(j).type==6){
							 if(reponses.get(i).reponseDate.get(Calendar.YEAR)>conditions.get(j).max ||reponses.get(i).reponseDate.get(Calendar.YEAR)<conditions.get(j).min){
								bRet= false;
								reponses.get(i).disqualif=true;
							}
						}
					
					}
				}
			}
		}
		return bRet;
	}
	
}
