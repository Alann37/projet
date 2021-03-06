package traitementPrintStudy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author dbinet
 *
 *classe servant a l'import et au traitement des print Studys
 *
 */
public class ImportTxt {
	public static List<SawtoothList> getSawtoothList(File file) throws IOException{
		boolean fillList = false;
		boolean constructed = false;
		int itemNumber = 1;
		List<SawtoothList> allList = new ArrayList<SawtoothList>();
		int i =0;
		int emptyLine=0;
		String questionName= "";
		boolean isRadioButton = false;
		String tempp="";
		for(String line : Files.readAllLines(file.toPath())){
				if(i>=2){
					fillList=false;
					itemNumber=-1;
				}
				if(line.contains("-----------")){
					fillList=false;
					itemNumber=-1;
					constructed=false;
				}
				if(allList.size()>=1){
					if(line.contains("Numeric") && questionName == allList.get(allList.size()-1).questionName){
						allList.get(allList.size()-1).setUse(false);
					}
					if(line.contains("Constant Sum") && questionName == allList.get(allList.size()-1).questionName){
						allList.get(allList.size()-1).setUse(false);
						allList.get(allList.size()-2).setUse(false);
					}
				}
				if(line.contains("Number of Items")){
					break;
				}
				if(emptyLine>1){
					fillList=false;
					itemNumber =-1;
				}
				if(fillList && !line.isEmpty() && !constructed){
					if(line.startsWith((String.valueOf(itemNumber)))){
						if(itemNumber<10){
							line = line.substring(1);
						} else if (itemNumber<100){
							line = line.substring(2);
						} 
						if(line.contains("<br>")){
							line = line.replaceAll("<br>"," ");
						}
						allList.get(allList.size()-1).listItem.add(line);
						itemNumber++;
						i=0;
					}/* else if (justAddItem) {
						if(itemNumber>=2){
							allList.get(allList.size()-1).listItem.get(itemNumber-2).concat(line);
						}
					}*/else {
						i++;
					}
				}
				if(constructed && fillList  && !line.contains("Parent List:")){
					if( !line.isEmpty() && line.startsWith((String.valueOf(itemNumber)))){
						if(itemNumber<10){
							line = line.substring(1);
						} else if (itemNumber<100){
							line = line.substring(2);
						} 
						if(line.contains("<br>")){
							line = line.replaceAll("<br>"," ");
						}
						allList.get(allList.size()-1).listItem.add(line);
						i=0; 
						itemNumber++;
					} else {
						i++;
					}
				}
				if(constructed && line.contains("Parent List:")){
					allList.add(new SawtoothList(line.replaceAll("Parent List: ", ""), questionName,isRadioButton));
					fillList =true;
					itemNumber =1;
					i=0;
				}
				if(line.contains("Type:")){
					if(line.contains("Constructed")){
						constructed=true;
						fillList= false;
						itemNumber =1;
						i=0;
					}/* else if(line.contains("Select")){
						//fillList=true;
						itemNumber =1;
						i=0;
					}*/
				}
				if(line.contains("Question Name: ")){
					questionName = line.replaceAll("Question Name: ", "");
					isRadioButton=false;

				}

				if(line.contains("Type: Select (Radio Button)")&&tempp.contains("Row") ||line.contains("Type: Select (Radio Button)")&&tempp.contains("Column") ){
					allList.get(allList.size()-1).isRadioButton=true;
					allList.get(allList.size()-2).isRadioButton=true;
					isRadioButton=false;
				}else if(line.contains("Type: Select (Radio Button)")){
					isRadioButton=true;
				}
				if((tempp.contains("Column") && line.contains("Numeric"))|| (tempp.contains("Row") && line.contains("Numeric"))){
					allList.get(allList.size()-1).setUse(false);
					allList.get(allList.size()-2).setUse(false);
				}
				
				if((tempp.contains("Column") && line.contains("Constant Sum"))|| (tempp.contains("Row") && line.contains("Constant Sum"))){
					allList.get(allList.size()-1).setUse(false);
				}
				if((tempp.contains("Ranking") && line.contains("Numeric"))|| (tempp.contains("Row") && line.contains("Ranking"))){
					allList.get(allList.size()-1).setUse(false);
					allList.get(allList.size()-2).setUse(false);
				}
				if(line.contains("[Scale Point Values]:")){
					allList.get(allList.size()-1).setUse(false);
					allList.get(allList.size()-2).setUse(false);
				}
				
				if(line.contains("List Name: ") && !line.contains("Const")){
					allList.add(new SawtoothList(line.replaceAll("List Name: ", ""), questionName,isRadioButton));
					fillList = true;
					
					itemNumber=1;
					i=0;
				}
				if(line.isEmpty()){
					emptyLine++;
				} else {
					emptyLine=0;
				}
				if(emptyLine == 3){
					constructed = false;
				}
				if(line.contains("LIST SECTION") && !line.contains("*")){
					break;
				}
				tempp = line;
		}
		
		for(int h = 0 ; h < allList.size();h++){
			if(allList.get(h).listName.contains("Row")){
				allList.get(h).isRowList=true;
				allList.get(h).isGridList= true;
			}
			if(allList.get(h).listName.contains("Col")){
				allList.get(h).isColList=true;
				allList.get(h).isGridList= true;
			}
		}
		/*for(int j = 0 ; j < allList.size();j++){
			System.out.println(allList.get(j).questionName + " used " +allList.get(j).isUsed() + " listName : "+allList.get(j).listName);
		}*/
		return allList;
	}

