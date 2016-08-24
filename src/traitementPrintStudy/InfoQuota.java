package traitementPrintStudy;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author dbinet
 *
 *classe servant à la création des Quotas via les prints studys
 *
 */
public class InfoQuota {
	public List<PartOfQuota> getPosibility() {
		return posibility;
	}

	public void setPosibility(List<PartOfQuota> posibility) {
		this.posibility = posibility;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	List<PartOfQuota> posibility;
	String name;
	public InfoQuota(String n){
		name = n;
		posibility= new ArrayList<PartOfQuota>();
	}
}
