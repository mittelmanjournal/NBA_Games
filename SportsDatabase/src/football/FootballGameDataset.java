package football;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import abstractClasses.Dataset;
import abstractClasses.DatasetItem;
import basketball.BasketballGame;

public class FootballGameDataset extends Dataset {

	public FootballGameDataset(String path, String name) throws IllegalArgumentException {
		super(path, name);
		createFolder();
	}
	
	public FootballGameDataset(String pathAndName) throws IllegalArgumentException {
		super(pathAndName);
		createFolder();
	}

	public FootballGameDataset(Path pathAndName) throws IllegalArgumentException {
		super(pathAndName);
		// TODO Consider verifying folder existence
	}
	
	public FootballGameDataset(File f) throws IllegalArgumentException {
		super(f);
		// TODO Consider verifying folder existence
	}

	@Override
	public void createDatasetItem(String id) {
		FootballGame fg = new FootballGame(getPath() + getName() + "\\", id);
		fg.create();
	}

	@Override
	public void updateDatasetItem(String id) {
		FootballGame fg = new FootballGame(getPath() + getName() + "\\", id);
		fg.update();
	}

	@Override
	public void buildDatasetItem(String id) {
		FootballGame fg = new FootballGame(getPath() + getName() + "\\", id);
		fg.build();
		
	}

	@Override
	public FootballGame getDatasetItem(String id) {
		return new FootballGame(new File(getPath() + getName() + "\\" + id + ".txt"));
	}

	@Override
	public void createDatasetItems(Set<String> ids) {
		int remainingIds = ids.size();
		for(String id : ids) {
			createDatasetItem(id);
			System.out.println(--remainingIds);
		}
	}

	@Override
	public void updateDatasetItems(Set<String> ids) {
		for(String id : ids) {
			updateDatasetItem(id);
		}
	}

	@Override
	public void buildDatasetItems(Set<String> ids) {
		for(String id : ids) {
			buildDatasetItem(id);
		}
	}

	@Override
	public List<FootballGame> getDatasetItems(Set<String> ids) {
		List<FootballGame> fgs = new ArrayList();
		for(String id : ids) {
			fgs.add(getDatasetItem(id));
		}
		return fgs;
	}
	
	public List<FootballGame> getDatasetItems(final Date low, final Date high) {
		List<FootballGame> fgs = new ArrayList();

		File[] footballGameFiles = new File(getPath() + getName()).listFiles();
		Arrays.asList(footballGameFiles).forEach(gameFile -> {

			FootballGame curFG = new FootballGame(gameFile);
			
			if (isDateInRange(curFG.getDateFromId(), low, high)) {
				System.out.println(gameFile.getName());
				fgs.add(curFG);
			}
		});

		return fgs;

	}
	
	private static boolean isDateInRange(Date date, Date lowerBound, Date upperBound) {
        return !date.before(lowerBound) && !date.after(upperBound);
    }


	@Override
	protected void verifyDatabaseEnd() {
		if (!getDatabaseFolderName().endsWith("__p")) {
	        throw new IllegalArgumentException("Invalid database folder ending");
	    }
	}

	@Override
	protected File getDatasetItemAsFile(String id) {
		return doesDatasetItemExist(id) ? new FootballGame(getPath() + getName() + "\\", id).getFile() : null;
	}

	@Override
	protected boolean doesDatasetItemExist(String id) {
		return new FootballGame(getPath() + getName() + "\\", id).fileExists();
	}

	@Override
	protected void verifyName() {
		if(!getName().endsWith("__g")) throw new IllegalArgumentException();
	}

	
	// TODO make a method that takes in 2 years one low, one high and gets the ids
	// in that range and makes all of those games in that range
	
	public void buildDatasetItems(final int lowY, final int highY) {
		buildDatasetItems(getIdsInRange(lowY, highY));
	}
	
	public void createDatasetItems(final int lowY, final int highY) {
		createDatasetItems(getIdsInRange(lowY, highY));
	}
	
	public void updateDatasetItems(final int lowY, final int highY) {
		updateDatasetItems(getIdsInRange(lowY, highY));
	}
	
	
	protected Set<String> getIdsInRange(final int low, final int high) {
		Set<String> ids = new HashSet();
		List<String> yearLinks = new ArrayList();
		Document allYearsPage = makeDoc("https://www.pro-football-reference.com/years/");
		
		allYearsPage.select("#content #all_years #div_years table#years tbody tr th a").forEach(year -> {
			if (inRange(Integer.parseInt(year.text()), low, high)) 
				yearLinks.add("https://www.pro-football-reference.com" + year.attr("href") + "games.htm");
		});
		
		yearLinks.forEach(yearLink -> 
			makeDoc(yearLink).select("#content #all_games #div_games table#games tbody tr td[data-stat=boxscore_word] a")
					.forEach(gameRow -> ids.add(gameRow.attr("href").substring(11, 23)))
		);

		return ids;
	}

	private boolean inRange(int yearInt, int low, int high) {
		return low <= high && yearInt >= low && yearInt <= high;
	}
	
}