	public static StudyQuotas getQuota(File file) throws IOException{
		boolean onFill=false;
		String questionName="";
		String previous="";
		StudyQuotas quotas = new StudyQuotas(file.getName());
		
		for(String line : Files.readAllLines(file.toPath())){
			if(line.contains("PAGE BREAK")){
				onFill = false;
			}
			if(line.contains("Question Name:")){
				questionName = line;
				questionName = questionName.replaceAll("Question Name:", "");
			}
			if(line.contains("Type: Quota")){
				if(questionName.startsWith(" ")){
					questionName = questionName.substring(1);
				}
				quotas.quotas.add(new InfoQuota(questionName));
				onFill=true;
			}
			if(onFill && line.contains("[")){
				previous=line;
			}
			if(quotas.quotas.size()>0){
				if(line.contains("Limit") && onFill){
					String temp = line;
					temp = temp.replaceAll("[^\\d.]", "");
					quotas.quotas.get(quotas.quotas.size()-1).posibility.get(quotas.quotas.get(quotas.quotas.size()-1).posibility.size()-1).setLimite(Integer.parseInt(temp));
				}
				if(line.contains("Value") && onFill){
					
					String temp = line;
					temp = temp.replaceAll("[^\\d.]", "");
					previous = previous.replaceAll("\\[", "");
					previous = previous.replaceAll("\\]", "");
					quotas.quotas.get(quotas.quotas.size()-1).posibility.add(new PartOfQuota(Integer.parseInt(temp)));
					quotas.quotas.get(quotas.quotas.size()-1).posibility.get(quotas.quotas.get(quotas.quotas.size()-1).posibility.size()-1).setName(previous);
						
				}
			}
			
		}
		return quotas;
	}
	public static List<String> getQuestionList(File file) throws IOException {
		List<String> lRet = new ArrayList<String>();
		boolean onFill=false;;
		List<FreeFormatGuide> freeFormat = new ArrayList<FreeFormatGuide>();
		boolean fillFreeFormat =false;
		String freeFormatName="";
		int j = 0;
		int ind = -2;
		for(String line : Files.readAllLines(file.toPath())){
			if(line.contains("Question List")){
				onFill=true;
			}
			
			if(onFill && !line.isEmpty()){
				if(line.contains("Free Format")){
					String temp = line.split("\\(")[0];
					temp = temp.replaceAll("\t", "");
					temp = temp.replaceAll(" ", "");
					freeFormat.add(new FreeFormatGuide(temp));
					
				}
				if(line.contains("(") && !line.split("\\(")[1].contains("Text") && !line.split("\\(")[1].contains("Terminate / Link")  && !line.split("\\(")[1].contains("CBC ")){
					line = line.split("\\(")[0] ;
					line = line.replaceAll(" ", "");
					line = line.replaceAll("\t","");
					lRet.add(line);
				}
			}
			if(line.contains("Data Field")){
				onFill = false;
			
			}
			if(!onFill){
				if(line.contains("Question Name")){
					for(int i = 0 ; i < freeFormat.size();i++){
						String temp = line.split(":")[1];
						temp = temp.replaceAll(" ", "");
						if(temp.equals(freeFormat.get(i).questionName)){
							freeFormatName = freeFormat.get(i).questionName;
							fillFreeFormat=true;
							ind = i;
							break;
						}
					}
				}
			}
			if(j==1){
				j=0;
				freeFormat.add(new FreeFormatGuide(freeFormatName,line.split(":")[1]));
			}
			if(fillFreeFormat){
				if(line.contains("[Variable")){
					j=1;
				}
			}
			
		}
		for(int i = 0 ; i < freeFormat.size();i++){
			if(freeFormat.get(i).variableName==null){
				freeFormat.remove(i);
				i--;
			}
		}
		for(int i = freeFormat.size()-1 ; i >=0;i--){
			for(j = 0 ; j < lRet.size(); j ++){
				if(lRet.get(j).equals(freeFormat.get(i).questionName)){
					lRet.add(j+1, freeFormat.get(i).variableName);
					if((i-1)>=0){
						if(!lRet.get(j).equals(freeFormat.get(i-1).questionName)){
							lRet.remove(j);
						}
					} else {
						lRet.remove(j);
					}
					break;
				}
			}
		}
		//System.out.println(lRet);
		return lRet;
	}
}
