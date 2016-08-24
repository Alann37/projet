package traitement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * 
 * 
 * @author dbinet
 *
 *classe représentant une condition simple d'une condition multiple
 *
 */
public class SpecificCondition {
	Condition c;
	int beginBraket;
	int endBraket;
	int braketPlace;
	boolean isLink;
	boolean isValue;
	boolean treated;
	boolean satisfied;
	boolean empty;
	int indice;
	boolean inLoop;
	String link;
	List<ValidationBoucle> loop;
	List<Reponse> answers;
	public SpecificCondition (String condition, int i ,boolean b,int begin	) throws IOException{
		empty=false;
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
		
			if(condition.contains(" ")){
				
				if(condition.startsWith(" ")){
					condition = condition.substring(1);
				}
				if(condition.split(" ").length>=2){
					link = condition.split(" ")[0];
					condition = condition.replaceAll(link, "");
				}
				if(link !=null){
					if(link.contains("_")){
						if(link.split("_").length==2 && !link.contains("date")){
							
							String tag = "_" + link.split("_")[1];
							condition +=tag;
							link = link.replaceAll(tag, "");
						}else if (link.split("_").length==3 &&!link.contains("date")){
							String tag = "_" + link.split("_")[1]+"_"+link.split("_")[2];
							condition += tag;
							link = link.replaceAll(tag, "");
							
						}
					}
				}
			}
			if(link!=null){
				link = link.replaceAll(" ", "");
			}
			if(condition.contains("VALUE")){
				isValue=true;
			}
			if(!condition.isEmpty()){
 				c=new Condition(condition,true);
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
