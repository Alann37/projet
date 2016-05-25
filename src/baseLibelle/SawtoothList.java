package baseLibelle;

import java.util.ArrayList;
import java.util.List;

public class SawtoothList {
	String listName;
	List<String> listItem;
	String questionName;
	boolean isUse;
	boolean isRowList;
	boolean isColList;
	boolean isGridList;
	boolean isRadioButton;
	public SawtoothList(String name,String question,boolean rButton){
		this.listName=name;
		questionName = question;
		this.listItem = new ArrayList<String>();
		isUse=true;
		isRowList=false;
		isColList=false;
		isRadioButton=rButton;
		isGridList = false;
	}
	public boolean isUsed(){
		return isUse;
	}
	public void setUse(boolean u){
		isUse=u;
	}
	
}
