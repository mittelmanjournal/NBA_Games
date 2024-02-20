package databases;

public abstract class Database {
	/**
	 * This is the String holding the directory of the database
	 * 
	 * <b>Description</b> a database is encapsulated by one folder that has 2 main
	 * subfolders. One of these subfolders is a collection of the Game files and the
	 * next is a collection of Player files.
	 * 
	 * This is where all of the files will be written to
	 */
	//@SuppressWarnings("unused")
	private String directory;

	
	public Database(String directory) {
		this.directory = directory;
	}
	
	public Database(String directory, int lowYear, int highYear) {
		this(directory);
		build(lowYear, highYear);
	}
	
	public Database(String directory, int lowYear) {
		this(directory);
		build(lowYear);
	}

	public abstract void build(int lowYear, int highYear);
	
	public abstract void build(int lowYearOrWeek);
	
	public abstract void build(Integer year, Integer week);
	
	protected void setDirectory(String dir) {
		directory = dir;
	}
	
	protected String getDirectory() {
		return directory;
	}
	//year, week
	
	
	
}
