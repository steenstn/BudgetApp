package budgetapp.main;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BudgetDatabase extends SQLiteOpenHelper{

	// The table for cash flow
	public static final String TABLE_CASHFLOW = "cashflow";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_VALUE = "value";
	public static final String COLUMN_DATE = "date";
	
	
	private static final String DATABASE_NAME = "budget.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_CASHFLOW + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " +COLUMN_VALUE +
			" integer, " + COLUMN_DATE + " text);";

	public BudgetDatabase(Context context)
	{
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database)
	{
		database.execSQL(DATABASE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(BudgetDatabase.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_CASHFLOW);
	    onCreate(db);
	}
}
