package databases;

import java.io.File;

import org.jsoup.nodes.Document;

public abstract class DatabaseItem {
	protected final static String LINK_BASE = "https://www.pro-football-reference.com";
	private String id;
	private String path; // for games its a path to a single HTML doc, for players its a path to

	/**
	 * Creates a new subclass of DatabaseItem with the given parameter String.
	 * 
	 * <p>
	 * Sets the two parameters to their respective class instance variables.
	 * </p>
	 * 
	 * @param path to the database folder representing this Item
	 * @param id   to be the file/directory name for this instance of hard drive
	 *             .HTML data
	 */
	public DatabaseItem(String path, String id) {
		setPath(path);
		setId(id);
	}

	/**
	 * Creates a new subclass of DatabaseItem with the given String parameters and
	 * if the third parameter is TRUE call the abstract method update which is to
	 * connect to some website and collect some data.
	 * 
	 * @param path
	 * @param id
	 * @param updateIfTrue
	 */
	public DatabaseItem(String path, String id, boolean updateIfTrue) {
		this(path, id);
		if (updateIfTrue) {
			update();
		}
		// else getExistingFile(); thought this could be a good line but does nothing,
		// maybe uncomment if you decide to make a class variable Path or File
	}

	/**
	 * This method will return a file if it already exists at String path + id (this
	 * is a file path) OR it will call update and connected to a website and create
	 * a file with some data from that website.
	 * 
	 * <p>
	 * If either of the class level variables are invalid (see method) throw an
	 * exception.
	 * </p>
	 * 
	 * @return File that represents this DatabaseItem object on the hard drive
	 */
	public File build() {
		if (arePathAndIdValid()) {
			return new File(path + id).exists() ? getExistingFile() : update();
		} else {
			throw new IllegalArgumentException("Either path or id are null or blank");
		}
	}

	/**
	 * DatabaseItem abstract method to be implemented in subclasses. This will do
	 * something along the lines of connecting to a website (able to do this based
	 * on id), delaying two seconds to not get IP banned, creating a document and
	 * copying some HTML data and saving it as a file at the path with the name id
	 * 
	 * @return File or Directory with Files inside representing the actual hard
	 *         drive data "underlying" or "referenced by" this java object
	 */
	protected abstract File update();

	/**
	 * Depending on how that data item connects to the website, using the base link
	 * and the id, implement a method that creates a usable link to the site where
	 * this data is initially stored
	 * 
	 * @return
	 */
	protected abstract String createLink();

	/**
	 * Connects to a file OR directory at path + id and if it exists return it.
	 * 
	 * <p>
	 * If either of the class level variables are invalid (see method) OR if the
	 * underlying file doesn't exist throw an exception.
	 * </p>
	 * 
	 * @return
	 */
	protected File getExistingFile() {
		if (arePathAndIdValid()) {
			File ret = new File(path + id);
			if (ret.exists()) {
				return ret;
			}
		}
		throw new IllegalArgumentException("Either path or id are null or blank");
		// if we leave if statement it means either invalid class variables OR the file
		// doesn't exist, in both cases we want to throw an exception
	}

	/**
	 * Return true if both class variables path and id aren't null and aren't blank,
	 * otherwise return false.
	 * 
	 * @return
	 */
	private boolean arePathAndIdValid() {
		return id != null && !id.isBlank() && path != null && !path.isBlank();
		// turn each of these variables' checks into
		// their own methods, if we ever need to
		// only check one
	}
	
	protected void removeExtraDivs(Document gamePage, String[] divs) {
		for (String str : divs)
			removeExtraDiv(gamePage, str);
	}

	protected void removeExtraDiv(Document gamePage, String str) {
		gamePage.selectFirst("div" + str).remove();
	}
	
	protected String getWritePath() {
		return getPath() + getId();
	}

	/*--------------------------------- GETTERS & SETTERS ---------------------------------*/

	/**
	 * @return the String id of this instance
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the class variable id for this instance
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the String path of this instance
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Set the class variable path for this instance
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

}
