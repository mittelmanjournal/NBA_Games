package datasetSubclasses;

import abstractDataTypes.Dataset;

public abstract class FootballDataset extends Dataset {

	protected FootballDataset(String path, String name, String link) {
		super(path, name, link);
	}

	// TODO potential optimization: both Football and Basketball DSs have similar
		// endpoints, you may be able to implement this method in some way in the
		// superclass
	@Override
	protected String getFileEnding() {
		switch(getLink()) {
		case ".com/players/": return "__l";
		case ".com/teams/": return "__t";
		case ".com/years/": return "__g";
		case ".com/draft/": return "__d";
		default: throw new IllegalArgumentException();
		}
	}
	
	// TODO potential optimization: both Football and Basketball DSs have similar
	// endpoints, you may be able to implement this method in some way in the
	// superclass
	@Override
	protected String getLink(String ending) {
		switch(ending) {
		case "__l": return ".com/players/";
		case "__t": return ".com/teams/";
		case "__g": return ".com/years/";
		case "__d": return ".com/draft/";
		default: throw new IllegalArgumentException();
		}
	}
}
