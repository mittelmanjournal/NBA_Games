package databaseSubclasses;

import java.io.File;

import abstractDataTypes.Database;
import abstractDataTypes.Dataset;
import basketballDatasets.BasketballGameDataset;
import basketballDatasets.BasketballPlayerDataset;

/**
 * @author Uri Mittelman
 *
 */

public class BasketballDatabase extends Database {

	/**
	 * Given what should be the DatabaseManager's folder as the path, create a
	 * folder to represent this Basketball database if it doesn't exist. We know the
	 * file ending will be "__b" because that's the ending associated with the
	 * basketball web link.
	 * 
	 * @param path
	 * @param name
	 */
	public BasketballDatabase(String path, String name) {
		super(path, name, getWebLink());
		createFolder();
	}
	
	/**
	 * Given an File that should be an existing directory that ends with "__b",
	 * assign all class level variables including the link.
	 * 
	 * @param f
	 */
	public BasketballDatabase(File f) {
		super(f);
//		createFolder(); // don't try to create the folder because it already exists
		//what if the given existing file has an ending that isn't the correct one (__b)
		//the link gets set based on the ending in the super constructor above
		if(!getLink().equals(getWebLink())) {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Given the name of a potentially existing ds folder with its type specific
	 * ending, create the folder if it doesn't exist.
	 * TODO clean up disgusting comments 
	 */
	@Override
	public void constructDataset(String dsNameAndEnding) {
		
		// get the full dbpath + \\ + param and create a file with it
		// if this file !exists do stuff, otherwise do nothing
			//make a switch statement based on the last 3 chars in the string, create the associated ds 
		int endIndex = dsNameAndEnding.length() - 3;
		
		File dsF = new File(getFullPath() + "\\" + dsNameAndEnding);
		if(!dsF.exists()) { //TODO: if this is already done in the DS constructors, don't bother to have this here
			switch (dsNameAndEnding.substring(endIndex)) {
			case "__g":
				//remove the ending because the BGDS class should handle the ending stuff
				new BasketballGameDataset(getFullPath() + "\\", dsNameAndEnding.substring(0, endIndex));
				// similar to db constructors, subclasses only take in path and name, link is auto set and whenever we need the ending we get it based on the link.
					// also the DS parent class takes in a link but its handled in each subclass
				// try to create that Dataset folder if it doesn't exist
				// IDEA: GameDataset(Type.Basketball, ..., ...)
			case "__l":
				new BasketballPlayerDataset(getFullPath() + "\\", dsNameAndEnding.substring(0, endIndex));
			}
		}
	}
	
	/**
	 * Given a File that should exist as a directory (Dataset file representation)
	 * inside this Database, based on its ending, return the specific correlated
	 * Dataset.
	 */
	@Override
	public Dataset getDataset(File dsFile) {
		String dsName = dsFile.getName();
		int endIndex = dsName.length() - 3;
		if(dsFile.exists() && dsFile.isDirectory()) {
			switch(dsName.substring(endIndex)) {
			case "__g": return new BasketballGameDataset(getFullPath() + "\\", dsName.substring(0, endIndex));
			// maybe I should make constructors for DBs and DSs that can parse when given a String with ending: ACTUALLY WONT WORK!
				// return new BasketballGameDataset(File dsFile); // assume lowest directory is
				// our Dataset, verify upper Directory to have correct DB ending, if so proceed
				// to try to create this ds if it doesn't exist
			case "__l": return new BasketballPlayerDataset(getFullPath() + "\\", dsName.substring(0, endIndex));
			default: throw new IllegalArgumentException();
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Given the full name of a file that ought to be an existing directory inside
	 * this Database's folder, get the Dataset java object representation of it.
	 */
	@Override
	public Dataset getDataset(String dsNameAndEnding) {
		return getDataset(new File(getFullPath() + "\\" + dsNameAndEnding));
	}
	
	public static String getWebLink() {
		return "basketball-reference";
	}
	
	public static String getFullWebLink() {
		return "https://www." + getWebLink() + ".com";
	}
}
