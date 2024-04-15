package abstractClasses;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class FileDatasetItem extends DatasetItem {

	protected FileDatasetItem(String path, String id) throws IllegalArgumentException {
		super(path, id);
		// TODO Auto-generated constructor stub
	}
	
	protected FileDatasetItem(String pathAndId) throws IllegalArgumentException {
		super(pathAndId);
		// TODO Auto-generated constructor stub
	}
	
	protected FileDatasetItem(Path pathAndId) throws IllegalArgumentException {
		super(pathAndId);
		// TODO Auto-generated constructor stub
	}
	
	protected FileDatasetItem(File f) throws IllegalArgumentException {
		super(f);
	}
	
	@Override // so that it does nothing, don't use this method
	protected void createFolder() { return; }
	
	@Override
	public boolean fileExists() {
		return new File(getPath() + getId() + ".txt").exists();
	}
	
	@Override // do not use this method
	protected String decompressToString(String subItemName) throws IllegalArgumentException {
		try {
			return decompress(new File(getPath() + getId() + ".txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		throw new IllegalArgumentException();
	}
	
	/**
	 * Assuming a compressed byte[] in a txt file exists at getWritePath() + ".txt",
	 * return a that file's decompressed data as a String.
	 * 
	 * @return
	 */
	protected String decompressToString() {
		return decompressToString("");
	}

	@Override // do not use this method
	protected File compressToTextFile(String contents, String subItemName) {
		File writeCompressedContentsHere = null;

		byte[] compressedContents;

		try {

			compressedContents = compress(contents);
			writeCompressedContentsHere = new File(getPath() + getId() + ".txt");

			try (FileOutputStream fos = new FileOutputStream(writeCompressedContentsHere)) {
				fos.write(compressedContents);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return writeCompressedContentsHere;
	}
	
	protected File compressToTextFile(String contents) {
		return compressToTextFile(contents, "");
	}
	
	@Override // do not use this method
	protected Document decompressToDocument(String subItemName) throws IllegalArgumentException {
		return Jsoup.parse(decompressToString());
	}
	
	protected Document decompressToDocument() throws IllegalArgumentException {
		return Jsoup.parse(decompressToString());
	}
	
}
