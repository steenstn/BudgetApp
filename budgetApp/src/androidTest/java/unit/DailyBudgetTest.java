package unit;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import budgetapp.models.BudgetModel;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.database.BudgetDataSource;
import budgetapp.util.entries.BudgetEntry;
import budgetapp.util.money.Money;
import budgetapp.util.money.MoneyFactory;
import unit.HelperFunctions;

public class DailyBudgetTest
    extends AndroidTestCase {

    String prefix = "test";
    String dateForInitialTransaction = "2022/01/01 00:00";
    String startDate = "2022/01/01 00:01";
    RenamingDelegatingContext mockContext;
    BudgetModel model;

    public void setUp() {
        BudgetFunctions.TESTING = true;
        mockContext = new RenamingDelegatingContext(getContext(), getContext(), prefix);

        model = new BudgetModel(mockContext);
        model.clearDatabaseInstance();
        model = new BudgetModel(mockContext);

        BudgetFunctions.theDate = dateForInitialTransaction;
        Money.setExchangeRate(1.0);
        model.setDailyBudget(MoneyFactory.createMoney());

        model.queueTransaction(new BudgetEntry(MoneyFactory.createMoney(), BudgetFunctions.getDateString(), "test"));
        model.processWholeQueue();
        BudgetFunctions.theDate = startDate;
        assertEquals("Incorrect starting budget.", 0.0, model.getCurrentBudget().get());
        assertEquals("Incorrect startDate", startDate, BudgetFunctions.theDate);

    }

    public void testDailyBudget() {

        int numberOfDays = 8;
        double dailyBudget = 100;
        model.setDailyBudget(MoneyFactory.createMoneyFromNewDouble(dailyBudget));

        addDays(numberOfDays);
        model.queueAddDailyBudget();
        model.processWholeQueue();
        assertEquals("Incorrect budget after adding daily budget.", dailyBudget * numberOfDays, model
                .getCurrentBudget()
                .get());
    }

    public void testDailyBudgetHighExchangeRate() {

        int numberOfDays = 1;
        double dailyBudget = 100;
        double exchangeRate = 2.0;
        Money.setExchangeRate(exchangeRate);
        model.setDailyBudget(MoneyFactory.createMoneyFromNewDouble(dailyBudget));
        assertEquals("Daily budget not correct", dailyBudget * exchangeRate, model.getDailyBudget().get());
        addDays(numberOfDays);
        model.queueAddDailyBudget();
        model.processWholeQueue();
        assertEquals("Inncorrect budget after adding daily budget.",
            dailyBudget * numberOfDays * Money.getExchangeRate(), model.getCurrentBudget().get());
    }

    public void testDailyBudgetLowExchangeRate() {

        int numberOfDays = 8;
        double dailyBudget = 100;
        Money.setExchangeRate(0.1);
        model.setDailyBudget(MoneyFactory.createMoneyFromNewDouble(dailyBudget));

        addDays(numberOfDays);
        model.queueAddDailyBudget();
        model.processWholeQueue();
        assertEquals("Inncorrect budget after adding daily budget.",
            dailyBudget * numberOfDays * Money.getExchangeRate(), model.getCurrentBudget().get());
    }

    public void testMultipleQueuing() {
        addDays(1);
        model.setDailyBudget(MoneyFactory.createMoneyFromNewDouble(100));
        int days1 = model.queueAddDailyBudget();
        int days2 = model.queueAddDailyBudget();
        model.processWholeQueue();

        assertEquals(1, days1);
        assertEquals(0, days2);
        assertEquals(100.0, model.getCurrentBudget().get());

    }

    public void tearDown() {
        BudgetFunctions.TESTING = false;

        model.clearDatabaseInstance();
    }

    private void addDays(int n) {
        BudgetFunctions.theDate = HelperFunctions.addDays(BudgetFunctions.theDate, n);
    }

}
