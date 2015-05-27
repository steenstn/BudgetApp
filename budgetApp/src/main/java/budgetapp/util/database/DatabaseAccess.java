package budgetapp.util.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import budgetapp.util.Currency;
import budgetapp.util.Event;
import budgetapp.util.Installment;
import budgetapp.util.entries.BudgetEntry;
import budgetapp.util.entries.CategoryEntry;
import budgetapp.util.entries.DayEntry;
import budgetapp.util.money.Money;
import budgetapp.util.money.MoneyFactory;

public class DatabaseAccess {

    private SQLiteDatabase database;
    private String[] allColumnsTransactions = { BudgetDatabase.COLUMN_ID, BudgetDatabase.COLUMN_VALUE,
            BudgetDatabase.COLUMN_DATE, BudgetDatabase.COLUMN_CATEGORY, BudgetDatabase.COLUMN_FLAGS };
    private String[] allColumnsCategories = { BudgetDatabase.COLUMN_ID, BudgetDatabase.COLUMN_CATEGORY,
            BudgetDatabase.COLUMN_NUM, BudgetDatabase.COLUMN_VALUE, BudgetDatabase.COLUMN_FLAGS };

    public static final String ASCENDING = "asc";
    public static final String DESCENDING = "desc";

    public DatabaseAccess(SQLiteDatabase theDatabase) {
        database = theDatabase;

    }

    public boolean addCategory(String theCategory) {
        ContentValues values = new ContentValues();
        String fixedCategory = theCategory.replaceAll("['\"]", "\'");
        values.put(BudgetDatabase.COLUMN_CATEGORY, fixedCategory);
        long insertID = database.insert(BudgetDatabase.TABLE_CATEGORY_NAMES, null, values);

        return insertID != -1;
    }

    public boolean removeCategory(String theCategory) {
        return database.delete(BudgetDatabase.TABLE_CATEGORY_NAMES, BudgetDatabase.COLUMN_CATEGORY + " = " + "\""
                + theCategory + "\"", null) > 0;
    }

    public boolean setFlags(long id, int flags) {
        ContentValues values = new ContentValues();
        values.put(BudgetDatabase.COLUMN_FLAGS, flags);
        int result = database.update(BudgetDatabase.TABLE_INSTALLMENTS, values, BudgetDatabase.COLUMN_ID + " = " + id,
            null);

        return result == 1;
    }

    /** Adds a transaction entry to the cash flow table in the dataase
     * 
     * @param theEntry
     *            - The BudgetEntry to add
     * @return - The added BudgetEntry, will have an id from the table */
    public BudgetEntry addEntry(BudgetEntry theEntry) {
        ContentValues values = new ContentValues();
        // Put in the values
        values.put(BudgetDatabase.COLUMN_VALUE, theEntry.getValue().get());
        values.put(BudgetDatabase.COLUMN_DATE, theEntry.getDate());
        values.put(BudgetDatabase.COLUMN_CATEGORY, theEntry.getCategory().replaceAll("['\"]", "\'"));
        values.put(BudgetDatabase.COLUMN_COMMENT, theEntry.getComment().replaceAll("['\"]", "\'"));
        values.put(BudgetDatabase.COLUMN_FLAGS, theEntry.getFlags());

        long insertId = database.insert(BudgetDatabase.TABLE_CASHFLOW, null, values);

        Cursor cursor = database.query(BudgetDatabase.TABLE_CASHFLOW, allColumnsTransactions, BudgetDatabase.COLUMN_ID
                + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();

        BudgetEntry entry = new BudgetEntry(cursor.getLong(0), MoneyFactory.convertDoubleToMoney(cursor.getDouble(1)),
            cursor.getString(2), cursor.getString(3), cursor.getInt(4));
        cursor.close();
        return entry;
    }

    /** Gets a BudgetEntry from the cash flow table
     * 
     * @param theId
     *            - Id of the entry to get
     * @return - The resulting BudgetEntry or a new BudgetEntry() if not found */
    public BudgetEntry getEntry(long theId) {
        Cursor cursor;
        cursor = database.query(BudgetDatabase.TABLE_CASHFLOW, null, BudgetDatabase.COLUMN_ID + " = " + theId, null,
            null, null, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            BudgetEntry entry = createBudgetEntryFromCursor(cursor);
            cursor.close();
            return entry;
        } else {
            cursor.close();
            return new BudgetEntry();
        }
    }

    /** Removes an entry from the cash flow table
     * 
     * @param theEntry
     *            - The entry to delete
     * @return - If the deletion was successful */
    public boolean removeEntry(BudgetEntry theEntry) {
        int res = database.delete(BudgetDatabase.TABLE_CASHFLOW, BudgetDatabase.COLUMN_ID + " = " + theEntry.getId(),
            null);

        return res != 0;
    }

    public void removeEvent(long id) {
        database.delete(BudgetDatabase.TABLE_EVENTS, BudgetDatabase.COLUMN_ID + " = " + id, null);
    }

    public void removeCurrency(long id) {
        database.delete(BudgetDatabase.TABLE_CURRENCIES, BudgetDatabase.COLUMN_ID + " = " + id, null);
    }

    public void addTransactionToEvent(long transactionId, long eventId) {
        ContentValues values = new ContentValues();
        values.put(BudgetDatabase.COLUMN_EVENT_ID, eventId);
        values.put(BudgetDatabase.COLUMN_TRANSACTION_ID, transactionId);

        Cursor cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_EVENT_TRANSACTION + " where "
                + BudgetDatabase.COLUMN_EVENT_ID + " = " + eventId + " and " + BudgetDatabase.COLUMN_TRANSACTION_ID
                + " = " + transactionId, null);

        if (cursor.getCount() == 0) {
            database.insert(BudgetDatabase.TABLE_EVENT_TRANSACTION, null, values);
        }
        cursor.close();
    }

