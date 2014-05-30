package budgetapp.util.commands;

import java.util.List;

import budgetapp.util.database.BudgetDataSource;
import budgetapp.util.entries.BudgetEntry;

/**
 * The class for executing a transaction. Uses the Command design pattern
 * to enable undo.
 * 
 * Only handles database transaction, not the currentBudget.
 * @author Steen
 *
 */

public class TransactionCommand extends Command {

	private BudgetEntry entry;
	private List<Long> eventIds;
	
	public TransactionCommand(BudgetDataSource theSource,BudgetEntry theEntry)
	{
		executed = false;
		datasource = theSource;
		entry = theEntry;
	}
	
	public TransactionCommand(BudgetDataSource theSource,BudgetEntry theEntry, List<Long> eventIds)
	{
		executed = false;
		datasource = theSource;
		entry = theEntry;
		this.eventIds = eventIds;
	}
	
	public BudgetEntry getEntry() {
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
		if(eventIds != null && eventIds.size() != 0) {
			for(Long id : eventIds) {
				datasource.addTransactionToEvent(entry.getId(), id.longValue());
			}
		}
		executed = true;
	}
	
	public void unexecute()
	{
		datasource.removeTransactionEntry(entry);
	}
}
