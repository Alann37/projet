package importExcel;

public class MultipleCondition {
	String questionName;
	Condition secondCondition;
	String partOfLoop;
	public MultipleCondition(String secondPart){
		if(secondPart.contains("<")){
			questionName= secondPart.split("<")[0];
			questionName = questionName.replaceAll(" ","");
			secondCondition = new Condition("<"+secondPart.split("<")[1]);
		}else if(secondPart.contains(">")){
			questionName= secondPart.split(">")[0];
			questionName = questionName.replaceAll(" ","");
			secondCondition = new Condition(">"+secondPart.split(">")[1]);
		}else if(secondPart.contains("==")){
			questionName= secondPart.split("==")[0];
			questionName = questionName.replaceAll(" ","");
			secondCondition = new Condition("=="+secondPart.split("==")[1]);
		} else if(secondPart.contains("=/=")){
			questionName= secondPart.split("=/=")[0];
			questionName = questionName.replaceAll(" ","");
			secondCondition = new Condition("=/="+secondPart.split("=/=")[1]);
		} else if(secondPart.contains(",")){
			if(secondPart.contains(" ")){
				if(secondPart.startsWith(" ")){
					secondPart=secondPart.substring(1);
				}
				questionName = secondPart.split(" ")[0];
				secondPart = secondPart.replace(questionName, "");
			}
			secondCondition = new Condition(secondPart);
		}else if(secondPart.contains("date")){
			
		}else if(secondPart.contains("MIN") && !secondPart.contains("date") ){
			questionName= secondPart.split("MIN")[0];
			questionName = questionName.replaceAll(" ","");
			secondCondition = new Condition("MIN"+secondPart.split("MIN")[1]);
		}
	}
	public MultipleCondition(String secondPart,String loopPart){
		if(secondPart.contains("<")){
			questionName= secondPart.split("<")[0];
			questionName = questionName.replaceAll(" ","");
			secondCondition = new Condition("<"+secondPart.split("<")[1]);
		}else if(secondPart.contains(">") ){
			questionName= secondPart.split(">")[0];
			questionName = questionName.replaceAll(" ","");
			secondCondition = new Condition(">"+secondPart.split(">")[1]);
		}else if(secondPart.contains("==")){
			questionName= secondPart.split("==")[0];
			questionName = questionName.replaceAll(" ","");
			secondCondition = new Condition("=="+secondPart.split("==")[1]);
		} else if(secondPart.contains("=/=")){
			questionName= secondPart.split("=/=")[0];
			questionName = questionName.replaceAll(" ","");
			secondCondition = new Condition("=/="+secondPart.split("=/=")[1]);
		} else if(secondPart.contains(",")){
			if(secondPart.contains(" ")){
				if(secondPart.startsWith(" ")){
					secondPart=secondPart.substring(1);
				}
				questionName = secondPart.split(" ")[0];
				secondPart = secondPart.replace(questionName, "");
			}
			secondCondition = new Condition(secondPart);
		}else if(secondPart.contains("date")){
			
		}else if(secondPart.contains("MIN") && !secondPart.contains("date")){
			questionName= secondPart.split("MIN")[0];
			questionName = questionName.replaceAll(" ","");
			secondCondition = new Condition("MIN"+secondPart.split("MIN")[1]);
		}
		partOfLoop=loopPart;
	}
	public MultipleCondition(String secondPart,double i,String s){
		
		if(secondPart.contains("<")){
			questionName= secondPart.split("<")[0];
			questionName = questionName.replaceAll(" ","");
			if(!s.isEmpty() ){
				secondCondition = new Condition("<"+s);
			}else {
				secondCondition = new Condition("<"+i);
			}
		}else if(secondPart.contains(">") ){
			questionName= secondPart.split(">")[0];
			questionName = questionName.replaceAll(" ","");
			if(!s.isEmpty() ){
				secondCondition = new Condition(">"+s);
			}else if ( i > 0) {
				secondCondition = new Condition(">"+i);
			}
		}else if(secondPart.contains("==")){
			questionName= secondPart.split("==")[0];
			questionName = questionName.replaceAll(" ","");
			if(!s.isEmpty() ){
				secondCondition = new Condition("=="+s);
			}else {
				secondCondition = new Condition("=="+i);
			}
			
		} else if(secondPart.contains("=/=")){
			questionName= secondPart.split("=/=")[0];
			questionName = questionName.replaceAll(" ","");
			if(!s.isEmpty() ){
				secondCondition = new Condition("=/="+s);
			}else {
				secondCondition = new Condition("=/="+i);
			}
		} else if(secondPart.contains(",")){
			if(secondPart.contains(" ")){
				if(secondPart.startsWith(" ")){
					secondPart=secondPart.substring(1);
				}
				questionName = secondPart.split(" ")[0];
				secondPart = secondPart.replace(questionName, "");
			}
			secondCondition = new Condition(secondPart);
		}else if(secondPart.contains("date")){
			
		}else if(secondPart.contains("MIN") && !secondPart.contains("date")){
			questionName= secondPart.split("MIN")[0];
			questionName = questionName.replaceAll(" ","");
			secondCondition = new Condition("MIN"+secondPart.split("MIN")[1]);
		}
	}
public MultipleCondition(String secondPart,double i,String s,String country){
		
		if(secondPart.contains("<")){
			questionName= secondPart.split("<")[0];
			questionName = questionName.replaceAll(" ","");
			if(!s.isEmpty() ){
				secondCondition = new Condition("<"+s);
			}else {
				secondCondition = new Condition("<"+i);
			}
		}else if(secondPart.contains(">") ){
			questionName= secondPart.split(">")[0];
			questionName = questionName.replaceAll(" ","");
			if(!s.isEmpty() ){
				secondCondition = new Condition(">"+s);
			}else {
				secondCondition = new Condition(">"+i);
			}
		}else if(secondPart.contains("==")){
			questionName= secondPart.split("==")[0];
			questionName = questionName.replaceAll(" ","");
			if(!s.isEmpty() ){
				secondCondition = new Condition("=="+s);
			}else {
				secondCondition = new Condition("=="+i);
			}
			
		} else if(secondPart.contains("=/=")){
			questionName= secondPart.split("=/=")[0];
			questionName = questionName.replaceAll(" ","");
			if(!s.isEmpty() ){
				secondCondition = new Condition("=/="+s);
			}else {
				secondCondition = new Condition("=/="+i);
			}
		} else if(secondPart.contains(",")){
			if(secondPart.contains(" ")){
				if(secondPart.startsWith(" ")){
					secondPart=secondPart.substring(1);
				}
				questionName = secondPart.split(" ")[0];
				secondPart = secondPart.replace(questionName, "");
			}
			secondCondition = new Condition(secondPart);
		}else if(secondPart.contains("date")){
			
		}else if(secondPart.contains("MIN") && !secondPart.contains("date")){
			questionName= secondPart.split("MIN")[0];
			questionName = questionName.replaceAll(" ","");
			secondCondition = new Condition("MIN"+secondPart.split("MIN")[1]);
		}
		secondCondition.countryTag=country;
	}
}
