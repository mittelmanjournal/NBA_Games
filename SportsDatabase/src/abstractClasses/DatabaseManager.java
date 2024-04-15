package abstractClasses;

import java.io.File;
import java.util.List;
import java.util.Set;

import basketball.BasketballDatabase;
import football.FootballDatabase;


//NOTE: in any class, wherever you see a getDB or DS or DSI we assume that this item exists, therefore we will often call the File constructors because that assumes there is an existing file being pointed to, and throws an exception if it doesn't exist
public class DatabaseManager extends DataContainer {
	//"C:\\Databases"
	public DatabaseManager(String pathAndName) {
		super(pathAndName);
		createFolder();
	}
	//"C:\\", "Databases"
	public DatabaseManager(String path, String name) {
		super(path, name);
		createFolder();
	}

	public DatabaseManager(File f) {
		this(f.getAbsolutePath());
	}
	
	// TODO CONSIDER* making it so that the verifyPathEnd forces the file to be in a
	// root drive directory by instead of checking for "\\" check for ":\\" to be at
	// the end of the path string. Do this by just overriding the verifyPathEnd() method, I think.
	@Override
	protected void verifyName() throws IllegalArgumentException {
		if(!getName().equals("DatabasesManager")) throw new IllegalArgumentException();
	}
	
	//tries to create the folder only if it doesn't yet exist
	public void createDatabase(String dbName) { 
		switch(dbName.substring(dbName.length() - 3)) { 
		case "__b": new BasketballDatabase(getPath() + getName() + "\\", dbName); break;
		case "__p": new FootballDatabase(getPath() + getName() + "\\", dbName); break;
		default: throw new IllegalArgumentException();
		}
	}
	
	//this database should exist as file so call the file constructor
	public <T extends Database> T getDatabase(String dbName) {
		switch(dbName.substring(dbName.length() - 3)) {
		case "__b": return (T) new BasketballDatabase(new File(getPath() + getName() + "\\" + dbName));
		case "__p": return (T) new FootballDatabase(new File(getPath() + getName() + "\\" + dbName));
		default: throw new IllegalArgumentException();
		}
	}
	
	//dbm.getDatabase("myBballDB__b").createDataset("myBballGameDS__g"); // could already be possible, but shorten this using strings
	
	//the db must exist for both of these below 
	public void createDataset(String dbName, String dsName) {
		Database db = getDatabase(dbName);
		db.createDataset(dsName); //TODO make sure that this will use the appropriate child DB's createDS method
	}
	
	
	public <T extends Dataset> T getDataset(String dbName, String dsName) {
		Database db = getDatabase(dbName);
		return (T) db.getDataset(dsName);
	}
	
	//dbm.getDatabase("myBballDB__b").getDataset("myBballGameDS__g").createDatasetItem("123456790ABC"); // could already be possible, but shorten this using strings
	public void createDatasetItem(String dbName, String dsName, String dsiId) {
		Database db = getDatabase(dbName);
		Dataset ds = db.getDataset(dsName);
		ds.createDatasetItem(dsiId);
	}
	
	public void updateDatasetItem(String dbName, String dsName, String dsiId) {
		Database db = getDatabase(dbName);
		Dataset ds = db.getDataset(dsName);
		ds.updateDatasetItem(dsiId);
	}
	
	public void buildDatasetItem(String dbName, String dsName, String dsiId) {
		Database db = getDatabase(dbName);
		Dataset ds = db.getDataset(dsName);
		ds.buildDatasetItem(dsiId);
	}
	
	public <T extends DatasetItem> T getDatasetItem(String dbName, String dsName, String dsiId) {
		Database db = getDatabase(dbName);
		Dataset ds = db.getDataset(dsName);
		return (T) ds.getDatasetItem(dsiId);
	}
	
	public void createDatasetItems(String dbName, String dsName, Set<String> dsiIds) {
		Database db = getDatabase(dbName);
		Dataset ds = db.getDataset(dsName);
		ds.createDatasetItems(dsiIds);
	}
	
	public void updateDatasetItems(String dbName, String dsName, Set<String> dsiIds) {
		Database db = getDatabase(dbName);
		Dataset ds = db.getDataset(dsName);
		ds.updateDatasetItems(dsiIds);
	}
	
	public void buildDatasetItems(String dbName, String dsName, Set<String> dsiIds) {
		Database db = getDatabase(dbName);
		Dataset ds = db.getDataset(dsName);
		ds.buildDatasetItems(dsiIds);
	}
	
	//TODO make methods that allow for specific sub item maker methods
	
	public List<? extends DatasetItem> getDatasetItems(String dbName, String dsName, Set<String> dsiIds) {
		Database db = getDatabase(dbName);
		Dataset ds = db.getDataset(dsName);
		return ds.getDatasetItems(dsiIds);
	}
	
}
