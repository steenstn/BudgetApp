package budgetapp.util.database;

import java.util.ArrayList;
import java.util.List;

import budgetapp.util.BudgetEntry;
import budgetapp.util.CategoryEntry;
import budgetapp.util.DayEntry;
import budgetapp.util.Money;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/***
 * DatabaseAccess. The class responsible for communicating with the database. Used by DatabaseSource
 * 
 * @author Steen
 *
 */
public class DatabaseAccess {

	private SQLiteDatabase database;
	private String[] allColumnsTransactions = {BudgetDatabase.COLUMN_ID,
			BudgetDatabase.COLUMN_VALUE, BudgetDatabase.COLUMN_DATE, BudgetDatabase.COLUMN_CATEGORY,BudgetDatabase.COLUMN_FLAGS};
	private String[] allColumnsCategories = {BudgetDatabase.COLUMN_ID,BudgetDatabase.COLUMN_CATEGORY,BudgetDatabase.COLUMN_NUM,BudgetDatabase.COLUMN_VALUE,BudgetDatabase.COLUMN_FLAGS};
	
	public static final String ASCENDING = "asc";
	public static final String DESCENDING = "desc";
	
	
	public DatabaseAccess(SQLiteDatabase theDatabase)
	{
		database = theDatabase;
		
	}
	
	public void resetTransactionTables()
	{
		database.execSQL("drop table " + BudgetDatabase.TABLE_CASHFLOW);
		database.execSQL("drop table " + BudgetDatabase.TABLE_CATEGORIES);
		database.execSQL("drop table " + BudgetDatabase.TABLE_DAYSUM);
		database.execSQL("drop table " + BudgetDatabase.TABLE_DAYTOTAL);
		
		database.execSQL(BudgetDatabase.DATABASE_CREATE_TABLE_CASHFLOW);
		database.execSQL(BudgetDatabase.DATABASE_CREATE_TABLE_CATEGORIES);
		database.execSQL(BudgetDatabase.DATABASE_CREATE_TABLE_DAYSUM);
		database.execSQL(BudgetDatabase.DATABASE_CREATE_TABLE_DAYTOTAL);
	}
	/**
	 * Adds a category to the category table in the database
	 * @param theCategory - Name of the category to add
	 * @return - If the insert was successful
	 */
	public boolean addCategory(String theCategory)
	{
		ContentValues values = new ContentValues();
		String fixedCategory = theCategory.replaceAll("['\"]", "\'");
		values.put(BudgetDatabase.COLUMN_CATEGORY, fixedCategory);
		long insertID = database.insert(BudgetDatabase.TABLE_CATEGORY_NAMES, null,values);
		
		if(insertID==-1)
			return false;
		else
			return true;
	}
	
	/**
	 * Removes a category from the category table in the database
	 * @param theCategory - Name of the category to delete
	 * @return - If the deletion was successful
	 */
	public boolean removeCategory(String theCategory)
	{
		return database.delete(BudgetDatabase.TABLE_CATEGORY_NAMES, BudgetDatabase.COLUMN_CATEGORY + " = " + "\""+theCategory+"\"", null) > 0;
	} 
	
