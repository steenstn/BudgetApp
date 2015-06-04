package budgetapp.util.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import java.util.Collections;
import java.util.List;

import budgetapp.banks.BankTransaction;
import budgetapp.util.BankTransactionEntry;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.Currency;
import budgetapp.util.Event;
import budgetapp.util.Installment;
import budgetapp.util.entries.BudgetEntry;
import budgetapp.util.entries.CategoryEntry;
import budgetapp.util.entries.DatabaseEntry;
import budgetapp.util.entries.DayEntry;
import budgetapp.util.money.Money;
import budgetapp.util.money.MoneyFactory;

public class BudgetDataSource {

    private static SQLiteDatabase database;
    private static BudgetDatabase dbHelper;
    private static DatabaseAccess dbAccess;
    public static final String ASCENDING = "asc";
    public static final String DESCENDING = "desc";
    private Context context;

    public BudgetDataSource(Context context) {
        this.context = context;
        dbHelper = BudgetDatabase.getInstance(context);
        open();
    }

    public void clearDatabaseInstance() {
        BudgetDatabase.clearInstance();
    }

    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        dbAccess = new DatabaseAccess(database);
    }

    /** Creates a transaction entry and updates the affected tables.
     * 
     * @param theEntry
     *            The entry to add
     * @return The entry that was added */
    public BudgetEntry createTransactionEntry(BudgetEntry theEntry) {
        BudgetEntry workingEntry = theEntry.clone();

        BudgetEntry result = dbAccess.addEntry(workingEntry);
        if (result != null) {
            addToAutocompleteValues(result.getValue().get(), workingEntry.getCategory());

            addToCategory(workingEntry.getCategory(), workingEntry.getValue().get());
        }
        return result;

    }

    public void addTransactionToEvent(long transactionId, long eventId) {
        dbAccess.addTransactionToEvent(transactionId, eventId);
    }

    public void removeTransactionFromEvents(long transactionId) {
        dbAccess.removeTransactionFromEvents(transactionId);
    }

    public long getIdFromEventName(String eventName) {
        return dbAccess.getIdFromEventName(eventName);
    }

    public Money getCurrentBudget() {
        return dbAccess.getCurrentBudget();
    }

    public List<Event> getLinkedEventsFromTransactionId(long id) {
        return dbAccess.getLinkedEventsFromTransactionId(id);
    }

    /** Removes a transaction entry from the database and updates the affected tables
     * 
     * @param theEntry
     *            The entry to remove
     * @return true if the removal was successful */
    public boolean removeTransactionEntry(BudgetEntry theEntry) {
        // Get the entry from the database, it may have been edited
        BudgetEntry workingEntry = dbAccess.getEntry(theEntry.getId());
        boolean result = dbAccess.removeEntry(theEntry);

        if (result) {
            removeTransactionFromEvents(workingEntry.getId());
            switch (workingEntry.getFlags()) {
            case DatabaseEntry.NORMAL_TRANSACTION:
                removeFromCategory(workingEntry.getCategory(), workingEntry.getValue().get() * -1);

                break;
            case DatabaseEntry.INSTALLMENT_TRANSACTION:
                removeFromCategory(workingEntry.getCategory(), workingEntry.getValue().get() * -1);

                break;
            }
        }
        return result;
    }

    public void editTransactionEntry(long id, BudgetEntry newEntry) {
        BudgetEntry workingEntry = newEntry.clone();
        BudgetEntry oldEntry = getTransaction(id);
        updateTransaction(oldEntry.getId(), workingEntry);

        // New category, move the entry from old category to new
        if (!oldEntry.getCategory().equalsIgnoreCase(workingEntry.getCategory())) {
            removeFromCategory(oldEntry.getCategory(), oldEntry.getValue().get() * -1);
            addToCategory(workingEntry.getCategory(), oldEntry.getValue().get());
        }

    }

    public void editInstallment(long id, Installment newInstallment) {
        updateInstallment(id, newInstallment.getTotalValue().get(), newInstallment.getDailyPayment().get(),
            newInstallment.getDateLastPaid(), newInstallment.getFlags());
    }

    public void editCurrency(long id, Currency newCurrency) {
        dbAccess.updateCurrency(id, newCurrency);
    }

    public void editEvent(long id, Event newEvent) {
        dbAccess.updateEvent(id, newEvent.getName(), newEvent.getStartDate(), newEvent.getEndDate(),
            newEvent.getComment(), newEvent.getFlags());
    }

    public List<Long> getIdsOfActiveEvents() {
        return dbAccess.getIdsOfActiveEvents();
    }

    /** Edit transaction and add the value to today's daily flow
     *
     * @param newEntry */
    public void editTransactionEntryToday(long id, BudgetEntry newEntry, String date) {
        BudgetEntry workingEntry = newEntry.clone();
        // Add the exchange rate to the entry
        workingEntry.setValue(workingEntry.getValue());
        BudgetEntry oldEntry = getTransaction(id);
        updateTransaction(oldEntry.getId(), workingEntry);

        // New category, move the entry from old category to new
        if (!oldEntry.getCategory().equalsIgnoreCase(workingEntry.getCategory())) {
            removeFromCategory(oldEntry.getCategory(), oldEntry.getValue().get() * -1);
            addToCategory(workingEntry.getCategory(), oldEntry.getValue().get());
        }

        if (!oldEntry.getValue().equals(workingEntry.getValue())) {
            BudgetEntry oldEntryClone = oldEntry.clone();
            oldEntryClone.setDate(date);

        }
    }

    public CategoryEntry createCategoryEntry(CategoryEntry theEntry) {
        CategoryEntry result = dbAccess.addEntry(theEntry);
        return result;
    }

    /** Creates a transaction in the database, an installment and links them together
     * 
     * @param installment
     * @return */
    public boolean createInstallment(Installment installment) {
        Money dailyPayment = installment.getDailyPayment();

        String dateLastPaid = installment.getDateLastPaid();
        String category = installment.getCategory();
        String comment = installment.getComment();
        BudgetEntry initialPayment = new BudgetEntry(dailyPayment, dateLastPaid, category, comment,
            DatabaseEntry.INSTALLMENT_TRANSACTION);

        initialPayment = createTransactionEntry(initialPayment);

        installment.setTransactionId(initialPayment.getId());

        long result = dbAccess.addInstallment(installment);
        //long dailyFlowId = dbAccess.getIdFromDayFlow(initialPayment.getDate());
        //dbAccess.addInstallmentPayment(result, dailyFlowId, dailyPayment.get());

        return result != -1;

    }

    public boolean createEvent(Event event) {
        Event eventDuplicate = dbAccess.getEvent(event.getName());
        if (eventDuplicate.getId() == -1) {
            long result = dbAccess.addEvent(event);
            return result != -1;
        }
        return false;
    }

    public boolean createCurrency(Currency currency) {
        long result = dbAccess.addCurrency(currency);
        return result != -1;
    }

    public void removeEvent(long id) {
        dbAccess.removeEvent(id);
    }

    /** Do a payment on an installment
     * 
     * @param installment
     *            - The installment to pay off
     * @param dateToEdit
     *            - The date to change daily flow for */
    public Money payOffInstallment(Installment installment, String dateToEdit) {
        Installment dbInstallment = dbAccess.getInstallment(installment.getId());
        if (dbInstallment.getId() == -1 || dbInstallment.isPaidOff() || dbInstallment.isPaused()) {
            return Money.zero();
        }
        Money remainingValue = dbInstallment.getRemainingValue();
        if (remainingValue.makePositive().almostZero()) {
            markInstallmentAsPaid(dbInstallment.getId());
            return Money.zero();
        }

        /*if (remainingValue.makePositive().smallerThan(dailyPay.makePositive())) { // Don't pay too much
            dailyPay = remainingValue;
        }*/
        Money dailyPay = dbInstallment.calculateDailyPayment();

        BudgetEntry oldEntry = getTransaction(installment.getTransactionId());
        BudgetEntry newEntry = oldEntry.clone();
        newEntry.setValue(new Money(oldEntry.getValue().add(dailyPay)));
        editTransactionEntryToday(oldEntry.getId(), newEntry, dateToEdit);

        //dbAccess.addInstallmentPayment(installment.getId(), dbAccess.getIdFromDayFlow(dateToEdit), dailyPay.get());

        updateInstallment(installment.getId(), dbInstallment.getTotalValue().get(), installment
            .getDailyPayment()
            .get(), BudgetFunctions.getDateString(), dbInstallment.getFlags());

        return new Money(dailyPay);

    }

    /** Gets all transactions in the database ordered by id
     * 
     * @param orderBy
     *            - BudgetDatbase.ASCENDING/BudgetDatabase.DESCENDING
     * @return An ArrayList of all entries */
    public List<BudgetEntry> getAllTransactions(String orderBy) {
        return dbAccess.getTransactions(0, orderBy);
    }

    public BudgetEntry getTransaction(long id) {
        return dbAccess.getTransaction(id);
    }

    /** Gets n number of DayEntries from daily cash flow table sorted by id
     * 
     * @param n
     *            - Number of transactions to fetch. Fetches all if n <= 0
     * @param orderBy
     *            - BudgetDatabase.ASCENDING/BudgetDatabase.DESCENDING
     * @return An ArrayList of the entries */
    public List<DayEntry> getSomeDays(int n, String orderBy) {
        return dbAccess.getDaySumCalculated(n, orderBy);
    }

    /** Gets n number of transactions. Returns all transactions if n <= 0
     * 
     * @param n
     *            - Number of entries to get
     * @param orderBy
     *            - BudgetDatabase.ASCENDING/BudgetDatabase.DESCENDING
     * @return An ArrayList of the entries */
    public List<BudgetEntry> getSomeTransactions(int n, String orderBy) {
        return dbAccess.getTransactions(n, orderBy);
    }

    public List<CategoryEntry> getCategoriesSortedByValue() {
        return dbAccess.getCategories(null, null, null, null, BudgetDatabase.COLUMN_VALUE);
    }

    public List<CategoryEntry> getCategoriesSortedByName() {
        return dbAccess.getCategories(null, null, null, null, BudgetDatabase.COLUMN_CATEGORY);
    }

    public List<CategoryEntry> getCategoriesSortedByNum() {
        return dbAccess.getCategories(null, null, null, null, BudgetDatabase.COLUMN_NUM);
    }

    public List<CategoryEntry> getCategoriesSortedByNumDesc() {
        List<CategoryEntry> categories = dbAccess.getCategories(null, null, null, null, BudgetDatabase.COLUMN_NUM);
        Collections.reverse(categories);
        return categories;
    }

    public List<String> getCategoryNames() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        boolean autoSortCategories = settings.getBoolean("autoSortCategories", false);
        if (autoSortCategories) {
            return dbAccess.getCategoryNamesSorted();
        }
        return dbAccess.getCategoryNames();

    }

    public List<Double> getAutocompleteValues() {
        return dbAccess.getAutocompleteValues();
    }

    public List<Double> getAutocompleteValues(String category) {
        List<Double> result;
        result = dbAccess.getAutocompleteValues(category);

        return result;
    }

    public List<Installment> getInstallments() {
        return dbAccess.getInstallments();
    }

    public List<Event> getEvents() {
        return dbAccess.getEvents();
    }

    public Event getEvent(long id) {
        return dbAccess.getEvent(id);
    }

    public List<Event> getActiveEvents() {
        return dbAccess.getActiveEvents();
    }

    public List<Currency> getCurrencies() {
        return dbAccess.getCurrencies();
    }

    public Currency getCurrency(long id) {
        return dbAccess.getCurrency(id);
    }

    public void removeCurrency(long id) {
        dbAccess.removeCurrency(id);
    }

    public List<Installment> getUnpaidInstallments() {
        return dbAccess.getUnpaidInstallments();
    }

    public Installment getInstallment(long id) {
        return dbAccess.getInstallment(id);
    }

    public boolean addCategory(String theCategory) {
        return dbAccess.addCategory(theCategory);
    }

    public boolean addBankTransaction(BankTransactionEntry bankTransaction) { return dbAccess.addBankTransaction(bankTransaction); }

    public List<BankTransactionEntry> getAllBankTransactions() {
        return dbAccess.getBankTransactions();
    }

    public boolean isBankTransactionProcessed(BankTransaction bankTransaction) {
        return dbAccess.isBankTransactionProcessed(bankTransaction);
    }

    public boolean removeCategory(String theCategory) {
        return dbAccess.removeCategory(theCategory);
    }

    public boolean markInstallmentAsPaid(long id) {

        Installment temp = dbAccess.getInstallment(id);
        temp.setPaidOff(true);
        int flags = temp.getFlags();

        return dbAccess.setFlags(id, flags);
    }

    public boolean markInstallmentAsPaused(long id, boolean state) {
        Installment temp = dbAccess.getInstallment(id);
        temp.setPaused(state);
        int flags = temp.getFlags();

        return dbAccess.setFlags(id, flags);
    }

    public void clearAutocompleteValues() {
        dbAccess.clearAutocompleteValues();
    }

    // Helper functions to update different tables correctly
    private void addToAutocompleteValues(double value, String category) {
        dbAccess.addAutocompleteValue(value, category);
    }

    private void addToCategory(String theCategory, double value) {
        dbAccess.addToCategory(theCategory, value);
    }

    private void removeFromCategory(String theCategory, double value) {
        dbAccess.removeFromCategory(theCategory, value);
    }

    private void updateTransaction(long id, BudgetEntry newEntry) {
        dbAccess.updateTransaction(id, newEntry);
    }

    private boolean updateInstallment(long id, double newTotalValue, double newDailyPay, String newDateLastPaid,
            int newFlags) {
        return dbAccess.updateInstallment(id, newTotalValue, newDailyPay, newDateLastPaid, newFlags);
    }

    public List<BudgetEntry> getNegativeTransactions() {
        return dbAccess.getNegativeTransactions();
    }

    public List<BudgetEntry> getTransactionsFromEvent(long eventId) {
        List<BudgetEntry> result;
        result = dbAccess.getTransactionsFromEvent(eventId);
        return result;
    }

}
