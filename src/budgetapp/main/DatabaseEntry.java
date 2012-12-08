package budgetapp.main;
/**
 * The abstract class for all database entries in the tables
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
