package traitementPrintStudy;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author dbinet
 *
 *classe contenant toute les informations relatives aux quotas d'une étude
 *
 */
public class StudyQuotas {
	List<InfoQuota> quotas;
	String language;
	String name;
	String printStudyName;
	public StudyQuotas(List<InfoQuota> quotas, String language) {
		super();
		this.quotas = quotas;
		this.language = language;
	}
	public String getPrintStudyName() {
		return printStudyName;
	}
	public void setPrintStudyName(String printStudyName) {
		this.printStudyName = printStudyName;
	}
	public StudyQuotas() {
	
		this.language = "";
		this.quotas = new ArrayList<InfoQuota>();
	}
	public StudyQuotas(String n) {
		printStudyName=n;
		String temp = n.replaceAll("Print", "");
		temp = temp.split("\\.")[0];
		
		language =temp.replaceAll("[\\d.]", "");
		name=temp.replaceAll("[^\\d.]", "");;
		this.quotas = new ArrayList<InfoQuota>();
	}
	public List<InfoQuota> getQuotas() {
		return quotas;
	}
	public void setQuotas(List<InfoQuota> quotas) {
		this.quotas = quotas;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
