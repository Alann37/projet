package importExcel;

public class MultipleCondition {
	String questionName;
	Condition secondCondition;
	String partOfLoop;
	public MultipleCondition(String secondPart){
		if(secondPart.contains("SUP")){
			questionName= secondPart.split("SUP")[0];
			questionName = questionName.replaceAll(" ","");
			secondCondition = new Condition("SUP"+secondPart.split("SUP")[1]);
		}else if(secondPart.contains("INF")){
			questionName= secondPart.split("INF")[0];
			questionName = questionName.replaceAll(" ","");
			secondCondition = new Condition("INF"+secondPart.split("INF")[1]);
		}else if(secondPart.contains("EQ") && !secondPart.contains("NEQ")){
			questionName= secondPart.split("EQ")[0];
			questionName = questionName.replaceAll(" ","");
			secondCondition = new Condition("EQ"+secondPart.split("EQ")[1]);
		} else if(secondPart.contains("NEQ")){
			questionName= secondPart.split("NEQ")[0];
			questionName = questionName.replaceAll(" ","");
			secondCondition = new Condition("NEQ"+secondPart.split("NEQ")[1]);
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
		if(secondPart.contains("SUP")){
			questionName= secondPart.split("SUP")[0];
			questionName = questionName.replaceAll(" ","");
			secondCondition = new Condition("SUP"+secondPart.split("SUP")[1]);
		}else if(secondPart.contains("INF") ){
			questionName= secondPart.split(">")[0];
			questionName = questionName.replaceAll(" ","");
			secondCondition = new Condition("INF"+secondPart.split("INF")[1]);
		}else if(secondPart.contains("EQ") && !secondPart.contains("NEQ")){
			questionName= secondPart.split("EQ")[0];
			questionName = questionName.replaceAll(" ","");
			secondCondition = new Condition("EQ"+secondPart.split("EQ")[1]);
		} else if(secondPart.contains("NEQ")){
			questionName= secondPart.split("NEQ")[0];
			questionName = questionName.replaceAll(" ","");
			secondCondition = new Condition("NEQ"+secondPart.split("NEQ")[1]);
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
		
		if(secondPart.contains("SUP")){
			questionName= secondPart.split("SUP")[0];
			questionName = questionName.replaceAll(" ","");
			if(!s.isEmpty() ){
				secondCondition = new Condition("SUP"+s);
			}else {
				secondCondition = new Condition("SUP"+i);
			}
		}else if(secondPart.contains("INF") ){
			questionName= secondPart.split("INF")[0];
			questionName = questionName.replaceAll(" ","");
			if(!s.isEmpty() ){
				secondCondition = new Condition("INF"+s);
			}else if ( i > 0) {
				secondCondition = new Condition("INF"+i);
			}
		}else if(secondPart.contains("EQ") && !secondPart.contains("NEQ")){
			questionName= secondPart.split("EQ")[0];
			questionName = questionName.replaceAll(" ","");
			if(!s.isEmpty() ){
				secondCondition = new Condition("EQ"+s);
			}else {
				secondCondition = new Condition("EQ"+i);
			}
			
		} else if(secondPart.contains("NEQ")){
			questionName= secondPart.split("NEQ")[0];
			questionName = questionName.replaceAll(" ","");
			if(!s.isEmpty() ){
				secondCondition = new Condition("NEQ"+s);
			}else {
				secondCondition = new Condition("NEQ"+i);
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
		
		if(secondPart.contains("SUP")){
			questionName= secondPart.split("SUP")[0];
			questionName = questionName.replaceAll(" ","");
			if(!s.isEmpty() ){
				secondCondition = new Condition("SUP"+s);
			}else {
				secondCondition = new Condition("SUP"+i);
			}
		}else if(secondPart.contains("INF") ){
			questionName= secondPart.split("INF")[0];
			questionName = questionName.replaceAll(" ","");
			if(!s.isEmpty() ){
				secondCondition = new Condition("INF"+s);
			}else {
				secondCondition = new Condition("INF"+i);
			}
		}else if(secondPart.contains("EQ")&& !secondPart.contains("NEQ")){
			questionName= secondPart.split("EQ")[0];
			questionName = questionName.replaceAll(" ","");
			if(!s.isEmpty() ){
				secondCondition = new Condition("EQ"+s);
			}else {
				secondCondition = new Condition("EQ"+i);
			}
			
		} else if(secondPart.contains("NEQ")){
			questionName= secondPart.split("NEQ")[0];
			questionName = questionName.replaceAll(" ","");
			if(!s.isEmpty() ){
				secondCondition = new Condition("NEQ"+s);
			}else {
				secondCondition = new Condition("NEQ"+i);
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