    public void removeTransactionFromEvents(long transactionId) {
        database.delete(BudgetDatabase.TABLE_EVENT_TRANSACTION, BudgetDatabase.COLUMN_TRANSACTION_ID + " = "
                + transactionId, null);
    }

    public List<Long> getIdsOfActiveEvents() {
        Cursor cursor = database.rawQuery("select " + BudgetDatabase.COLUMN_ID + " from " + BudgetDatabase.TABLE_EVENTS
                + " where " + BudgetDatabase.COLUMN_FLAGS + " = " + Event.EVENT_ACTIVE, null);

        if (cursor.getCount() != 0) {
            List<Long> ids = new ArrayList<Long>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ids.add(cursor.getLong(0));
                cursor.moveToNext();
            }
            cursor.close();
            return ids;
        }
        cursor.close();
        return new ArrayList<Long>();
    }

    public long getIdFromEventName(String eventName) {
        Cursor cursor = database.rawQuery("select " + BudgetDatabase.COLUMN_ID + " from " + BudgetDatabase.TABLE_EVENTS
                + " where " + BudgetDatabase.COLUMN_NAME + " = '" + eventName + "'", null);

        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            long id = cursor.getLong(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return -1;
    }

    /** Updates a transaction entry in the cash flow table
     *
     * @param newEntry
     *            - Entry containing the new values
     * @return - If the editing was successful */
    public boolean updateTransaction(long id, BudgetEntry newEntry) {
        Cursor cursor;
        cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_CASHFLOW + " where _id = " + id, null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            ContentValues values = new ContentValues();
            values.put(BudgetDatabase.COLUMN_CATEGORY, newEntry.getCategory().replaceAll("['\"]", "\'"));
            values.put(BudgetDatabase.COLUMN_VALUE, newEntry.getValue().get());
            values.put(BudgetDatabase.COLUMN_COMMENT, newEntry.getComment().replaceAll("['\"]", "\'"));
            int res = database.update(BudgetDatabase.TABLE_CASHFLOW, values, BudgetDatabase.COLUMN_ID + " = " + id,
                null);

            if (res != 0) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    public boolean updateInstallment(long id, double newTotalValue, double newDailyPayment, String newDateLastPaid,
            int newFlags) {
        ContentValues values = new ContentValues();
        values.put(BudgetDatabase.COLUMN_VALUE, newTotalValue);
        values.put(BudgetDatabase.COLUMN_DAILY_PAYMENT, newDailyPayment);
        values.put(BudgetDatabase.COLUMN_DATE_LAST_PAID, newDateLastPaid);
        values.put(BudgetDatabase.COLUMN_FLAGS, newFlags);

        int res = database.update(BudgetDatabase.TABLE_INSTALLMENTS, values, BudgetDatabase.COLUMN_ID + " = " + id,
            null);
        return res != 0;
    }

    public boolean updateEvent(long id, String newName, String newStartDate, String newEndDate, String newComment,
            int newFlags) {
        ContentValues values = new ContentValues();
        values.put(BudgetDatabase.COLUMN_NAME, newName.replaceAll("['\"]", "\'"));
        values.put(BudgetDatabase.COLUMN_START_DATE, newStartDate);
        values.put(BudgetDatabase.COLUMN_END_DATE, newEndDate);
        values.put(BudgetDatabase.COLUMN_COMMENT, newComment.replaceAll("['\"]", "\'"));
        values.put(BudgetDatabase.COLUMN_FLAGS, newFlags);

        int res = database.update(BudgetDatabase.TABLE_EVENTS, values, BudgetDatabase.COLUMN_ID + " = " + id, null);
        return res != 0;
    }

    public void updateCurrency(long id, Currency newCurrency) {
        ContentValues values = new ContentValues();
        values.put(BudgetDatabase.COLUMN_CURRENCY_SYMBOL, newCurrency.getSymbol());
        values.put(BudgetDatabase.COLUMN_CURRENCY_EXCHANGE_RATE, newCurrency.getExchangeRate());
        values.put(BudgetDatabase.COLUMN_FLAGS, newCurrency.getFlags());

        database.update(BudgetDatabase.TABLE_CURRENCIES, values, BudgetDatabase.COLUMN_ID + " = " + id, null);

    }

    /** Add a value to a category in the category table
     * 
     * @param theCategory
     *            - The category to add to
     * @param value
     *            - The value to add */
    public void addToCategory(String theCategory, double value) {

        Cursor cursor;
        cursor = database.rawQuery("select " + BudgetDatabase.COLUMN_NUM + "," + BudgetDatabase.COLUMN_VALUE + " from "
                + BudgetDatabase.TABLE_CATEGORIES + " where " + BudgetDatabase.COLUMN_CATEGORY + "=" + "\""
                + theCategory + "\"", null);

        if (cursor.getCount() != 0) { // The category already has transactions
            cursor.moveToFirst();
            int num = cursor.getInt(0) + 1; // Number of transactions of this category
            double newTotal = cursor.getDouble(1) + value;
            ContentValues values = new ContentValues();
            values.put(BudgetDatabase.COLUMN_NUM, num);
            values.put(BudgetDatabase.COLUMN_VALUE, newTotal);
            database.update(BudgetDatabase.TABLE_CATEGORIES, values, BudgetDatabase.COLUMN_CATEGORY + " = '"
                    + theCategory + "'", null);

        } else { // Insert a new row
            ContentValues values = new ContentValues();
            values.put(BudgetDatabase.COLUMN_CATEGORY, theCategory);
            values.put(BudgetDatabase.COLUMN_NUM, 1);
            values.put(BudgetDatabase.COLUMN_VALUE, value);

            database.insert(BudgetDatabase.TABLE_CATEGORIES, null, values);

        }
        cursor.close();
    }

    /** Remove a value from the category table, also decreases the number of transactions by 1
     * 
     * @param theCategory
     *            - The category to remove from
     * @param value
     *            - The value to remove */
    public void removeFromCategory(String theCategory, double value) {
        Cursor cursor;
        cursor = database.rawQuery("select " + BudgetDatabase.COLUMN_NUM + "," + BudgetDatabase.COLUMN_VALUE + " from "
                + BudgetDatabase.TABLE_CATEGORIES + " where " + BudgetDatabase.COLUMN_CATEGORY + "=" + "\""
                + theCategory + "\"", null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            int num = cursor.getInt(0) - 1; // Number of transactions of this category
            double newTotal = cursor.getDouble(1) + value;

            if (num == 0 && newTotal == 0) { // No transactions left here, remove
                database.delete(BudgetDatabase.TABLE_CATEGORIES, BudgetDatabase.COLUMN_CATEGORY + " = \"" + theCategory
                        + "\"", null);
            } else { // Update the transaction entry
                ContentValues values = new ContentValues();
                values.put(BudgetDatabase.COLUMN_NUM, num);
                values.put(BudgetDatabase.COLUMN_VALUE, newTotal);
                database.update(BudgetDatabase.TABLE_CATEGORIES, values, BudgetDatabase.COLUMN_CATEGORY + " = '"
                        + theCategory + "'", null);
            }

        }
        cursor.close();

    }


    public CategoryEntry addEntry(CategoryEntry theEntry) {
        ContentValues values = new ContentValues();
        values.put(BudgetDatabase.COLUMN_CATEGORY, theEntry.getCategory().replaceAll("['\"]", "\'"));
        long insertId = database.insert(BudgetDatabase.TABLE_CATEGORIES, null, values);

        Cursor cursor = database.query(BudgetDatabase.TABLE_CATEGORIES, allColumnsCategories, BudgetDatabase.COLUMN_ID
                + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        CategoryEntry entry = new CategoryEntry(cursor.getLong(0), cursor.getString(1));
        cursor.close();
        return entry;
    }

    /** Adds a value to the autocomplete table, if the table is full, the oldest value is replaces, queue-style
     * 
     * @param theValue
     *            - The value to add */
    public void addAutocompleteValue(double theValue, String theCategory) {
        Cursor cursor;
        // See if value already exists
        cursor = database.rawQuery("select " + BudgetDatabase.COLUMN_VALUE + ", " + BudgetDatabase.COLUMN_NUM
                + " from " + BudgetDatabase.TABLE_AUTOCOMPLETE_VALUES + " where " + BudgetDatabase.COLUMN_VALUE + " = "
                + theValue + " and " + BudgetDatabase.COLUMN_CATEGORY + " = '" + theCategory + "'", null);

        if (cursor.getCount() == 0) { // It does not exist, see how many values are in the table
            cursor = database.rawQuery("select count(*) from " + BudgetDatabase.TABLE_AUTOCOMPLETE_VALUES, null);
            cursor.moveToFirst();
            int numValues = cursor.getInt(0);

            if (numValues > 40) { // Too many entries, delete the oldest one
                database.delete(BudgetDatabase.TABLE_AUTOCOMPLETE_VALUES, BudgetDatabase.COLUMN_ID + " = (select "
                        + BudgetDatabase.COLUMN_ID + " from " + BudgetDatabase.TABLE_AUTOCOMPLETE_VALUES + " where "
                        + BudgetDatabase.COLUMN_NUM + " = (select min(" + BudgetDatabase.COLUMN_NUM + ")" + " from "
                        + BudgetDatabase.TABLE_AUTOCOMPLETE_VALUES + "))", null);

            }
            ContentValues values = new ContentValues();
            values.put(BudgetDatabase.COLUMN_VALUE, theValue);
            values.put(BudgetDatabase.COLUMN_NUM, 1);
            values.put(BudgetDatabase.COLUMN_CATEGORY, theCategory);
            database.insert(BudgetDatabase.TABLE_AUTOCOMPLETE_VALUES, null, values);
        } else {
            cursor.moveToFirst();
            ContentValues values = new ContentValues();
            values.put(BudgetDatabase.COLUMN_NUM, cursor.getInt(1) + 1);
            database.update(BudgetDatabase.TABLE_AUTOCOMPLETE_VALUES, values, BudgetDatabase.COLUMN_VALUE + " = "
                    + theValue, null);
        }
        cursor.close();

    }

    public long addInstallment(Installment installment) {
        ContentValues values = new ContentValues();
        values.put(BudgetDatabase.COLUMN_TRANSACTION_ID, installment.getTransactionId());
        values.put(BudgetDatabase.COLUMN_VALUE, installment.getTotalValue().get());
        values.put(BudgetDatabase.COLUMN_DATE_LAST_PAID, installment.getDateLastPaid());
        values.put(BudgetDatabase.COLUMN_DAILY_PAYMENT, installment.getDailyPayment().get());
        values.put(BudgetDatabase.COLUMN_FLAGS, installment.getFlags());

        long insertId = database.insert(BudgetDatabase.TABLE_INSTALLMENTS, null, values);
        return insertId;
    }

    public long addEvent(Event event) {
        ContentValues values = new ContentValues();
        values.put(BudgetDatabase.COLUMN_NAME, event.getName().replaceAll("['\"]", "\'"));
        values.put(BudgetDatabase.COLUMN_START_DATE, event.getStartDate());
        values.put(BudgetDatabase.COLUMN_END_DATE, event.getEndDate());
        values.put(BudgetDatabase.COLUMN_COMMENT, event.getComment().replaceAll("['\"]", "\'"));
        values.put(BudgetDatabase.COLUMN_FLAGS, event.getFlags());

        long insertId = database.insert(BudgetDatabase.TABLE_EVENTS, null, values);
        return insertId;
    }

    public long addCurrency(Currency currency) {
        ContentValues values = new ContentValues();
        values.put(BudgetDatabase.COLUMN_CURRENCY_SYMBOL, currency.getSymbol().replaceAll("['\"]", "\'"));
        values.put(BudgetDatabase.COLUMN_CURRENCY_EXCHANGE_RATE, currency.getExchangeRate());
        values.put(BudgetDatabase.COLUMN_FLAGS, currency.getFlags());

        long insertId = database.insert(BudgetDatabase.TABLE_CURRENCIES, null, values);
        return insertId;

    }

    public List<Double> getAutocompleteValues() {
        List<Double> values = new ArrayList<Double>();
        Cursor cursor;
        cursor = database.rawQuery("select distinct " + BudgetDatabase.COLUMN_VALUE + " from "
                + BudgetDatabase.TABLE_AUTOCOMPLETE_VALUES + " where " + BudgetDatabase.COLUMN_NUM + " > 1", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            values.add(cursor.getDouble(0));
            cursor.moveToNext();
        }
        cursor.close();
        return values;
    }

    public List<Double> getAutocompleteValues(String category) {
        List<Double> values = new ArrayList<Double>();
        Cursor cursor;
        cursor = database.rawQuery("select " + BudgetDatabase.COLUMN_VALUE + " from "
                + BudgetDatabase.TABLE_AUTOCOMPLETE_VALUES + " where " + BudgetDatabase.COLUMN_CATEGORY + " = '"
                + category + "' order by " + BudgetDatabase.COLUMN_NUM + " desc limit 0,5", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            values.add(cursor.getDouble(0));
            cursor.moveToNext();
        }
        cursor.close();
        return values;
    }

    /** Get n number of transactions. If n = 0, returns all transactions
     * 
     * @param n
     *            - Number of transactions
     * @param mode
     *            Ascending/descending by id
     * @return n number of transactions */
    public List<BudgetEntry> getTransactions(int n, String mode) {
        List<BudgetEntry> entries = new ArrayList<BudgetEntry>();
        Cursor cursor;
        if (n <= 0) {// Get all entries
            cursor = database
                .rawQuery("select * from " + BudgetDatabase.TABLE_CASHFLOW + " order by " + BudgetDatabase.COLUMN_DATE + " " + mode, null);
        } else {
            cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_CASHFLOW + " order by " + BudgetDatabase.COLUMN_DATE + " " + mode
                    + " limit 0," + n, null);
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entries.add(createBudgetEntryFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public BudgetEntry getTransaction(long id) {
        Cursor cursor;
        cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_CASHFLOW + " where _id = " + id, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            BudgetEntry entry = createBudgetEntryFromCursor(cursor);
            cursor.close();
            return entry;
        }
        return new BudgetEntry();
    }

    public Installment getInstallment(long id) {
        Cursor cursor;
        cursor = database.rawQuery("select i.*, t.value, t.category, t.comment from "
                + BudgetDatabase.TABLE_INSTALLMENTS + " i join " + BudgetDatabase.TABLE_CASHFLOW + " t on i."
                + BudgetDatabase.COLUMN_TRANSACTION_ID + " = t." + BudgetDatabase.COLUMN_ID + " where i."
                + BudgetDatabase.COLUMN_ID + " = " + id, null);

        cursor.moveToFirst();
        if (cursor.getCount() == 1) {
            Installment installment = createInstallmentFromCursor(cursor);
            cursor.close();
            return installment;
        } else {
            cursor.close();
            return new Installment(-1, -1, MoneyFactory.createMoney(), MoneyFactory.createMoney(), "ERROR",
                MoneyFactory.createMoney(), "ERROR", "ERROR");
        }
    }

    public Money getCurrentBudget() {
        Cursor cursor = database.rawQuery("select sum("+BudgetDatabase.COLUMN_VALUE+") total from " + BudgetDatabase.TABLE_CASHFLOW, null);
        cursor.moveToFirst();
        Money currentBudget = MoneyFactory.convertDoubleToMoney(cursor.getDouble(0));
        return currentBudget;
    }

    public List<DayEntry> getDaySumCalculated(int n, String mode) {
        List<DayEntry> entries = new ArrayList<DayEntry>();
        Cursor cursor;
        if(n <= 0) {
            cursor = database.rawQuery("select " + BudgetDatabase.COLUMN_DATE
                    + ", sum(" + BudgetDatabase.COLUMN_VALUE + ") from " + BudgetDatabase.TABLE_CASHFLOW
                    + " group by substr(" + BudgetDatabase.COLUMN_DATE + ",1,10) order by " + BudgetDatabase.COLUMN_DATE + " " + mode, null);
        } else {
            cursor = database.rawQuery("select " + BudgetDatabase.COLUMN_DATE
                    + ", sum(" + BudgetDatabase.COLUMN_VALUE + ") from " + BudgetDatabase.TABLE_CASHFLOW
                    + " group by substr(" + BudgetDatabase.COLUMN_DATE + ",1,10) order by " + BudgetDatabase.COLUMN_DATE + " " + mode + " limit 0," + n, null);
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DayEntry entry = new DayEntry(cursor.getString(0), MoneyFactory.convertDoubleToMoney(cursor.getDouble(1)));

            entries.add(entry);
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public List<CategoryEntry> getCategories(String selection, String[] selectionArgs, String groupBy, String having,
            String orderBy) {
        List<CategoryEntry> entries = new ArrayList<CategoryEntry>();

        Cursor cursor;
        cursor = database.query(BudgetDatabase.TABLE_CATEGORIES, allColumnsCategories, selection, selectionArgs,
            groupBy, having, orderBy);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entries.add(new CategoryEntry(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), MoneyFactory
                .convertDoubleToMoney(cursor.getDouble(3)), cursor.getInt(4)));
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public List<String> getCategoryNames() {
        List<String> entries = new ArrayList<String>();

        Cursor cursor;
        cursor = database.rawQuery("select " + BudgetDatabase.COLUMN_CATEGORY + " from "
                + BudgetDatabase.TABLE_CATEGORY_NAMES, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entries.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public List<String> getCategoryNamesSorted() {
        List<String> entries = new ArrayList<String>();

        Cursor cursor;
        cursor = database
            .rawQuery("select " + BudgetDatabase.COLUMN_CATEGORY + " from " + BudgetDatabase.TABLE_CATEGORY_NAMES
                    + " order by UPPER(" + BudgetDatabase.COLUMN_CATEGORY + ")", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entries.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public void clearAutocompleteValues() {
        database.delete(BudgetDatabase.TABLE_AUTOCOMPLETE_VALUES, BudgetDatabase.COLUMN_ID + " > 0", null);
    }

    public List<Event> getEvents() {
        List<Event> events = new ArrayList<Event>();
        Cursor cursor;
        cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_EVENTS, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            events.add(createEventFromCursor(cursor));
            cursor.moveToNext();

        }
        cursor.close();
        return events;
    }

    public List<Currency> getCurrencies() {
        List<Currency> currencies = new ArrayList<Currency>();
        Cursor cursor;
        cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_CURRENCIES, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            currencies.add(createCurrencyFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return currencies;
    }

    public Currency getCurrency(long id) {
        Cursor cursor;
        cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_CURRENCIES + " where "
                + BudgetDatabase.COLUMN_ID + " = " + id, null);

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            Currency currency = createCurrencyFromCursor(cursor);
            cursor.close();
            return currency;
        }
        cursor.close();
        return new Currency();
    }

    public List<Event> getActiveEvents() {
        List<Event> events = new ArrayList<Event>();
        Cursor cursor;
        cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_EVENTS + " where "
                + BudgetDatabase.COLUMN_FLAGS + " = " + Event.EVENT_ACTIVE, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            events.add(createEventFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return events;
    }

    public Event getEvent(long id) {
        Cursor cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_EVENTS + " where "
                + BudgetDatabase.COLUMN_ID + " = " + id, null);
        cursor.moveToFirst();

        if (cursor.getCount() == 1) {
            Event event = createEventFromCursor(cursor);
            cursor.close();
            return event;
        }
        cursor.close();
        return new Event();
    }

    private List<BudgetEntry> getEntriesForEvent(long eventId) {
        List<BudgetEntry> entries = new ArrayList<BudgetEntry>();

        Cursor cursor = database.rawQuery(
            "select " + BudgetDatabase.COLUMN_TRANSACTION_ID + " from " + BudgetDatabase.TABLE_EVENT_TRANSACTION
                    + " where " + BudgetDatabase.COLUMN_EVENT_ID + " = " + eventId, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            BudgetEntry entry = getTransaction(cursor.getLong(0));
            entries.add(entry);
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public List<Installment> getInstallments() {
        List<Installment> entries = new ArrayList<Installment>();

        Cursor cursor;
        cursor = database.rawQuery("select i.*, t.value, t.category, t.comment from "
                + BudgetDatabase.TABLE_INSTALLMENTS + " i join " + BudgetDatabase.TABLE_CASHFLOW + " t on i."
                + BudgetDatabase.COLUMN_TRANSACTION_ID + " = t." + BudgetDatabase.COLUMN_ID, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entries.add(createInstallmentFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public List<Installment> getUnpaidInstallments() {
        List<Installment> entries = new ArrayList<Installment>();

        Cursor cursor;
        cursor = database.rawQuery("select i.*, t.value, t.category, t.comment from "
                + BudgetDatabase.TABLE_INSTALLMENTS + " i join " + BudgetDatabase.TABLE_CASHFLOW + " t on i."
                + BudgetDatabase.COLUMN_TRANSACTION_ID + " = t." + BudgetDatabase.COLUMN_ID + " where i."
                + BudgetDatabase.COLUMN_FLAGS + " != " + Installment.INSTALLMENT_PAID, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entries.add(createInstallmentFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public List<BudgetEntry> getNegativeTransactions() {
        Cursor cursor;
        cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_CASHFLOW + " where "
                + BudgetDatabase.COLUMN_VALUE + " < 0", null);
        cursor.moveToFirst();
        List<BudgetEntry> entries = new ArrayList<BudgetEntry>();
        while (!cursor.isAfterLast()) {
            entries.add(createBudgetEntryFromCursor(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return entries;
    }

    public List<BudgetEntry> getTransactionsFromEvent(long eventId) {
        List<BudgetEntry> entries = new ArrayList<BudgetEntry>();
        Cursor cursor;
        cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_CASHFLOW + " where "
                + BudgetDatabase.COLUMN_ID + " in " + "(select " + BudgetDatabase.COLUMN_TRANSACTION_ID + " from "
                + BudgetDatabase.TABLE_EVENT_TRANSACTION + " where " + BudgetDatabase.COLUMN_EVENT_ID + " = " + eventId
                + ") order by " + BudgetDatabase.COLUMN_DATE + " desc", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            entries.add(createBudgetEntryFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public List<Event> getLinkedEventsFromTransactionId(long id) {
        List<Event> events = new ArrayList<Event>();
        Cursor cursor;
        cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_EVENTS + " where "
                + BudgetDatabase.COLUMN_ID + " in " + "(select " + BudgetDatabase.COLUMN_EVENT_ID + " from "
                + BudgetDatabase.TABLE_EVENT_TRANSACTION + " where " + BudgetDatabase.COLUMN_TRANSACTION_ID + " = "
                + id + ")", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            events.add(createEventFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return events;
    }

    public Event getEvent(String name) {
        Cursor cursor = database.rawQuery("select * from " + BudgetDatabase.TABLE_EVENTS + " where "
                + BudgetDatabase.COLUMN_NAME + " = " + "'" + name.replaceAll("['\"]", "\'") + "'", null);
        cursor.moveToFirst();

        if (cursor.getCount() == 1) {
            Event event = createEventFromCursor(cursor);
            cursor.close();
            return event;
        }
        cursor.close();
        return new Event();
    }

    private Event createEventFromCursor(Cursor cursor) {
        List<BudgetEntry> entries = getEntriesForEvent(cursor.getLong(0));

        Event event = new Event(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
            cursor.getString(4), cursor.getInt(5), entries);
        return event;
    }

    private BudgetEntry createBudgetEntryFromCursor(Cursor cursor) {
        BudgetEntry entry = new BudgetEntry(cursor.getLong(0), MoneyFactory.convertDoubleToMoney(cursor.getDouble(1)),
            cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getString(5));
        return entry;
    }

    private Installment createInstallmentFromCursor(Cursor cursor) {
        Installment installment = new Installment(cursor.getLong(0), cursor.getLong(1),
            MoneyFactory.convertDoubleToMoney(cursor.getDouble(2)), MoneyFactory.convertDoubleToMoney(cursor
                .getDouble(3)), cursor.getString(4), MoneyFactory.convertDoubleToMoney(cursor.getDouble(6)),
            cursor.getString(7), cursor.getString(8));
        installment.setFlags(cursor.getInt(5));
        return installment;
    }

    private Currency createCurrencyFromCursor(Cursor cursor) {
        Currency currency = new Currency(cursor.getLong(0), cursor.getString(1), cursor.getDouble(2), cursor.getInt(3));
        return currency;
    }

}
