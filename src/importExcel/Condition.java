package importExcel;

public class Condition {
	// 0 superieur 1 inferieur 2 egaliter 3 difference 4 checkbox 5 minmax 6 date 
	int type;
	boolean skip;
	boolean isCheckBox;
	String questionSkip;
	double min;
	double max;
	double inf;
	double sup;
	double eq;
	double neq;
	int[] checkbox;
	String tag;
	
	public Condition(String condition){
		String newCondition = "";
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
			if(condition.contains("->")){
				String preGoTo = condition.split("->")[0];
				questionSkip = condition.split("->")[1];
				skip = true;
				if(preGoTo.contains("<")){
					newCondition = preGoTo.replaceAll("[^\\d.]", "");
					sup = Integer.parseInt(newCondition);
					type=0;
				}else if(preGoTo.contains(">")){
					newCondition = preGoTo.replaceAll("[^\\d.]", "");
					inf = Integer.parseInt(newCondition);
					type=1;
				}else if(preGoTo.contains("==")){
					type = 2;
					newCondition = preGoTo.replaceAll("[^\\d.]", "");
					newCondition = newCondition.replaceAll("==","");
					eq = Double.valueOf(newCondition);
				} else if(preGoTo.contains("=/=")){
					type = 3;
					newCondition = preGoTo.replaceAll("[^\\d.]", "");
					newCondition = newCondition.replaceAll("=/=","");
					neq = Double.valueOf(newCondition);
				} else if(preGoTo.contains(",")){
					type = 4;
					String [] possibility = preGoTo.split(",");
					checkbox = new int[possibility.length];
					for(int i = 0 ; i < possibility.length; i++){
						possibility[i] = possibility[i].replaceAll(" ", "");
						possibility[i] = possibility[i].replaceAll("[^\\d.]", "");
						checkbox[i] = Integer.valueOf(possibility[i]);					
					}
				}else if(preGoTo.contains("date")){
					type = 6;
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
					type = 5;
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
					type=0;
				}else if(condition.contains(">")){
					newCondition = condition.replaceAll("[^\\d.]", "");
					inf = Integer.parseInt(newCondition);
					type=1;
				}else if(condition.contains("==")){
					type = 2;
					newCondition = condition.replaceAll("[^\\d.]", "");
					newCondition = newCondition.replaceAll("==","");
					eq = Double.valueOf(newCondition);
				} else if(condition.contains("=/=")){
					type = 3;
					newCondition = condition.replaceAll("[^\\d.]", "");
					newCondition = newCondition.replaceAll("=/=","");
					neq = Double.valueOf(newCondition);
				} else if(condition.contains(",")){
					type = 4;
					String [] possibility = condition.split(",");
					checkbox = new int[possibility.length];
					for(int i = 0 ; i < possibility.length; i++){
						checkbox[i] = Integer.valueOf(possibility[i]);					
					}
				}else if(condition.contains("date")){
					type = 6;
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
					type = 5;
					newCondition = condition.replaceAll("MIN","");
					newCondition= newCondition.replaceAll("MAX","");
					newCondition = newCondition.replaceAll(" ","");
					min = Integer.parseInt(newCondition.split("-")[0]);
					newCondition = newCondition.split("-")[1];
					newCondition = newCondition.replaceAll("[^\\d.]", "");
					max = Integer.parseInt(newCondition);
				}
				
			}	
		}
	

	@Override
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

}
