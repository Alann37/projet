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
	
	String tag;
	String multi;
	String questionSkipTo;	
	boolean doubleSkip;
	boolean multiple;
	boolean questionValue;
	String countryTag;
	boolean isNa;
	
	public Condition(String condition){
		isNa=false;
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
		multiple = false;
		questionValue = false;
		isCheckBox=false;
		countryTag = "";
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
		}else if(condition.contains("then")){
			String preThen = condition.split("then")[0];
			questionSkip = condition.split("then")[1];
			multiple = true;
			if(preThen.contains("<")){
				newCondition = preThen.replaceAll("[^\\d.]", "");
				sup = Integer.parseInt(newCondition);
				type[indice]=0;				
			}else if(preThen.contains(">")){
				newCondition = preThen.replaceAll("[^\\d.]", "");
				inf = Integer.parseInt(newCondition);
				type[indice]=1;
			}else if(preThen.contains("==")){
				type[indice] = 2;
				newCondition = preThen.replaceAll("[^\\d.]", "");
				newCondition = newCondition.replaceAll("==","");
				eq = Double.valueOf(newCondition);
			} else if(preThen.contains("=/=")){
				type[indice] = 3;
				newCondition = preThen.replaceAll("[^\\d.]", "");
				newCondition = newCondition.replaceAll("=/=","");
				neq = Double.valueOf(newCondition);
			}else if(preThen.contains("#")) {
				type[indice] = 8;
				preThen = preThen.replaceAll("#", "");
				String [] possibility = preThen.split(",");
				checkbox = new int[possibility.length];
				for(int i = 0 ; i < possibility.length; i++){
					possibility[i] = possibility[i].replaceAll(" ", "");
					possibility[i] = possibility[i].replaceAll("[^\\d.]", "");
					checkbox[i] = Integer.valueOf(possibility[i]);					
				}
			} else if(preThen.contains(",")){
				type[indice] = 4;
				String [] possibility = preThen.split(",");
				checkbox = new int[possibility.length];
				for(int i = 0 ; i < possibility.length; i++){
					possibility[i] = possibility[i].replaceAll(" ", "");
					possibility[i] = possibility[i].replaceAll("[^\\d.]", "");
					checkbox[i] = Integer.valueOf(possibility[i]);					
				}
			}else if(preThen.contains("date")){
				type[indice] = 6;
				newCondition = preThen.replaceAll("date","");
				newCondition = newCondition.replaceAll("MIN","");
				newCondition= newCondition.replaceAll("MAX","");
				newCondition = newCondition.replaceAll(" ","");
				min = Integer.parseInt(newCondition.split("-")[0]);
				newCondition = newCondition.split("-")[1];
				newCondition = newCondition.replaceAll("[^\\d.]", "");
				max = Integer.parseInt(newCondition);
				tag = "date";
			}else if(preThen.contains("MIN") && !condition.contains("date")){
				type[indice] = 5;
				newCondition = preThen.replaceAll("MIN","");
				newCondition= newCondition.replaceAll("MAX","");
				newCondition = newCondition.replaceAll(" ","");
				min = Integer.parseInt(newCondition.split("-")[0]);
				newCondition = newCondition.split("-")[1];
				newCondition = newCondition.replaceAll("[^\\d.]", "");
				max = Integer.parseInt(newCondition);
			}
		}else if(condition.contains("->")){
			String preGoTo = condition.split("->")[0];
			if(condition.split("->")[1].contains("to")){
				questionSkip = condition.split("->")[1].split("to")[0];
				questionSkipTo = condition.split("->")[1].split("to")[1];
				questionSkip = questionSkip.replaceAll(" ", "");
				questionSkipTo = questionSkipTo.replaceAll(" ", "");
				doubleSkip = true;
			} else {
				questionSkip = condition.split("->")[1];
			}
			skip = true;
			if(preGoTo.contains("<")){
				newCondition = preGoTo.replaceAll("[^\\d.]", "");
				sup = Integer.parseInt(newCondition);
				type[indice]=0;				
			}else if(preGoTo.contains(">")){
				newCondition = preGoTo.replaceAll("[^\\d.]", "");
				inf = Integer.parseInt(newCondition);
				type[indice]=1;
			}else if(preGoTo.contains("==")){
				type[indice] = 2;
				newCondition = preGoTo.replaceAll("[^\\d.]", "");
				newCondition = newCondition.replaceAll("==","");
				eq = Double.valueOf(newCondition);
			} else if(preGoTo.contains("=/=")){
				type[indice] = 3;
				newCondition = preGoTo.replaceAll("[^\\d.]", "");
				newCondition = newCondition.replaceAll("=/=","");
				neq = Double.valueOf(newCondition);
			} else if(preGoTo.contains(",")){
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
			}else if(preGoTo.contains("date")){
				type[indice] = 6;
				newCondition = preGoTo.replaceAll("date","");
				newCondition = newCondition.replaceAll("MIN","");
				newCondition= newCondition.replaceAll("MAX","");
				newCondition = newCondition.replaceAll(" ","");
				min = Integer.parseInt(newCondition.split("-")[0]);
				newCondition = newCondition.split("-")[1];
				newCondition = newCondition.replaceAll("[^\\d.]", "");
				max = Integer.parseInt(newCondition);
				tag = "date";
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
		} else {
			if(condition.contains("<")){
				newCondition = condition.replaceAll("[^\\d.]", "");
				sup = Integer.parseInt(newCondition);
				type[indice]=0;
			}else if(condition.contains(">")){
				newCondition = condition.replaceAll("[^\\d.]", "");
				inf = Integer.parseInt(newCondition);
				type[indice]=1;
			}else if(condition.contains("==")){
				type[indice] = 2;
				newCondition = condition.replaceAll("[^\\d.]", "");
				newCondition = newCondition.replaceAll("==","");
				eq = Double.valueOf(newCondition);
			} else if(condition.contains("=/=")){
				type[indice] = 3;
				newCondition = condition.replaceAll("[^\\d.]", "");
				newCondition = newCondition.replaceAll("=/=","");
				neq = Double.valueOf(newCondition);
			} else if(condition.contains(",")){
				type[indice] = 4;
				String [] possibility = condition.split(",");
				checkbox = new int[possibility.length];
				for(int i = 0 ; i < possibility.length; i++){
					possibility[i] = possibility[i].replaceAll(" ", "");
					possibility[i] = possibility[i].replaceAll("[^\\d.]", "");
					checkbox[i] = Integer.valueOf(possibility[i]);					
				}
			}else if(condition.contains("date")){
				type[indice] = 6;
				newCondition = condition.replaceAll("date","");
				newCondition = newCondition.replaceAll("MIN","");
				newCondition= newCondition.replaceAll("MAX","");
				newCondition = newCondition.replaceAll(" ","");
				min = Integer.parseInt(newCondition.split("-")[0]);
				newCondition = newCondition.split("-")[1];
				newCondition = newCondition.replaceAll("[^\\d.]", "");
				max = Integer.parseInt(newCondition);
				tag = "date";
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
