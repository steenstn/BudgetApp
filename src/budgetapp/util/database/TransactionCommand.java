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
	private boolean unexecuted;
	public TransactionCommand(BudgetDataSource theSource,BudgetEntry theEntry)
	{
		_datasource = theSource;
		_entry = theEntry;
		unexecuted = false;
	}
	
	public BudgetEntry getEntry()
	{
		return _entry;
	}
	public void execute()
	{
		BudgetEntry temp = _datasource.createTransactionEntry(_entry);
		_entry.setId(temp.getId());
		
    	_datasource.addToCategory(_entry.getCategory(),_entry.getValue().get());
    	_datasource.updateDaySum(_entry);
    	_datasource.updateDayTotal(_entry);
	}
	
	public boolean unexecute()
	{
		if(!unexecuted) // Only enable one unexecute()
		{
			//Remove transactionEntry
			_datasource.removeTransactionEntry(_entry);
			//Update category by adding the negative value that was added
			_datasource.removeFromCategory(_entry.getCategory(),_entry.getValue().get()*-1);
			//Update daysum and daytotal by adding the negative value that was added
			_entry.setValue(_entry.getValue().get()*-1);
			_datasource.updateDaySum(_entry);
			_datasource.updateDayTotal(_entry);
			_entry.setValue(_entry.getValue().get()*-1);
			
			
			unexecuted=true;
			return true;
		}
		return false;
	}
}
