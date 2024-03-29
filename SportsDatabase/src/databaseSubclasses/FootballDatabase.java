package databaseSubclasses;

import java.io.File;

import abstractDataTypes.Database;
import abstractDataTypes.Dataset;
import footballDatasets.FootballGameDataset;
import footballDatasets.FootballPlayerDataset;

public class FootballDatabase extends Database {

	public FootballDatabase(String path, String name) {
		super(path, name, getWebLink());
		createFolder();
	}
	
	public FootballDatabase(File f) {
		super(f);
		if(!getLink().equals(getWebLink())) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void constructDataset(String dsNameAndEnding) {
		int endIndex = dsNameAndEnding.length() - 3;
		
		File dsF = new File(getFullPath() + "\\" + dsNameAndEnding);
		if(!dsF.exists()) { //TODO: if this is already done in the DS constructors, don't bother to have this here
			switch (dsNameAndEnding.substring(endIndex)) {
			case "__g":
				//remove the ending because the BGDS class should handle the ending stuff
				new FootballGameDataset(getFullPath() + "\\", dsNameAndEnding.substring(0, endIndex));
				// similar to db constructors, subclasses only take in path and name, link is auto set and whenever we need the ending we get it based on the link.
					// also the DS parent class takes in a link but its handled in each subclass
				// try to create that Dataset folder if it doesn't exist
				// IDEA: GameDataset(Type.Basketball, ..., ...)
			case "__l":
				new FootballPlayerDataset(getFullPath() + "\\", dsNameAndEnding.substring(0, endIndex));
			}
		}
	}
	
	@Override
	public Dataset getDataset(File dsFile) {
		String dsName = dsFile.getName();
		int endIndex = dsName.length() - 3;
		if(dsFile.exists() && dsFile.isDirectory()) {
			switch(dsName.substring(endIndex)) {
			case "__g": return new FootballGameDataset(getFullPath() + "\\", dsName.substring(0, endIndex));
			case "__l": return new FootballPlayerDataset(getFullPath() + "\\", dsName.substring(0, endIndex));
			default: throw new IllegalArgumentException();
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public Dataset getDataset(String dsNameAndEnding) {
		return getDataset(new File(getFullPath() + "\\" + dsNameAndEnding));
	}

	public static String getWebLink() {
		return "pro-football-reference";
	}
	
	public static String getFullWebLink() {
		return "https://www." + getWebLink() + ".com";
	}
	
}
