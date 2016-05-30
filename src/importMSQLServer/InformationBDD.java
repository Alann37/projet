package importMSQLServer;

import java.util.ArrayList;
import java.util.List;

public class InformationBDD {
	private String base;
	private List<String>langues;
	public InformationBDD(String base) {
		this.base = base;
		this.langues = new ArrayList<String>();
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public List<String> getLangues() {
		return langues;
	}
	public void setLangues(List<String> langues) {
		this.langues = langues;
	}
	
	
}
