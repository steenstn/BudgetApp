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
	public static final String TABLE_CASHFLOW = "cashflow";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_VALUE = "value";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_CATEGORY = "category";
	
	// The table for cataegories
	public static final String TABLE_CATEGORIES = "categories";
	

	private static final String DATABASE_NAME = "budget.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE_TABLE_CASHFLOW = "create table "
			+ TABLE_CASHFLOW + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " +COLUMN_VALUE +
			" integer, " + COLUMN_DATE + " text, " + COLUMN_CATEGORY + " text);";
	
	private static final String DATABASE_CREATE_TABLE_CATEGORIES = "create table "
			+ TABLE_CATEGORIES + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_CATEGORY +
			" text);";

	public BudgetDatabase(Context context)
	{
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database)
	{
		database.execSQL(DATABASE_CREATE_TABLE_CASHFLOW);
		database.execSQL(DATABASE_CREATE_TABLE_CATEGORIES);
		// Put in initial categories
		ContentValues values = new ContentValues();
		values.put(BudgetDatabase.COLUMN_CATEGORY, "Drugs");
		database.insert(BudgetDatabase.TABLE_CATEGORIES, null,values);
		values.put(BudgetDatabase.COLUMN_CATEGORY, "Ravioli");
		database.insert(BudgetDatabase.TABLE_CATEGORIES, null,values);
		values.put(BudgetDatabase.COLUMN_CATEGORY, "Hoes");
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