	/**
	 * Adds a transaction entry to the cash flow table in the dataase
	 * @param theEntry - The BudgetEntry to add
	 * @return - The added BudgetEntry, will have an id from the table
	 */
	public BudgetEntry addEntry(BudgetEntry theEntry)
	{
		ContentValues values = new ContentValues();
		// Put in the values
		values.put(BudgetDatabase.COLUMN_VALUE, theEntry.getValue().get());
		values.put(BudgetDatabase.COLUMN_DATE,theEntry.getDate());
		values.put(BudgetDatabase.COLUMN_CATEGORY, theEntry.getCategory().replaceAll("['\"]", "\'"));
		values.put(BudgetDatabase.COLUMN_COMMENT, theEntry.getComment().replaceAll("['\"]", "\'"));
		
		long insertId = database.insert(BudgetDatabase.TABLE_CASHFLOW, null,values);
		
		Cursor cursor = database.query(BudgetDatabase.TABLE_CASHFLOW,
				allColumnsTransactions,BudgetDatabase.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		
		BudgetEntry entry = new BudgetEntry(cursor.getLong(0),new Money(cursor.getDouble(1)),cursor.getString(2),cursor.getString(3),cursor.getInt(4));
		cursor.close();
		return entry;
	}
	
	/**
	 * Gets a BudgetEntry from the cash flow table
	 * @param theId - Id of the entry to get
	 * @return - The resulting BudgetEntry or a new BudgetEntry() if not found
	 */
	public BudgetEntry getEntry(long theId)
	{
		Cursor cursor;
		cursor = database.query(BudgetDatabase.TABLE_CASHFLOW, null, BudgetDatabase.COLUMN_ID + " = " + theId, null, null, null, null);
		if(cursor.getCount()==1)
		{
			cursor.moveToFirst();
			BudgetEntry entry = new BudgetEntry(cursor.getLong(0),new Money(cursor.getDouble(1)),cursor.getString(2),cursor.getString(3),cursor.getInt(4));
			return entry;
		}
		else
			return new BudgetEntry();
	}
	
	/**
	 * Removes an entry from the cash flow table
	 * @param theEntry - The entry to delete
	 * @return - If the deletion was successful
	 */
	public boolean removeEntry(BudgetEntry theEntry)
	{
		int res = database.delete(BudgetDatabase.TABLE_CASHFLOW, BudgetDatabase.COLUMN_ID + " = " + theEntry.getId(), null);
		
		if(res!=0)
		{
			return true;
		}
			return false;
	}
	
	/**
	 * Updates the daysum table
	 * @param theEntry - The entry to add
	 * @param newEntry - Possible other entry. This means that an entry was edited
	 * @return - If the adding was successful
	 */
	public boolean updateDaySum(BudgetEntry theEntry, BudgetEntry newEntry)
	{
		//COLUMN_DATE
		//COLUMN_VALUE
		Cursor cursor;
		cursor = database.rawQuery("select "+BudgetDatabase.COLUMN_VALUE+" from "+BudgetDatabase.TABLE_DAYSUM+" where "+BudgetDatabase.COLUMN_DATE+"="+"\""+theEntry.getDate().substring(0,10)+"\"",null);
		
		// If two entries was sent in, the new value should be newValue - oldValue to get the correct result
		double newValue = theEntry.getValue().get();
		if(newEntry != null)
		{
			newValue = newEntry.getValue().get()-theEntry.getValue().get();
		}
		if(cursor.getCount()<=0) // No entry yet this day, create a new entry
		{
			ContentValues values = new ContentValues();
			
			// Put in the values
			values.put(BudgetDatabase.COLUMN_DATE,theEntry.getDate().substring(0, 10)); // Don't use the hours and minutes in the daysum
			values.put(BudgetDatabase.COLUMN_VALUE, newValue);
			
			database.insert(BudgetDatabase.TABLE_DAYSUM, null,values);
			cursor.close();
			return true;
		}
		// There exists an entry for this day, update it.
		cursor.moveToFirst();
		double total = cursor.getDouble(0);
		total += newValue;
		ContentValues values = new ContentValues();
		values.put(BudgetDatabase.COLUMN_VALUE, total);
		
		database.update(BudgetDatabase.TABLE_DAYSUM, values, BudgetDatabase.COLUMN_DATE + " = \"" + theEntry.getDate().substring(0, 10) + "\"", null);
		cursor.close();
		return true;
	}
	
	/**
	 * Updates the daytotal table, goes through all daytotal entries when updating
	 * @param theEntry - The entry to add
	 * @param newEntry - Possible second entry. This means that an entry was edited
	 * @return - If the updating was successful
	 */
	public boolean updateDayTotal(BudgetEntry theEntry, BudgetEntry newEntry)
	{
		Cursor cursor;
		cursor = database.rawQuery("select "+BudgetDatabase.COLUMN_VALUE+", " + BudgetDatabase.COLUMN_ID + " from "+BudgetDatabase.TABLE_DAYTOTAL+" where "+BudgetDatabase.COLUMN_DATE+"="+"\""+theEntry.getDate().substring(0,10)+"\"",null);
		double newValue = theEntry.getValue().get();
		if(cursor.getCount()<=0) // No entry for today, search for last entry
		{		
			ContentValues values = new ContentValues();
			Cursor yesterdayCursor = database.rawQuery("select " + BudgetDatabase.COLUMN_VALUE + " from " + BudgetDatabase.TABLE_DAYTOTAL+ " order by _id desc limit 1", null);
			if(yesterdayCursor.getCount()<=0) // No yesterday, create new entry from incoming entry
			{
				
				values.put(BudgetDatabase.COLUMN_DATE,theEntry.getDate().substring(0, 10)); // Don't use the hours and minutes in the daysum
				values.put(BudgetDatabase.COLUMN_VALUE, newValue);
				database.insert(BudgetDatabase.TABLE_DAYTOTAL, null,values);
				cursor.close();
			}
			else // Yesterday exists, create new entry with yesterdays total + new total
			{
				yesterdayCursor.moveToFirst();
				double yesterdayValue = yesterdayCursor.getDouble(0);
				values.put(BudgetDatabase.COLUMN_DATE,theEntry.getDate().substring(0, 10)); // Don't use the hours and minutes in the daysum
				values.put(BudgetDatabase.COLUMN_VALUE, newValue+yesterdayValue);
				database.insert(BudgetDatabase.TABLE_DAYTOTAL, null,values);
				cursor.close();
			}
			return true;
		}
		else // There is an entry for today, add to it
		{
			cursor.moveToFirst();
			double total = cursor.getDouble(0);
			long theId = cursor.getLong(1);
			
			// If two entries was sent in, the new value should be newValue - oldValue to get the correct result
			if(newEntry != null)
			{
				newValue = newEntry.getValue().get()-theEntry.getValue().get();
			}
			total += newValue;
			ContentValues values = new ContentValues();
			values.put(BudgetDatabase.COLUMN_VALUE, total);
			database.update(BudgetDatabase.TABLE_DAYTOTAL, values, BudgetDatabase.COLUMN_DATE + " = '" + theEntry.getDate().substring(0, 10) + "'", null);
			cursor.close();
			addValueToRemaindingDays(newValue, theId);
			
			return true;
		}
		
	}
	/**
	 * Adds a value to all entries in table daytotal that have an id > theId
	 * @param theValue - The value to add
	 * @param theId - The id
	 */
	private void addValueToRemaindingDays(double theValue, long theId)
	{
		database.execSQL("update " +BudgetDatabase.TABLE_DAYTOTAL + " set " + BudgetDatabase.COLUMN_VALUE + " = " + BudgetDatabase.COLUMN_VALUE + " + " + theValue + " where _id > " + theId);
	}
	
	/**
	 * Updates a transaction entry in the cash flow table
	 * @param oldEntry - The entry to edit
	 * @param newEntry - Entry containging the new values
	 * @return - If te editing was successful
	 */
	public boolean updateTransaction(BudgetEntry oldEntry, BudgetEntry newEntry)
	{
		Cursor cursor;
		cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_CASHFLOW + " where _id = " + oldEntry.getId(), null);
		
		if(cursor.getCount()==1)
		{
			cursor.moveToFirst();
			ContentValues values = new ContentValues();
			values.put(BudgetDatabase.COLUMN_CATEGORY, newEntry.getCategory());
			values.put(BudgetDatabase.COLUMN_VALUE, newEntry.getValue().get());
			values.put(BudgetDatabase.COLUMN_COMMENT, newEntry.getComment());
			int res = database.update(BudgetDatabase.TABLE_CASHFLOW, values, BudgetDatabase.COLUMN_ID + " = " + oldEntry.getId(), null);
			
			if(res!=0)
			{
				cursor.close();
				return true;
			}
		}
		cursor.close();
		return false;
	}
	
	
	/**
	 * Add a value to a category in the category table
	 * @param theCategory - The category to add to
	 * @param value - The value to add
	 */
	public void addToCategory(String theCategory,double value)
	{
		
		Cursor cursor;
		cursor = database.rawQuery("select "+BudgetDatabase.COLUMN_NUM+","+BudgetDatabase.COLUMN_VALUE+" from "+BudgetDatabase.TABLE_CATEGORIES+" where "+BudgetDatabase.COLUMN_CATEGORY+"="+"\""+theCategory+"\"",null);
		
		if(cursor.getCount()!=0) // The category already has transactions
		{
			cursor.moveToFirst();
			int num = cursor.getInt(0)+1; // Number of transactions of this category 
			double newTotal = cursor.getDouble(1)+value;
			ContentValues values = new ContentValues();
			values.put(BudgetDatabase.COLUMN_NUM,num);
			values.put(BudgetDatabase.COLUMN_VALUE,newTotal);
			database.update(BudgetDatabase.TABLE_CATEGORIES, values, BudgetDatabase.COLUMN_CATEGORY+" = '"+theCategory+"'", null);
			
		}
		else // Insert a new row
		{
			ContentValues values = new ContentValues();
			values.put(BudgetDatabase.COLUMN_CATEGORY, theCategory);
			values.put(BudgetDatabase.COLUMN_NUM,1);
			values.put(BudgetDatabase.COLUMN_VALUE,value);
			
			database.insert(BudgetDatabase.TABLE_CATEGORIES, null,values);
			
		}
		cursor.close();
	}
	
