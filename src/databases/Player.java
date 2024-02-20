package databases;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import compressionDecompression.Compressible;

public class Player extends DatabaseItem {
	
	public Player(String path, String id) {
		super(path, id);
	}

	public Player(String path, String id, boolean updateIfTrue) {
		super(path, id, updateIfTrue);
	}

	@Override
	protected File update() {
		Document playerOverviewPage = null;

		try {
			playerOverviewPage = Jsoup.connect(createLink()).get();
			Thread.sleep(2000); // we must delay 2 seconds to not get IP banned
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		// remove all useless elements
		removeExtraDivs(playerOverviewPage,
				new String[] { "#header", "#srcom", "#inner_nav", "#footer", "#bottom_nav", ".stathead_event" });

		String contents = gamePage.outerHtml();
		byte[] compressedContents;
		try {
			compressedContents = Compressible.compress(contents);
			try (FileOutputStream fos = new FileOutputStream(new File(getWritePath()))) {
				fos.write(compressedContents);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new File(getWritePath());
	}

	protected String createLink() {
		return LINK_BASE + "/players/" + getId().charAt(0) + "/" + getId() + ".htm";
	}

}
