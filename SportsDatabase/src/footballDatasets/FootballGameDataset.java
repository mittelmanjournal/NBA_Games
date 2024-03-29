package footballDatasets;

import java.io.File;

import abstractDataTypes.Dataset;

public class FootballGameDataset extends Dataset {
	protected FootballGameDataset(String path, String name, String link) {
		super(path, name, link);
		// TODO Auto-generated constructor stub
	}

	protected FootballGameDataset(File f) {
		super(f);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getLink(String ending) {
		// TODO Auto-generated method stub
		return null;
	}
}
