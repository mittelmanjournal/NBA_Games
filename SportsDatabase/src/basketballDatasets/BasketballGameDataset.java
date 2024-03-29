package basketballDatasets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import abstractDataTypes.Dataset;
import abstractDataTypes.DatasetItem;
import basketballDatasetItems.BasketballGame;
//import basketballDatasetItems.BasketballGame;
import datasetSubclasses.BasketballDataset;

public class BasketballGameDataset extends BasketballDataset {

	public BasketballGameDataset(String path, String name) {
		super(path, name, ".com/leagues/");
		// able to append this Class' link to the BasketballDatabase class' link in
		// order to get the web link to connect to
		createFolder();
	}

	public BasketballGameDataset(File f) {
		super(f);
		// NOT CREATING FOLDER BECAUSE IF WE TAKE IN A FILE ASSUME IT ALREADY SHOULD
		// EXIST
		if (!getLink().equals(".com/leagues/")) {
			throw new IllegalArgumentException();
			// maybe also do a verifaction that the folder above this one is of
			// basketball type, technically not necessary because datasets can
			// only be created from databases of the aligned type
		}
	}

	@Override
	public void createDatasetItem(String id) {
		// TODO Auto-generated method stub
			// What do I want to do with this method?
				// I want to create a new file representation
				// of the DatasetItem at this ID for this game
				// ONLY IF it doesn't already exist in this Dataset (the folder dataset)
		
		// this should just create a java object, without creating any files
		DatasetItem basketballGame = constructDatasetItem(id); 
		// the file representation of basketballGame may already exist, create() method should handle
		// whether or not to connect to web and compress and download to path + "\\" + id.
		basketballGame.create();
	}

	@Override
	public void updateDatasetItem(String id) {
		DatasetItem basketballGame = constructDatasetItem(id); 
		// the file representation of basketballGame may already exist, create() method should handle
		// whether or not to connect to web and compress and download to path + "\\" + id.
		basketballGame.update();
	}

	@Override
	public void buildDatasetItem(String id) {
		DatasetItem basketballGame = constructDatasetItem(id);
		basketballGame.build();
	}

	@Override
	public DatasetItem getDatasetItem(String id) {
		DatasetItem basketballGame = constructDatasetItem(id);
		if(basketballGame.fileExists()) { // could also do basketballGame.getFile().exists()
			//must exist in file
			return basketballGame;
		} else {
			throw new IllegalArgumentException(); // maybe change to exception throwing
		}
	}
	
	protected DatasetItem constructDatasetItem(String id) {
		// may or may not exist in file
		return new BasketballGame(getFullPath() + "\\", id);
	}
	
	public void createDatasetItems(final int lowY, final int highY) {
		constructGamesInYearRange(1, lowY, highY);
	}
	
	public void updateDatasetItems(final int lowY, final int highY) {
		constructGamesInYearRange(2, lowY, highY);
	}
	
	public void buildDatasetItems(final int lowY, final int highY) {
		constructGamesInYearRange(3, lowY, highY);
	}
	
	private void constructGamesInYearRange(final int create1Update2Build3, final int lowY, final int highY) {
		
		if (lowY < 1947 || highY > 2024 || lowY > highY) {
			throw new IllegalArgumentException();
		}
		
		ArrayList<String> gameIdsInRange = new ArrayList();
		
		for (String seasonLink : getYearLinksInRange(lowY, highY)) {
			try {
				gameIdsInRange.addAll(getAllGameIdsFromSeason(seasonLink));
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
			
		}
		
		for(String gameLink : gameIdsInRange) {
			String id = gameLink.substring("/boxscores/".length(), gameLink.indexOf(".html"));
			System.out.println(id);
			
			BasketballGame bg = new BasketballGame(getPath() + "\\", id);
			switch(create1Update2Build3) {
			case 1: bg.create(); break;
			case 2: bg.update(); break;
			case 3: bg.build(); break;
			default: throw new IllegalArgumentException();
			}
			
		}

	}
	
	private ArrayList<String> getAllGameIdsFromSeason(String seasonLink) throws InterruptedException, IOException {
		Document seasonPage = makeDoc(seasonLink);
		
		ArrayList<String> gamesList = getThisMonthsGames(seasonPage);
		
		Elements monthsList = seasonPage.select("div.filter div");
		
		for(int i = 1; i < monthsList.size(); i++) {
			
			String monthStr = monthsList.get(i).selectFirst("a").attr("href");
			System.out.println(monthStr);
			
			Document monthlyPage = makeDoc(monthStr); 
			System.out.println(monthlyPage.text());
			
			gamesList.addAll(getThisMonthsGames(monthlyPage));
			
		}
		
		return gamesList;
	}

	private ArrayList<String> getThisMonthsGames(Document seasonPage) throws IOException {
		ArrayList<String> gamesList = new ArrayList();
		
		Elements anchorTags = seasonPage.select("div#all_schedule div#div_schedule table#schedule tbody tr:not(.thead) td[data-stat=box_score_text] a");

		for (Element anchorTag : anchorTags) {
			
			if (anchorTag != null) {
				String href = anchorTag.attr("href");
				System.out.println(href);
				
				gamesList.add(href);
			} else {
				throw new IOException();
			}
		}
		return gamesList;
	}

	private ArrayList<String> getYearLinksInRange(final int lowY, final int highY) {
		ArrayList<String> yearlyLinksToReturn = new ArrayList();
		
		Document seasonsPage = makeDoc("/leagues/");
		
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
	
	private Document makeDoc(String ending) {
		Document doc = null;
		try {
			doc = Jsoup.connect("https://www.basketball-reference.com" + ending).get();
			Thread.sleep(2000);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	// TODO given a date range
	// get all game ids that are in this range in a list return it
}
