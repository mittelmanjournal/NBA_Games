package basketball;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import abstractClasses.Dataset;


public class BasketballGameDataset extends Dataset {

	protected BasketballGameDataset(String path, String name) throws IllegalArgumentException {
		super(path, name);
		createFolder();
	}

	protected BasketballGameDataset(String pathAndName) throws IllegalArgumentException {
		super(pathAndName);
		createFolder();
	}
	
	protected BasketballGameDataset(Path pathAndName) throws IllegalArgumentException {
		super(pathAndName);
	}
	
	public BasketballGameDataset(File f) throws IllegalArgumentException {
		super(f);
	}

	@Override
	protected void createDatasetItem(String id) {
		BasketballGame bg = new BasketballGame(getPath() + getName() + "\\", id);
		bg.create();
	}

	@Override
	protected void updateDatasetItem(String id) {
		BasketballGame bg = new BasketballGame(getPath() + getName() + "\\", id);
		bg.update();
	}

	@Override
	protected void buildDatasetItem(String id) {
		BasketballGame bg = new BasketballGame(getPath() + getName() + "\\", id);
		bg.build();
	}

	@Override
	protected BasketballGame getDatasetItem(String id) {
		return new BasketballGame(new File(getPath() + getName() + "\\" + id));
	}
	
	protected void createDatasetItem(String id, String subItem) {
		BasketballGame bg = new BasketballGame(getPath() + getName() + "\\", id);
		bg.create(subItem);
	}

	protected void updateDatasetItem(String id, String subItem) {
		BasketballGame bg = new BasketballGame(getPath() + getName() + "\\", id);
		bg.update(subItem);
	}

	protected void buildDatasetItem(String id, String subItem) {
		BasketballGame bg = new BasketballGame(getPath() + getName() + "\\", id);
		bg.build(subItem);
	}
	
