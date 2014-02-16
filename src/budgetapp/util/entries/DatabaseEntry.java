package budgetapp.util.entries;

import budgetapp.util.Money;

/**
 * The abstract class for all database entries in the tables. Just has an ID
 * @author Steen
 *
 */
public abstract class DatabaseEntry {
	
	public static final int NORMAL_TRANSACTION = 0;
	public static final int INSTALLMENT_TRANSACTION = 1;
	private long id;
	private int flags;

	private Money value; // How large the transaction was
	
	public void setId(long id){
		this.id = id;
	}
	
	public long getId(){
		return id;
	}
	
	public void setValue(Money value){
		this.value = value;
	}
	
	public Money getValue(){
		return value;
	}
	
	public void setFlags(int flags)
	{
		this.flags = flags;
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
