package entities;

/**
 * Trieda Vlak - sluzi na ulozenie atributov o danom vlaku
 * 
 * @author Slavomir Sarik
 * 
 */
public class Train {

	private int number;
	private String name;
	private String type;

	/**
	 * Konstruktor
	 * 
	 * @param type
	 *            typ vlaku
	 * @param number
	 *            cislo vlaku
	 * @param name
	 *            nazov vlaku
	 */
	public Train(String type, int number, String name) {
		this.type = type;
		this.number = number;
		this.name = name;
	}

	/**
	 * Konstruktor
	 * 
	 * @param type
	 *            typ vlaku
	 * @param number
	 *            cislo vlaku
	 */
	public Train(String type, int number) {
		this.type = type;
		this.number = number;
		this.name = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * vrati detaily o vlaku
	 * 
	 * @return detaily o vlaku
	 */
	public String toString() {
		if (name == null)
			return "Type: " + type + ", Number: " + number;
		else
			return "Type: " + type + ", Number: " + number + ", Name: " + name;
	}

}