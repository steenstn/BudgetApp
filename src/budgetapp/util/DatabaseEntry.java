package budgetapp.util;
/**
 * The abstract class for all database entries in the tables. Just has an ID
 * @author Steen
 *
 */
public abstract class DatabaseEntry {
	
	protected long id;
	public long getId(){
		return id;
	}
	
	@Override
	public String toString(){
		return "DatabaseEntry";
	}
}
