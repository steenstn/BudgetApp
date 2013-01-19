package budgetapp.util;
/**
 * The class for executing a transaction. Uses the Command design pattern
 * to enable undo.
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
    	_datasource.updateCategory(_entry.getCategory(),_entry.getValue());
    	_datasource.updateDaySum(_entry);
	}
	
	public boolean unexecute()
	{
		if(!unexecuted) // Only enable one unexecute()
		{
			//Remove transactionEntry
			_datasource.removeTransactionEntry(_entry);
			//Update category by adding the negative value that was added
			_datasource.updateCategory(_entry.getCategory(),_entry.getValue()*-1);
			//Update daysum by adding the negative value that was added
			_entry.setValue(_entry.getValue()*-1);
			_datasource.updateDaySum(_entry);
			_entry.setValue(_entry.getValue()*-1);
			unexecuted=true;
			return true;
		}
		return false;
	}
}
