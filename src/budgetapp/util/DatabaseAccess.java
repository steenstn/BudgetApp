package budgetapp.util;

import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
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
	private String[] allColumnsCategories = {BudgetDatabase.COLUMN_ID,BudgetDatabase.COLUMN_CATEGORY,BudgetDatabase.COLUMN_NUM,BudgetDatabase.COLUMN_TOTAL,BudgetDatabase.COLUMN_FLAGS};
	
	public static final String ASCENDING = "asc";
	public static final String DESCENDING = "desc";
	
	
	public DatabaseAccess(SQLiteDatabase theDatabase)
	{
		database = theDatabase;
	}
	
	
	public boolean addCategory(String theCategory)
	{
		ContentValues values = new ContentValues();
		values.put(BudgetDatabase.COLUMN_CATEGORY, theCategory);
		long insertID = database.insert(BudgetDatabase.TABLE_CATEGORY_NAMES, null,values);
		
		if(insertID==-1)
			return false;
		else
			return true;
	}

	public boolean removeCategory(String theCategory)
	{
		return database.delete(BudgetDatabase.TABLE_CATEGORY_NAMES, BudgetDatabase.COLUMN_CATEGORY + " = " + "'"+theCategory+"'", null) > 0;
	} 
	
	public BudgetEntry addEntry(BudgetEntry theEntry)
	{
		
		ContentValues values = new ContentValues();
		// Put in the values
		values.put(BudgetDatabase.COLUMN_VALUE, theEntry.getValue());
		values.put(BudgetDatabase.COLUMN_DATE,theEntry.getDate());
		values.put(BudgetDatabase.COLUMN_CATEGORY, theEntry.getCategory());
		
		long insertId = database.insert(BudgetDatabase.TABLE_CASHFLOW, null,values);
		
		Cursor cursor = database.query(BudgetDatabase.TABLE_CASHFLOW,
				allColumnsTransactions,BudgetDatabase.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		BudgetEntry entry = new BudgetEntry(cursor.getLong(0),cursor.getInt(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4));
		cursor.close();
		return entry;
	}
	
	public boolean removeEntry(BudgetEntry theEntry)
	{
		System.out.println("Id of entry: "+theEntry.getId());
		int res = database.delete(BudgetDatabase.TABLE_CASHFLOW, BudgetDatabase.COLUMN_ID + " = " + theEntry.getId(), null);
		System.out.println("\nid: "+res);
		if(res!=0)
			return true;
		return false;
	}
	public boolean updateDaySum(BudgetEntry theEntry)
	{
		//COLUMN_DATE
		//COLUMN_TOTAL
		Cursor cursor;
		cursor = database.rawQuery("select "+BudgetDatabase.COLUMN_TOTAL+" from "+BudgetDatabase.TABLE_DAYSUM+" where "+BudgetDatabase.COLUMN_DATE+"="+"'"+theEntry.getDate().substring(0,10)+"'",null);
		//System.out.println("substring: "+ theEntry.getDate().substring(0, 10));
		if(cursor.getCount()<=0) // No entry yet this day, create a new entry
		{
			ContentValues values = new ContentValues();
			// Put in the values
			values.put(BudgetDatabase.COLUMN_DATE,theEntry.getDate().substring(0, 10)); // Don't use the hours and minutes in the daysum
			values.put(BudgetDatabase.COLUMN_TOTAL, theEntry.getValue());
			
			database.insert(BudgetDatabase.TABLE_DAYSUM, null,values);
			cursor.close();
			return true;
		}
		// There exists an entry for this day, update it.
		cursor.moveToFirst();
		long total = cursor.getLong(0);
		total += theEntry.getValue();
		ContentValues values = new ContentValues();
		values.put(BudgetDatabase.COLUMN_TOTAL, total);
		
		database.update(BudgetDatabase.TABLE_DAYSUM, values, BudgetDatabase.COLUMN_DATE + " = '" + theEntry.getDate().substring(0, 10) + "'", null);
		cursor.close();
		return true;
	}
	public boolean updateDayTotal(BudgetEntry theEntry)
	{
		Cursor cursor;
		cursor = database.rawQuery("select "+BudgetDatabase.COLUMN_TOTAL+" from "+BudgetDatabase.TABLE_DAYTOTAL+" where "+BudgetDatabase.COLUMN_DATE+"="+"'"+theEntry.getDate().substring(0,10)+"'",null);
		//om den itne finns, ta fr�n ig�r
		//OM ig�r itne finns, skapa ny
		if(cursor.getCount()<=0) // No entry for today, search for last entry
		{
			ContentValues values = new ContentValues();
			Cursor yesterdayCursor = database.rawQuery("select " + BudgetDatabase.COLUMN_TOTAL + " from " + BudgetDatabase.TABLE_DAYTOTAL+ " order by _id desc limit 1", null);
			if(yesterdayCursor.getCount()<=0) // No yesterday, create new entry from incoming entry
			{
				values.put(BudgetDatabase.COLUMN_DATE,theEntry.getDate().substring(0, 10)); // Don't use the hours and minutes in the daysum
				values.put(BudgetDatabase.COLUMN_TOTAL, theEntry.getValue());
				database.insert(BudgetDatabase.TABLE_DAYTOTAL, null,values);
				cursor.close();
			}
			else // Yesterday exists, create new entry with yesterdays total + new total
			{
				yesterdayCursor.moveToFirst();
				long yesterdayValue = yesterdayCursor.getLong(0);
				values.put(BudgetDatabase.COLUMN_DATE,theEntry.getDate().substring(0, 10)); // Don't use the hours and minutes in the daysum
				values.put(BudgetDatabase.COLUMN_TOTAL, theEntry.getValue()+yesterdayValue);
				database.insert(BudgetDatabase.TABLE_DAYTOTAL, null,values);
				cursor.close();
			}
			return true;
		}
		else // There is an entry for today, add to it
		{
			cursor.moveToFirst();
			long total = cursor.getLong(0);
			total += theEntry.getValue();
			ContentValues values = new ContentValues();
			values.put(BudgetDatabase.COLUMN_TOTAL, total);
			database.update(BudgetDatabase.TABLE_DAYTOTAL, values, BudgetDatabase.COLUMN_DATE + " = '" + theEntry.getDate().substring(0, 10) + "'", null);
			cursor.close();
			return true;
		}
		
	}
	
	public void addToCategory(String theCategory,long value)
	{
		
		Cursor cursor;
		cursor = database.rawQuery("select "+BudgetDatabase.COLUMN_NUM+","+BudgetDatabase.COLUMN_TOTAL+" from "+BudgetDatabase.TABLE_CATEGORIES+" where "+BudgetDatabase.COLUMN_CATEGORY+"="+"'"+theCategory+"'",null);
		
		if(cursor.getCount()!=0) // The category already has transactions
		{
			cursor.moveToFirst();
			int num = cursor.getInt(0)+1; // Number of transactions of this category 
			long newTotal = cursor.getLong(1)+value;
			ContentValues values = new ContentValues();
			values.put(BudgetDatabase.COLUMN_NUM,num);
			values.put(BudgetDatabase.COLUMN_TOTAL,newTotal);
			database.update(BudgetDatabase.TABLE_CATEGORIES, values, BudgetDatabase.COLUMN_CATEGORY+" = '"+theCategory+"'", null);
			
		}
		else // Insert a new row
		{
			ContentValues values = new ContentValues();
			values.put(BudgetDatabase.COLUMN_CATEGORY, theCategory);
			values.put(BudgetDatabase.COLUMN_NUM,1);
			values.put(BudgetDatabase.COLUMN_TOTAL,value);
			
			database.insert(BudgetDatabase.TABLE_CATEGORIES, null,values);
			
		}
		cursor.close();
	}
	
	// Remove a value from the category table, also decreases the number of transactions by 1
	public void removeFromCategory(String theCategory,long value)
	{
		Cursor cursor;
		cursor = database.rawQuery("select "+BudgetDatabase.COLUMN_NUM+","+BudgetDatabase.COLUMN_TOTAL+" from "+BudgetDatabase.TABLE_CATEGORIES+" where "+BudgetDatabase.COLUMN_CATEGORY+"="+"'"+theCategory+"'",null);
		if(cursor.getCount()!=0)
		{
			cursor.moveToFirst();
			int num = cursor.getInt(0)-1; // Number of transactions of this category 
			long newTotal = cursor.getLong(1)+value;
			
			if(num==0 && newTotal==0) // No transactions left here, remove
			{
				database.delete(BudgetDatabase.TABLE_CATEGORIES, BudgetDatabase.COLUMN_CATEGORY + " = '" + theCategory + "'", null);
			}
			else // Update the transaction entry
			{
				ContentValues values = new ContentValues();
				values.put(BudgetDatabase.COLUMN_NUM,num);
				values.put(BudgetDatabase.COLUMN_TOTAL,newTotal);
				database.update(BudgetDatabase.TABLE_CATEGORIES, values, BudgetDatabase.COLUMN_CATEGORY+" = '"+theCategory+"'", null);
			}
			
		}
		cursor.close();
			
	}
	// Add a CategoryEntry to the database.
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
	// Get n number of transactions
	public List<BudgetEntry> getTransactions(int n,String mode)
	{
		List<BudgetEntry> entries = new ArrayList<BudgetEntry>();
		//database.
		Cursor cursor;
		if(n<=0) // Get all entries
			cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_CASHFLOW + " order by _id " + mode,null);
		else 
			cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_CASHFLOW + " order by _id " + mode + " limit 0,"+n,null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			BudgetEntry entry =  new BudgetEntry(cursor.getLong(0),cursor.getInt(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4));
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
			DayEntry entry =  new DayEntry(cursor.getLong(0),cursor.getString(1),cursor.getLong(2),cursor.getInt(3));
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
			DayEntry entry =  new DayEntry(cursor.getLong(0),cursor.getString(1),cursor.getLong(2),cursor.getInt(3));
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
			entries.add(new CategoryEntry(cursor.getLong(0),cursor.getString(1),cursor.getInt(2),cursor.getLong(3),cursor.getInt(4)));
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
