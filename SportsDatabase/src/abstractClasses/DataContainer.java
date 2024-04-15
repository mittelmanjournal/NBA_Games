package abstractClasses;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.file.Path;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class DataContainer {
	
	protected abstract void verifyName();
	
	/**
	 * The absolute path directory of where this Database's folder is stored,
	 * excluding the name of the folder.
	 * 
	 * To be valid, must end with a '\\' character as this class often gets the full
	 * path by path + name.
	 */
	private String path;

	/**
	 * The name of this Database folder. In subclasses the ending of this folder
	 * name will be determine based on what the link is. The ending is necessary as
	 * a way to determine what Database subclass an existing Database folder
	 * represents.
	 */
	private String name;

	//assume may not exist in files
	protected DataContainer(String path, String name) throws IllegalArgumentException {
		this.path = path;
		this.name = name;
		
		verifyAll();
	}
	
	//assume may not exist in files
	protected DataContainer(String pathAndName) throws IllegalArgumentException {
		// the full path (including the name) should just end with the name without '\\'
		if (pathAndName.charAt(pathAndName.length() - 1) == '\\')
			throw new IllegalArgumentException();
		
		this.path = pathAndName.substring(0, pathAndName.lastIndexOf("\\") + 1);
		this.name = pathAndName.substring(pathAndName.lastIndexOf("\\") + 1);
		
		verifyAll();
	}

	// when given a Path or file object assume it already exists
	protected DataContainer(File f) throws IllegalArgumentException {
		if (f.exists()) {
			String absPath = f.getAbsolutePath();
			this.path = absPath.substring(0, absPath.lastIndexOf("\\") + 1);
			this.name = absPath.substring(absPath.lastIndexOf("\\") + 1).replace(".txt", ""); //TODO confirm that the removal of the ending doesn't mess the creation of file DSIs
		} else {
			throw new IllegalArgumentException();
		}
		
		verifyAll();
	}
	
	// when given a Path or file object assume it already exists
	protected DataContainer(Path pathAndName) throws IllegalArgumentException { 
		this(pathAndName.toFile());
		// call createFolder in DBs DSs and maybe DDSIs
	}
	
	protected void verifyPathEnd() throws IllegalArgumentException {
		if(path.charAt(path.length()-1) != '\\') throw new IllegalArgumentException();
	}
	
	protected boolean fileExists() {
		return new File(path + name).exists();
	}
	
	protected void createFolder() {
		File f = new File(path + name);
		if(!f.exists()) {
			f.mkdir();
		}
	}
	
	protected Document makeDoc(String websiteLink) {
		Document gamePage = null;
		boolean success = false;

		while (!success) {
			System.out.println("Creating Document at " + websiteLink);
		    try {
		    	Thread.sleep(2100);
		    	System.out.println(websiteLink);
		    	gamePage = Jsoup.connect(websiteLink).get();  
		        success = true; // If makeDoc succeeds, set success to true to exit the loop
		    } catch (IOException | InterruptedException e) {
		        // Do nothing, the loop will retry
		    	e.printStackTrace();
		    }
		}
	
		return gamePage;
	}
	
	protected void verifyAll() {
		verifyPathEnd();
		verifyName();
	}
	
	public File getFile() {
		return fileExists() ? new File(path + name): null;
	}
	
	/**
	 * Get the path class level variable.
	 * 
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Get the name class level variable. Without subclass/link specific ending.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	
}
