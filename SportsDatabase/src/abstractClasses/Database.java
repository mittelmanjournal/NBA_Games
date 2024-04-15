package abstractClasses;

import java.io.File;
import java.nio.file.Path;

public abstract class Database extends DataContainer {
	
	public abstract void createDataset(String dsName);
	public abstract <T extends Dataset> T getDataset(String dsName);
	
	protected Database(String path, String name) throws IllegalArgumentException {
		super(path,name);
	}

	protected Database(String pathAndName) throws IllegalArgumentException {
		super(pathAndName);
	}

	protected Database(File f) throws IllegalArgumentException {
		super(f);
	}
	
	protected Database(Path pathAndName) throws IllegalArgumentException { 
		super(pathAndName);
	}
	
	// TODO MAYBE* refactor to use the Dataset method so that we can also validate
	// an ending. However if we call createFolder() in the constructor we use then a
	// problem may arise that the folder will exist and be empty. But if we are
	// given a file we don't call createFolder();
	protected boolean doesDatasetFolderExist(String dsName) {
		File f = new File(getPath() + getName() + "\\" + dsName);
		return f.exists() && f.isDirectory();
	}
	
	protected File getDatasetFile(String dsName) {
		return doesDatasetFolderExist(dsName) ? new File(getPath() + getName() + "\\" + dsName) : null;
	}
}
