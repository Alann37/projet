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
	List<SpecificCondition> subCondition;
	
	double min;
	double max;
	double inf;
	double sup;
	double eq;
	double neq;
	double constSumRes;
	
	// 0 superieur 1 inferieur 2 egaliter 3 difference 4 radioButton 5 minmax 6 date 7 constantSum 8 checkbox
	int[] type;
	int[] checkbox;
	int conditionSens; // 0 colonne 1 row
	
	String tag;
	String questionSkipTo;	
	String questionSkip;
	String countryTag;
	String associateCondition;
	
	
	
	public Condition(String condition){
		countryTag="";
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
				inBr++;
				link = null;
				if(!temp.isEmpty()){
					
					
					
					subCondition.add(new SpecificCondition(temp.split("\\)")[0],inBr,false));
					if(temp.contains(")")){
						inBr--;
						if(temp.split("\\)").length==2){
							if(!temp.split("\\)")[1].isEmpty()){
								link = temp.split("\\)")[1];
								subCondition.add(new SpecificCondition(link, inBr, true));
								
							}
						}else if(temp.split("\\)").length>2){
					
							for(int j = 1 ; j < temp.split("\\)").length;j++){
								
								if(!temp.split("\\)")[j].isEmpty()){
									link = temp.split("\\)")[j];
									link = link.replaceAll(" ", "");
									if(!link.isEmpty()){
										subCondition.add(new SpecificCondition(link, inBr, true));
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
			for(int i = 0 ; i < subCondition.size();i++){
				System.out.println("Braket Place : " + subCondition.get(i).braketPlace + " link " + subCondition.get(i).link);
			}
			nbr++;
		}
	}
	
	public void traitement(String condition,int indice){
		String newCondition = "";
		boolean init=false;
		
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
			} else if(preGoTo.contains(",") && !preGoTo.contains("#")){
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
			} else if(condition.contains("NEQ")){
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
					checkbox[i] = Integer.valueOf(possibility[i]);					
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
