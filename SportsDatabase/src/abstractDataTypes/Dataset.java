package abstractDataTypes;

import java.io.File;

public abstract class Dataset extends DataContainer {

	protected Dataset(String path, String name, String link) {
		super(path, name, link);
	}

	protected Dataset(File f) {
		super(f);
	}

	public abstract void createDatasetItem(String id);

	public abstract void updateDatasetItem(String id);
	
	public abstract void buildDatasetItem(String id);
	
	public abstract DatasetItem getDatasetItem(String id);
	
	// for Game items there should be a create, update and build and get method that
	// given a DateRange does some iterating through the dataset folder, does some
	// iterating on the internet in that given range and creates some Files or
	// returns a list of games.

	// For players, because they aren't a date based item, a list of IDs or letters
	// or games could be provided where you can create, update or build Player pages
	// that exist in the govem letters, IDS or games (for games reference rosters).
}
