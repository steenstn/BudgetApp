package budgetapp.main;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BudgetDataSource {

	
	// Database fields
	private SQLiteDatabase database;
	private BudgetDatabase dbHelper;
	private DatabaseAccess dbAccess;
	
	private String[] allColumnsTransactions = {BudgetDatabase.COLUMN_ID,
			BudgetDatabase.COLUMN_VALUE, BudgetDatabase.COLUMN_DATE, BudgetDatabase.COLUMN_CATEGORY};
	private String[] allColumnsCategories = {BudgetDatabase.COLUMN_ID,BudgetDatabase.COLUMN_CATEGORY};
	
	public BudgetDataSource(Context context)
	{
		dbHelper = new BudgetDatabase(context);
	}
	
	public void open() throws SQLException
	{
		database = dbHelper.getWritableDatabase();
		dbAccess = new DatabaseAccess(database);
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public BudgetEntry createTransactionEntry(BudgetEntry theEntry)
	{
		return dbAccess.addEntry(theEntry);
	}
	
	public CategoryEntry createCategoryEntry(CategoryEntry theEntry)
	{
		return dbAccess.addEntry(theEntry);
	}
	public void updateCategory(String theCategory,int value)
	{
		dbAccess.updateCategory(theCategory,value);
	}
	
	public void dropTables()
	{
		database.execSQL("DROP TABLE IF EXISTS " + "cashflow");
		database.execSQL("DROP TABLE IF EXISTS " + "categories");
		
		
	}
	public List<BudgetEntry> getAllTransactions()
	{
		return dbAccess.getTransactions(0);
	}
	
	public List<BudgetEntry> getSomeTransactions(int n)
	{
		return dbAccess.getTransactions(n);
	}
	
	public List<CategoryEntry> getAllCategories()
	{
		
		return dbAccess.getCategories();
	}
	public boolean addCategory(String theCategory)
	{
		return dbAccess.addCategory(theCategory);
	}

	public boolean removeCategory(String theCategory)
	{
		return dbAccess.removeCategory(theCategory);
	}
	/*
	public List<BudgetEntry> getSomeEntries(int n)
	{
		List<BudgetEntry> entries = new ArrayList<BudgetEntry>();
		String fixedInt = ""+n;
		Cursor cursor = database.query(BudgetDatabase.TABLE_CASHFLOW, allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			BudgetEntry entry = cursorToBudgetEntry(cursor);
			entries.add(entry);
			cursor.moveToNext();
		}
		cursor.close();
		return entries;
	}
	*/
	
	private BudgetEntry cursorToBudgetEntry(Cursor cursor)
	{
		BudgetEntry entry = new BudgetEntry(cursor.getLong(0),cursor.getInt(1),cursor.getString(2),cursor.getString(3));

		return entry;
	}
	
}
