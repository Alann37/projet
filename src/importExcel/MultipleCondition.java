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
			
		}else if(secondPart.contains("date")){
			
		}else if(secondPart.contains("MIN") && !secondPart.contains("date")){
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
			
		}else if(secondPart.contains("date")){
			
		}else if(secondPart.contains("MIN") && !secondPart.contains("date")){
			questionName= secondPart.split("MIN")[0];
			questionName = questionName.replaceAll(" ","");
			secondCondition = new Condition("MIN"+secondPart.split("MIN")[1]);
		}
		partOfLoop=loopPart;
	}
}
