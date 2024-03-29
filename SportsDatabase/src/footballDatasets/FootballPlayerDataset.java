package footballDatasets;

import java.io.File;

import abstractDataTypes.Dataset;

public class FootballPlayerDataset extends Dataset {
	protected FootballPlayerDataset(String path, String name, String link) {
		super(path, name, link);
		// TODO Auto-generated constructor stub
	}

	protected FootballPlayerDataset(File f) {
		super(f);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getLink(String ending) {
		// TODO Auto-generated method stub
		return null;
	}
}