	/**
	 *  Remove a value from the category table, also decreases the number of transactions by 1
	 * @param theCategory - The category to remove from
	 * @param value - The value to remove
	 */
	public void removeFromCategory(String theCategory,double value)
	{
		Cursor cursor;
		cursor = database.rawQuery("select "+BudgetDatabase.COLUMN_NUM+","+BudgetDatabase.COLUMN_VALUE+" from "+BudgetDatabase.TABLE_CATEGORIES+" where "+BudgetDatabase.COLUMN_CATEGORY+"="+"\""+theCategory+"\"",null);
		if(cursor.getCount()!=0)
		{
			cursor.moveToFirst();
			int num = cursor.getInt(0)-1; // Number of transactions of this category 
			double newTotal = cursor.getDouble(1)+value;
			
			if(num==0 && newTotal==0) // No transactions left here, remove
			{
				database.delete(BudgetDatabase.TABLE_CATEGORIES, BudgetDatabase.COLUMN_CATEGORY + " = \"" + theCategory + "\"", null);
			}
			else // Update the transaction entry
			{
				ContentValues values = new ContentValues();
				values.put(BudgetDatabase.COLUMN_NUM,num);
				values.put(BudgetDatabase.COLUMN_VALUE,newTotal);
				database.update(BudgetDatabase.TABLE_CATEGORIES, values, BudgetDatabase.COLUMN_CATEGORY+" = '"+theCategory+"'", null);
			}
			
		}
		cursor.close();
			
	}
	/**
	 * Add a CategoryEntry to the database.
	 * @param theEntry - The CategoryEntry to add
	 * @return - The resulting CategoryEntry with an id added
	 */
	public CategoryEntry addEntry(CategoryEntry theEntry)
	{
		ContentValues values = new ContentValues();
		values.put(BudgetDatabase.COLUMN_CATEGORY,theEntry.getCategory());
		long insertId = database.insert(BudgetDatabase.TABLE_CATEGORIES, null, values);
		
		Cursor cursor = database.query(BudgetDatabase.TABLE_CATEGORIES,
				allColumnsCategories,BudgetDatabase.COLUMN_ID + " = " + insertId,null,
				null,null,null);
		cursor.moveToFirst();
		CategoryEntry entry = new CategoryEntry(cursor.getLong(0),cursor.getString(1));
		cursor.close();
		return entry;
	}
	
