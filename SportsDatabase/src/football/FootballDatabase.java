package football;

import java.io.File;
import java.nio.file.Path;

import abstractClasses.Database;
import abstractClasses.Dataset;

public class FootballDatabase extends Database {
	
	public FootballDatabase(String path, String name) throws IllegalArgumentException {
		super(path, name);
		createFolder();
	}
	
	protected FootballDatabase(String pathAndName) throws IllegalArgumentException {
		super(pathAndName);
		createFolder();
	}

	protected FootballDatabase(Path pathAndName) throws IllegalArgumentException {
		super(pathAndName);
		// TODO Consider verifying folder existence
	}
	
	public FootballDatabase(File f) throws IllegalArgumentException {
		super(f);
		// TODO Consider verifying folder existence
	}
	
	@Override
	public void createDataset(String dsName) throws IllegalArgumentException{
		switch(dsName.substring(dsName.length() - 3)) { // make sure that I shouldn't remove 3 
		case "__g": new FootballGameDataset(getPath() + getName() + "\\", dsName); break;
//		case "__d": new BasketballDraftDataset(getPath() + getName() + "\\", dsName); break;
//		case "__l": new BasketballPlayerGameLogsDataset(getPath() + getName() + "\\", dsName); break;
//		case "__o": new BasketballPlayerOverviewDataset(getPath() + getName() + "\\", dsName); break; //TODO create the missing classes and their respective DSIs
//		case "__c": new BasketballCoachDataset(getPath() + getName() + "\\", dsName); break;
		default: throw new IllegalArgumentException();
		}
		
	}

	// Assume the Dataset we are getting here already exists on file
	@Override
	public <T extends Dataset> T getDataset(String dsName) {
		switch(dsName.substring(dsName.length() - 3)) { // make sure that I shouldn't remove 3 
		case "__g": return (T) new FootballGameDataset(new File(getPath() + getName() + "\\" + dsName)); 
		case "__c": return (T) new FootballPlayerCareerGameLogDataset(new File(getPath() + getName() + "\\" + dsName));
//		case "__d": return new BasketballDraftDataset(new File(getPath() + getName() + "\\" + dsName));
//		case "__l": return new BasketballPlayerGameLogsDataset(new File(getPath() + getName() + "\\" + dsName));
//		case "__o": return new BasketballPlayerOverviewDataset(new File(getPath() + getName() + "\\" + dsName));
//		case "__c": return new BasketballCoachDataset(new File(getPath() + getName() + "\\" + dsName));
		default: throw new IllegalArgumentException();
		}
	}

	@Override
	protected void verifyName() throws IllegalArgumentException {
		if(!getName().endsWith("__p")) throw new IllegalArgumentException();
	}
	
}