	@Override
	protected void createDatasetItems(Set<String> ids) {
		for(String id : ids) {
			createDatasetItem(id);
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
	protected List<BasketballGame> getDatasetItems(Set<String> ids) {
		List<BasketballGame> bgs = new ArrayList();
		for(String id : ids) {
			bgs.add(getDatasetItem(id));
		}
		return bgs;
	}

	protected void createDatasetItems(Set<String> ids, String subItem) {
		for(String id : ids) {
			createDatasetItem(id, subItem);
		}
	}

	protected void updateDatasetItems(Set<String> ids, String subItem) {
		for(String id : ids) {
			updateDatasetItem(id, subItem);
		}
	}

	protected void buildDatasetItems(Set<String> ids, String subItem) {
		for(String id : ids) {
			buildDatasetItem(id, subItem);
		}
	}
	
	// we use 2 years here instead of dates because its easy based on the way the seasons are organized
	public void createDatasetItems(final int lowY, final int highY) {
		createDatasetItems(getGameIdsInRange(lowY, highY));
	}
	
	public void updateDatasetItems(final int lowY, final int highY) {
		updateDatasetItems(getGameIdsInRange(lowY, highY));
	}
	
	public void buildDatasetItems(final int lowY, final int highY) {
		buildDatasetItems(getGameIdsInRange(lowY, highY));
	}
	
	// we use Date rather than a year here because he way the Id works its simpler to get a set of games in a range of specific dates.
	public List<BasketballGame> getDatasetItems(final Date low, final Date high){
		//create a list of BGs
		List<BasketballGame> games = new ArrayList<>();
		//for each of the subfiles
		for(File bgFile : new File(getPath() + getName()).listFiles()) {
			
			BasketballGame bg = new BasketballGame(bgFile);

	        // Check if the date is within the range
	        if (bg.inDateRange(low, high)) {
	            games.add(bg);
	        }

		}
		return games;
	}
	
//	public void createDatasetItemsFromSeasonSchedule(Set<SeasonSchedule> seasons) {
//		for(SeasonSchedule season : seasons) {
//			createDatasetItems(season.getAllGameIds()); // TODO if the season exists it will access that file and get all of the games without connecting to the web.
//		}
//	}
//	
//	public void updateDatasetItemsFromSeasonSchedule(Set<SeasonSchedule> seasons) {
//		for(SeasonSchedule season : seasons) {
//			updateDatasetItems(season.getAllGameIds());
//		}
//	}
//	
//	public void buildDatasetItemsFromSeasonSchedule(Set<SeasonSchedule> seasons) {
//		for(SeasonSchedule season : seasons) {
//			buildDatasetItems(season.getAllGameIds());
//		}
//	}

	@Override
	protected File getDatasetItemAsFile(String id) {
		return doesDatasetItemExist(id) ? new BasketballGame(getPath() + getName() + "\\", id).getFile() : null;
	}

	@Override
	protected boolean doesDatasetItemExist(String id) {
		return new BasketballGame(getPath() + getName() + "\\", id).fileExists();
	}

	@Override
	protected void verifyName() {
		if(!getName().endsWith("__g")) throw new IllegalArgumentException();
	}
	
	@Override	
	protected void verifyDatabaseEnd() {
	    // Check if the last 3 characters of the substring are "__b"
	    if (!getDatabaseFolderName().endsWith("__b")) {
	        throw new IllegalArgumentException("Invalid database folder ending");
	    }
	}
	
	private Set<String> getGameIdsInRange(final int lowY, final int highY) {
		
		if (lowY < 1947 || highY > 2024 || lowY > highY) {
			throw new IllegalArgumentException();
		}
		
		Set<String> gameIdsInRange = new HashSet();
		
		for (String seasonLink : getYearLinksInRange(lowY, highY)) {
			try {
				gameIdsInRange.addAll(getAllGameIdsFromSeason(seasonLink));
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
			
		}
		
		return gameIdsInRange;
	
	}
	
	private Set<String> getAllGameIdsFromSeason(String seasonLink) throws InterruptedException, IOException {
		Document seasonPage = makeDoc("https://www.basketball-reference.com" + seasonLink);
		
		Set<String> gamesList = getThisMonthsGames(seasonPage);
		
		Elements monthsList = seasonPage.select("div.filter div");
		
		for(int i = 1; i < monthsList.size(); i++) {
			
			String monthStr = monthsList.get(i).selectFirst("a").attr("href");
			System.out.println(monthStr);
			
			Document monthlyPage = makeDoc("https://www.basketball-reference.com" + monthStr); 
			System.out.println(monthlyPage.text());
			
			gamesList.addAll(getThisMonthsGames(monthlyPage));
			
		}
		
		return gamesList;
	}

	private Set<String> getThisMonthsGames(Document seasonPage) throws IOException {
		Set<String> gamesList = new HashSet();
		
		Elements anchorTags = seasonPage.select("div#all_schedule div#div_schedule table#schedule tbody tr:not(.thead) td[data-stat=box_score_text] a");

		for (Element anchorTag : anchorTags) {
			
			if (anchorTag != null) {
				String href = anchorTag.attr("href");
				System.out.println(href);
				
				gamesList.add(href.replace("/boxscores/", "").replace(".html", ""));
			} else {
				throw new IOException();
			}
		}
		return gamesList;
	}

	private ArrayList<String> getYearLinksInRange(final int lowY, final int highY) {
		ArrayList<String> yearlyLinksToReturn = new ArrayList();
		
		Document seasonsPage = makeDoc("https://www.basketball-reference.com/leagues/");
		
		int curY = lowY;
		while (curY <= highY) {
			
			Elements seasonLinks = seasonsPage.select("#content #div_stats table#stats tbody tr th a[href*=" + curY + "]");
			
			for (Element seasonLink : seasonLinks) {
				
				yearlyLinksToReturn.add(seasonLink.attr("href").replace(".html", "_games.html"));
				System.out.println(seasonLink.attr("href").replace(".html", "_games.html"));
				
			}
			
			curY++;
		}
		
		return yearlyLinksToReturn;
	}
}
