package databases;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import compressionDecompression.Compressible;
import compressionDecompression.Decompressible;

public class Game extends DatabaseItem {

	public Game(String path, String id, boolean updateIfTrue) {
		super(path, id, updateIfTrue);
	}

	public Game(String path, String id) {
		super(path, id);
	}

	@Override
	protected File update() {
		Document gamePage = null;

		try {
			gamePage = Jsoup.connect(createLink()).get();
			Thread.sleep(2000); // we must delay 2 seconds to not get IP banned
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		// remove all useless elements
		removeExtraDivs(gamePage,
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

	public String decompressToString() {
		try {
			return Decompressible.decompress(new File(getWritePath())); // maybe just make it a class var of file
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException();
	}

	public Document decompressToDocument() {
		return Jsoup.parse(decompressToString());
	}

	private void removeExtraDivs(Document gamePage, String[] divs) {
		for (String str : divs)
			removeExtraDiv(gamePage, str);
	}

	private void removeExtraDiv(Document gamePage, String str) {
		gamePage.selectFirst("div" + str).remove();
	}

	private String getWritePath() {
		return getPath() + getId() + ".txt";
	}

	protected String createLink() {
		return LINK_BASE + "/boxscores/" + getId() + ".htm"; // this will differ for Player DatabaseItems
	}

}
