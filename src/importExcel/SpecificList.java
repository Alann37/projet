package importExcel;

import java.util.ArrayList;
import java.util.List;

public class SpecificList {
	List<SpecificCondition> conditions;
	String countryTag;
	public SpecificList(){
		conditions = new ArrayList<SpecificCondition>();
		countryTag="";
	}
	public SpecificList(List<SpecificCondition> c){
		conditions = c;
		countryTag="";
	}

}

