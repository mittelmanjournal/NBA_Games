package abstractDataTypes;

import java.io.File;

public abstract class DataContainer {
	
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
	
	/**
	 * Each subclass of the Database abstract class holds a single sport at a time,
	 * meaning that folder will only contain Datasets that contain Items of a
	 * specified sport, this sport's data can be found at a specific link. Based on
	 * this link, when you create a folder representation of this Database, this
	 * class will determine what ending to append to the folder name so that later
	 * on if we decide to access that folder the class can know what subclass to
	 * create it as. Link will be specified/parameterised in subclasses.
	 */
	private String link;
	
	
	protected DataContainer(String path, String name, String link) {
		this.path = path;
		this.name = name;
		this.link = link;
	}
	
	protected DataContainer(File f) {
		if (f.exists() && f.isDirectory()) {
			String absPath = f.getAbsolutePath();
			this.path = absPath.substring(0, absPath.lastIndexOf("\\") + 1);
			this.name = absPath.substring(absPath.lastIndexOf("\\") + 1);
			this.link = getLink(f);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	protected String getLink(File f) {
		if (f.exists() && f.isDirectory()) {
			String name = f.getName();
			name = name.substring(name.length() - 3);
			return getLink(name);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	protected abstract String getFileEnding();
	
	protected abstract String getLink(String ending);
	
	/**
	 * Create a folder at path + name + ending to represent this database if this
	 * folder doesn't yet exist and those previously named variables are valid).
	 * This method is to be only used by children classes of Database as you can
	 * only know what the ending is in a child class.
	 */
	protected void createFolder() {
		File f = new File(getFullPath());
		ifClassMembersValidAndFileDoesNotExistCreateFile(f);
	}
	
	/**
	 * Create a directory at the given file if class members are valid and the file
	 * is yet to exist.
	 * 
	 * @param f
	 */
	protected void ifClassMembersValidAndFileDoesNotExistCreateFile(File f) {
		if(allValid() && !f.exists()) {
			f.mkdir();
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Return true if all class level instance variables aren't null and blank.
	 * 
	 * @return
	 */
	protected boolean allValid() { return isValid(path) && isValid(name) && isValid(link); }
	
	/**
	 * Return true if the given string isn't null and blank.
	 * @param str
	 * @return
	 */
	private boolean isValid(String str) { return str != null && !str.isBlank(); }
	
	/**
	 * This method ought to only be used in contexts that will not be called from
	 * this abstract database. Only database children should use this because only
	 * they have a link variable that isn't null, therefore allowing us to get the
	 * file ending based on the link.
	 * 
	 * Gets the path of where this db may exist.
	 * 
	 * @return e.g. "C:\\Databases\\myHockeyDB__h";
	 */
	protected String getFullPath() { return path + name + getFileEnding(); }
	
	/**
	 * Get the path class level variable.
	 * @return
	 */
	public String getPath() { return path; }

	/**
	 * Get the name class level variable. Without subclass/link specific ending.
	 * @return
	 */
	public String getName() { return name; }

	/**
	 * Get the link class level variable.
	 * @return
	 */
	public String getLink() { return link; }
}
