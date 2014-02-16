package budgetapp.util.commands;

import budgetapp.util.database.BudgetDataSource;

public abstract class Command {

	BudgetDataSource datasource;
	boolean executed = false;
	
	public boolean isExecuted() {
		return executed;
	}
	
	public abstract void execute();
	public abstract void unexecute();
	
}
