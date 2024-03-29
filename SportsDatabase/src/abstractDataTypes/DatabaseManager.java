package abstractDataTypes;

import java.io.File;

import databaseSubclasses.BasketballDatabase;
import databaseSubclasses.FootballDatabase;
import enums.DatabaseType;
import enums.DatasetType;

public class DatabaseManager {
	
	private String path;
	
	public DatabaseManager(String driveName) {
		path = driveName + ":\\Databases\\";
		File f = new File(path);
		if(!f.exists())
			f.mkdir();
	}
	
	public void constructDatabase(String dbName, DatabaseType dbType) {
		switch(dbType) {
		case FOOTBALL: new FootballDatabase(path, dbName); // will auto append "__p" to name and will try to make this folder.
		case BASKETBALL: new BasketballDatabase(path, dbName); 
		default: throw new IllegalArgumentException();
		}
	}
	
	public void constructDatabase(String dbName) { // assume ending already exists
		switch(dbName.substring(dbName.length() - 3)) {
		case "__p": new FootballDatabase(path, dbName.substring(0, dbName.length() - 3)); // exclude the ending
		case "__b": new BasketballDatabase(path, dbName.substring(0, dbName.length() - 3));
		default: throw new IllegalArgumentException();
		}
	}
	
	public Database getDatabase(String dbName, DatabaseType dbType) {
		switch(dbType) {
		case FOOTBALL: return new FootballDatabase(path, dbName); // will auto append "__p" to name and will try to make this folder.
		case BASKETBALL: return new BasketballDatabase(path, dbName);
		default: throw new IllegalArgumentException();
		}
	}
	
	public Database getDatabase(String dbName) {
		switch(dbName.substring(dbName.length() - 3)) {
		case "__p": return new FootballDatabase(path, dbName.substring(0, dbName.length() - 3)); // exclude the ending
		case "__b": return new BasketballDatabase(path, dbName.substring(0, dbName.length() - 3));
		default: throw new IllegalArgumentException();
		}
	}
	
	
	public Dataset constructDataset(String dsNameWithEnding) {
		return null;
		
	}
	
	public Dataset constructDataset(String dsName, DatasetType dsT) {
		return null;
		
	}
	
	//public Dataset getDataset
	
//	public void construct
	
}
