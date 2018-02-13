package fmcr.util;

/**
 * A data structure that holds the attributes of local and global variables visited during traversal.
 * It contains the name and type of a variable
 * For example:
 * <pre>
 *    Variable v = new Variable(name, type);
 * </pre>
 *
 * @author  Inah Omoronyia
 */
public class Variable {
	private String name;
	private String type;
	
	public Variable(String name, String type) {
		this.name = name;
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return name+"|"+type;
	}
}
