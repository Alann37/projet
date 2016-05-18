package baseLibelle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ImportTxt {
	public static List<SawtoothList> test(File file) throws IOException{
		boolean fillList = false;
		boolean constructed = false;
		boolean gridRow=false;
		boolean gridColumn=false;
		int itemNumber = 1;
		List<SawtoothList> allList = new ArrayList<SawtoothList>();
		int i =0;
		int emptyLine=0;
		String questionName= "";
		for(String line : Files.readAllLines(file.toPath())){
				if(i>=2){
					fillList=false;
					itemNumber=-1;
				}
				if(allList.size()>=1){
					if(line.contains("Numeric") && questionName == allList.get(allList.size()-1).questionName){
						allList.get(allList.size()-1).setUse(false);
					}
				}
				if(line.contains("Number of Items")){
					break;
				}
				if(emptyLine>2){
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
					allList.add(new SawtoothList(line.replaceAll("Parent List: ", ""), questionName));
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
					gridRow=false;
					gridColumn=false;
				}
				if(line.contains("Question Direction: Rows")){
					gridRow=true;
					gridColumn=false;
				}
				if(line.contains("Question Direction: Columns")){
					gridRow=false;
					gridColumn=true;
				}
				if(gridRow && line.contains("Numeric")){
					allList.get(allList.size()-1).setUse(false);
					allList.get(allList.size()-2).setUse(false);
				}
				if(gridColumn && line.contains("Numeric")){
					allList.get(allList.size()-1).setUse(false);
					allList.get(allList.size()-2).setUse(false);
				}
				if(gridRow && line.contains("Constant Sum")){
					allList.get(allList.size()-1).setUse(false);
					allList.get(allList.size()-2).setUse(false);
				}
				if(gridColumn && line.contains("Constant Sum")){
					allList.get(allList.size()-1).setUse(false);
					allList.get(allList.size()-2).setUse(false);
				}
				if(gridRow && line.contains("Ranking")){
					allList.get(allList.size()-1).setUse(false);
					allList.get(allList.size()-2).setUse(false);
				}
				if(gridColumn && line.contains("Ranking")){
					allList.get(allList.size()-1).setUse(false);
					allList.get(allList.size()-2).setUse(false);
				}
				if(gridRow && line.contains("Select")){
					allList.get(allList.size()-2).setUse(false);
				}
				if(gridColumn && line.contains("Select")){
					allList.get(allList.size()-1).setUse(false);
				}
				if(line.contains("List Name: ") && !line.contains("Const")){
					allList.add(new SawtoothList(line.replaceAll("List Name: ", ""), questionName));
					fillList = true;
					itemNumber=1;
					i=0;
				}
				if(line.isEmpty()){
					emptyLine++;
				} else {
					emptyLine=0;
				}
				if(emptyLine == 2){
					constructed = false;
				}
		}
		for(int j = 0 ; j < allList.size() ; j ++){
			if(allList.get(j).isUsed()){
				System.out.println("List name : " + allList.get(j).listName + " question name : " + allList.get(j).questionName + "isUsed  " + allList.get(j).isUsed());
				System.out.println("number of item :  "+allList.get(j).listItem.size()+"   " +allList.get(j).listItem.toString());
			}
		}
		System.out.println("name of txt file "+file.getName() );
		return allList;
	}
}
