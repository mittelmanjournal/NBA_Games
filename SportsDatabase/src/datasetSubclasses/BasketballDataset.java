package datasetSubclasses;

import java.io.File;

import abstractDataTypes.Dataset;

public abstract class BasketballDataset extends Dataset {
	
	protected BasketballDataset(String path, String name, String link) {
		super(path, name, link);
	}
	
	protected BasketballDataset(File f) {
		super(f);
	}

//	@Override
	protected String getFileEnding() {
		switch(getLink()) {
		case ".com/players/": return "__l";
		case ".com/teams/": return "__t";
		case ".com/leagues/": return "__g";
		case ".com/draft/": return "__d";
		default: throw new IllegalArgumentException();
		}
	}

	@Override
	protected String getLink(String ending) {
		switch(ending) {
		case "__l": return ".com/players/";
		case "__t": return ".com/teams/";
		case "__g": return ".com/leagues/";
		case "__d": return ".com/draft/";
		default: throw new IllegalArgumentException();
		}
	}
}
