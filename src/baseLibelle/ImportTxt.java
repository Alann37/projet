package baseLibelle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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
		for(int j = 0 ; j < allList.size();j++){
			//System.out.println(allList.get(j).questionName);
		}
		return allList;
	}

	public static List<String> getQuestionList(File file) throws IOException {
		List<String> lRet = new ArrayList<String>();
		boolean onFill=false;;
		
		for(String line : Files.readAllLines(file.toPath())){
			if(line.contains("Question List")){
				onFill=true;
			}
			if(onFill && !line.isEmpty()){
				if(line.contains("(") && !line.contains("Text") && !line.contains("Terminate / Link")){
					line = line.split("\\(")[0] ;
					line = line.replaceAll(" ", "");
					lRet.add(line);
				}
			}
			if(line.contains("Data Field")){
				onFill = false;
				break;
			}
		}
		//System.out.println(lRet);
		return lRet;
	}
}
