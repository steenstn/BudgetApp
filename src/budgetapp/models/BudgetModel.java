package budgetapp.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import budgetapp.util.BudgetEntry;
import budgetapp.util.CategoryEntry;
import budgetapp.util.IBudgetObserver;
import budgetapp.util.DayEntry;
import budgetapp.util.Money;
import budgetapp.util.database.BudgetDataSource;
import budgetapp.util.database.TransactionCommand;
public class BudgetModel {
	
	private BudgetDataSource datasource;
	private Money dailyBudget;
	private ArrayList<TransactionCommand> transactions;
	private ArrayList<IBudgetObserver> observers;
	
	public BudgetModel(Context context)
	{
		datasource = new BudgetDataSource(context);
		dailyBudget = new Money(100);
		transactions = new ArrayList<TransactionCommand>();
		observers = new ArrayList<IBudgetObserver>();
	}
	
	public void createTransaction(BudgetEntry entry)
	{
		addDailyBudget();
		transactions.add(new TransactionCommand(datasource,entry));
		transactions.get(transactions.size()-1).execute();
		notifyObservers();
	}
	
	public void undoLatestTransaction()
	{
		if(transactions.size()>0){
			transactions.get(transactions.size()-1).unexecute();
			transactions.remove(transactions.size()-1);
			notifyObservers();
		}
	}
	
	public Money getCurrentBudget()
	{
		List<DayEntry> dayTotal = datasource.getSomeDaysTotal(1, BudgetDataSource.DESCENDING);
		if(dayTotal.size()==1)
		{
			return new Money(dayTotal.get(0).getValue());
		}
		else
			return new Money();
	}
	
	public Money getDailyBudget()
	{
		return dailyBudget;
	}
	public List<BudgetEntry> getSomeTransactions(int n, String orderBy)
	{
		return datasource.getSomeTransactions(n, orderBy);
	}
	
	public List<DayEntry> getSomeDays(int n, String orderBy)
	{
		return datasource.getSomeDays(n, orderBy);
	}
	
	public List<DayEntry> getSomeDaysTotal(int n, String orderBy)
	{
		return datasource.getSomeDaysTotal(n, orderBy);
	}
	
	public void addDailyBudget()
    {
    	List<DayEntry> lastTotal = datasource.getSomeDaysTotal(1,BudgetDataSource.DESCENDING);
    	
    	List<DayEntry> lastDay = datasource.getSomeDays(1,BudgetDataSource.DESCENDING);
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		
    	
    	if(!lastDay.isEmpty())
    	{
    		SimpleDateFormat compareFormat = new SimpleDateFormat("yyyy/MM/dd");
    		
    		
	    	String lastDayString = lastDay.get(0).getDate();
	    	Calendar lastDayCalendar = Calendar.getInstance();
	    	// Convert the string to a Calendar time. Subtract 1 from month because month 0 = January
	    	// Set HH:mm to 00:00
	    	lastDayCalendar.set(Integer.parseInt(lastDayString.substring(0, 4)),Integer.parseInt(lastDayString.substring(5, 7))-1,Integer.parseInt(lastDayString.substring(8, 10)),0,0);
	    	
	    	//System.out.println("Last day: " + dateFormat.format(lastDayCalendar.getTime()));
	    	lastDayCalendar.add(Calendar.DAY_OF_MONTH, 1); // We want to start counting from the first day without transactions

	    	// Step up to the day before tomorrow
	    	Calendar nextDay = Calendar.getInstance();
	    	nextDay.add(Calendar.DAY_OF_MONTH,1);
	    	
	    	System.out.println("Next day: " + dateFormat.format(nextDay.getTime()));
	    	Calendar tempDate = (Calendar)lastDayCalendar.clone();
	    	while(tempDate.before(nextDay))
	    	{
	    		if(!compareFormat.format(tempDate.getTime()).equalsIgnoreCase(compareFormat.format(nextDay.getTime())))
	    		{
	    			System.out.println("Day to add: " + dateFormat.format(tempDate.getTime()));
	    			BudgetEntry entry = new BudgetEntry(new Money(dailyBudget), dateFormat.format(tempDate.getTime()),"Income");
		        	datasource.createTransactionEntry(entry);
	    		}
	    		
	    		tempDate.add(Calendar.DAY_OF_MONTH,1);	
	    	}
    	}
    	else // Add a transaction of 0
    	{
    		Calendar tempDate = Calendar.getInstance();
    		
    		datasource.createTransactionEntry(new BudgetEntry(new Money(), dateFormat.format(tempDate.getTime()),"Income"));
    		
    	}
    	notifyObservers();
    	
    }
	
	public List<CategoryEntry> getCategoriesSortedByNum() {
		return datasource.getCategoriesSortedByNum();
	}
	
	public void addObserver(IBudgetObserver observer)
	{
		observers.add(observer);
	}
	
	private void notifyObservers()
	{
		for(int i = 0; i < observers.size(); i++)
		{
			observers.get(i).update();
		}
		
	}

}
