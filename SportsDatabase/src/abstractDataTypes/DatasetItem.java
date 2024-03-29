package abstractDataTypes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import compressionDecompression.Codec;

public abstract class DatasetItem extends Codec {

	private String path;
	private String id;

	protected DatasetItem(String path, String id) {
		this.path = path;
		this.id = id;
		//verify path ends with \\
//		if(path.lastIndexOf("\\") != path.length() - 1) { //path.charAt(path.length() - 1) != '\\'
//			throw new IllegalArgumentException();
//		}
		//above if sttmnt deprecated because verifyPathEnding() made
		
		verifyAll();
		
		// in subclasses, verify that the last string in the path before the last "\\"
		// is of the correct DS type: so if we are in Football game, it should be __g
//		verifyDatasetFolderEnding();
		// verify the folder above that one should be a database name ending with __p
//		verifyDatabaseFolderEnding();
		//also check the format of ID so that its valid as another verification step
//		verifyIDFormat(); // this may be second to last in path sometimes because some DSIs are folder represented 	
		// if any of these aren't valid, as expressed in the subclasses, throw an uncaught exception in them
	}
	
	protected DatasetItem(File f) {
		// file must exist,
		if(!f.exists()) {
			throw new IllegalArgumentException();
		}
		// verify its DB and DS folders and ID format match whatever this subclass specifies
		// set path and id based on how this subclass specifies
	}
	
	// some classes have a lot of data at one id, so instead of having a file
	// represent it, we have a folder which is why we must abstract how we create,
	// update and build and check if they exist and get the file of different
	// subclass items
	
	public abstract void create();
	
	public abstract void update();
	
	public abstract void build();
	
	public abstract boolean fileExists();
	
	public abstract boolean webPageExists();
	
	public abstract File getFile();
	
	protected abstract void verifyDatasetFolderEnding();
	
	protected abstract void verifyDatabaseFolderEnding();
	
	protected abstract void verifyIDFormat();
	
	protected void verifyPathEnding() {
		if(path.lastIndexOf("\\") != path.length() - 1) { //path.charAt(path.length() - 1) != '\\'
			throw new IllegalArgumentException();
		}
	}
	
	protected void verifyAll() {
		verifyPathEnding();
		verifyDatasetFolderEnding();
		verifyDatabaseFolderEnding();
		verifyIDFormat();
	}
	
	/**
	 * Combine the path String and the ID string into the path to this Object's
	 * underlying file or folder that contains data files
	 * 
	 * @return
	 */
	protected String getFullPath() {
		return getPath() + getId();
	}

	/**
	 * @return the String id of this instance
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the String path of this instance
	 */
	public String getPath() {
		return path;
	}
	
	protected void setId(String id) {
		this.id = id;
	}
	
	protected void setPath(String path) {
		this.path = path;
	}
}