	/**
	 *  Get n number of transactions. If n = 0, returns all transactions
	 * @param n - Number of transactions
	 * @param mode Ascending/descending by id
	 * @return n number of transactions
	 */
	public List<BudgetEntry> getTransactions(int n,String mode)
	{
		List<BudgetEntry> entries = new ArrayList<BudgetEntry>();
		Cursor cursor;
		if(n<=0) // Get all entries
			cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_CASHFLOW + " order by _id " + mode,null);
		else 
			cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_CASHFLOW + " order by _id " + mode + " limit 0,"+n,null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			
			BudgetEntry entry =  new BudgetEntry(cursor.getLong(0),new Money(cursor.getDouble(1)),cursor.getString(2),cursor.getString(3),cursor.getInt(4),cursor.getString(5));
			entries.add(entry);
			cursor.moveToNext();
		}
		cursor.close();
		return entries;
	}
	
	
	public List<DayEntry> getDaySum(int n,String mode)
	{
		List<DayEntry> entries = new ArrayList<DayEntry>();
	
		Cursor cursor;
		if(n<=0) // Get all entries
			cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_DAYSUM + " order by _id "+mode,null);
		else 
			cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_DAYSUM + " order by _id " + mode + " limit 0,"+n,null);//database.query(false, BudgetDatabase.TABLE_CASHFLOW, allColumnsTransactions, null, null, null, null, "desc", ""+n, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			DayEntry entry =  new DayEntry(cursor.getLong(0),cursor.getString(1),new Money(cursor.getDouble(2)),cursor.getInt(3));
			entries.add(entry);
			cursor.moveToNext();
		}
		cursor.close();
		return entries;
	}
	
	public List<DayEntry> getDayTotal(int n, String mode)
	{
		List<DayEntry> entries = new ArrayList<DayEntry>();
		
		Cursor cursor;
		if(n<=0)
			cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_DAYTOTAL + " order by _id " + mode,null);
		else
			cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_DAYTOTAL + " order by _id " + mode + " limit 0,"+n,null);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			DayEntry entry =  new DayEntry(cursor.getLong(0),cursor.getString(1),new Money(cursor.getDouble(2)),cursor.getInt(3));
			entries.add(entry);
			cursor.moveToNext();
		}
		cursor.close();
		return entries;
	}
	
	// Gets all the categories in the database
	public List<CategoryEntry> getCategories(String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
	{
		List<CategoryEntry> entries = new ArrayList<CategoryEntry>();
		
		Cursor cursor;
		cursor = database.query(BudgetDatabase.TABLE_CATEGORIES,allColumnsCategories,selection, selectionArgs, groupBy, having, orderBy);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			entries.add(new CategoryEntry(cursor.getLong(0),cursor.getString(1),cursor.getInt(2),new Money(cursor.getDouble(3)),cursor.getInt(4)));
			cursor.moveToNext();
		}
		cursor.close();
		return entries;
	}
	public List<String> getCategoryNames()
	{
		List<String> entries = new ArrayList<String>();
		
		Cursor cursor;
		cursor = database.rawQuery("select " + BudgetDatabase.COLUMN_CATEGORY + " from " + BudgetDatabase.TABLE_CATEGORY_NAMES,null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			entries.add(cursor.getString(0));
			cursor.moveToNext();
		}
		cursor.close();
		return entries;
	}
}
