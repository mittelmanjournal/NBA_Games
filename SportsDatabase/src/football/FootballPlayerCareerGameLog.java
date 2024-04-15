package football;

import java.io.File;
import java.nio.file.Path;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import abstractClasses.FileDatasetItem;

//TODO note that kickers and sometimes punters have an entirely different ID system: G/gramamar01 V/vinatada01
public class FootballPlayerCareerGameLog extends FileDatasetItem {
	public FootballPlayerCareerGameLog(String path, String id) throws IllegalArgumentException {
		super(path, id);
		// TODO Auto-generated constructor stub
	}
	
	protected FootballPlayerCareerGameLog(String pathAndId) throws IllegalArgumentException {
		super(pathAndId);
		// TODO Auto-generated constructor stub
	}
	
	protected FootballPlayerCareerGameLog(Path pathAndId) throws IllegalArgumentException {
		super(pathAndId);
		// TODO Auto-generated constructor stub
	}
	
	protected FootballPlayerCareerGameLog(File f) throws IllegalArgumentException {
		super(f);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void create() {
		if(!fileExists()) build();
	}

	@Override
	public void update() {
		if(fileExists()) build();
	}

	@Override
	public void build() { //TODO consider implementing this method in the FileDatasetItem class as its identical to FootballGame
		Document gamePage = makeDoc(getFullWebLink());

		// remove all useless elements
		removeExtraDivs(gamePage, "#header", "#srcom", "#inner_nav", "#footer", "#bottom_nav", ".stathead_event");

		// remove all useless elements
		removeExtraDivs(gamePage, "#header", "#srcom", "#inner_nav", "#footer", "#bottom_nav", ".stathead_event");

		String contents = gamePage.outerHtml();

		compressToTextFile(contents); // should be overwriting
	}

	@Override
	protected String getFullWebLink() {
		return "https://www.pro-football-reference.com/players/" + getId().charAt(0) + "/" + getId() + "/gamelog/";
	}

	@Override
	protected String getRequiredDatabaseEnd() {
		return "__p";
	}

	@Override
	protected String getRequiredDatasetEnd() {
		return "__c";
	}

	@Override
	protected void verifyName() throws IllegalArgumentException {
		System.out.println(getId());
		if (!getId().matches("[A-Za-z.\\-]{6}\\d{2}"))
			throw new IllegalArgumentException();
	}
	
	public Element getActiveGameRowByNumber(int nthGame) {
		return decompressToDocument().selectFirst("#all_stats #div_stats table#stats tbody").getElementById("stats." + nthGame);
	}
	
}
