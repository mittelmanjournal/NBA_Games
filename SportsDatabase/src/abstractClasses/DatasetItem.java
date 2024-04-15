package abstractClasses;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class DatasetItem extends DataContainer {

	protected abstract void create();
	protected abstract void update();
	protected abstract void build();
	protected abstract String getFullWebLink();
	protected abstract String getRequiredDatabaseEnd();
	protected abstract String getRequiredDatasetEnd();
	protected abstract File compressToTextFile(String compressedContents, String subItemName);
	protected abstract String decompressToString(String subItem);
	protected abstract Document decompressToDocument(String subItemName);
	
	protected DatasetItem(String path, String id) throws IllegalArgumentException {
		super(path, id);
	}

	protected DatasetItem(String pathAndId) throws IllegalArgumentException {
		super(pathAndId);
	}
	
	protected DatasetItem(Path pathAndId) throws IllegalArgumentException {
		super(pathAndId);
	}
	
	protected DatasetItem(File f) throws IllegalArgumentException {
		super(f);
	}
	
	
	protected boolean webPageExists() {
		int statusCode = 0;
		Connection.Response response = null; // TODO recall what this line does/how it works
		try {
			response = Jsoup.connect(getFullWebLink()).execute();
			Thread.sleep(2000);
			statusCode = response.statusCode();
		} catch (HttpStatusException e) {
			e.printStackTrace();
			statusCode = e.getStatusCode();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		
		if (statusCode >= 400) {
			return false;
		}
		return true;
	}
	
	@Override
	protected void verifyAll() {
		super.verifyAll();
		verifyDatasetEnd(getRequiredDatasetEnd());
		verifyDatabaseEnd(getRequiredDatabaseEnd());
	}
	
	protected void verifyDatasetEnd(String requiredDatasetEnd) {
		// Check if the last 3 characters of the substring are "__g" or whatever other
		// DS we are working in, actually call this method in the concrete DSI subclass
		// with the parameter being the supposed ending
	    if (!getDatasetFolderName().endsWith(requiredDatasetEnd)) {
	        throw new IllegalArgumentException("Invalid dataset folder ending");
	    }
	}

	protected void verifyDatabaseEnd(String requiredDatabaseEnd) {
	    // Check if the last 3 characters of the substring are "__b"
	    if (!getDatabaseFolderName().endsWith(requiredDatabaseEnd)) {
	        throw new IllegalArgumentException("Invalid database folder ending");
	    }
	}
	
	protected String getDatabaseFolderName() {
		 // Find the third to last and second to last occurrences of '\'
	    int lastIndex = getPath().lastIndexOf('\\');
	    int secondToLastIndex = getPath().lastIndexOf('\\', lastIndex - 1);
	    int thirdToLastIndex = getPath().lastIndexOf('\\', secondToLastIndex - 1);

	    // Get the substring between the third to last and second to last '\'
	    String folderName = getPath().substring(thirdToLastIndex + 1, secondToLastIndex);
	    
	    return folderName;
	}
	
	protected String getDatasetFolderName() {
		// Find the second to last and last occurrences of '\'
	    int lastIndex = getPath().lastIndexOf('\\');
	    int secondToLastIndex = getPath().lastIndexOf('\\', lastIndex - 1);

	    // Get the substring between the second to last and last '\'
	    String folderName = getPath().substring(secondToLastIndex + 1, lastIndex);
	    
	    return folderName;
	}
	
	public String getId() {
		return getName();
	}
	
	protected byte[] compress(String data) throws IOException {
		byte[] input = data.getBytes();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		Deflater deflater = new Deflater();
		try (DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, deflater)) {
			deflaterOutputStream.write(input);
		}
		return byteArrayOutputStream.toByteArray();
	}
	
	protected String decompress(File compressedFile) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Inflater inflater = new Inflater();
        try (FileInputStream fis = new FileInputStream(compressedFile);
             InflaterInputStream iis = new InflaterInputStream(fis, inflater)) {
            int len;
            while ((len = iis.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
        }
        return byteArrayOutputStream.toString();
    }
	
	/**
	 * Takes in a Document and remove the all of the array parameter's divs from
	 * that document.
	 * 
	 * @param gamePage
	 * @param divs
	 */
	//TODO MAYBE* make static and implement in DSI
	protected void removeExtraDivs(Document gamePage, String... divs) { // TODO potentially abstractable
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
	//TODO MAYBE* make static and implement in DSI
	//maybe make static because not an instance specific method
	private void removeExtraDiv(Document gamePage, String str) { // TODO potentially abstractable
		try {
			if (gamePage == null || gamePage.selectFirst("div" + str) == null)
				throw new IOException();
			gamePage.selectFirst("div" + str).remove();
		} catch (IOException ioe) {
			//ioe.printStackTrace();
		}

	}

}
