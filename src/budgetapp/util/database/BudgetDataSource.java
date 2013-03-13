package budgetapp.util.database;
/**
 * The class used to access the database. It itself uses the DatabaseAccess class to
 * query the database.
 * 
 * @author Steen
 * 
 */

import java.util.List;

import budgetapp.util.BudgetEntry;
import budgetapp.util.CategoryEntry;
import budgetapp.util.DayEntry;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BudgetDataSource {

	
	// Database fields
	private static SQLiteDatabase database;
	private static BudgetDatabase dbHelper;
	private static DatabaseAccess dbAccess;
	public static final String ASCENDING = "asc";
	public static final String DESCENDING = "desc";
	private static BudgetDataSource instance;
	
	
	public static BudgetDataSource instance(Context context)
	{
		if(instance==null)
			instance = new BudgetDataSource(context);
		
		return instance;
		
	}
	
	private BudgetDataSource(Context context)
	{
		dbHelper = new BudgetDatabase(context);
		open();
		close();
	}
	
	private void open() throws SQLException
	{
		database = dbHelper.getWritableDatabase();
		dbAccess = new DatabaseAccess(database);
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	/**
	 * Creates a transaction entry and updates the affected tables
	 * @param theEntry The entry to add
	 * @return The entry that was added
	 */
	public BudgetEntry createTransactionEntry(BudgetEntry theEntry)
	{
		open();
		BudgetEntry result = dbAccess.addEntry(theEntry);
		if(result != null)
		{
			addToCategory(theEntry.getCategory(),theEntry.getValue().get());
	    	addToDaySum(theEntry);
	    	addToDayTotal(theEntry);
		}
		close();
		return result;
		
	}
	/**
	 * Removes a transaction entry from the database and updates the affected tables
	 * @param theEntry The entry to remove
	 * @return true if the removal was successful
	 */
	public boolean removeTransactionEntry(BudgetEntry theEntry)
	{
		open();
		boolean result = dbAccess.removeEntry(theEntry);
		if(result == true)
		{
			removeFromCategory(theEntry.getCategory(),theEntry.getValue().get()*-1);
			//Update daysum and daytotal by adding the negative value that was added
			theEntry.setValue(theEntry.getValue().get()*-1);
			addToDaySum(theEntry);
			addToDayTotal(theEntry);
			theEntry.setValue(theEntry.getValue().get()*-1);
		}
		close();
		return result;
	}
	
	// Changes the fields that are changeable of the transaction entry
	public void editTransactionEntry(BudgetEntry oldEntry, BudgetEntry newEntry)
	{
		open();
		updateTransaction(oldEntry, newEntry);
		
		// New category, move the entry from old category to new
		if(!oldEntry.getCategory().equalsIgnoreCase(newEntry.getCategory()))
		{
			removeFromCategory(oldEntry.getCategory(),oldEntry.getValue().get()*-1);
			addToCategory(newEntry.getCategory(),oldEntry.getValue().get());
		}
		
		if(oldEntry.getValue().get()!=newEntry.getValue().get())
		{
			updateDaySum(oldEntry,newEntry);
			updateDayTotal(oldEntry,newEntry);
		}
		close();
	}
	
	public boolean addComment(BudgetEntry theEntry, String comment)
	{
		open();
		boolean result = dbAccess.updateComment(theEntry, comment);
		close();
		return result;
	}
	public CategoryEntry createCategoryEntry(CategoryEntry theEntry)
	{
		open();
		CategoryEntry result;
		result = dbAccess.addEntry(theEntry);
		close();
		return result;
	}
		
	public List<BudgetEntry> getAllTransactions(String orderBy)
	{
		List<BudgetEntry> result;
		open();
		result = dbAccess.getTransactions(0,orderBy);
		close();
		return result;
	}
	
	public List<DayEntry> getAllDays(String orderBy)
	{
		List<DayEntry> result;
		open();
		result = dbAccess.getDaySum(0,orderBy);
		close();
		return result;
	}
	public List<DayEntry> getSomeDays(int n,String orderBy)
	{
		List<DayEntry> result;
		open();
		result = dbAccess.getDaySum(n, orderBy);
		close();
		return result;
	}
	public List<DayEntry> getAllDaysTotal(String orderBy)
	{
		List<DayEntry> result;
		open();
		result = dbAccess.getDayTotal(0,orderBy);
		close();
		return result;
	}
	public List<DayEntry> getSomeDaysTotal(int n,String orderBy)
	{
		List<DayEntry> result;
		open();
		result = dbAccess.getDayTotal(n, orderBy);
		close();
		return result;
	}
	public List<BudgetEntry> getSomeTransactions(int n, String orderBy)
	{
		List<BudgetEntry> result;
		open();
		result = dbAccess.getTransactions(n,orderBy);
		close();
		return result;
	}
	
	// Returns all categories in the category table
	public List<CategoryEntry> getAllCategories(int mode)
	{
		List<CategoryEntry> result;
		open();
		result = dbAccess.getCategories(null, null, null, null, null);
		close();
		return result;
	}
	
	// Returns all categories in the category table sorted by total
	public List<CategoryEntry> getCategoriesSorted()
	{
		List<CategoryEntry> result;
		open();
		result = dbAccess.getCategories(null, null, null, null, BudgetDatabase.COLUMN_VALUE);
		close();
		return result;
	}
	public List<CategoryEntry> getCategoriesSortedByNum()
	{
		List<CategoryEntry> result;
		open();
		result = dbAccess.getCategories(null, null, null, null, BudgetDatabase.COLUMN_NUM);
		close();
		return result;
	}
	public List<String> getCategoryNames()
	{
		List<String> result;
		open();
		result = dbAccess.getCategoryNames();
		close();
		return result;
	}
	public boolean addCategory(String theCategory)
	{
		boolean result;
		open();
		result = dbAccess.addCategory(theCategory);
		close();
		return result;
	}

	public boolean removeCategory(String theCategory)
	{
		boolean result;
		open();
		result = dbAccess.removeCategory(theCategory);
		close();
		return result;
	}
	
	// Helper functions to update different tables correctly
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
	
}
