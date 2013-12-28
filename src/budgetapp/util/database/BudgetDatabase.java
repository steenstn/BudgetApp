package budgetapp.util.database;
/**
 * The database containing all the tables used by the application
 * 
 * @author Steen
 * 
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BudgetDatabase extends SQLiteOpenHelper{

	// The table for cash flow
	// Basically a log for the transactions
	public static final String TABLE_CASHFLOW = "cashflow";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_VALUE = "value";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_CATEGORY = "category";
	public static final String COLUMN_FLAGS = "flags";
	public static final String COLUMN_COMMENT = "comment";
	
	//The table with the different names for categories, used by the Spinner to select categories
	public static final String TABLE_CATEGORY_NAMES = "categorynames";
	// The table for transactions for cataegories
	// Keeps track of total number of transactions of a category
	// and the total money spent on a category
	public static final String TABLE_CATEGORIES = "categories";
	//COLUMN_ID
	//COLUMN_CATEGORY
	public static final String COLUMN_NUM = "num"; // Number of times this has been bought
	//public static final String COLUMN_TOTAL = "total"; // Total sum of money spent on this category
	
	// The table for cash flow in a day
	public static final String TABLE_DAYSUM = "daysum";
	
	// Total sum for a day
	public static final String TABLE_DAYTOTAL = "daytotal";
	
	//COLUMN_ID
	//COLUMN_DATE
	//COLUMN_TOTAL
	
	// Currencies and their exhange values
	public static final String TABLE_CURRENCIES = "currencies";
	
	public static final String TABLE_INSTALLMENTS = "installments";
	
	public static final String COLUMN_TRANSACTION_ID = "transaction_id";
	public static final String COLUMN_DATE_LAST_PAID = "date_last_paid";
	public static final String COLUMN_DAILY_PAYMENT = "daily_payment";
	
	public static final String TABLE_INSTALLMENT_DAYFLOW_PAID = "installment_dayflow_paid";
	public static final String COLUMN_DAYFLOW_ID = "dayflow_id";
	public static final String COLUMN_INSTALLMENT_ID = "installment_id";
	//COLUMN_ID
	public static final String COLUMN_NAME = "name"; // The name of the currency
	public static final String COLUMN_SYMBOL = "symbol"; // The symbol used when printing
	public static final String COLUMN_RATE = "rate"; // The exchange rate of the currency
	//COLUMN_FLAGS
	
	// Autocomplete values for the AutoCompleteEditText
	public static final String TABLE_AUTOCOMPLETE_VALUES = "autocompletevalues";

	
	private static final String DATABASE_NAME = "budget.db";
	private static final int DATABASE_VERSION = 14;
	
	public static final String DATABASE_CREATE_TABLE_CATEGORY_NAMES = "create table "
			+ TABLE_CATEGORY_NAMES + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_CATEGORY
			+ " text);";

	public static final String DATABASE_CREATE_TABLE_CASHFLOW = "create table "
			+ TABLE_CASHFLOW + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " +COLUMN_VALUE +
			" double, " + COLUMN_DATE + " text, " + COLUMN_CATEGORY + " text, " + COLUMN_FLAGS + " integer, "
			+ COLUMN_COMMENT + " text);";
	
	
	public static final String DATABASE_CREATE_TABLE_CATEGORIES = "create table "
			+ TABLE_CATEGORIES + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_CATEGORY +
			" text, "+ COLUMN_NUM + " integer not null, " + COLUMN_VALUE + " double not null, " + COLUMN_FLAGS + " integer);";
	
	public static final String DATABASE_CREATE_TABLE_DAYSUM = "create table "
			+ TABLE_DAYSUM + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_DATE
			+ " text, " + COLUMN_VALUE + " double not null, " + COLUMN_FLAGS + " integer);";
	public static final String DATABASE_CREATE_TABLE_DAYTOTAL = "create table "
			+ TABLE_DAYTOTAL + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_DATE
			+ " text, " + COLUMN_VALUE + " double not null, " + COLUMN_FLAGS + " integer);";
	
	public static final String DATABASE_CREATE_TABLE_AUTOCOMPLETE_VALUES = "create table "
			+ TABLE_AUTOCOMPLETE_VALUES + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_VALUE
			+ " double, " + COLUMN_NUM + " integer, " + COLUMN_CATEGORY
			+ " text);";
	
	public static final String DATABASE_CREATE_TABLE_INSTALLMENTS = "create table "
			+ TABLE_INSTALLMENTS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, "
			+ COLUMN_TRANSACTION_ID + " integer, "
			+ COLUMN_VALUE + " double, " 
			+ COLUMN_DAILY_PAYMENT + " double, "
			+ COLUMN_DATE_LAST_PAID + " text, "
			+ COLUMN_FLAGS + " integer); ";
			
	
	public static final String DATABASE_CREATE_TABLE_INSTALLMENT_DAYFLOW_PAID = "create table "
			+ TABLE_INSTALLMENT_DAYFLOW_PAID + "(" + COLUMN_ID
			+ " integer primary key autoincrement, "
			+ COLUMN_INSTALLMENT_ID + " integer, "
			+ COLUMN_DAYFLOW_ID + " integer, "
			+ COLUMN_VALUE + " double);";
	
	
	private static BudgetDatabase instance;
	
	public static synchronized BudgetDatabase getInstance(Context context)
	{
		if(instance == null)
			instance = new BudgetDatabase(context);
		
		return instance;
	}
	
	public static synchronized void clearInstance()
	{
		instance = null;
	}
	
	private BudgetDatabase(Context context)
	{
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database)
	{
		database.execSQL(DATABASE_CREATE_TABLE_CASHFLOW);
		database.execSQL(DATABASE_CREATE_TABLE_CATEGORIES);
		database.execSQL(DATABASE_CREATE_TABLE_DAYSUM);
		database.execSQL(DATABASE_CREATE_TABLE_DAYTOTAL);
		database.execSQL(DATABASE_CREATE_TABLE_CATEGORY_NAMES);
		
		database.execSQL(DATABASE_CREATE_TABLE_AUTOCOMPLETE_VALUES);
		database.execSQL(DATABASE_CREATE_TABLE_INSTALLMENTS);
		database.execSQL(DATABASE_CREATE_TABLE_INSTALLMENT_DAYFLOW_PAID);
		
		// Put in initial categories
		ContentValues values = new ContentValues();
		values.put(BudgetDatabase.COLUMN_CATEGORY, "Income");
		database.insert(BudgetDatabase.TABLE_CATEGORY_NAMES, null,values);
		values.put(BudgetDatabase.COLUMN_CATEGORY, "Groceries");
		database.insert(BudgetDatabase.TABLE_CATEGORY_NAMES, null,values);
		values.put(BudgetDatabase.COLUMN_CATEGORY, "Entertainment");
		database.insert(BudgetDatabase.TABLE_CATEGORY_NAMES, null,values);
		
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Cursor cursor;
		switch(oldVersion)  
		{
		case 7: // Updating from 2.2
			db.execSQL("ALTER TABLE " + TABLE_CASHFLOW + " ADD COLUMN " + COLUMN_COMMENT);
		case 8: // Updating from 2.2, changing to doubles by creating new tables
			
			ContentValues values;
			db.execSQL("create table temp"+"(" + COLUMN_ID
					+ " integer primary key autoincrement, " +COLUMN_VALUE +
					" double, " + COLUMN_DATE + " text, " + COLUMN_CATEGORY + " text, " + COLUMN_FLAGS + " integer, "
					+ COLUMN_COMMENT + " text);");
			
			cursor = db.rawQuery("select * from " + TABLE_CASHFLOW,null);
			if(cursor.getCount()!=0)
			{
				cursor.moveToFirst();
				while(!cursor.isAfterLast())
				{
					values = new ContentValues();
					values.put(COLUMN_ID,cursor.getLong(0));
					values.put(COLUMN_VALUE,(double)(cursor.getInt(1)));
					values.put(COLUMN_DATE,cursor.getString(2));
					values.put(COLUMN_CATEGORY,cursor.getString(3));
					values.put(COLUMN_FLAGS,cursor.getInt(4));
					values.put(COLUMN_COMMENT,cursor.getString(5));
					db.insert("temp", null,values);
					
					cursor.moveToNext();
				}
				
			}
			db.execSQL("drop table " + TABLE_CASHFLOW);
			db.execSQL("ALTER TABLE temp RENAME TO " + TABLE_CASHFLOW);
			
		//	db.execSQL("ALTER TABLE " + TABLE_CASHFLOW + " MODIFY " + COLUMN_VALUE + " DOUBLE");
			db.execSQL("create table temp"+"(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_CATEGORY +
			" text, "+ COLUMN_NUM + " integer not null, " + COLUMN_VALUE + " double not null, " + COLUMN_FLAGS + " integer);");
			
			cursor = db.rawQuery("select * from " + TABLE_CATEGORIES,null);
			if(cursor.getCount()!=0)
			{
				cursor.moveToFirst();
				while(!cursor.isAfterLast())
				{
					values = new ContentValues();
					values.put(COLUMN_ID,cursor.getLong(0));
					values.put(COLUMN_CATEGORY,cursor.getString(1));
					values.put(COLUMN_NUM,cursor.getInt(2));
					values.put(COLUMN_VALUE,(double)(cursor.getInt(3)));
					values.put(COLUMN_FLAGS,cursor.getInt(4));
					db.insert("temp", null,values);
					
					cursor.moveToNext();
				}
			}
			db.execSQL("drop table " + TABLE_CATEGORIES);
			db.execSQL("ALTER TABLE temp RENAME TO " + TABLE_CATEGORIES);
			
			
			//db.execSQL("ALTER TABLE " + TABLE_CATEGORIES + " MODIFY total DOUBLE");
			
			db.execSQL("create table temp"+"(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_DATE
			+ " text, " + COLUMN_VALUE + " double not null, " + COLUMN_FLAGS + " integer);");
			
			cursor = db.rawQuery("select * from " + TABLE_DAYSUM,null);
			if(cursor.getCount()!=0)
			{
				cursor.moveToFirst();
				while(!cursor.isAfterLast())
				{
					values = new ContentValues();
					values.put(COLUMN_ID,cursor.getLong(0));
					values.put(COLUMN_DATE,cursor.getString(1));
					values.put(COLUMN_VALUE,(double)(cursor.getInt(2)));
					values.put(COLUMN_FLAGS,cursor.getInt(3));
					db.insert("temp", null,values);
					
					cursor.moveToNext();
				}
			}
			db.execSQL("drop table " + TABLE_DAYSUM);
			db.execSQL("ALTER TABLE temp RENAME TO " + TABLE_DAYSUM);
			
		//	db.execSQL("ALTER TABLE " + TABLE_DAYSUM + " MODIFY total DOUBLE");
			
			db.execSQL("create table temp"+ "(" + COLUMN_ID
					+ " integer primary key autoincrement, " + COLUMN_DATE
					+ " text, " + COLUMN_VALUE + " double not null, " + COLUMN_FLAGS + " integer);");
			
			cursor = db.rawQuery("select * from " + TABLE_DAYTOTAL,null);
			if(cursor.getCount()!=0)
			{
				cursor.moveToFirst();
				while(!cursor.isAfterLast())
				{
					values = new ContentValues();
					values.put(COLUMN_ID,cursor.getLong(0));
					values.put(COLUMN_DATE,cursor.getString(1));
					values.put(COLUMN_VALUE,(double)(cursor.getInt(2)));
					values.put(COLUMN_FLAGS,cursor.getInt(3));
					db.insert("temp", null,values);
					
					cursor.moveToNext();
				}
			}
			db.execSQL("drop table " + TABLE_DAYTOTAL);
			db.execSQL("ALTER TABLE temp RENAME TO " + TABLE_DAYTOTAL);
			
		case 10:
			db.execSQL(DATABASE_CREATE_TABLE_AUTOCOMPLETE_VALUES);
		case 11:
			db.execSQL(DATABASE_CREATE_TABLE_INSTALLMENTS);
		case 12:
			db.execSQL(DATABASE_CREATE_TABLE_INSTALLMENT_DAYFLOW_PAID);
		case 13:
			db.execSQL("drop table " + TABLE_AUTOCOMPLETE_VALUES);
			db.execSQL(DATABASE_CREATE_TABLE_AUTOCOMPLETE_VALUES);
		}
		
		System.out.println("Updated database from " + oldVersion + " to " + newVersion);
	    
	}
	
}
