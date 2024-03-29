package driver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import abstractDataTypes.DatabaseManager;
import databaseSubclasses.BasketballDatabase;

public class Tests {

	public static void main(String[] args) {
		create(2020, 2024);
		
	}
	
	
	
	public static void create(final int lowY, final int highY) {
		if (lowY < 1947 || highY > 2024)
			throw new IllegalArgumentException();

		ArrayList<String> linkStrings = getYearLinksInRange(lowY, highY);
		
		ArrayList<String> gameIdsInRange = new ArrayList<String>();
		
		for(String seasonLink : linkStrings) {
			try {
//				Document seasonPage = Jsoup.connect("https://www.basketball-reference.com" + seasonLink).get();
//				Thread.sleep(2000);
				// Given a document which has the game list for the first month of that season
				// Go through each month and copy all of the IDs into an arraylist and return
				gameIdsInRange.addAll(getAllGameIdsFromSeason(seasonLink));
				
				// here we start getting the games create(getIDGivenMonthPage());
				// here also would be the place where we would create the BballSeason DSI
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		for(String gameLink : gameIdsInRange) {
			// "https://www.basketball-reference.com" + gameLink to getthe website
			// do stuff for each game here, call create on it maybe
			// construct a BBall Game DSI, using the BBAll GDS instance's path + the ID and attempt to create it
			System.out.println(gameLink);
		}

	}
	
	public static ArrayList<String> getAllGameIdsFromSeason(String seasonLink) throws InterruptedException, IOException {
		Document seasonPage = Jsoup.connect("https://www.basketball-reference.com" + seasonLink).get();
		Thread.sleep(2000);
		ArrayList<String> gamesList = getThisMonthsGames(seasonPage);
		
		Elements monthsList = seasonPage.select("div.filter div");
		for(int i = 1; i < monthsList.size(); i++) {
			System.out.println(monthsList.get(i).selectFirst("a").attr("href"));
			
			seasonPage = Jsoup.connect("https:/www.basketball-reference.com" + monthsList.get(i).selectFirst("a").attr("href")).get();
			Thread.sleep(2000);
			gamesList.addAll(getThisMonthsGames(seasonPage));
			
		}
		
		return gamesList;
	}



	private static ArrayList<String> getThisMonthsGames(Document seasonPage) throws IOException {
		ArrayList<String> gamesList = new ArrayList();
		
		Elements rows = seasonPage.select("div#all_schedule div#div_schedule table#schedule tbody tr:not(.thead)");

		for (Element row : rows) {
			row.selectFirst("td[data-stat=box_score_text]");
			if (row != null) {
				String href = row.selectFirst("a").attr("href");
				gamesList.add(href);
			} else {
				throw new IOException();
			}
		}
		return gamesList;
	}



	public static ArrayList<String> getYearLinksInRange(final int lowY, final int highY) {
		ArrayList<String> linkStrings = new ArrayList();
		try {
			// BasketballDatabase.getFullWebLink() == https://www.basketball-reference.com
			// getLink() == .com/leagues/
			Document seasonsPage = Jsoup.connect("https://www.basketball-reference.com/leagues/").get();
			Thread.sleep(2000);
			int curY = lowY;
			while (curY <= highY) {
				Elements seasonLinks = seasonsPage.select("#content #div_stats table#stats tbody tr th a[href*=" + curY + "]");
				for (Element seasonLink : seasonLinks) {
					linkStrings.add(seasonLink.attr("href").replace(".html", "_games.html"));
					System.out.println(seasonLink.attr("href").replace(".html", "_games.html"));
				}
				curY++;
			}
			// now link Strings contains every single endpoint in this range

		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return linkStrings;
	}
	
	
	private static void databaseManagerTests() {
		DatabaseManager dbm = new DatabaseManager("C");
	}
//	
//	protected static boolean webPageExists() {
//		//202210190BRK.html
//		//replace 220210190BRK with the ID of this instance, try to connect to the web
//		int statusCode = 0;
//		try {
//            Document doc = Jsoup.connect(BasketballDatabase.getFullWebLink() + "/boxscores/202210190BRK.html").get();
//        } catch (HttpStatusException e ) {
//            e.printStackTrace();
//            statusCode = e.getStatusCode();
//            
//        } catch (IOException ioe) {
//        	ioe.printStackTrace();
//        }
//		
//		if(statusCode >= 400) return false;
//        return true;
//	}
	
	public static boolean webPageExists() {
		int statusCode = 0;
		Connection.Response response = null;
		try {
			response = Jsoup.connect(BasketballDatabase.getFullWebLink() + "/boxscores/202210190BRK.html").execute();
			statusCode = response.statusCode();
		} catch (HttpStatusException e) {
			e.printStackTrace();
			statusCode = e.getStatusCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		//System.out.println("Status code: " + statusCode);
		if (statusCode >= 400) {
			return false;
		}
		return true;
	}
	
	public static File compressToTextFile(String contents, String path) { // either return a String or return a File

		byte[] compressedContents;
		File underlyingHtmlDoc = null;

		try {

			compressedContents = compress(contents);
			underlyingHtmlDoc = new File(path);

			try (FileOutputStream fos = new FileOutputStream(underlyingHtmlDoc)) {
				fos.write(compressedContents);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return underlyingHtmlDoc;
	}
	
	public static byte[] compress(String data) throws IOException {
		byte[] input = data.getBytes();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		Deflater deflater = new Deflater();
		try (DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, deflater)) {
			deflaterOutputStream.write(input);
		}
		return byteArrayOutputStream.toByteArray();
	}
}
