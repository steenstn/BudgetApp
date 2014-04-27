package budgetapp.util.commands;

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
	private long eventId;
	private boolean linkedToEvent;
	
	public TransactionCommand(BudgetDataSource theSource,BudgetEntry theEntry)
	{
		executed = false;
		linkedToEvent = false;
		datasource = theSource;
		entry = theEntry;
	}
	
	public TransactionCommand(BudgetDataSource theSource,BudgetEntry theEntry, long eventId)
	{
		executed = false;
		linkedToEvent = true;
		datasource = theSource;
		entry = theEntry;
		this.eventId = eventId;
	}
	
	public BudgetEntry getEntry() {
		return entry;
	}
	
	public void setEventId(long id) {
		this.eventId = id;
	}
	/**
	 * Creates a new transaction entry in the database and sets its id. The id is created
	 * within the database
	 */
	public void execute()
	{
		BudgetEntry temp = datasource.createTransactionEntry(entry);
		entry.setId(temp.getId());
		if(linkedToEvent) {
			datasource.addTransactionToEvent(entry.getId(), eventId);
		}
		executed = true;
	}
	
	public void unexecute()
	{
		datasource.removeTransactionEntry(entry);
	}
}
