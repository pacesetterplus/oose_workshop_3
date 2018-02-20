package fmcr.visitors;
/**
 * Starter code for JP2 lab 3. Name: Yusuf Ogunjobi Student Number: 1009097
 */
public abstract class BrickSet {
	/** Fields */
	public int setNumber, numPieces;
	private String name;
	final private String theme;

	/** Creates a new BrickSet object with the given parameters */
	public BrickSet(int setNumber, String name, String theme, int numPieces) {
		this.setNumber = setNumber;
		this.name = name;
		this.theme = theme;
		this.numPieces = numPieces;
	}
	
	/** Getters and setters */
	public int getSetNumber() {
		return setNumber;
	}

	public String getName(String play) {
		return name;
	}

	public String getTheme() {
		return theme;
	}

	public int getNumPieces() {
		return numPieces;
	}

	/**
	 * Override toString() to return a nicer string representation of the BrickSet
	 */
	public String toString() {
		return setNumber + ": " + name + " (" + theme + "), " + numPieces + " pieces, ";
	}

	protected abstract String getDetails();

}
