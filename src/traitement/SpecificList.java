package traitement;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * 
 * @author dbinet
 *
 *contient l'ensemble des conditions simple d'une condition multiple
 *
 */
public class SpecificList {
	List<SpecificCondition> conditions;
	String countryTag;
	boolean isAer;
	public SpecificList(){
		conditions = new ArrayList<SpecificCondition>();
		countryTag="";
	}
	public SpecificList(List<SpecificCondition> c,boolean b){
		conditions = c;
		countryTag="";
		isAer = b;
	}

}

