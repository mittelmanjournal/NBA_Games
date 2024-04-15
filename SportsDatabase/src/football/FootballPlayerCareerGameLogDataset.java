package football;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import abstractClasses.Dataset;
import abstractClasses.DatasetItem;

public class FootballPlayerCareerGameLogDataset extends Dataset {
	public FootballPlayerCareerGameLogDataset(String path, String name) throws IllegalArgumentException {
		super(path, name);
		createFolder();
	}
	
	public FootballPlayerCareerGameLogDataset(String pathAndName) throws IllegalArgumentException {
		super(pathAndName);
		createFolder();
	}

	public FootballPlayerCareerGameLogDataset(Path pathAndName) throws IllegalArgumentException {
		super(pathAndName);
	}
	
	public FootballPlayerCareerGameLogDataset(File f) throws IllegalArgumentException {
		super(f);
	}

	@Override
	protected void createDatasetItem(String id) {
		FootballPlayerCareerGameLog fpcgl = new FootballPlayerCareerGameLog(getPath() + getName() + "\\", id);
		fpcgl.create();
	}

	@Override
	protected void updateDatasetItem(String id) {
		FootballPlayerCareerGameLog fpcgl = new FootballPlayerCareerGameLog(getPath() + getName() + "\\", id);
		fpcgl.update();
	}

	@Override
	protected void buildDatasetItem(String id) {
		FootballPlayerCareerGameLog fpcgl = new FootballPlayerCareerGameLog(getPath() + getName() + "\\", id);
		fpcgl.build();
	}

	@Override
	protected FootballPlayerCareerGameLog getDatasetItem(String id) {
		return new FootballPlayerCareerGameLog(new File(getPath() + getName() + "\\" + id + ".txt"));
	}

	@Override
	protected void createDatasetItems(Set<String> ids) {
		int remainingIds = ids.size();
		for(String id : ids) {
			try {
				createDatasetItem(id);
			} catch (IllegalArgumentException iae) {
				iae.printStackTrace();
			}
			System.out.println(--remainingIds);
		}
	}

	@Override
	protected void updateDatasetItems(Set<String> ids) {
		for(String id : ids) {
			updateDatasetItem(id);
		}
	}

	@Override
	protected void buildDatasetItems(Set<String> ids) {
		for(String id : ids) {
			buildDatasetItem(id);
		}
	}

	@Override
	protected List<FootballPlayerCareerGameLog> getDatasetItems(Set<String> ids) {
		List<FootballPlayerCareerGameLog> fpcgls = new ArrayList();
		for(String id : ids) {
			fpcgls.add(getDatasetItem(id));
		}
		return fpcgls;

	}

	@Override
	protected void verifyDatabaseEnd() {
		if (!getDatabaseFolderName().endsWith("__p")) {
	        throw new IllegalArgumentException("Invalid database folder ending");
	    }
	}

	@Override
	protected File getDatasetItemAsFile(String id) {
		return doesDatasetItemExist(id) ? new FootballPlayerCareerGameLog(getPath() + getName() + "\\", id).getFile() : null;
	}

	@Override
	protected boolean doesDatasetItemExist(String id) {
		return new FootballPlayerCareerGameLog(getPath() + getName() + "\\", id).fileExists();
	}

	@Override
	protected void verifyName() {
		if(!getName().endsWith("__c")) throw new IllegalArgumentException();
	}
	
	public void createDatasetItems(FootballGame fg) {
		createDatasetItems(fg.getAllPlayerIds());
	}
	
	public void updateDatasetItems(FootballGame fg) {
		updateDatasetItems(fg.getAllPlayerIds());
	}
	
	public void buildDatasetItems(FootballGame fg) {
		buildDatasetItems(fg.getAllPlayerIds());
	}
	
	public void createDatasetItems(List<FootballGame> fgs) {
		Set<String> playerIds = new HashSet();
		for(FootballGame fg : fgs) {
			playerIds.addAll(fg.getAllOffensePlayerIds());
			playerIds.addAll(fg.getAllDefensePlayerIds());
		}
		createDatasetItems(playerIds);
	}
	
	public void updateDatasetItems(List<FootballGame> fgs) {
		Set<String> playerIds = new HashSet(); //
		for(FootballGame fg : fgs) {
			playerIds.addAll(fg.getAllPlayerIds());
		}
		updateDatasetItems(playerIds);
	}
	
	public void buildDatasetItems(List<FootballGame> fgs) {
		Set<String> playerIds = new HashSet();
		for(FootballGame fg : fgs) {
			playerIds.addAll(fg.getAllPlayerIds());
		}
		buildDatasetItems(playerIds);
	}
}
