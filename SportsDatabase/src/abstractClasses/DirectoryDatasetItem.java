package abstractClasses;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class DirectoryDatasetItem extends DatasetItem {
	
	protected abstract void create(String subItem);
	protected abstract void update(String subItem);
	protected abstract void build(String subItem);
	protected abstract boolean validSubItem(String subItem);
	
	protected DirectoryDatasetItem(String path, String id) throws IllegalArgumentException {
		super(path, id);
	}
	
	protected DirectoryDatasetItem(String pathAndId) throws IllegalArgumentException {
		super(pathAndId);
	}
	
	protected DirectoryDatasetItem(Path pathAndId) throws IllegalArgumentException {
		super(pathAndId);
	}
	
	protected DirectoryDatasetItem(File f) throws IllegalArgumentException {
		super(f);
	}
	
	//if false either subFileName is invalid or the subFile doesn't exist
	protected boolean fileExists(String subFileName) {
		return validSubItem(subFileName) ? new File(getPath() + getId() + "\\" + subFileName + ".txt").exists() : false;
		//BUGFIXED HERE: I forgot to include the ".txt" because the subFileNames are actually files and must include file extension
	}
	
	//in subclasses you can make specific subFile existence checkers.
	//this one will just return true if at least one of the subFiles exist in its folder.
	@Override
	public boolean fileExists() {
	    File file = new File(getPath() + getId());

	    // Check if the file is a directory
	    if (!file.isDirectory()) {
	        return false;
	    }

	    // Get list of files and directories inside the directory
	    File[] files = file.listFiles();

	    // Check if there is at least one non-directory subfile
	    for (File subFile : files) {
	        if (!subFile.isDirectory() && validSubItem(subFile.getName())) {
	            return true;
	        }
	    }

	    return false;
	}
	
	@Override
	protected File compressToTextFile(String contents, String subItemName) {
		File writeCompressedContentsHere = null;
		
		if (validSubItem(subItemName)) {
			
			byte[] compressedContents;

			try {

				compressedContents = compress(contents);
				// NOTE: to write here the ID name folder that contains the sub files must exist,
				// which is why we createFolder() in DDSI constructors
				writeCompressedContentsHere = new File(getPath() + getId() + "\\" + subItemName + ".txt"); 

				try (FileOutputStream fos = new FileOutputStream(writeCompressedContentsHere)) {
					fos.write(compressedContents);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return writeCompressedContentsHere;
	}
	
	/**
	 * Assuming a compressed byte[] in a txt file exists at getWritePath() + ".txt",
	 * return a that file's decompressed data as a String.
	 * 
	 * @return
	 */
	@Override
	protected String decompressToString(String subItemName) throws IllegalArgumentException {
		if (validSubItem(subItemName)) {
			try {
				return decompress(new File(getPath() + getId() + "\\" + subItemName + ".txt"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		throw new IllegalArgumentException();
	}
	
	/**
	 * Returns a document representation of the data at this object's underlying
	 * file. This will be used to access data from specific objects in the database.
	 * 
	 * @return
	 */
	@Override
	public Document decompressToDocument(String subItemName) throws IllegalArgumentException {
		return Jsoup.parse(decompressToString(subItemName));
	}
	
	//TODO consider making decompressBoxscoreToDoc() or decompressPBPToDoc() etc...
	
}
