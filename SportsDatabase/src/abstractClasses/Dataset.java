package abstractClasses;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public abstract class Dataset extends DataContainer {

	protected abstract void createDatasetItem(String id);
	protected abstract void updateDatasetItem(String id);
	protected abstract void buildDatasetItem(String id);
	protected abstract <T extends DatasetItem> T getDatasetItem(String id);
	protected abstract void createDatasetItems(Set<String> ids);
	protected abstract void updateDatasetItems(Set<String> ids);
	protected abstract void buildDatasetItems(Set<String> ids);
	protected abstract List<? extends DatasetItem> getDatasetItems(Set<String> ids);
	protected abstract void verifyDatabaseEnd();
	protected abstract File getDatasetItemAsFile(String id); //
	protected abstract boolean doesDatasetItemExist(String id); //use DSI behaviors
	
	protected Dataset(String path, String name) throws IllegalArgumentException {
		super(path, name);
		// TODO Auto-generated constructor stub
	}
	
	protected Dataset(String pathAndName) throws IllegalArgumentException {
		super(pathAndName);
		// TODO Auto-generated constructor stub
	}
	
	protected Dataset(Path pathAndName) throws IllegalArgumentException {
		super(pathAndName);
		// TODO Auto-generated constructor stub
	}
	
	protected Dataset(File f) throws IllegalArgumentException {
		super(f);
	}

	@Override
	protected void verifyAll() {
		super.verifyAll();
		verifyDatabaseEnd();
	}
	
	protected String getDatabaseFolderName() {
		// Find the second to last and last occurrences of '\'
	    int lastIndex = getPath().lastIndexOf('\\');
	    int secondToLastIndex = getPath().lastIndexOf('\\', lastIndex - 1);

	    // Get the substring between the second to last and last '\'
	    String folderName = getPath().substring(secondToLastIndex + 1, lastIndex);
	    
	    return folderName;
	}
	
}
