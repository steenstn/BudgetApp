package budgetapp.models;

/**
 * The Model for MrCashManager, controls the database and all logic and notifies Observers when changed
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import budgetapp.util.BudgetBackup;
import budgetapp.util.BudgetConfig;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.IBudgetObserver;
import budgetapp.util.Installment;
import budgetapp.util.TransactionQueue;
import budgetapp.util.commands.PayOffInstallmentCommand;
import budgetapp.util.commands.TransactionCommand;
import budgetapp.util.database.BudgetDataSource;
import budgetapp.util.entries.BudgetEntry;
import budgetapp.util.entries.CategoryEntry;
import budgetapp.util.entries.DayEntry;
import budgetapp.util.money.Money;
import budgetapp.util.money.MoneyFactory;
public class BudgetModel {
	
	private BudgetDataSource datasource;
	
	// The daily budget for the user
	private Money dailyBudget;
	
	// List of transactions that have been done. Enables undo
	private ArrayList<TransactionCommand> transactions;
	
	// The observers of this BudgetModel
	private ArrayList<IBudgetObserver> observers;
	
	// Flag whether or not the model has been altered
	private boolean stateChanged;
	
	private TransactionQueue transactionQueue;
	
	// Class containing config values
	private BudgetConfig config;
	
	// Class responsible for backups
	private BudgetBackup backup;
	
	
	public BudgetModel(Context context)
	{
		datasource = new BudgetDataSource(context);
		config = new BudgetConfig(context);
		Money.after = config.getBooleanValue(BudgetConfig.Fields.printCurrencyAfter);
		Money.setCurrency(config.getStringValue(BudgetConfig.Fields.currency));
		Money.setExchangeRate(config.getDoubleValue(BudgetConfig.Fields.exchangeRate));
		dailyBudget = MoneyFactory.convertDoubleToMoney(config.getDoubleValue(BudgetConfig.Fields.dailyBudget));
		
		backup = new BudgetBackup(context);
		transactions = new ArrayList<TransactionCommand>();
		observers = new ArrayList<IBudgetObserver>();
		transactionQueue = new TransactionQueue();
		stateChanged = true;
	}
	
	/**
	 * Adds daily budget and adds a new transaction to the database and the transaction list.
	 * Notifies observers.
	 * @param entry - The BudgetEntry to create
	 */
	public void queueTransaction(BudgetEntry entry)	{
		addDailyBudget();
		transactionQueue.queueItem(new TransactionCommand(datasource, entry));
		transactions.add(new TransactionCommand(datasource, entry));
	}
	
	/**
	 * Removes a transaction from the database.
	 * Notifies observers.
	 * @param entry
	 */
	public void removeTransaction(BudgetEntry entry) {
		datasource.removeTransactionEntry(entry);
		stateChanged = true;
		notifyObservers();
	}
	
	/**
	 * Edits a transaction in the database.
	 * Notifies observers.
	 * @param oldEntry - The entry to edit
	 * @param newEntry - Entry with new information
	 */
	public void editTransaction(long id, BudgetEntry newEntry)
	{
		datasource.editTransactionEntry(id, newEntry);
		stateChanged = true;
		notifyObservers();
	}
	
	public void undoLatestTransaction()
	{
		if(transactions.size()>0){
			transactions.get(transactions.size()-1).unexecute();
			transactions.remove(transactions.size()-1);
			stateChanged = true;
			notifyObservers();
		}
	}
	
	/**
	 * Used for testing to make sure transactions are in place before asserting
	 */
	public void processWholeQueue() {
		for(int i = 0; i < transactionQueue.getSize(); i++) {
			transactionQueue.processQueueItem();
		}
		stateChanged = true;
		notifyObservers();
	}
	
	public void processQueueItem() {
		transactionQueue.processQueueItem();
		stateChanged = true;
	}
	
	public int getRemainingItemsInQueue() {
		return transactionQueue.getRemainingItems();
	}
	public Money getCurrentBudget()	{
		List<DayEntry> dayTotal = datasource.getSomeDaysTotal(1, BudgetDataSource.DESCENDING);
		if(dayTotal.size()==1) {
			return new Money(dayTotal.get(0).getValue());
		}
		else
			return MoneyFactory.createMoney();
	}
	
	public int getQueueSize() {
		return transactionQueue.getSize();
	}
	public Money getDailyBudget() {
		return dailyBudget;
	}
	
	public List<String> getCategoryNames() {
		return datasource.getCategoryNames();
	}
	
	public List<Double> getAutocompleteValues() {
		return datasource.getAutocompleteValues();
	}
	
	public List<Double> getAutocompleteValues(String category) {
		return datasource.getAutocompleteValues(category);
	}
	
	public void setDailyBudget(Money budget) {
		dailyBudget = budget;
		config.writeValue(BudgetConfig.Fields.dailyBudget, dailyBudget.get());
		config.saveToFile();
		stateChanged = true;
		notifyObservers();
	}
	
	public boolean addCategory(String category)
	{
		if(!category.equalsIgnoreCase(""))
		{
			boolean result = datasource.addCategory(category);
			if(result == true)
			{
				stateChanged = true;
				notifyObservers();
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}
	
	public boolean removeCategory(String category)
	{
		boolean result = datasource.removeCategory(category);
		if(result == true)
		{
			stateChanged = true;
			notifyObservers();
			return true;
		}
		else
			return false;
	}
	
	public boolean addInstallment(Installment installment)
	{
		boolean result = datasource.createInstallment(installment);
		
		if(result == true)
		{
			stateChanged = true;
			notifyObservers();
			return true;
		}
		else
			return false;
	}
	
	public void editInstallment(long id, Installment newInstallment)
	{
		datasource.editInstallment(id, newInstallment);
		stateChanged = true;
		notifyObservers();
		
	}
	
	public boolean removeInstallment(long id)
	{
		boolean result = datasource.markInstallmentAsPaid(id);
		if(result == true)
		{
			stateChanged = true;
			notifyObservers();
			return true;
		}
		else
			return false;
	}
	
	public List<Installment> getInstallments()
	{
		return datasource.getInstallments();
	}
	
	public Installment getInstallment(long id)
	{
		return datasource.getInstallment(id);
	}
	
	public BudgetEntry getTransaction(long id)
	{
		return datasource.getTransaction(id);
	}
	
	public List<BudgetEntry> getNegativeTransactions()
	{
		return datasource.getNegativeTransactions();
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
	
	/**
	 * Looks for last day with entries and adds the daily budget up to current date.
	 * Returns number of daily budgets was added
	 * @return - The number of times the daily budget was added
	 */
	public int addDailyBudget()
    {
    	List<DayEntry> lastDay = datasource.getSomeDays(1,BudgetDataSource.DESCENDING);
    	
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		
    	int daysAdded = 0;
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
	    	
	    	nextDay.set(BudgetFunctions.getYear(), BudgetFunctions.getMonth(), BudgetFunctions.getDay(), 0,0);
	    	nextDay.add(Calendar.DAY_OF_MONTH,1);
	    	
	    	//System.out.println("Next day: " + dateFormat.format(nextDay.getTime()));
	    	Calendar tempDate = (Calendar)lastDayCalendar.clone();
	    	
	    	while(tempDate.before(nextDay))
	    	{
	    		if(!compareFormat.format(tempDate.getTime()).equalsIgnoreCase(compareFormat.format(nextDay.getTime())))
	    		{
	    			//System.out.println("Day to add: " + dateFormat.format(tempDate.getTime()));
	    			BudgetEntry entry = new BudgetEntry(new Money(dailyBudget), dateFormat.format(tempDate.getTime()),"Income");
		        	transactionQueue.queueItem(new TransactionCommand(datasource, entry));
		        	System.out.println("Queueing daily budget");
	    			//datasource.createTransactionEntry(entry);
		    		//stateChanged = true;
		    		daysAdded++;
	    		}
	    		tempDate.add(Calendar.DAY_OF_MONTH,1);	
	    	}

    	}
    	else // Add a transaction of 0
    	{
    		Calendar tempDate = Calendar.getInstance();
    		BudgetEntry entry = new BudgetEntry(MoneyFactory.createMoney(), dateFormat.format(tempDate.getTime()),"Income");
    		System.out.println("queuing daily budget");
    		transactionQueue.queueItem(new TransactionCommand(datasource, entry));
			
    		//datasource.createTransactionEntry(new BudgetEntry(new Money(), dateFormat.format(tempDate.getTime()),"Income"));
    		//stateChanged = true;
    		daysAdded = 1;
    	}
    	//notifyObservers();
    	return daysAdded;
    }
	
	public void pauseInstallment(long id) {
		datasource.markInstallmentAsPaused(id, true);
	}
	public void unpauseInstallment(long id) {
		datasource.markInstallmentAsPaused(id, false);
	}
	public Money payOffInstallments()
	{
		List<Installment> installments = datasource.getInstallments();
		if(installments.isEmpty())
			return MoneyFactory.createMoney();
		
    	
		Money moneyPaid = MoneyFactory.createMoney();
		SimpleDateFormat compareFormat = new SimpleDateFormat("yyyy/MM/dd");
		for(int i = 0; i < installments.size(); i++)
		{
	    	String lastDayString = installments.get(i).getDateLastPaid();
	    	Calendar lastDayCalendar = Calendar.getInstance();
	    	// Convert the string to a Calendar time. Subtract 1 from month because month 0 = January
	    	// Set HH:mm to 00:00
	    	lastDayCalendar.set(Integer.parseInt(lastDayString.substring(0, 4)),Integer.parseInt(lastDayString.substring(5, 7))-1,Integer.parseInt(lastDayString.substring(8, 10)),0,0);
	    	
	    	//System.out.println("Last day: " + dateFormat.format(lastDayCalendar.getTime()));
	    	lastDayCalendar.add(Calendar.DAY_OF_MONTH, 1); // We want to start counting from the first day without transactions

	    	// Step up to the day before tomorrow
	    	Calendar nextDay = Calendar.getInstance();
	    	nextDay.set(BudgetFunctions.getYear(), BudgetFunctions.getMonth(),BudgetFunctions.getDay(),0,0);
	    	nextDay.add(Calendar.DAY_OF_MONTH,1);
	    	
	    	//System.out.println("Next day: " + dateFormat.format(nextDay.getTime()));
	    	Calendar tempDate = (Calendar)lastDayCalendar.clone();
	    	
	    	while(tempDate.before(nextDay))
	    	{
	    		String tempDateString = compareFormat.format(tempDate.getTime());
	    		if(!tempDateString.equalsIgnoreCase(compareFormat.format(nextDay.getTime())))
	    		{
	    			System.out.println("ququeing installment");
	    			transactionQueue.queueItem(new PayOffInstallmentCommand(datasource, installments.get(i), tempDateString));
	    			
	    			//moneyPaid = moneyPaid.add(datasource.payOffInstallment(installments.get(i), tempDateString));
	    			
		    		//stateChanged = true;
	    		}
	    		tempDate.add(Calendar.DAY_OF_MONTH,1);	
	    	}
		}
		//notifyObservers();
		return moneyPaid;
	}
	public List<CategoryEntry> getCategoriesSortedByNum() {
		return datasource.getCategoriesSortedByNum();
	}
	public List<CategoryEntry> getCategoriesSortedByValue() {
		return datasource.getCategoriesSortedByValue();
	}
	
	public void saveConfig()
	{
		config.writeValue(BudgetConfig.Fields.currency,Money.getCurrency());
		config.writeValue(BudgetConfig.Fields.printCurrencyAfter, Money.after);
		config.writeValue(BudgetConfig.Fields.exchangeRate, Money.getExchangeRate());
		config.saveToFile();
	}
	
	public boolean saveBackup(String filename) throws Exception
	{
		ArrayList<BudgetEntry> entries = (ArrayList<BudgetEntry>) getSomeTransactions(0,"desc");
		if(backup.writeBackupFile(entries, filename))
			return true;
		else
			return false;
		
	}
	
	/**
	 * Reads a backup file and merges it with the current database if successful
	 * @param filename - Name of file to read
	 */
	public boolean readAndMergeBackup(String filename) throws Exception
	{
		ArrayList<BudgetEntry> backupList = backup.readBackupFile(filename);
		
		if(backupList.isEmpty() || backupList == null)
		{
			return false;
		}
		// Save current entries
		List<BudgetEntry> currentList = new ArrayList<BudgetEntry>();
		currentList = datasource.getAllTransactions("asc");
		
		// Merge the two lists, sorting them 
		currentList.addAll(backupList);
		// Sort the list
		for(int i = 0; i < currentList.size() - 1; i++)
		{
			for(int j = i+1; j < currentList.size();j++)
			{
				if(currentList.get(i).getDate().compareTo(currentList.get(j).getDate())>0)
				{	
					BudgetEntry temp = new BudgetEntry(currentList.get(i));
					currentList.set(i, new BudgetEntry(currentList.get(j)));
					currentList.set(j, new BudgetEntry(temp));
				}
			}
		}
		// Reset all the transaction tables
		datasource.resetTransactionTables();
		// Add all transactions again
		for(int i = 0; i < currentList.size(); i++)
		{
			datasource.createTransactionEntry(currentList.get(i));
		}
		
		return true;
		
	}
	
	public void clearDatabaseInstance()
	{
		datasource.clearDatabaseInstance();
	}
	
	public void clearAutocompleteValues() {
		datasource.clearAutocompleteValues();
	}
	
	public void addObserver(IBudgetObserver observer) {
		observers.add(observer);
	}
	
	public void notifyObservers() {
		if(stateChanged)
		{
			for(int i = 0; i < observers.size(); i++)
			{
				observers.get(i).update();
			}
			stateChanged = false;
		}
		
	}

}
