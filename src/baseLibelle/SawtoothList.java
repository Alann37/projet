package baseLibelle;

import java.util.ArrayList;
import java.util.List;

public class SawtoothList {
	String listName;
	List<String> listItem;
	String questionName;
	boolean isUse;
	public SawtoothList(String name,String question){
		this.listName=name;
		questionName = question;
		this.listItem = new ArrayList<String>();
		isUse=true;
	}
	public boolean isUsed(){
		return isUse;
	}
	public void setUse(boolean u){
		isUse=u;
	}
	
}
