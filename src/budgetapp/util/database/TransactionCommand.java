package budgetapp.util.database;

import budgetapp.util.BudgetEntry;

/**
 * The class for executing a transaction. Uses the Command design pattern
 * to enable undo.
 * 
 * Only handles database transaction, not the currentBudget.
 * @author Steen
 *
 */

public class TransactionCommand {

	private BudgetDataSource _datasource;
	private BudgetEntry _entry;
	public TransactionCommand(BudgetDataSource theSource,BudgetEntry theEntry)
	{
		_datasource = theSource;
		_entry = theEntry;
	}
	
	public BudgetEntry getEntry()
	{
		return _entry;
	}
	public void execute()
	{
		BudgetEntry temp = _datasource.createTransactionEntry(_entry);
		_entry.setId(temp.getId());
	}
	
	public void unexecute()
	{
			//Remove transactionEntry
			_datasource.removeTransactionEntry(_entry);
			
	}
}
