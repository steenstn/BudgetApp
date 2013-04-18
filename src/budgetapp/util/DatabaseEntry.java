package budgetapp.util;
/**
 * The abstract class for all database entries in the tables. Just has an ID
 * @author Steen
 *
 */
public abstract class DatabaseEntry {
	
	private long id;
	protected int flags;
	
	public void setId(long id){
		this.id = id;
	}
	
	public long getId(){
		return id;
	}
	
	public int getFlags()
	{
		return flags;
	}
	@Override
	public String toString(){
		return "DatabaseEntry";
	}
}
