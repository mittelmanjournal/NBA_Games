package basketballDatasets;

import java.io.File;

import abstractDataTypes.Dataset;
import abstractDataTypes.DatasetItem;
import datasetSubclasses.BasketballDataset;

public class BasketballPlayerDataset extends BasketballDataset {
	
	public BasketballPlayerDataset(String path, String name) {
		super(path, name, ".com/players/");
		// able to append this Class' link to the BasketballDatabase class' link in
		// order to get the web link to connect to
		createFolder();
	}

	protected BasketballPlayerDataset(File f) {
		super(f);
		// NOT CREATING FOLDER BECAUSE IF WE TAKE IN A FILE ASSUME IT ALREADY SHOULD
		// EXIST
		if (!getLink().equals(".com/players/")) {
			throw new IllegalArgumentException();
			// maybe also do a verifaction that the folder above this one is of
			// basketball type, technically not necessary because datasets can
			// only be created from databases of the aligned type
		}
	}

	@Override
	public void createDatasetItem(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDatasetItem(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void buildDatasetItem(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public DatasetItem getDatasetItem(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
