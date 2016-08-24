package importMSQLServer;
/**
 * 
 * @author dbinet
 * classe servant � r�cup�rer les diff�rentes tables de la base et 
 * a extraire leurs num�ros pour permettre un tri coh�rent des donn�es
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
