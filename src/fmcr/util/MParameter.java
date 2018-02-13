package fmcr.util;

/**
 * A data structure that holds the attributes of method parameters
 * It contains the name and type of the parameter
 * For example:
 * <pre>
 *    MParameter v = new MParameter(name, type);
 * </pre>
 *
 * @author  Inah Omoronyia
 */
public class MParameter {
	private String name;
	private String type;
	
	public MParameter(String name, String type) {
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
