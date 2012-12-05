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
	
	private String[] allColumns = {BudgetDatabase.COLUMN_ID,
			BudgetDatabase.COLUMN_VALUE, BudgetDatabase.COLUMN_DATE};
	
	public BudgetDataSource(Context context)
	{
		dbHelper = new BudgetDatabase(context);
	}
	
	public void open() throws SQLException
	{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public BudgetEntry createEntry(int theValue, String theDate)
	{
		ContentValues values = new ContentValues();
		
		values.put(BudgetDatabase.COLUMN_VALUE, theValue);

		values.put(BudgetDatabase.COLUMN_DATE,theDate);
		long insertId = database.insert(BudgetDatabase.TABLE_CASHFLOW, null,values);
		
		Cursor cursor = database.query(BudgetDatabase.TABLE_CASHFLOW,
				allColumns,BudgetDatabase.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		BudgetEntry newEntry = cursorToBudgetEntry(cursor);
		cursor.close();
		return newEntry;
		
		
	}
	
	public List<BudgetEntry> getAllEntries()
	{
		List<BudgetEntry> entries = new ArrayList<BudgetEntry>();

		Cursor cursor = database.query(BudgetDatabase.TABLE_CASHFLOW,allColumns,null,null,null,null,null);
		
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
	
	
	private BudgetEntry cursorToBudgetEntry(Cursor cursor)
	{
		BudgetEntry entry = new BudgetEntry();
		entry.setId(cursor.getLong(0));
		entry.setValue(cursor.getInt(1));
		entry.setDate(cursor.getString(2));
		return entry;
	}
	
}
