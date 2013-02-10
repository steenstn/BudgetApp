package budgetapp.util.stats;
import java.util.ArrayList;
import java.util.List;

import budgetapp.util.*;
public class Stats {
	
	private List<CategoryEntry> categories;
	private List<BudgetEntry> transactions;
	protected long total;
	protected String name;
	public Stats()
	{
		total = 0;
		categories = new ArrayList<CategoryEntry>();
		transactions = new ArrayList<BudgetEntry>();
	}
	public Stats(String theName)
	{
		name = theName;
		total = 0;
		categories = new ArrayList<CategoryEntry>();
		transactions = new ArrayList<BudgetEntry>();
	}
	public String getName()
	{
		return name;
	}
	
	public void addEntry(BudgetEntry theEntry)
	{
		transactions.add(theEntry);
		total+=theEntry.getValue();
	}
	
	public void addToTotal(long x)
	{
		total+=x;
	}
	
	public List<BudgetEntry> getTransactions()
	{
		return transactions;
	}
	public long getTotal()
	{
		return total;
	}
}
