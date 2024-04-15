package basketball;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import abstractClasses.DirectoryDatasetItem;

//TODO make the specific datapoint getters that decompress the document (so it must exist on file) and get the String value in certain elements of the document
public class BasketballGame extends DirectoryDatasetItem {
	//TODO MAYBE* abstract some of the behaviors in these constructors in the DDSI constructors
	
	// Assume this BasketballGame folder may not exist so try to create it (don't create any innards).
	// We do this because we assume that when we construct this BG we will want to
	// create subFiles, or access them.
	public BasketballGame(String path, String id) throws IllegalArgumentException {
		super(path, id); // verification called here
		createFolder();
	}
	
	public BasketballGame(String pathAndId) throws IllegalArgumentException {
		super(pathAndId);
		createFolder();
	}
	
	public BasketballGame(Path pathAndId) throws IllegalArgumentException {
		this(pathAndId.toFile());
	}
	
	// The reason why we create if the given File doesn't exist as a DDSI is because
	// we assume that the dev will want to point to an file existing DSI and that
	// they don't mind waiting time for creation.
	//TODO: MAYBE* make one that allows the user to specify which sub files to create if they don't exist
	public BasketballGame(File f) throws IllegalArgumentException {
		super(f); // if a file already exists it may be missing any sub files, and therefore not exist (at least in the way I want it to)
		if(!fileExists()) {
			throw new IllegalArgumentException();
		}
	}
	
	public BasketballGame(File f, boolean createOrUpdateIfFileDoesntExist) throws IllegalArgumentException {
		super(f); // if a file already exists it may be missing any sub files, and therefore not exist (at least in the way I want it to)
		if(!fileExists() && createOrUpdateIfFileDoesntExist) { 
			create(); 
		} else if(!fileExists() && !createOrUpdateIfFileDoesntExist) {
			update();
		}
	}
	
	public BasketballGame(Path p, boolean createOrUpdateIfFileDoesntExist) {
		this(p.toFile(), createOrUpdateIfFileDoesntExist);
	}
	
	// TODO make constructors that don't auto make ALL (also for the file
	// constructor) sub items. So like input a set of strings that represents sub items
	public BasketballGame(String pathAndId, boolean createOrUpdate) {
		this(pathAndId);
		if (createOrUpdate) {
			create();
		} else {
			update();
		}
	}

	public BasketballGame(String path, String id, boolean createOrUpdate) {
		this(path, id);
		if (createOrUpdate) {
			create();
		} else {
			update();
		}
	}
	
	@Override
	public void create() {
		create("boxscore");
		create("pbp");
		create("shot-chart");
		create("plus-minus");
	}

	@Override
	public void update() {
		update("boxscore");
		update("pbp");
		update("shot-chart");
		update("plus-minus");
	}

	@Override
	public void build() {
		build("boxscore");
		build("pbp");
		build("shot-chart");
		build("plus-minus");
	}
	
	/**
	 * If and only if the subItem file doesn't already exist, create a new subItem
	 * file by calling build(subItem);
	 * 
	 */
	@Override
	public void create(String subItem) {
		if(!fileExists(subItem)) build(subItem);
	}

	/**
	 * If and only if the subItem file already exists, overwrite it by calling
	 * build(subItem); 
	 * 
	 */
	@Override
	public void update(String subItem) {
		if(fileExists(subItem))	build(subItem);
	}

	@Override
	public void build(String subItem) {
		System.out.println(getId());
		//if we have not yet created the folder for some reason, create it so that we can make the sub files
		createFolder(); 
		
		Document gamePage = makeDoc(getFullWebLink(subItem));

		// remove all useless elements
		removeExtraDivs(gamePage, "#header", "#srcom", "#inner_nav", "#footer", "#bottom_nav", ".stathead_event");

		String contents = gamePage.outerHtml();

		compressToTextFile(contents, subItem); // should be overwriting

	}
	//TODO consider making a specific maker method that does buildBoxscore() or buildPBP() or makePBP("update") or makeShotChart("create")
	//todo above may be redundant
	/**
	 * If the given subItem isn't one of BasketballGame's specified subItem/Files
	 * return false otherwise true.
	 */
	@Override
	protected boolean validSubItem(String subItem) {
		subItem = subItem.replace(".txt", "");
		return subItem.equals("boxscore") || subItem.equals("pbp") || subItem.equals("shot-chart") || subItem.equals("plus-minus");
	}

	// TODO DISALLOW creation of games with pbp, shot-chart and plus-minus for games before 1997, only create the overview/boxscore one
	 
	protected String getFullWebLink(String subItem) throws IllegalArgumentException {
		if (validSubItem(subItem)) {
			return subItem.equals("boxscore") ? getFullWebLink()
					: getFullWebLink().replace("boxscores", "boxscores/" + subItem);
		} else throw new IllegalArgumentException();
	}

	@Override
	protected String getFullWebLink() {
		return "https://www.basketball-reference.com/boxscores/" + getId() + ".html";
	}
	
	@Override
	protected void verifyName() {
		if (!getId().matches("\\d{8}0[A-Z]{3}")) throw new IllegalArgumentException("Invalid ID format");
	}

	@Override
	protected String getRequiredDatabaseEnd() { return "__b"; }

	@Override
	protected String getRequiredDatasetEnd() { return "__g"; }
	
	protected boolean inDateRange(Date low, Date high) {
		
		if (high.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().compareTo(low.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) < 0)
		    throw new IllegalArgumentException("High date cannot be less than low date");
		
		int year = getYearFromId();
	    int month = getMonthFromId();
	    int day = getMonthDayFromId();

	    LocalDate date = LocalDate.of(year, month, day);

	    LocalDate lowDate = low.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	    LocalDate highDate = high.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	    return date.compareTo(lowDate) >= 0 && date.compareTo(highDate) <= 0;

	}
	
	// Note that boxscores don't exist for unplayed games, unlike the NFL reference,
	// where we can see a preview.
	// This means that it will never be the case that getAwayScore will return
	// nothing, because boxscores are always played.
	
	public int getAwayScore() {
		return 0;	
	}
	
	public int getHomeScore() {
		return 0;
	}
	
	public String getHomeTeamName() {
		return null;
	}
	
	public String getAwayTeamName() {
		return null;
	}
	
	public String getTime() {
		return null;
	}
	
	public String getDate() {
		return null;
	}
	
	public int getYearFromId() {
		return Integer.parseInt(getId().substring(0, 4));
	}
	
	public int getMonthFromId() {
		return Integer.parseInt(getId().substring(4, 6));
	}
	
	public int getMonthDayFromId() {
		return Integer.parseInt(getId().substring(6, 8));
	}
	
	public String getYearFromDoc() {
		return null;
	}
	
	public String getArenaName() {
		Document gameDoc = decompressToDocument("boxscore");
		return gameDoc.selectFirst("div.scorebox div.scorebox_meta").child(1).text();
	}
	
	public String getHomeTeamRecordAfter() {
		return null;
	}
	
	public String getAwayTeamRecordAfter() {
		return null;
	}
	
	public Set<String> getAllPlayerIds(){
		return null;
	}

}
