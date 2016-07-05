package importExcel;

import java.util.ArrayList;
import java.util.List;

public class Condition {
	
	boolean skip;
	boolean isCheckBox;
	boolean doubleSkip;
	boolean multiple;
	boolean questionValue;
	boolean andorOr; //true and false or
	boolean notEmptyCondition;
	boolean isNa;
	boolean isDate;
	boolean withBraket;
	boolean isCheckedCondition;
	boolean infSup; //true si inf false si sup
	boolean bEq;
	boolean isAer;
	List<SpecificCondition> subCondition;
	
	double checked;
	double min;
	double max;
	double inf;
	double sup;
	double eq;
	double neq;
	double constSumRes;
	
	// 0 superieur 1 inferieur 2 egaliter 3 difference 4 radioButton 5 minmax 6 date 7 constantSum 8 checkbox 9 nb Item Check 10 item uncheck
	int[] type;
	int[] checkbox;
	int[] uncheck;
	int conditionSens; // 0 colonne 1 row
	
	String tag;
	String questionSkipTo;	
	String questionSkip;
	String countryTag;
	String associateCondition;
	
	
	
	public Condition(String condition){
		if(condition.contains("AER")){
			isAer=true;
			condition = condition.replaceAll("AER", "");
		} else {
			isAer = false;
		}
		countryTag="";
		isCheckedCondition=false;
		if(condition.contains("(")){
			braket(condition);
		}else {
			isNa=false;
			withBraket=false;
			multiple = false;
			notEmptyCondition=false;
			associateCondition="";
			if(condition.contains("answerC")){
				conditionSens=0;
				condition = condition.replace("answerC", "");
				notEmptyCondition=true;
			}
			if(condition.contains("answerR")){
				conditionSens=1;
				notEmptyCondition=true;
				condition = condition.replace("answerR", "");
			}
			if(condition.contains("AND") ||condition.contains("OR")){
				if(condition.indexOf("AND")>condition.indexOf("OR") && condition.indexOf("OR")!=-1 || condition.indexOf("AND")==-1){
					String temp = condition.split("OR")[0];
					andorOr=false;
					associateCondition=condition.replaceAll(temp+"OR", "");
					condition = temp;	
				} else if (condition.indexOf("AND")<condition.indexOf("OR") && condition.indexOf("AND")!=-1 || condition.indexOf("OR")==-1){
					String temp = condition.split("AND")[0];
					andorOr=true;
					associateCondition=condition.replaceAll(temp+"AND", "");
					condition = temp;	
				}
				
			}
			if(condition.contains("NA")){
				condition= condition.replaceAll(" ","");
				condition = condition.replaceAll("NA", "");
				eq = Double.parseDouble(condition);
				isNa = true;
				tag ="";
				countryTag="";
				questionSkipTo="";
				type = new int[0];
			}else {
			if(condition.contains("then")){
				questionSkip = condition.split("then")[1];
				condition = condition.split("then")[0];
				multiple = true;
			}
			String newConditionMulti="";
				if(condition.contains("&"))
				{
					if(condition.split("&").length!=0)
					{
						type=new int [condition.split("&").length];
						for(int lg=0; lg!= condition.split("&").length; lg++)
						{
							newConditionMulti=condition.split("&")[lg];
							traitement(newConditionMulti, lg);
						}
					}
				}else{
					type=new int [1];
					traitement(condition, 0);
				}
			}
		}
	}
	
