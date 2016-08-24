package traitement;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;

import ErrorLog.Error;
/**
 * 
 * @author dbinet
 *
 *classe contenant une condition pour une question
 *
 */
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
	List<Reponse> valueCondition;
	double checked;
	double min;
	double max;
	double inf;
	double sup;
	double eq;
	double neq;
	double constSumRes;
	Date dateCondition;
	String dateType;
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
	
	
	
	public Condition(String condition) throws IOException{
		dateType="";
		valueCondition = new ArrayList<Reponse>();
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
					andorOr=true;
					associateCondition=condition.replaceAll(temp+"OR", "");
					condition = temp;	
				} else if (condition.indexOf("AND")<condition.indexOf("OR") && condition.indexOf("AND")!=-1 || condition.indexOf("OR")==-1){
					String temp = condition.split("AND")[0];
					andorOr=false;
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
	
	public Condition(String condition, boolean braket) throws IOException{
		valueCondition = new ArrayList<Reponse>();
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
		if(condition.contains("(")){ //gestion de création de condition multiple
			braket(condition);
		}else {
			isNa=false;
			multiple = false;
			notEmptyCondition=false;
			associateCondition="";
			if(condition.contains(":")){
				countryTag=condition.split(":")[0];
				countryTag= countryTag.replaceAll(" ", "");
			}
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
					andorOr=true;
					associateCondition=condition.replaceAll(temp+"OR", "");
					condition = temp;	
				} else if (condition.indexOf("AND")<condition.indexOf("OR") && condition.indexOf("AND")!=-1 || condition.indexOf("OR")==-1){
					String temp = condition.split("AND")[0];
					andorOr=false;
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
	 
	private void  braket(String condition) throws IOException{
		
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
			int lastBegin =0;
			for(int i = 0 ; i < subCondition.size();i++){
				if(subCondition.get(i).braketPlace==0){
					if(beginBraket==-1){
						beginBraket=i;
						for(int h = lastBegin ; h< beginBraket; h++){
							subCondition.get(h).endBraket=beginBraket;
						}
						lastBegin=beginBraket;
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
								subCondition.get(j).endBraket=endBraket;
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
	// 0 supérieur 1 inférieur 2 égalité 3 difference 
	/**
	 * fonction servant a retraiter une value Condition
	 * @param s
	 */
	public void setCondition(String s){
		if(type[0]==0){
			traitementValueCondition("SUP"+s);
		}else if (type[0]==1){
			traitementValueCondition("INF"+s);
		}else if (type[0]==2){
			traitementValueCondition("EQ"+s);
		}else if (type[0]==3){
			traitementValueCondition("NEQ"+s);
		}
	}
	
	
	private void traitementValueCondition(String s) {
		if(s.contains("SUP")){
			s = s.replaceAll("[^\\d.]", "");
			sup = Double.parseDouble(s);
		}else if(s.contains("INF")){
			s = s.replaceAll("[^\\d.]", "");
			inf = Double.parseDouble(s);		
		}else if(s.contains("EQ") && !s.contains("NEQ")){
			s = s.replaceAll("[^\\d.]", "");
			s = s.replaceAll("==","");
			eq = Double.valueOf(s);
		}else if(s.contains("NEQ")){
			s = s.replaceAll("[^\\d.]", "");
			s = s.replaceAll("=/=","");
			neq = Double.valueOf(s);
		}
	}
	/**
	 * fonction servant à créer une condition simple
	 * @param condition
	 * @param indice
	 * @throws IOException 
	 */
	public void traitement(String condition,int indice) throws IOException{
		String newCondition = "";
		if(condition.startsWith(" ")){
			condition = condition.replaceFirst(" ", "");
		}
		boolean init=false;
		bEq=false;
		isDate=false;
		questionValue = false;
		isCheckBox=false;
		countryTag = "";
	
		if(condition.contains(":")){
			countryTag=condition.split(":")[0];
			condition = condition.substring(countryTag.length()+1);
			countryTag=countryTag.replaceAll(" ","");
		}
		if(condition.contains("VALUE")){
			questionValue=true;	
			questionSkip = condition.replaceAll("VALUE","");
			if(questionSkip.contains(" ")){
				if(questionSkip.split(" ").length>=2){
					if(this.withBraket){
						questionSkip = questionSkip.split(" ")[1];
					} else {
						questionSkip = questionSkip.split(" ")[0];
					}
				}
			}
			type[indice]=-1;

		}else if(condition.contains("_")){
			/*
			 	rajout du tag si besoin ( _rX_cX ) 
			 */
			if(condition.split("_").length==2){
				tag = "_"+condition.split("_")[1];
				condition = condition.replaceAll(tag,"");
			}
			if(condition.split("_").length==3){
				tag = "_"+condition.split("_")[1];
				tag+="_"+condition.split("_")[2];
				tag = tag.split(" ")[0];
				condition = condition.replaceAll(tag,"");
				tag = tag.replaceAll(" ", "");
				}

		}else{
			tag = null;
		}
		newCondition = ""; 
	
		if(!questionValue){
			if(condition.contains("date")){
				isDate= true;
				if(condition.contains("US")){
					dateType="us";
					condition = condition.split("date")[1].replaceAll("US	", "");
				} else {
					dateType="fr";
				}
				condition = condition.replaceAll("date","");
			}
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
			if(preGoTo.contains("SUP")&& !isDate){
				newCondition = preGoTo.replaceAll("[^\\d.]", "");
				sup = Double.parseDouble(newCondition);
				type[indice]=0;				
			}else if(preGoTo.contains("INF")){
				newCondition = preGoTo.replaceAll("[^\\d.]", "");
				inf = Double.parseDouble(newCondition);
				type[indice]=1;
			}else if (preGoTo.contains("NBCHECKED") && !preGoTo.contains("UNCHECKED") && !isDate){
				isCheckedCondition=true;
				newCondition = preGoTo.replaceAll("NBCHECKED","");
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
			}else if (preGoTo.contains("UNCHECKED") && !isDate){
				type[indice]=10;
				isCheckedCondition=true;
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
			}else if(preGoTo.contains("EQ") && !preGoTo.contains("NEQ")&& !isDate){
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
			}else if (preGoTo.contains("#")&& !isDate){
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
			}else if(preGoTo.contains("MIN") && !condition.contains("date") && !isDate){
				type[indice] = 5;
				newCondition = preGoTo.replaceAll("MIN","");
				newCondition = newCondition.replaceAll("MAX","");
				newCondition = newCondition.replaceAll(" ","");
				min = Double.parseDouble(newCondition.split("-")[0]);
				newCondition = newCondition.split("-")[1];
				newCondition = newCondition.replaceAll("[^\\d.]", "");
				max = Double.parseDouble(newCondition);
			}
		} 
		if(!init && !isDate){
			if(condition.contains("SUP")){
				newCondition = condition.replaceAll("[^\\d.]", "");
				if(!questionValue){
					sup = Double.parseDouble(newCondition);
				}
				type[indice]=0;
			}else if(condition.contains("INF")){
				newCondition = condition.replaceAll("[^\\d.]", "");
				if(!questionValue){
					inf = Double.parseDouble(newCondition);
				}
				type[indice]=1;
			}else if(condition.contains("EQ") && !condition.contains("NEQ")){
				type[indice] = 2;
				newCondition = condition.replaceAll("[^\\d.]", "");
				newCondition = newCondition.replaceAll("==","");
				if(!questionValue){
					eq = Double.valueOf(newCondition);
				}
			} else if (condition.contains("NBCHECKED") && !condition.contains("UNCHECKED")){
				isCheckedCondition=true;
				type[indice] = 9;
				newCondition = condition.replaceAll("NBCHECKED", "");
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
				isCheckedCondition=true;
				if(!questionValue){
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
				}
			}else if(condition.contains("NEQ")){
				type[indice] = 3;
				if(!questionValue){
					newCondition = condition.replaceAll("[^\\d.]", "");
					newCondition = newCondition.replaceAll("=/=","");
					neq = Double.valueOf(newCondition);
				}
			}else if (condition.contains("#")){
				 type[indice]=8;
				 if(!questionValue){
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
				 
				 }
				} else if(condition.contains(",") && !condition.contains("#")){
				type[indice] = 4;
				if(!questionValue){
					String [] possibility = condition.split(",");
					checkbox = new int[possibility.length];
					for(int i = 0 ; i < possibility.length; i++){
						possibility[i] = possibility[i].replaceAll(" ", "");
						possibility[i] = possibility[i].replaceAll("[^\\d.]", "");
						if(!possibility[i].isEmpty()){
							checkbox[i] = Integer.valueOf(possibility[i]);		
						}
					}
				}
			}else if(condition.contains("MIN") && !condition.contains("date")){
				type[indice] = 5;
				if(!questionValue){
					newCondition = condition.replaceAll("MIN","");
					newCondition= newCondition.replaceAll("MAX","");
					newCondition = newCondition.replaceAll(" ","");
					min = Double.parseDouble(newCondition.split("-")[0]);
					newCondition = newCondition.split("-")[1];
					newCondition = newCondition.replaceAll("[^\\d.]", "");
					max = Double.parseDouble(newCondition);
				}
			}else if(condition.contains("SUM")){
				type[indice]=7;
				newCondition = condition.replaceAll("[^\\d.]", "");
				constSumRes = Double.valueOf(newCondition);
			}
			
		} 
		if(isDate){
	
			
			if(condition.contains("SUP")){
				condition  = condition.replaceAll("SUP","");
				type[indice] =0 ;
				DateFormat d = new SimpleDateFormat("dd/mm/yy");
				
		
				try {
					dateCondition = d.parse(condition);
					dateCondition.setMonth(Integer.valueOf(condition.split("\\/")[1])-1);
				} catch (ParseException e) {
					Error.printError(e.getMessage());
				}
				
				
			}else if(condition.contains("INF")){
				condition  = condition.replaceAll("INF","");
				type[indice] = 1;
				DateFormat d = new SimpleDateFormat("dd/mm/yy");
				
		
				try {
					dateCondition = d.parse(condition);
					dateCondition.setMonth(Integer.valueOf(condition.split("\\/")[1])-1);
				} catch (ParseException e) {
					Error.printError(e.getMessage());
				}
				
			}else if(condition.contains("EQ") && !condition.contains("NEQ")){
				condition  = condition.replaceAll("EQ","");
				type[indice] = 2;
				
				DateFormat d = new SimpleDateFormat("dd/mm/yy");
				
		
				try {
					dateCondition = d.parse(condition);
					dateCondition.	setMonth(Integer.valueOf(condition.split("\\/")[1])-1);
				} catch (ParseException e) {
					Error.printError(e.getMessage());
				}
			}else if(condition.contains("NEQ")){
				condition  = condition.replaceAll("NEQ","");
				type[indice] = 3;
				DateFormat d = new SimpleDateFormat("dd/mm/yy");
				
	
				try {
					dateCondition = d.parse(condition);
					dateCondition.setMonth(Integer.valueOf(condition.split("\\/")[1])-1);
				} catch (ParseException e) {
					Error.printError(e.getMessage());
				}
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
