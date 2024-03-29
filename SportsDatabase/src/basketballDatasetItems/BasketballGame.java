package basketballDatasetItems;


//TODO organize comments, make JavaDocs
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import abstractDataTypes.DatasetItem;
import databaseSubclasses.BasketballDatabase;

/**
 * <p>
 * Java object representation of a potential webpage and/or file that is a
 * Basketball Game from the site basketball-reference.com.
 * 
 * This object gives access to behaviors of this class that saves compressed
 * game files from the web, gets information from the compressed game file (if
 * it exists) and has methods to manage keeping this game up to date.
 * </p>
 * 
 * <p>
 * This instance may represent a Basketball Game that doesn't have a file
 * representation but exists on the website, or both have an existing file and
 * web page and the last option is it being an invalid object with no existing
 * file because there is no valid web page representation.
 * </p>
 * 
 * @author urimi
 *
 */
public class BasketballGame extends DatasetItem {

	/**
	 * 
	 * 
	 * @param path
	 * @param id
	 */
	public BasketballGame(String path, String id) {
		super(path, id);
		// note that all of the file getting services and updating services won't work
		// this essentially references a theoretical game file that may not yet exist, but should be able to be created
	}
	
	public BasketballGame(String path, String id, boolean createElseUpdate) {
		super(path, id);
		//Assume this file mayn't exist, therefore call create() so that we can access all of the file getting methods
		if(createElseUpdate)
			create();
		else
			update();
	}
	
	public BasketballGame(File f) {
		super(f);
		//BBallGame files come like this path\\DBfolder__b\\DSfolder__g\\202210190BRK
		setId(f.getName());
		setPath(f.getAbsolutePath().replace(getId(), "")); // if doesn't work change it to a substring getter from 0 to (including) last "\\"
		
		if(getPath().lastIndexOf("\\") != getPath().length() - 1) {
			throw new IllegalArgumentException();
		}
		
		verifyAll();
		
	}

	@Override
	public void create() {
		if (!fileExists()) {
			build();
		}
		
	}

	@Override
	public void update() {
		// only if this exists as a file ALREADY! then overwrite that file, reconnect to
		// web and download that page.
		if (fileExists()) {
			// maybe delete that file to make sure we are overwriting!
			// or it may auto-overwrite
			build();

		} // DO I WANT IT TO THROW EXCEPTIONS? Probably no.
	}
	
	/**
	 * Takes in a Document and remove the all of the array parameter's divs from
	 * that document.
	 * 
	 * @param gamePage
	 * @param divs
	 */
	private void removeExtraDivs(Document gamePage, String... divs) { // TODO potentially abstractable
		for (String str : divs) {
			removeExtraDiv(gamePage, str);
		}
	}

