package abstractDataTypes;

import java.io.File;

public abstract class Database extends DataContainer {
	
	/**
	 * Sets the path, name and link class level instance variables.
	 * <p>
	 * Subclasses ought to specify what link to use to find their sport's data.
	 * Subclasses will also attempt to create the folder representation of that
	 * Database (with the link based name ending) if it doesn't yet exist.
	 * </p>
	 * 
	 * @param path ought to not be null, blank, and a valid Path.
	 * @param name ought to not be null, blank and a valid file name.
	 * @param link specified in sub classes.
	 */
	protected Database(String path, String name, String link) {
		super(path, name, link);
	}
	
	/**
	 * Checks to see if the file exists and is a directory. Extracts the path and
	 * the name and the link (getLink(f)) from the file and sets them to our
	 * instance variables.
	 * 
	 * @param f ought to exist, and be a directory (HANDLED)
	 */
	protected Database(File f) {
		super(f);
	}
	
	/**
	 * If the given String doesn't exist as a folder (dataset) inside this
	 * Database's folder path, create this folder.
	 * 
	 * Why abstract? Because each Database can only hold Datasets of its own sport
	 * type. Meaning we cannot know what sport this Dataset is from an abstract
	 * context (we are in an abstract class Database).
	 * 
	 * @param dsNameAndEnding will be __g for games, __l for players (because p is taken), __d for drafts etc...
	 */
	protected abstract void constructDataset(String dsNameAndEnding);

	/**
	 * Based on the calling object type (which db child class) and the parameter
	 * file, if it exists and is a directory, figure out what dataset type it is and
	 * return that sport's respective dataset type. If the file doesn't exist, throw an exception.
	 * 
	 * @param dsFile
	 * @return
	 */
	protected abstract Dataset getDataset(File dsFile);
	
	/**
	 * The file at this db's path + name + \\ + dsNameAndEnding must exist as a
	 * directory. If not throw an exception. If it exists, interpret what dataset to
	 * return based on the ending and return that Dataset.
	 * 
	 * @param dsNameAndEnding
	 * @return
	 */
	protected abstract Dataset getDataset(String dsNameAndEnding);
	
	/**
	 * Based on this class' this.link instance variable, return the associated file
	 * ending. If link is bad (null or not one of the cases), throw an exception.
	 * 
	 * @return
	 */
	@Override
	protected String getFileEnding() {
		switch(getLink()) {
		case "pro-football-reference": return "__p";
		case "basketball-reference": return "__b";
		case "sports-reference.com/cbb/": return "__c";
		case "sports-reference.com/cfb/": return "__f";
		case "baseball-reference": return "__s";
		case "hockey-reference": return "__h";
		case "fbref": return "__r";
		case "basketball-reference.com/wnba/": return "__w";
		default: throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Given what should be three characters that are at the end of an existing
	 * folder name, return the web link that corresponds to that ending.
	 * 
	 * @param ending
	 * @return
	 */
	@Override
	protected String getLink(String ending) {
		switch(ending) {
		case "__p": return "pro-football-reference";
		case "__b": return "basketball-reference";
		case "__c": return "sports-reference.com/cbb/";
		case "__f": return "sports-reference.com/cfb/";
		case "__s": return "baseball-reference";
		case "__h": return "hockey-reference";
		case "__r": return "fbref";
		case "__w": return "basketball-reference.com/wnba/";
		default: throw new IllegalArgumentException();
		}
	}
}
