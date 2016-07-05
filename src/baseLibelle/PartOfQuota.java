package baseLibelle;

public class PartOfQuota {
	int limite;
	int value;
	String name;
	public PartOfQuota(int limite, int value, String name) {
		this.limite = limite;
		this.value = value;
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLimite() {
		return limite;
	}
	public void setLimite(int limite) {
		this.limite = limite;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public PartOfQuota(int value) {
		this.value=value;
	}
	
}
