package budgetapp.util;

import java.util.ArrayList;

import budgetapp.util.database.TransactionCommand;

public class TransactionQueue {
	
	private ArrayList<TransactionCommand> transactions;
	
	public TransactionQueue()
	{
		transactions = new ArrayList<TransactionCommand>();
	}
	
	public void queueTransaction(TransactionCommand transaction)
	{
		transactions.add(transaction);
	}
	
	public void processQueue()
	{
		for(int i = 0; i < transactions.size(); i++)
		{
			transactions.get(i).execute();
		}
	}
	
}