	/**
	 * Remove a single div from a Document.
	 * 
	 * @param gamePage
	 * @param str
	 */
	private void removeExtraDiv(Document gamePage, String str) { // TODO potentially abstractable
		try {
			if (gamePage == null || gamePage.selectFirst("div" + str) == null)
				throw new IOException();
			gamePage.selectFirst("div" + str).remove();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

	@Override
	public void build() {
		// regardless of whether file exists or not try to save a file for this ID, may
		// not exist as a webPage will throw exception
		try { 
			Document gamePage = Jsoup.connect(getFullWebLink()).get(); //maybe methodize this and below
			Thread.sleep(2000); // and this

			// remove all useless elements
			removeExtraDivs(gamePage, "#header", "#srcom", "#inner_nav", "#footer", "#bottom_nav", ".stathead_event");
			
			String contents = gamePage.outerHtml();
			
			compressToTextFile(contents); // should be overwriting
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean fileExists() {
		return new File(getFullPath() + ".txt").exists();
	}

	@Override
	public boolean webPageExists() {
		int statusCode = 0;
		Connection.Response response = null;
		try {
			response = Jsoup.connect(getFullWebLink()).execute();
			Thread.sleep(2000);
			statusCode = response.statusCode();
		} catch (HttpStatusException e) {
			e.printStackTrace();
			statusCode = e.getStatusCode();
		} catch (IOException | InterruptedException e) {
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

	private String getFullWebLink() {
		return BasketballDatabase.getFullWebLink() + "/boxscores/" + getId() + ".html";
	}

	@Override
	public File getFile() {
		
		// if the file representation exists, return it
		if(fileExists()) {
			return new File(getFullPath() + ".txt");
		}
		return null; // potentially throw exception instead.
	}

	public Element getElement(String element) throws IOException {
		List<String> validStrings = Arrays.asList("#div_pbp, #div_game_info, #div_officials, #div_player_offense, #div_player_defense, #div_returns, #div_kicking, .scorebox".split(", "));

		if (!validStrings.contains(element))
			throw new IOException();
		else return decompressToDocument().selectFirst("div" + element);
	}
	
	public Element getGameInfo() throws IOException {
		return getElement("#div_game_info");
	}
	
	public Element getOfficials() throws IOException {
		return getElement("#div_officials");
	}

	public Element getOffensivePlayerStats() throws IOException {
		return getElement("#div_player_offense");
	}

	public Element getDefensivePlayerStats() throws IOException {
		return getElement("#div_player_defense");
	}

	public Element getReturnerStats() throws IOException {
		return getElement("#div_returns");
	}

	public Element getKickerAndPunterStats() throws IOException {
		return getElement("#div_kicking");
	}

	public Element getScorebox() throws IOException {
		return getElement(".scorebox");
	}
	
	public Element getPlayByPlay() throws IOException {
		return getElement("#div_pbp");
	}
	
	@Override
	protected void verifyDatasetFolderEnding() {
		// TODO Auto-generated method stub
		// in a game ds it looks likeso: C:\DBs\BDB__b\GDS__g\202210180BOS (access by getFullPath())
		// get the string between the second to last \ and the last \ and check to see if the last 3 chars in that string are __g
		// if it isn't __g throw an illegal arg exception
		// Get the full path
		String fullPath = getFullPath(); // DON't need to add + ".txt" because we are interested in the path names more
											// than the file itself

	    // Find the second to last and last occurrences of '\'
	    int lastIndex = fullPath.lastIndexOf('\\');
	    int secondToLastIndex = fullPath.lastIndexOf('\\', lastIndex - 1);

	    // Get the substring between the second to last and last '\'
	    String folderName = fullPath.substring(secondToLastIndex + 1, lastIndex);

	    // Check if the last 3 characters of the substring are "__g"
	    if (!folderName.endsWith("__g") || getPath().charAt(getPath().length() - 1) != '\\') {
	        throw new IllegalArgumentException("Invalid dataset folder ending");
	    }
	}

	@Override
	protected void verifyDatabaseFolderEnding() {
		// TODO Auto-generated method stub
		// in a game ds it looks likeso: C:\DBs\BDB__b\GDS__g\202210180BOS (access by getFullPath())
		// get the String in between the third to last \ and the second to last \ and check to see if the last 3 chars of that string are __b
		// if it isn't __b throw an illegal arg exception
		
		// Get the full path
	    String fullPath = getFullPath(); // DON't need to add + ".txt" because we are interested in the path names more

	    // Find the third to last and second to last occurrences of '\'
	    int lastIndex = fullPath.lastIndexOf('\\');
	    int secondToLastIndex = fullPath.lastIndexOf('\\', lastIndex - 1);
	    int thirdToLastIndex = fullPath.lastIndexOf('\\', secondToLastIndex - 1);

	    // Get the substring between the third to last and second to last '\'
	    String folderName = fullPath.substring(thirdToLastIndex + 1, secondToLastIndex);

	    // Check if the last 3 characters of the substring are "__b"
	    if (!folderName.endsWith("__b")) {
	        throw new IllegalArgumentException("Invalid database folder ending");
	    }
		
	}

	@Override
	protected void verifyIDFormat() {
		// 202210180BOS
		// if its a string of 8 nums then a 0 then 3 capital chars
		if (!getId().matches("\\d{8}0[A-Z]{3}")) {
	        throw new IllegalArgumentException("Invalid ID format");
	    }
		
	}
	
	/**
	 * Assuming a compressed byte[] in a txt file exists at getWritePath() + ".txt",
	 * return a that file's decompressed data as a String.
	 * 
	 * @return
	 */
	@Override
	public String decompressToString() {
		try {
			return decompress(new File(getFullPath() + ".txt")); // maybe just make it a class var of
																	// file
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Returns a document representation of the data at this object's underlying
	 * file. This will be used to access data from specific objects in the database.
	 * 
	 * @return
	 */
	public Document decompressToDocument() {
		return Jsoup.parse(decompressToString());
	}

	/**
	 * 
	 */
	@Override
	public File compressToTextFile(String contents) { // either return a String or return a File

		byte[] compressedContents;
		File underlyingHtmlDoc = null;

		try {

			compressedContents = compress(contents);
			underlyingHtmlDoc = new File(getFullPath() + ".txt");

			try (FileOutputStream fos = new FileOutputStream(underlyingHtmlDoc)) {
				fos.write(compressedContents);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return underlyingHtmlDoc;
	}

}