	public Condition(String condition, boolean braket){
		if(condition.contains("AER")){
			isAer=true;
			condition = condition.replaceAll("AER", "");
			if(condition.endsWith(" ")){
				condition = condition.replace(" ", "");
			}
		} else {
			isAer = false;
		}
		withBraket=braket;
		countryTag="";
		isCheckedCondition=false;
		if(condition.contains("(")){
			braket(condition);
		}else {
			isNa=false;
			
			multiple = false;
			notEmptyCondition=false;
			associateCondition="";
			if(condition.contains("answerC")){
				conditionSens=0;
				condition = condition.replace("answerC", "");
				notEmptyCondition=true;
			}
			if(condition.contains("answerR")){
				conditionSens=1;
				notEmptyCondition=true;
				condition = condition.replace("answerR", "");
			}
			if(condition.contains("AND") ||condition.contains("OR")){
				if(condition.indexOf("AND")>condition.indexOf("OR") && condition.indexOf("OR")!=-1 || condition.indexOf("AND")==-1){
					String temp = condition.split("OR")[0];
					andorOr=false;
					associateCondition=condition.replaceAll(temp+"OR", "");
					condition = temp;	
				} else if (condition.indexOf("AND")<condition.indexOf("OR") && condition.indexOf("AND")!=-1 || condition.indexOf("OR")==-1){
					String temp = condition.split("AND")[0];
					andorOr=true;
					associateCondition=condition.replaceAll(temp+"AND", "");
					condition = temp;	
				}
				
			}
			if(condition.contains("NA")){
				condition= condition.replaceAll(" ","");
				condition = condition.replaceAll("NA", "");
				eq = Double.parseDouble(condition);
				isNa = true;
				tag ="";
				countryTag="";
				questionSkipTo="";
				type = new int[0];
			}else {
			if(condition.contains("then")){
				questionSkip = condition.split("then")[1];
				condition = condition.split("then")[0];
				multiple = true;
			}
			String newConditionMulti="";
				if(condition.contains("&"))
				{
					if(condition.split("&").length!=0)
					{
						type=new int [condition.split("&").length];
						for(int lg=0; lg!= condition.split("&").length; lg++)
						{
							newConditionMulti=condition.split("&")[lg];
							traitement(newConditionMulti, lg);
						}
					}
				}else{
					type=new int [1];
					traitement(condition, 0);
				}
			}
		}
	}
	 
	private void  braket(String condition){
		
		if(condition.contains(":")){
			countryTag=condition.split(":")[0];
			condition = condition.substring(countryTag.length()+1);
			countryTag=countryTag.replaceAll(" ","");
		} else{
			countryTag="";
			
		}
		 tag=null;
		 questionSkipTo="";	
		 questionSkip="";
		 associateCondition="";
		 isCheckBox=false;
		 isDate=false;
		 doubleSkip=false;
		 skip=false;
		 int beginBraket=0;
		 
		 if(condition.contains("(")){
			withBraket=true;
			int nbr = condition.split("\\(").length;
			subCondition= new ArrayList<SpecificCondition>();
			int inBr=0;
			String link;
			for(int i = 1 ; i < nbr; i++){
				String temp = condition.split("\\(")[i];
				if(temp.contains("OR")){
					link=null;
				}
				if(inBr ==0){
					if(subCondition.size()>0){
						beginBraket= subCondition.size()-1;
					}else {
						beginBraket=i-1;
					}
				}
				inBr++;
				link = null;
				if(!temp.isEmpty()){
					
					String aer ="";
					if(isAer){
						aer="AER";
					}
					subCondition.add(new SpecificCondition(temp.split("\\)")[0]+aer,inBr,false,beginBraket));
					
					if(temp.contains(")")){
						inBr--;
						if(temp.split("\\)").length==2){
							if(!temp.split("\\)")[1].isEmpty()){
								link = temp.split("\\)")[1];
								link = link.replaceAll(" ","");
								if(!link.isEmpty()){
									subCondition.add(new SpecificCondition(link, inBr, true,beginBraket));
								}
							}
						}else if(temp.split("\\)").length>2){
					
							for(int j = 1 ; j < temp.split("\\)").length;j++){
								
								if(!temp.split("\\)")[j].isEmpty()){
									link = temp.split("\\)")[j];
									link = link.replaceAll(" ", "");
									if(!link.isEmpty()){
										if(inBr==0){
											subCondition.add(new SpecificCondition(link, inBr, true,i+1));
										}else {
											subCondition.add(new SpecificCondition(link, inBr, true,beginBraket));
										}
									}
										
								} else {
									inBr--;
								}
							}
						}
					}
					
					
				} 
				
			}
			for(int i = 0 ; i < subCondition.size();i++){
				if(!subCondition.get(i).isLink && subCondition.get(i).link==null && subCondition.get(i).c==null){
					subCondition.remove(i);
					i--;
				}
			}
			int endBraket=-1;
			beginBraket=-1;
			for(int i = 0 ; i < subCondition.size();i++){
				if(subCondition.get(i).braketPlace==0){
					if(beginBraket==-1){
						beginBraket=i;
						endBraket=-1;
					}else if(endBraket ==-1){
						endBraket=i;
						for(int j = beginBraket ; j < i; j ++){
							if(beginBraket== subCondition.get(j).beginBraket){
								subCondition.get(j).endBraket=i;
							}
						}
						beginBraket=-1;
					} 
					
				}
				if((i+1)==subCondition.size()){
					if(endBraket>=0){
						for(int j = endBraket ; j < subCondition.size(); j ++){
							if(0== subCondition.get(j).endBraket){
								subCondition.get(j).endBraket=subCondition.size()-1;
							}
						}
					}else {
						for(int j = 0 ; j < subCondition.size(); j ++){
							if(0== subCondition.get(j).endBraket){
								subCondition.get(j).endBraket=subCondition.size()-1;
							}
						}
					}
				}
			}
			
			for(int i = 0 ; i < subCondition.size();i++){
				System.out.println("Braket Place : " + subCondition.get(i).braketPlace + " link " + subCondition.get(i).link+ " beginBraket "+subCondition.get(i).beginBraket + " endBraket "+subCondition.get(i).endBraket);
			}
			System.out.println("<<<<< END CONDITION >>>>>");
		}
	}
	
