package importMSQLServer;
/**
 * 
 * @author dbinet
 * classe servant à récupérer les différentes tables de la base et 
 * a extraire leurs numéros pour permettre un tri cohérent des données
 */
public class TableImport {
	String name;
	int number;
	public TableImport(String all){
		name = all;
		if(all.contains("data")){
			String temp = all.split("data")[1];
			temp = temp.replaceAll("[^\\d.]", "");
			if(!temp.isEmpty()){
				number = Integer.parseInt(temp);
			}
		} else {
			number = -1;
		}
	}
	@Override
	public String toString() {
		return "TableImport [name=" + name + ", number=" + number + "]";
	}
}
