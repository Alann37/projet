package importExcel;

import java.util.ArrayList;
import java.util.List;

public class SpecificCondition {
	Condition c;
	int beginBraket;
	int endBraket;
	int braketPlace;
	boolean isLink;
	boolean isValue;
	boolean treated;
	boolean satisfied;
	int indice;
	boolean inLoop;
	String link;
	List<ValidationBoucle> loop;
	List<Reponse> answers;
	public SpecificCondition (String condition, int i ,boolean b,int begin){
		satisfied=false;
		beginBraket=begin;
		loop=new ArrayList<ValidationBoucle>();
		treated = false;
		indice =-1;
		answers= new ArrayList<Reponse>();
		isLink  = b;
		inLoop=false;
		isValue=false;
		braketPlace=i;
		if(!b){
			if(condition.contains(" ") && !condition.contains("VALUE")){
				
				if(condition.startsWith(" ")){
					condition = condition.substring(1);
				}
				if(condition.split(" ").length>=2){
					link = condition.split(" ")[0];
					condition = condition.split(" ")[1];
				}
			}
			if(condition.contains("VALUE")){
				isValue=true;
			}
			if(!condition.isEmpty()){
				c=new Condition(condition);
			}
		}else {
			link = condition;
			c=null;
		}
	}
	@Override
	public String toString() {
		return "SpecificCondition [c=" + c + ", braketPlace=" + braketPlace + ", isLink=" + isLink + ", link=" + link
				+ "]";
	}
	
}
