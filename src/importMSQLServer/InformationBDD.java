package importMSQLServer;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author dbinet
 *
 * Classe contenant les informations de ImportDatabase pour chaque étude
 */

public class InformationBDD {
	private String base;
	private String serveur;
	private List<String>langues;
	private String ValidationGuideName;
	public String getValidationGuideName() {
		return ValidationGuideName;
	}
	public void setValidationGuideName(String validationGuideName) {
		ValidationGuideName = validationGuideName;
	}
	public String getServeur() {
		return serveur;
	}
	public void setServeur(String serveur) {
		this.serveur = serveur;
	}
	
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
