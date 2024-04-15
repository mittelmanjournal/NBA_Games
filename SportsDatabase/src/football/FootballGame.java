package football;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import abstractClasses.FileDatasetItem;

public class FootballGame extends FileDatasetItem {

	protected FootballGame(String path, String id) throws IllegalArgumentException {
		super(path, id);
		// TODO Auto-generated constructor stub
	}
	
	protected FootballGame(String pathAndId) throws IllegalArgumentException {
		super(pathAndId);
		// TODO Auto-generated constructor stub
	}
	
	protected FootballGame(Path pathAndId) throws IllegalArgumentException {
		super(pathAndId);
		// TODO Auto-generated constructor stub
	}
	
	protected FootballGame(File f) throws IllegalArgumentException {
		super(f);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void create() {
		if(!fileExists()) build();
	}

	@Override
	protected void update() {
		if(fileExists()) build();
	}

	@Override
	protected void build() { //TODO consider implementing this method in the FileDatasetItem class as its identical to FootballPlayerCareerGameLog
		System.out.println(getId());
		
		Document gamePage = makeDoc(getFullWebLink());

		// remove all useless elements
		removeExtraDivs(gamePage, "#header", "#srcom", "#inner_nav", "#footer", "#bottom_nav", ".stathead_event");

		String contents = gamePage.outerHtml();

		compressToTextFile(contents); // should be overwriting
	}

	@Override
	protected String getFullWebLink() {   
		return "https://www.pro-football-reference.com/boxscores/" + getId() + ".htm";
	}

	@Override
	protected String getRequiredDatabaseEnd() {
		return "__p";
	}

	@Override
	protected String getRequiredDatasetEnd() {
		return "__g";
	}

	@Override
	protected void verifyName() {
		System.out.println(getId());
		if (!getId().matches("\\d{8}0[a-z]{3}")) throw new IllegalArgumentException("Invalid ID format");
	}
	
	public Element getElement(String elementClassOrIdString) {
		return fileExists() ? decompressToDocument().selectFirst("div" + elementClassOrIdString) : null;
	}
	
	public Element getScorebox() {
		return getElement(".scorebox");
	}
	
	public String getAwayScore() {
		return getScorebox().child(0).selectFirst(".scores .score").text();
	}
	
	public int getAwayScoreAsInt() {
		return Integer.parseInt(getAwayScore());
	}
	
	public String getAwayRecord() {
		return getScorebox().child(0).child(2).text();
	}

	public Set<String> getAllPlayerIds() {
		Set<String> playerIds = new HashSet();
		Document fg = decompressToDocument();
		
		playerIds.addAll(getAllOffensePlayerIds());
		
		playerIds.addAll(getAllDefensePlayerIds());
		
		Consumer<Element> addPlayerIdsToSet = player -> playerIds.add(player.attr("href").substring(11, 19));
		
		Jsoup.parse(fg.select("#all_returns").toString().replaceAll("<!--", "").replaceAll("-->", ""))
				.select("#all_returns #div_returns table#returns tbody tr th a").forEach(addPlayerIdsToSet);
		
		Jsoup.parse(fg.select("#all_kicking").toString().replaceAll("<!--", "").replaceAll("-->", ""))
				.select("#all_kicking #div_kicking table#kicking tbody tr th a").forEach(addPlayerIdsToSet);

		return playerIds;
	}
	
	public Set<String> getAllOffensePlayerIds(){
		Set<String> playerIds = new HashSet();
		Document fg = decompressToDocument();
		
		Consumer<Element> addPlayerIdsToSet = player -> playerIds.add(player.attr("href").substring(11, 19));
		
		fg.select("#all_player_offense #div_player_offense table#player_offense tbody tr th a")
				.forEach(addPlayerIdsToSet);
		
		return playerIds;
	}
	
	public Set<String> getAllDefensePlayerIds(){
		Set<String> playerIds = new HashSet();
		Document fg = decompressToDocument();
		
		Consumer<Element> addPlayerIdsToSet = player -> playerIds.add(player.attr("href").substring(11, 19));
		
		Jsoup.parse(fg.select("#all_player_defense").toString().replaceAll("<!--", "").replaceAll("-->", ""))
		.select("#all_player_defense #div_player_defense table#player_defense tbody tr th a")
		.forEach(addPlayerIdsToSet);
		
		return playerIds;
	}
	
	@SuppressWarnings("deprecation")
	public Date getDateFromId() {
		String year = getId().substring(0, 4);
        String month = getId().substring(4, 6);
        String day = getId().substring(6, 8);
        return new Date(Integer.parseInt(year) - 1900, Integer.parseInt(month) - 1, Integer.parseInt(day));

	}

}
