package unit;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import budgetapp.models.BudgetModel;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.entries.BudgetEntry;
import budgetapp.util.money.Money;
import budgetapp.util.money.MoneyFactory;

public abstract class AbstractIntegrationTest extends AndroidTestCase {

    protected BudgetModel model;
    protected RenamingDelegatingContext mockContext;
    protected String prefix = "test";
    protected String dateForInitialTransaction = "2022/01/01 00:00";
    protected String startDate = "2022/01/01 00:01";

    public void setUp() {
        BudgetFunctions.TESTING = true;
        mockContext = new RenamingDelegatingContext(getContext(), getContext(), prefix);

        model = new BudgetModel(mockContext);
        model.clearDatabaseInstance();
        model = new BudgetModel(mockContext);

        BudgetFunctions.theDate = dateForInitialTransaction;
        Money.setExchangeRate(1.0);
        model.setDailyBudget(MoneyFactory.createMoney());

        model.queueTransaction(new BudgetEntry(MoneyFactory.createMoney(),BudgetFunctions.getDateString(),"initialTransaction"));

        model.processWholeQueue();
        BudgetFunctions.theDate = startDate;
        assertEquals("Incorrect starting budget.", 0.0,model.getCurrentBudget().get());
        assertEquals("Incorrect startDate", startDate, BudgetFunctions.theDate);

    }

    public void tearDown() {
        BudgetFunctions.TESTING = false;
        model.clearDatabaseInstance();
    }

    protected void addDays(int n) {
        BudgetFunctions.theDate = HelperFunctions.addDays(BudgetFunctions.theDate, n);
    }
}
