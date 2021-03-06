package budgetapp.models;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import budgetapp.banks.BankTransaction;
import budgetapp.util.BankTransactionEntry;
import budgetapp.util.BudgetConfig;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.Currency;
import budgetapp.util.Event;
import budgetapp.util.IBudgetObserver;
import budgetapp.util.Installment;
import budgetapp.util.TransactionQueue;
import budgetapp.util.commands.Command;
import budgetapp.util.commands.PayOffInstallmentCommand;
import budgetapp.util.commands.TransactionCommand;
import budgetapp.util.database.BudgetDataSource;
import budgetapp.util.entries.BudgetEntry;
import budgetapp.util.entries.CategoryEntry;
import budgetapp.util.entries.DayEntry;
import budgetapp.util.money.Money;
import budgetapp.util.money.MoneyFactory;

public class BudgetModel {

    private BudgetDataSource datasource;
    private Money dailyBudget;
    private ArrayList<TransactionCommand> transactions;
    private String dateDailyBudgetLastQueued = "1990/01/01";
    private String dateInstallmentsLastQueued = "1990/01/01";

    private ArrayList<IBudgetObserver> observers;
    private boolean stateChanged;
    private TransactionQueue transactionQueue;
    private BudgetConfig config;

    public BudgetModel(Context context) {
        datasource = new BudgetDataSource(context);
        config = new BudgetConfig(context);
        Money.after = config.getBooleanValue(BudgetConfig.Fields.printCurrencyAfter);
        Money.setCurrency(config.getStringValue(BudgetConfig.Fields.currency));
        Money.setExchangeRate(config.getDoubleValue(BudgetConfig.Fields.exchangeRate));
        dailyBudget = MoneyFactory.convertDoubleToMoney(config.getDoubleValue(BudgetConfig.Fields.dailyBudget));
        transactions = new ArrayList<TransactionCommand>();
        observers = new ArrayList<IBudgetObserver>();
        transactionQueue = new TransactionQueue();
        stateChanged = true;
    }

    public synchronized void queueTransaction(BudgetEntry entry) {
        queueAddDailyBudget();
        if(entry.getCategory().equalsIgnoreCase("")) {
            return;
        }
        transactionQueue.queueItem(new TransactionCommand(datasource, entry));
        transactions.add(new TransactionCommand(datasource, entry));
    }

    public synchronized void queueTransaction(BudgetEntry entry, List<Long> eventIds) {
        queueAddDailyBudget();
        if(entry.getCategory().equalsIgnoreCase("")) {
            return;
        }
        transactionQueue.queueItem(new TransactionCommand(datasource, entry, eventIds));
        transactions.add(new TransactionCommand(datasource, entry, eventIds));
    }

    public void removeTransaction(BudgetEntry entry) {
        datasource.removeTransactionEntry(entry);
        stateChanged = true;
        notifyObservers();
    }

    public void editTransaction(long id, BudgetEntry newEntry) {
        datasource.editTransactionEntry(id, newEntry);
        stateChanged = true;
        notifyObservers();
    }

    public void editCurrency(long id, Currency newCurrency) {
        datasource.editCurrency(id, newCurrency);
        stateChanged = true;
        notifyObservers();
    }

    public void undoLatestTransaction() {
        if (transactions.size() > 0) {
            transactions.get(transactions.size() - 1).unexecute();
            transactions.remove(transactions.size() - 1);
            stateChanged = true;
            notifyObservers();
        }
    }

    /**
     * Used for testing to make sure transactions are in place before asserting
     */
    public void processWholeQueue() {
        for (int i = 0; i < transactionQueue.getSize(); i++) {
            transactionQueue.processQueueItem();
        }
        stateChanged = true;
        notifyObservers();
    }

    public Command processQueueItem() {
        Command processedCommand = transactionQueue.processQueueItem();
        stateChanged = true;
        return processedCommand;
    }

    public int getRemainingItemsInQueue() {
        return transactionQueue.getRemainingItems();
    }

    public Money getCurrentBudget() {
        return datasource.getCurrentBudget();
    }

