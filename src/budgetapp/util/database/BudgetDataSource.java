package budgetapp.util.database;
/**
 * The class used to access the database. It itself uses the DatabaseAccess class to
 * query the database. All transactions that go into the database are multiplied 
 * with the exchange rate
 * 
 * @author Steen
 * 
 */

import java.util.List;

import budgetapp.util.BudgetEntry;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.CategoryEntry;
import budgetapp.util.DayEntry;
import budgetapp.util.Installment;
import budgetapp.util.Money;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BudgetDataSource {

	
	private static SQLiteDatabase database;
	private static BudgetDatabase dbHelper;
	private static DatabaseAccess dbAccess;
	public static final String ASCENDING = "asc";
	public static final String DESCENDING = "desc";
	
	
	public BudgetDataSource(Context context)
	{
		dbHelper = BudgetDatabase.getInstance(context);
		open();
	}
	
	public void clearDatabaseInstance()
	{
		BudgetDatabase.clearInstance();
	}
	private void open() throws SQLException
	{
		database = dbHelper.getWritableDatabase();
		dbAccess = new DatabaseAccess(database);
	}
	
	
	/**
	 * Creates a transaction entry and updates the affected tables. 
	 * Multiplies value with exchange rate.
	 * @param theEntry The entry to add
	 * @return The entry that was added
	 */
	public BudgetEntry createTransactionEntry(BudgetEntry theEntry)
	{
		BudgetEntry workingEntry = theEntry.clone();
		// Add the exchange rate to the entry
		workingEntry.setValue(workingEntry.getValue());
		
		BudgetEntry result = dbAccess.addEntry(workingEntry);
		if(result != null)
		{
			addToAutocompleteValues(result.getValue().get());
			
			addToCategory(workingEntry.getCategory(),workingEntry.getValue().get());
	    	addToDaySum(workingEntry);
	    	addToDayTotal(workingEntry);
		}
		return result;
		
	}
	
	/**
	 * Removes a transaction entry from the database and updates the affected tables
	 * @param theEntry The entry to remove
	 * @return true if the removal was successful
	 */
	public boolean removeTransactionEntry(BudgetEntry theEntry)
	{
		//open();
		// Get the entry from the database, it may have been edited
		BudgetEntry workingEntry = dbAccess.getEntry(theEntry.getId());
		boolean result = dbAccess.removeEntry(theEntry);
		
		if(result == true)
		{
			removeFromCategory(workingEntry.getCategory(),workingEntry.getValue().get()*-1);
			//Update daysum and daytotal by adding the negative value that was added
			workingEntry.setValue(workingEntry.getValue().makeNegative());
			addToDaySum(workingEntry);
			addToDayTotal(workingEntry);
			workingEntry.setValue(workingEntry.getValue().makePositive());
		}
		return result;
	}
	
	/** 
	 * Changes the fields that are changeable of the transaction entry.
	 * Multiplies with exchange rate.
	 * @param oldEntry - The entry to change
	 * @param newEntry - Entry containing the new values
	 */
	public void editTransactionEntry(BudgetEntry oldEntry, BudgetEntry newEntry)
	{
		BudgetEntry workingEntry = newEntry.clone();
		
		updateTransaction(oldEntry, workingEntry);
		
		// New category, move the entry from old category to new
		if(!oldEntry.getCategory().equalsIgnoreCase(workingEntry.getCategory()))
		{
			removeFromCategory(oldEntry.getCategory(),oldEntry.getValue().get()*-1);
			addToCategory(workingEntry.getCategory(),oldEntry.getValue().get());
		}
		
		if(oldEntry.getValue().get()!=workingEntry.getValue().get())
		{
			updateDaySum(oldEntry,workingEntry);
			updateDayTotal(oldEntry,workingEntry);
		}
	}
	
	/**
	 * Edit transaction and add the value to today's daily flow
	 * @param oldEntry
	 * @param newEntry
	 */
	public void editTransactionEntryToday(BudgetEntry oldEntry, BudgetEntry newEntry)
	{
		BudgetEntry workingEntry = newEntry.clone();
		// Add the exchange rate to the entry
		workingEntry.setValue(workingEntry.getValue());
		//open();
		updateTransaction(oldEntry, workingEntry);
		
		// New category, move the entry from old category to new
		if(!oldEntry.getCategory().equalsIgnoreCase(workingEntry.getCategory()))
		{
			removeFromCategory(oldEntry.getCategory(),oldEntry.getValue().get()*-1);
			addToCategory(workingEntry.getCategory(),oldEntry.getValue().get());
		}
		
		if(oldEntry.getValue().get()!=workingEntry.getValue().get())
		{
			updateDayTotal(oldEntry,workingEntry);
			
			BudgetEntry oldEntryClone = oldEntry.clone();
			oldEntryClone.setDate(BudgetFunctions.getDateString());
			updateDaySum(oldEntryClone,workingEntry);
		}
	}
	
	/**
	 * Creates and returns a new CategoryEntry
	 * @param theEntry - The CategoryEntry to add
	 * @return The created CategoryEntry
	 */
	public CategoryEntry createCategoryEntry(CategoryEntry theEntry)
	{
		//open();
		CategoryEntry result;
		result = dbAccess.addEntry(theEntry);
		
		return result;
	}
	
	/**
	 * Creates a transaction in the database, an installment and links them together
	 * @param installment
	 * @return
	 */
	public boolean createInstallment(Installment installment)
	{
		Money dailyPayment = installment.getDailyPayment();
		
		String dateLastPaid = installment.getDateLastPaid();
		String category = installment.getCategory();
		String comment = installment.getComment();
		BudgetEntry initialPayment = new BudgetEntry(dailyPayment, dateLastPaid, category, comment);
		
		initialPayment = createTransactionEntry(initialPayment);
		
		installment.setTransactionId(initialPayment.getId());
		
		//open();
		long result = dbAccess.addInstallment(installment);
		
		
		if(result != -1)
			return true;
		else
			return false;
		
	}
	
	/**
	 * Do a payment on an installment
	 * @param installment - The installment to pay off
	 */
	public Money payOffInstallment(Installment installment)
	{
		BudgetEntry oldEntry = getTransaction(installment.getTransactionId());
		
		BudgetEntry newEntry = oldEntry.clone();
		Money dailyPay = installment.getDailyPayment();
		
		Money remainingValue = installment.getRemainingValue();
		
		if(remainingValue.makePositive().smallerThan(dailyPay)) // Don't pay too much
			dailyPay = remainingValue;
		
		newEntry.setValue(new Money(oldEntry.getValue().add(new Money(dailyPay))));
		editTransactionEntryToday(oldEntry, newEntry);
		
		Installment newInstallment = getInstallment(installment.getId());
		
		Money newRemainingValue = newInstallment.getRemainingValue();
		// If the installment has gone positive or is small enough, delete it
		if(newRemainingValue.biggerThanOrEquals(new Money(0)) || newRemainingValue.makePositive().almostZero())
		{
			dbAccess.removeInstallment(installment.getId());
		}
		else
		{
			updateInstallment(installment.getId(), installment.getTotalValue().get(), installment.getDailyPayment().get(), BudgetFunctions.getDateString());

		}
		return new Money(dailyPay);
	}
	
	/**
	 * Gets all transactions in the database ordered by id
	 * @param orderBy - BudgetDatbase.ASCENDING/BudgetDatabase.DESCENDING
	 * @return An ArrayList of all entries
	 */
	public List<BudgetEntry> getAllTransactions(String orderBy)
	{
		List<BudgetEntry> result;
		result = dbAccess.getTransactions(0,orderBy);
		return result;
	}
	
	/**
	 * Gets n number of DayEntries from daily cash flow table sorted by id
	 * @param n - Number of transactions to fetch. Fetches all if n <= 0
	 * @param orderBy - BudgetDatabase.ASCENDING/BudgetDatabase.DESCENDING
	 * @return An ArrayList of the entries
	 */
	public List<DayEntry> getSomeDays(int n,String orderBy)
	{
		List<DayEntry> result;
		result = dbAccess.getDaySum(n, orderBy);
		return result;
	}
	
	/**
	 * Gets n number of DayEntries from day total table sorted by id
	 * @param n - Number of transactions to get. Gets all if n <= 0
	 * @param orderBy - BudgetDatabase.ASCENDING/BudgetDatabase.DESCENDING
	 * @return An ArrayList of the entries
	 */
	public List<DayEntry> getSomeDaysTotal(int n,String orderBy)
	{
		List<DayEntry> result;
		result = dbAccess.getDayTotal(n, orderBy);
		return result;
	}
	
	/**
	 * Gets n number of transactions. Returns all transactions if n <= 0
	 * @param n - Number of entries to get
	 * @param orderBy - BudgetDatabase.ASCENDING/BudgetDatabase.DESCENDING
	 * @return An ArrayList of the entries
	 */
	public List<BudgetEntry> getSomeTransactions(int n, String orderBy)
	{
		List<BudgetEntry> result;
		//open();
		result = dbAccess.getTransactions(n,orderBy);
		
		return result;
	}
	
	/**
	 * Gets all categories, sorted by total value
	 * @return ArrayList containging all CategoryEntries
	 */
	public List<CategoryEntry> getCategoriesSortedByValue()
	{
		List<CategoryEntry> result;
		//open();
		result = dbAccess.getCategories(null, null, null, null, BudgetDatabase.COLUMN_VALUE);
		
		return result;
	}
	
	/**
	 * Gets all categories, sorted by total number of entries in the category
	 * @return ArrayList containging all CategoryEntries
	 */
	public List<CategoryEntry> getCategoriesSortedByNum()
	{
		List<CategoryEntry> result;
		//open();
		result = dbAccess.getCategories(null, null, null, null, BudgetDatabase.COLUMN_NUM);
		
		return result;
	}
	
	/**
	 * Gets all category names
	 * @return ArrayList of the category names
	 */
	public List<String> getCategoryNames()
	{
		List<String> result;
		//open();
		result = dbAccess.getCategoryNames();
		
		return result;
	}
	
	public List<Double> getAutocompleteValues()
	{
		List<Double> result;
		//open();
		result = dbAccess.getAutocompleteValues();
		
		return result;
	}
	
	public List<Installment> getInstallments()
	{
		List<Installment> result;
		//open();
		result = dbAccess.getInstallments();
		
		return result;
	}
	/**
	 * Adds a category
	 * @param theCategory - Name of the new category
	 * @return - Wether or not the adding was successful
	 */
	public boolean addCategory(String theCategory)
	{
		boolean result;
		//open();
		result = dbAccess.addCategory(theCategory);
		
		return result;
	}
	
	/**
	 * Removes a category
	 * @param theCategory - The category to remove
	 * @return - Wether or not the removal was successful
	 */
	public boolean removeCategory(String theCategory)
	{
		boolean result;
		//open();
		result = dbAccess.removeCategory(theCategory);
		
		return result;
	}
	
	public boolean removeInstallment(long id)
	{
		boolean result;
		//open();
		result = dbAccess.removeInstallment(id);
		
		return result;
	}
	
	public void resetTransactionTables()
	{
		//open();
		dbAccess.resetTransactionTables();
		
	}
	
	
	// Helper functions to update different tables correctly
	private void addToAutocompleteValues(double value)
	{
		dbAccess.addAutocompleteValue(value);
	}
	private void addToCategory(String theCategory,double value)
	{
		dbAccess.addToCategory(theCategory,value);
	}
	private void removeFromCategory(String theCategory,double value)
	{
		dbAccess.removeFromCategory(theCategory,value);
	}
	private void updateDaySum(BudgetEntry oldEntry, BudgetEntry newEntry)
	{
		dbAccess.updateDaySum(oldEntry, newEntry);
	}
	private void updateDayTotal(BudgetEntry oldEntry, BudgetEntry newEntry)
	{
		dbAccess.updateDayTotal(oldEntry, newEntry);
	}
	
	private void addToDaySum(BudgetEntry theEntry)
	{
		dbAccess.updateDaySum(theEntry, null);
	}
	private void addToDayTotal(BudgetEntry theEntry)
	{
		dbAccess.updateDayTotal(theEntry, null);
	}
	private void updateTransaction(BudgetEntry oldEntry, BudgetEntry newEntry)
	{
		dbAccess.updateTransaction(oldEntry, newEntry);
	}
	private BudgetEntry getTransaction(long id)
	{
		return dbAccess.getTransaction(id);
	}
	private Installment getInstallment(long id)
	{
		return dbAccess.getInstallment(id);
	}
	private boolean updateInstallment(long id, double newTotalValue, double newDailyPay, String newDateLastPaid)
	{
		return dbAccess.updateInstallment(id, newTotalValue, newDailyPay, newDateLastPaid);
	}
	
}
