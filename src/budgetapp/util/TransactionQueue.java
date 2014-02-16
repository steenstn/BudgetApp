package budgetapp.util;

import java.util.ArrayList;

import budgetapp.util.commands.Command;

public class TransactionQueue {
	
	private ArrayList<Command> transactions;
	
	public TransactionQueue()
	{
		transactions = new ArrayList<Command>();
	}
	
	public void queueTransaction(Command transaction)
	{
		transactions.add(transaction);
	}
	
	public void processQueue()
	{
		for(int i = 0; i < transactions.size(); i++)
		{
			if(!transactions.get(i).isExecuted()) {
					System.out.println("Executing " + transactions.get(i).getClass().toString());
					transactions.get(i).execute();
				}
		}
	}
	
}
