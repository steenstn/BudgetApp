package budgetapp.util.database;

import budgetapp.util.entries.BudgetEntry;

/**
 * The class for executing a transaction. Uses the Command design pattern
 * to enable undo.
 * 
 * Only handles database transaction, not the currentBudget.
 * @author Steen
 *
 */

public class TransactionCommand {

	private BudgetDataSource datasource;
	private BudgetEntry entry;
	public TransactionCommand(BudgetDataSource theSource,BudgetEntry theEntry)
	{
		datasource = theSource;
		entry = theEntry;
	}
	
	public BudgetEntry getEntry()
	{
		return entry;
	}
	
	/**
	 * Creates a new transaction entry in the database and sets its id. The id is created
	 * within the database
	 */
	public void execute()
	{
		BudgetEntry temp = datasource.createTransactionEntry(entry);
		entry.setId(temp.getId());
	}
	
	public void unexecute()
	{
		datasource.removeTransactionEntry(entry);
	}
}