	public void traitement(String condition,int indice){
		String newCondition = "";
		boolean init=false;
		bEq=false;
		isDate=false;
		questionValue = false;
		isCheckBox=false;
		countryTag = "";
		if(condition.contains("date")){
			isDate= true;
			condition = condition.replaceAll("date","");
		}
		if(condition.contains(":")){
			countryTag=condition.split(":")[0];
			condition = condition.substring(countryTag.length()+1);
			countryTag=countryTag.replaceAll(" ","");
		}
		if(condition.contains("_")){
			if(condition.split("_").length==2){
				tag = "_"+condition.split("_")[1];
				condition = condition.replaceAll(tag,"");
			}
			if(condition.split("_").length==3){
				tag = "_"+condition.split("_")[1];
				tag+="_"+condition.split("_")[2];
				condition = condition.replaceAll(tag,"");
				tag = tag.replaceAll(" ", "");
				}

		}else{
			tag = null;
		}
		newCondition = ""; 
		if(condition.contains("VALUE")){
			questionValue=true;	
			questionSkip = condition.replaceAll("VALUE","");
			type[indice]=-1;
			init = true;
		}
		if(condition.contains("SKIP") && !init ){
			init=true;
			String preGoTo = condition.split("SKIP")[0];
			if(condition.split("SKIP")[1].contains("to")){
				questionSkip = condition.split("SKIP")[1].split("to")[0];
				questionSkipTo = condition.split("SKIP")[1].split("to")[1];
				questionSkip = questionSkip.replaceAll(" ", "");
				questionSkipTo = questionSkipTo.replaceAll(" ", "");
				doubleSkip = true;
			} else {
				questionSkip = condition.split("SKIP")[1];
			}
			skip = true;
			if(preGoTo.contains("SUP")){
				newCondition = preGoTo.replaceAll("[^\\d.]", "");
				sup = Double.parseDouble(newCondition);
				type[indice]=0;				
			}else if(preGoTo.contains("INF")){
				newCondition = preGoTo.replaceAll("[^\\d.]", "");
				inf = Double.parseDouble(newCondition);
				type[indice]=1;
			}else if (preGoTo.contains("CHECKED") && !preGoTo.contains("UNCHECKED")){
				isCheckedCondition=true;
				newCondition = preGoTo.replaceAll("CHECKED","");
				if(newCondition.contains("+")){
					newCondition = newCondition.replaceAll("+", "");
					infSup=false;
				}else if (newCondition.contains("=")){
					bEq=true;
					newCondition = newCondition.replaceAll("=", "");
				}
				else {
					newCondition = newCondition.replaceAll("-", "");
					infSup=true;
				}
				type[indice] = 9;
				newCondition = preGoTo.replaceAll("[^\\d.]", "");
				checked=Double.parseDouble(newCondition);
			}else if (preGoTo.contains("UNCHECKED")){
				type[indice]=10;
				preGoTo = preGoTo.replaceAll("UNCHECKED","");
				String [] possibility = preGoTo.split(",");
				uncheck = new int[possibility.length];
				for(int i = 0 ; i < possibility.length; i++){
					possibility[i] = possibility[i].replaceAll(" ", "");
					possibility[i] = possibility[i].replaceAll("[^\\d.]", "");
					if(!possibility[i].isEmpty()){
						uncheck[i] = Integer.valueOf(possibility[i]);		
					}
				}
			}else if(preGoTo.contains("EQ") && !preGoTo.contains("NEQ")){
				type[indice] = 2;
				newCondition = preGoTo.replaceAll("[^\\d.]", "");
				newCondition = newCondition.replaceAll("==","");
				eq = Double.valueOf(newCondition);
			} else if(preGoTo.contains("NEQ")){
				type[indice] = 3;
				newCondition = preGoTo.replaceAll("[^\\d.]", "");
				newCondition = newCondition.replaceAll("=/=","");
				neq = Double.valueOf(newCondition);
			} else if(preGoTo.contains(",") && !preGoTo.contains("#") && !preGoTo.contains("UNCHECKED")){
				type[indice] = 4;
				String [] possibility = preGoTo.split(",");
				checkbox = new int[possibility.length];
				for(int i = 0 ; i < possibility.length; i++){
					possibility[i] = possibility[i].replaceAll(" ", "");
					possibility[i] = possibility[i].replaceAll("[^\\d.]", "");
					if(!possibility[i].isEmpty()){
						checkbox[i] = Integer.valueOf(possibility[i]);		
					}
				}
			}else if (preGoTo.contains("#")){
			 type[indice]=8;
			 newCondition = preGoTo.replaceAll("#","");
			 newCondition= newCondition.replaceAll(" ","");
			 if(newCondition.contains(",")){
				 String [] possibility = newCondition.split(",");
					checkbox = new int[possibility.length];
					for(int i = 0 ; i < possibility.length; i++){
						possibility[i] = possibility[i].replaceAll(" ", "");
						possibility[i] = possibility[i].replaceAll("[^\\d.]", "");
						if(!possibility[i].isEmpty()){
							checkbox[i] = Integer.valueOf(possibility[i]);		
						}
					}
			 } else {
				 newCondition = newCondition.replaceAll("[^\\d.]", "");
				 if(!newCondition.isEmpty()) {
					 checkbox = new int[1];
					 checkbox[0] = Integer.valueOf(newCondition);	
				 }
			 }
			}else if(preGoTo.contains("MIN") && !condition.contains("date")){
				type[indice] = 5;
				newCondition = preGoTo.replaceAll("MIN","");
				newCondition= newCondition.replaceAll("MAX","");
				newCondition = newCondition.replaceAll(" ","");
				min = Double.parseDouble(newCondition.split("-")[0]);
				newCondition = newCondition.split("-")[1];
				newCondition = newCondition.replaceAll("[^\\d.]", "");
				max = Double.parseDouble(newCondition);
			}
		} 
		if(!init){
			if(condition.contains("SUP")){
				newCondition = condition.replaceAll("[^\\d.]", "");
				sup = Double.parseDouble(newCondition);
				type[indice]=0;
			}else if(condition.contains("INF")){
				newCondition = condition.replaceAll("[^\\d.]", "");
				inf = Double.parseDouble(newCondition);
				type[indice]=1;
			}else if(condition.contains("EQ") && !condition.contains("NEQ")){
				type[indice] = 2;
				newCondition = condition.replaceAll("[^\\d.]", "");
				newCondition = newCondition.replaceAll("==","");
				eq = Double.valueOf(newCondition);
			} else if (condition.contains("CHECKED") && !condition.contains("UNCHECKED")){
				isCheckedCondition=true;
				type[indice] = 9;
				newCondition = condition.replaceAll("CHECKED", "");
				if(newCondition.contains("+")){
					newCondition = newCondition.replaceAll("\\+", "");
					infSup=false;
				}else if (newCondition.contains("=")){
					bEq=true;
					newCondition = newCondition.replaceAll("=", "");
				}else {
					newCondition = newCondition.replaceAll("\\-", "");
					infSup=true;
				}
				checked=Double.parseDouble(newCondition);
			}else if(condition.contains(",")&& condition.contains("UNCHECKED")){
				type[indice] = 10;
				condition = condition.replaceAll("UNCHECKED","");
				String [] possibility = condition.split(",");
				uncheck = new int[possibility.length];
				for(int i = 0 ; i < possibility.length; i++){
					possibility[i] = possibility[i].replaceAll(" ", "");
					possibility[i] = possibility[i].replaceAll("[^\\d.]", "");
					if(!possibility[i].isEmpty()){
						uncheck[i] = Integer.valueOf(possibility[i]);		
					}
				}
			}else if(condition.contains("NEQ")){
				type[indice] = 3;
				newCondition = condition.replaceAll("[^\\d.]", "");
				newCondition = newCondition.replaceAll("=/=","");
				neq = Double.valueOf(newCondition);
			}else if (condition.contains("#")){
				 type[indice]=8;
				 newCondition = condition.replaceAll("#","");
				 newCondition= newCondition.replaceAll(" ","");
				 if(newCondition.contains(",")){
					 String [] possibility = newCondition.split(",");
						checkbox = new int[possibility.length];
						for(int i = 0 ; i < possibility.length; i++){
							possibility[i] = possibility[i].replaceAll(" ", "");
							possibility[i] = possibility[i].replaceAll("[^\\d.]", "");
							if(!possibility[i].isEmpty()){
								checkbox[i] = Integer.valueOf(possibility[i]);		
							}
						}
				 } else {
					 newCondition = newCondition.replaceAll("[^\\d.]", "");
					 if(!newCondition.isEmpty()) {
						 checkbox = new int[1];
						 checkbox[0] = Integer.valueOf(newCondition);	
					 }
 				 }
				} else if(condition.contains(",") && !condition.contains("#")){
				type[indice] = 4;
				String [] possibility = condition.split(",");
				checkbox = new int[possibility.length];
				for(int i = 0 ; i < possibility.length; i++){
					possibility[i] = possibility[i].replaceAll(" ", "");
					possibility[i] = possibility[i].replaceAll("[^\\d.]", "");
					if(!possibility[i].isEmpty()){
						checkbox[i] = Integer.valueOf(possibility[i]);		
					}
				}
			}else if(condition.contains("MIN") && !condition.contains("date")){
				type[indice] = 5;
				newCondition = condition.replaceAll("MIN","");
				newCondition= newCondition.replaceAll("MAX","");
				newCondition = newCondition.replaceAll(" ","");
				min = Double.parseDouble(newCondition.split("-")[0]);
				newCondition = newCondition.split("-")[1];
				newCondition = newCondition.replaceAll("[^\\d.]", "");
				max = Double.parseDouble(newCondition);
			}else if(condition.contains("SUM")){
				type[indice]=7;
				newCondition = condition.replaceAll("[^\\d.]", "");
				constSumRes = Double.valueOf(newCondition);
			}
			
		} 
	}
}
	
	
	/*@Override
	public String toString() {
		String sRet = "";
		if(type == 0){
			sRet = " >"+ sup;
			if(tag !=null){
				sRet += " " + tag;
			}
		}
		if(type == 1){
			sRet = " < " + inf ;
			if(tag !=null){
				sRet += " " + tag;
			}
		}
		if (type == 2){
			sRet =  "==" +eq;
			if(tag !=null){
				sRet += " " + tag;
			}
		} 
		if (type ==3){
			sRet =  "=/=" + neq ;
			if(tag !=null){
				sRet += " " + tag;
			}
		}
		if (type == 5 || type ==6){
			sRet = "min : " + min+ " max : "+ + max;
			if(tag !=null){
				sRet += " " + tag;
			}
		}
		return sRet;
	}

	public void setCondition(String condition,boolean skip , String questionSkip){
		
	}

}*/
