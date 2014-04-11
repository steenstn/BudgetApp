package budgetapp.util;

import java.util.ArrayList;

import budgetapp.util.commands.Command;

public class TransactionQueue {
	
	private ArrayList<Command> transactions;
	private int index;
	
	public TransactionQueue() {
		index = 0;
		transactions = new ArrayList<Command>();
	}
	
	public void queueItem(Command transaction) {
		transactions.add(transaction);
	}
	
	public int getSize() {
		return transactions.size();
	}
	
	public int getRemainingItems() {
		int remainingItems = 0;
		for(int i = 0; i < transactions.size(); i++) {
			if(!transactions.get(i).isExecuted()) {
				remainingItems++;
			}
		}
		return remainingItems;
	}
	
	public Command processQueueItem()
	{
		if(index >= transactions.size())
			return null;
		
		if(!transactions.get(index).isExecuted()) {
			transactions.get(index).execute();
			index++;
		}
		
		return transactions.get(index-1);
		
	}
	
}
