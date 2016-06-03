package importExcel;

public class Condition {
	// 0 superieur 1 inferieur 2 egaliter 3 difference 4 radioButton 5 minmax 6 date 7 constantSum 8 checkbox
	int[] type;
	boolean skip;
	boolean isCheckBox;
	String questionSkip;
	double min;
	double max;
	double inf;
	double sup;
	double eq;
	double neq;
	double constSumRes;
	int[] checkbox;
	boolean isDate;
	String tag;
	String multi;
	String questionSkipTo;	
	boolean doubleSkip;
	boolean multiple;
	boolean questionValue;
	String countryTag;
	boolean isNa;
	
	String associateCondition;
	boolean andorOr; //true and false or
	boolean notEmptyCondition;
	int conditionSens; // 0 colonne 1 row
	
	public Condition(String condition){
		isNa=false;
		
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
			multi="";
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
	 
	
	public void traitement(String condition,int indice){
		String newCondition = "";
		boolean init=false;
		multiple = false;
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
				sup = Integer.parseInt(newCondition);
				type[indice]=0;				
			}else if(preGoTo.contains("INF")){
				newCondition = preGoTo.replaceAll("[^\\d.]", "");
				inf = Integer.parseInt(newCondition);
				type[indice]=1;
			}else if(preGoTo.contains("EQ")){
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
				min = Integer.parseInt(newCondition.split("-")[0]);
				newCondition = newCondition.split("-")[1];
				newCondition = newCondition.replaceAll("[^\\d.]", "");
				max = Integer.parseInt(newCondition);
			}
		} 
		if(!init){
			if(condition.contains("SUP")){
				newCondition = condition.replaceAll("[^\\d.]", "");
				sup = Integer.parseInt(newCondition);
				type[indice]=0;
			}else if(condition.contains("INF")){
				newCondition = condition.replaceAll("[^\\d.]", "");
				inf = Integer.parseInt(newCondition);
				type[indice]=1;
			}else if(condition.contains("EQ")){
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
				min = Integer.parseInt(newCondition.split("-")[0]);
				newCondition = newCondition.split("-")[1];
				newCondition = newCondition.replaceAll("[^\\d.]", "");
				max = Integer.parseInt(newCondition);
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
