package databases;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import compressionDecompression.Compressible;

public class Player extends DatabaseItem {
	private final static String[] VALID_PAGE_NAMES = new String[] { ".htm", "/gamelog/", "/splits/", "/fantasy/",
			"/penalties/" };

	public Player(String path, String id) {
		super(path, id);
	}

	public Player(String path, String id, boolean updateIfTrue) {
		super(path, id, updateIfTrue);
	}

	@Override
	protected File update() {
		/*
		 * Because this is the general update method, only include NECESSARY data (for
		 * example exclude)
		 */
		// update(".htm"); // Overview Page (unnecessary because all of this data
		// encapsulated by gamelog)
		update("/gamelog/", false);
		// update("/splits/", true); // we are able to determine most of the splits from
		// gamelog
		// update("/fantasy/", true); // we are able to determine fantasy points from
		// gamelog
		update("/penalties/", true); // we aren't able to determine penalties from gamelogs therefore we scrape this
		// MAYBE MAKE A REFEREE SUBCLASS inheriting from DatabaseItem
		// Inheritance and good development organization allows for easy updates in that
		// case that I do want to make a Referee object

		return new File(getWritePath()); // FOLDER that contains the created files
	}

	protected File update(String[] pagesToScrape) {
		boolean isFirstIteration = true;
		for (String pageName : pagesToScrape) {
			if (isFirstIteration) {
				if (isPageValid(pageName))
					update(pageName, false); // for the first page include the header player info
				isFirstIteration = false;
			} else {
				if (isPageValid(pageName))
					update(pageName, true);
			}
		}
		return new File(getWritePath());
	}

	private boolean isPageValid(String pageName) {
		if (pageName.isBlank() || pageName == null || !Arrays.asList(VALID_PAGE_NAMES).contains(pageName))
			return false;
		return true;
	}

	private void update(String page, boolean removePlayerInfoHeader) {
		Document playerPage = null;

		try {
			if (!isPageValid(page))
				throw new IOException();

			playerPage = Jsoup.connect(createLink().replace(".htm", page)).get();
			Thread.sleep(2000); // we must delay 2 seconds to not get IP banned
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		if (removePlayerInfoHeader)
			removeExtraDiv(playerPage, "#info");
		// remove all useless elements
		removeExtraDivs(playerPage, new String[] { "#header", "#srcom", "#inner_nav", "#footer", "#bottom_nav",
				".new_stathead_player_highlight", ".adblock", ".stathead_event" });

		String contents = playerPage.outerHtml();
		byte[] compressedContents;
		try {
			compressedContents = Compressible.compress(contents);
			try (FileOutputStream fos = new FileOutputStream(new File(getWritePath() + determineFileName(page)))) {
				fos.write(compressedContents);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String determineFileName(String page) throws IOException {
		if (!isPageValid(page))
			throw new IOException();
		switch (page) {
		case ".htm":
			return "\\Overview.txt";
		case "/gamelog/":
			return "\\Gamelog.txt";
		case "/splits/":
			return "\\Splits.txt";
		case "/fantasy/":
			return "\\Fantasy.txt";
		case "/penalties":
			return "\\Penalties.txt";
		default:
			throw new IOException();
		}
	}

	protected String createLink() {
		return LINK_BASE + "/players/" + getId().charAt(0) + "/" + getId() + ".htm";
	}

}
