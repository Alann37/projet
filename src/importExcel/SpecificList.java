package importExcel;

import java.util.ArrayList;
import java.util.List;

public class SpecificList {
	List<SpecificCondition> conditions;
	public SpecificList(){
		conditions = new ArrayList<SpecificCondition>();
	}
	public SpecificList(List<SpecificCondition> c){
		conditions = c;
	}
}
