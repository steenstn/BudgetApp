package budgetapp.main;
/**
 * The database containing all the tables used by the application
 * 
 * @author Steen
 * 
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BudgetDatabase extends SQLiteOpenHelper{

	// The table for cash flow
	// Basically a log for the transactions
	public static final String TABLE_CASHFLOW = "cashflow";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_VALUE = "value";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_CATEGORY = "category";
	
	// The table for cataegories
	// Keeps track of total number of transactions of a category
	// and the total money spent on a category
	public static final String TABLE_CATEGORIES = "categories";
	//COLUMN_ID
	//COLUMN_CATEGORY
	public static final String COLUMN_NUM = "num"; // Number of times this has been bought
	public static final String COLUMN_TOTAL = "total"; // Total sum of money spent on this category
	
	// The table for total sum in a day
	public static final String TABLE_DAYSUM = "daysum";
	//COLUMN_ID
	//COLUMN_DATE
	//COLUMN_TOTAL

	private static final String DATABASE_NAME = "budget.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE_TABLE_CASHFLOW = "create table "
			+ TABLE_CASHFLOW + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " +COLUMN_VALUE +
			" integer, " + COLUMN_DATE + " text, " + COLUMN_CATEGORY + " text);";
	
	private static final String DATABASE_CREATE_TABLE_CATEGORIES = "create table "
			+ TABLE_CATEGORIES + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_CATEGORY +
			" text, "+ COLUMN_NUM + " integer not null, " + COLUMN_TOTAL + " long integer not null);";
	
	private static final String DATABASE_CREATE_TABLE_DAYSUM = "create table "
			+ TABLE_DAYSUM + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_DATE
			+ " text, " + COLUMN_TOTAL + " long integer not null);";

	public BudgetDatabase(Context context)
	{
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database)
	{
		database.execSQL(DATABASE_CREATE_TABLE_CASHFLOW);
		database.execSQL(DATABASE_CREATE_TABLE_CATEGORIES);
		database.execSQL(DATABASE_CREATE_TABLE_DAYSUM);
		// Put in initial categories
		ContentValues values = new ContentValues();
		values.put(BudgetDatabase.COLUMN_CATEGORY, "Choose category");
		values.put(BudgetDatabase.COLUMN_NUM, 0);
		values.put(BudgetDatabase.COLUMN_TOTAL, 0);
		database.insert(BudgetDatabase.TABLE_CATEGORIES, null,values);
		
		values.put(BudgetDatabase.COLUMN_CATEGORY, "Income");
		values.put(BudgetDatabase.COLUMN_NUM, 0);
		values.put(BudgetDatabase.COLUMN_TOTAL, 0);
		database.insert(BudgetDatabase.TABLE_CATEGORIES, null,values);
		
		values.put(BudgetDatabase.COLUMN_CATEGORY, "Coffee");
		values.put(BudgetDatabase.COLUMN_NUM, 0);
		values.put(BudgetDatabase.COLUMN_TOTAL, 0);
		database.insert(BudgetDatabase.TABLE_CATEGORIES, null,values);
		
		values.put(BudgetDatabase.COLUMN_CATEGORY, "Food");
		values.put(BudgetDatabase.COLUMN_NUM, 0);
		values.put(BudgetDatabase.COLUMN_TOTAL, 0);
		database.insert(BudgetDatabase.TABLE_CATEGORIES, null,values);
		
		values.put(BudgetDatabase.COLUMN_CATEGORY, "Groceries");
		values.put(BudgetDatabase.COLUMN_NUM, 0);
		values.put(BudgetDatabase.COLUMN_TOTAL, 0);
		database.insert(BudgetDatabase.TABLE_CATEGORIES, null,values);
		
		values.put(BudgetDatabase.COLUMN_CATEGORY, "Misc");
		values.put(BudgetDatabase.COLUMN_NUM, 0);
		values.put(BudgetDatabase.COLUMN_TOTAL, 0);
		database.insert(BudgetDatabase.TABLE_CATEGORIES, null,values);
		
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