    public int getQueueSize() {
        return transactionQueue.getSize();
    }

    public Money getDailyBudget() {
        return dailyBudget;
    }

    public List<String> getCategoryNames() {
        return datasource.getCategoryNames();
    }

    public List<Double> getAutocompleteValues() {
        return datasource.getAutocompleteValues();
    }

    public List<Double> getAutocompleteValues(String category) {
        return datasource.getAutocompleteValues(category);
    }

    public void setDailyBudget(Money budget) {
        dailyBudget = budget;
        config.writeValue(BudgetConfig.Fields.dailyBudget, dailyBudget.get());
        config.saveToFile();
        stateChanged = true;
        notifyObservers();
    }

    public boolean addCategory(String category) {
        if (!category.equalsIgnoreCase("")) {
            boolean result = datasource.addCategory(category);
            if (result) {
                stateChanged = true;
                notifyObservers();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean removeCategory(String category) {
        boolean result = datasource.removeCategory(category);
        if (result) {
            stateChanged = true;
            notifyObservers();
            return true;
        } else {
            return false;
        }
    }

    public boolean addBankTransaction(BankTransactionEntry bankTransaction) {
        boolean result = datasource.addBankTransaction(bankTransaction);
        if (result) {
            stateChanged = true;
            notifyObservers();
            return true;
        } else {
            return false;
        }
    }

    public List<BankTransactionEntry> getBankTransactions() {
        return datasource.getAllBankTransactions();
    }

    public boolean isBankTransactionProcessed(BankTransaction transaction) {
        return datasource.isBankTransactionProcessed(transaction);
    }

    public void readdCategories(List<String> categories) {
        for (String c : categories) {
            removeCategory(c);
            addCategory(c);
        }
    }

    public boolean addInstallment(Installment installment) {
        boolean result = datasource.createInstallment(installment);

        if (result) {
            stateChanged = true;
            notifyObservers();
            return true;
        } else {
            return false;
        }
    }

    public boolean addEvent(Event event) {
        boolean result = datasource.createEvent(event);

        if (result) {
            stateChanged = true;
            notifyObservers();
            return true;
        }
        return false;
    }

    public boolean addCurrency(Currency currency) {
        boolean result = datasource.createCurrency(currency);

        if (result) {
            stateChanged = true;
            notifyObservers();
            return true;
        }
        return false;
    }

    public void editEvent(long id, Event newEvent) {
        datasource.editEvent(id, newEvent);
        stateChanged = true;
        notifyObservers();
    }

    public void removeEvent(long id) {
        datasource.removeEvent(id);
        stateChanged = true;
        notifyObservers();
    }

    public List<Long> getIdsOfActiveEvents() {
        return datasource.getIdsOfActiveEvents();
    }

    public List<Event> getActiveEvents() {
        return datasource.getActiveEvents();
    }

    public long getIdFromEventName(String eventName) {
        return datasource.getIdFromEventName(eventName);
    }

    public List<Event> getLinkedEventsFromTransactionId(long id) {
        return datasource.getLinkedEventsFromTransactionId(id);
    }

    public void linkTransactionToEvent(long transactionId, long eventId) {
        datasource.addTransactionToEvent(transactionId, eventId);
    }

    public void removeTransactionFromEvents(long transactionId) {
        datasource.removeTransactionFromEvents(transactionId);
    }

    public void editInstallment(long id, Installment newInstallment) {
        datasource.editInstallment(id, newInstallment);
        stateChanged = true;
        notifyObservers();

    }

    public boolean removeInstallment(long id) {
        boolean result = datasource.markInstallmentAsPaid(id);
        if (result) {
            stateChanged = true;
            notifyObservers();
            return true;
        } else {
            return false;
        }
    }

    public List<Installment> getInstallments() {
        return datasource.getInstallments();
    }

    public List<Event> getEvents() {
        return datasource.getEvents();
    }

    public Event getEvent(long id) {
        return datasource.getEvent(id);
    }

    public List<Currency> getCurrencies() {
        return datasource.getCurrencies();
    }

    public Currency getCurrency(long id) {
        return datasource.getCurrency(id);
    }

    public void removeCurrency(long id) {
        datasource.removeCurrency(id);
        stateChanged = true;
        notifyObservers();
    }

    public Installment getInstallment(long id) {
        return datasource.getInstallment(id);
    }

    public BudgetEntry getTransaction(long id) {
        return datasource.getTransaction(id);
    }

    public List<BudgetEntry> getNegativeTransactions() {
        return datasource.getNegativeTransactions();
    }

    public List<BudgetEntry> getSomeTransactions(int n, String orderBy) {
        return datasource.getSomeTransactions(n, orderBy);
    }

    public List<BudgetEntry> getTransactionsFromEvent(long eventId) {
        return datasource.getTransactionsFromEvent(eventId);
    }

    public List<DayEntry> getSomeDays(int n, String orderBy) {
        return datasource.getSomeDays(n, orderBy);
    }

    public synchronized int queueAddDailyBudget() {
        if (isDateStringEqualToToday(dateDailyBudgetLastQueued)) {
            return 0;
        }
        Calendar nextDay = Calendar.getInstance();
        nextDay.set(BudgetFunctions.getYear(), BudgetFunctions.getMonth(), BudgetFunctions.getDay(), 0, 0);
        nextDay.add(Calendar.DAY_OF_MONTH, 1);

        List<DayEntry> lastDay = datasource.getSomeDays(1, BudgetDataSource.DESCENDING);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        int daysAdded = 0;
        if (!lastDay.isEmpty()) {

            String lastDayString = lastDay.get(0).getDate();
            Calendar lastDayCalendar = Calendar.getInstance();
            // Convert the string to a Calendar time. Subtract 1 from month because month 0 = January
            // Set HH:mm to 00:00
            lastDayCalendar.set(Integer.parseInt(lastDayString.substring(0, 4)),
                Integer.parseInt(lastDayString.substring(5, 7)) - 1, Integer.parseInt(lastDayString.substring(8, 10)),
                0, 0);

            lastDayCalendar.add(Calendar.DAY_OF_MONTH, 1); // We want to start counting from the first day without
                                                           // transactions

            Calendar tempDate = (Calendar) lastDayCalendar.clone();
            SimpleDateFormat compareFormat = new SimpleDateFormat("yyyy/MM/dd");

            while (tempDate.before(nextDay)) {
                if (!compareFormat.format(tempDate.getTime()).equalsIgnoreCase(compareFormat.format(nextDay.getTime()))) {
                    BudgetEntry entry = new BudgetEntry(new Money(dailyBudget), dateFormat.format(tempDate.getTime()),
                        "Income");
                    System.out.println("Queueing daily budget");
                    transactionQueue.queueItem(new TransactionCommand(datasource, entry));

                    daysAdded++;
                }
                tempDate.add(Calendar.DAY_OF_MONTH, 1);
            }
            tempDate.add(Calendar.DAY_OF_MONTH, -1);
            dateDailyBudgetLastQueued = dateFormat.format(tempDate.getTime());
        } else { // Add a transaction of 0
            Calendar tempDate = Calendar.getInstance();
            BudgetEntry entry = new BudgetEntry(Money.zero(), dateFormat.format(tempDate.getTime()),
                "Income");
            transactionQueue.queueItem(new TransactionCommand(datasource, entry));
            tempDate.add(Calendar.DAY_OF_MONTH, -1);
            dateDailyBudgetLastQueued = dateFormat.format(tempDate.getTime());
            daysAdded = 1;
        }
        return daysAdded;
    }

    private boolean isDateStringEqualToToday(String compareString) {
        Calendar today = Calendar.getInstance();
        today.set(BudgetFunctions.getYear(), BudgetFunctions.getMonth(), BudgetFunctions.getDay(), 0, 0);

        Calendar lastDayQueuedCalender = Calendar.getInstance();
        lastDayQueuedCalender
            .set(Integer.parseInt(compareString.substring(0, 4)), Integer.parseInt(compareString.substring(5, 7)) - 1,
                Integer.parseInt(compareString.substring(8, 10)), 0, 0);

        SimpleDateFormat compareFormat = new SimpleDateFormat("yyyy/MM/dd");
        if (compareFormat.format(today.getTime()).equalsIgnoreCase(
            compareFormat.format(lastDayQueuedCalender.getTime()))) {
            return true;
        }
        return false;
    }

    public void pauseInstallment(long id) {
        datasource.markInstallmentAsPaused(id, true);
    }

    public void unpauseInstallment(long id) {
        datasource.markInstallmentAsPaused(id, false);
    }

    public synchronized void queuePayOffInstallments() {
        List<Installment> installments = datasource.getUnpaidInstallments();
        if (isDateStringEqualToToday(dateInstallmentsLastQueued)) {
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        SimpleDateFormat compareFormat = new SimpleDateFormat("yyyy/MM/dd");
        for (int i = 0; i < installments.size(); i++) {
            if (installments.get(i).isPaused()) {
                installments.get(i).setDateLastPaid(BudgetFunctions.getDateString());
                datasource.editInstallment(installments.get(i).getId(), installments.get(i));
                continue;
            }
            String lastDayString = installments.get(i).getDateLastPaid();
            Calendar lastDayCalendar = Calendar.getInstance();
            // Convert the string to a Calendar time. Subtract 1 from month because month 0 = January
            // Set HH:mm to 00:00
            lastDayCalendar.set(Integer.parseInt(lastDayString.substring(0, 4)),
                Integer.parseInt(lastDayString.substring(5, 7)) - 1, Integer.parseInt(lastDayString.substring(8, 10)),
                0, 0);

            lastDayCalendar.add(Calendar.DAY_OF_MONTH, 1); // We want to start counting from the first day without
                                                           // transactions

            // Step up to the day before tomorrow
            Calendar nextDay = Calendar.getInstance();
            nextDay.set(BudgetFunctions.getYear(), BudgetFunctions.getMonth(), BudgetFunctions.getDay(), 0, 0);
            nextDay.add(Calendar.DAY_OF_MONTH, 1);

            Calendar tempDate = (Calendar) lastDayCalendar.clone();

            while (tempDate.before(nextDay)) {
                String tempDateString = compareFormat.format(tempDate.getTime());
                if (!tempDateString.equalsIgnoreCase(compareFormat.format(nextDay.getTime()))) {
                    transactionQueue.queueItem(new PayOffInstallmentCommand(datasource, installments.get(i),
                        tempDateString));
                    dateInstallmentsLastQueued = dateFormat.format(tempDate.getTime());
                }
                tempDate.add(Calendar.DAY_OF_MONTH, 1);
            }

        }
    }

    public List<CategoryEntry> getCategoriesSortedByNum() {
        return datasource.getCategoriesSortedByNum();
    }

    public List<CategoryEntry> getCategoriesSortedByNumDesc() {
        return datasource.getCategoriesSortedByNumDesc();
    }

    public List<CategoryEntry> getCategoriesSortedByValue() {
        return datasource.getCategoriesSortedByValue();
    }

    public List<CategoryEntry> getCategoriesSortedByName() {
        return datasource.getCategoriesSortedByName();
    }

    public void saveConfig() {
        config.writeValue(BudgetConfig.Fields.currency, Money.getCurrency());
        config.writeValue(BudgetConfig.Fields.printCurrencyAfter, Money.after);
        config.writeValue(BudgetConfig.Fields.exchangeRate, Money.getExchangeRate());
        config.saveToFile();
    }

    public void clearDatabaseInstance() {
        datasource.clearDatabaseInstance();
    }

    public void clearAutocompleteValues() {
        datasource.clearAutocompleteValues();
    }

    public void addObserver(IBudgetObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        if (stateChanged) {
            for (int i = 0; i < observers.size(); i++) {
                observers.get(i).update();
            }
            stateChanged = false;
        }

    }

}
