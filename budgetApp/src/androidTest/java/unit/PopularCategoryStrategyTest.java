package unit;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import java.util.List;

import budgetapp.models.BudgetModel;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.entries.BudgetEntry;
import budgetapp.util.entries.CategoryEntry;
import budgetapp.util.favbuttz.strategies.PopularCategoryStrategy;
import budgetapp.util.money.Money;
import budgetapp.util.money.MoneyFactory;

public class PopularCategoryStrategyTest extends AndroidTestCase{

    private String prefix = "test";
    private String startDate = "2012/01/01 00:00";
    private RenamingDelegatingContext mockContext;
    private BudgetModel model;

    String mostPopularCategory = "XX";
    String secondMostPopularCategory = "AA";

    public void setUp() {
        BudgetFunctions.TESTING = true;

        BudgetFunctions.theDate = startDate;
        mockContext = new RenamingDelegatingContext(getContext(), getContext(), prefix);

        model = new BudgetModel(mockContext);

        Money.setExchangeRate(1.0);
        model.setDailyBudget(MoneyFactory.createMoney());

        assertTrue("Incorrect startDate", startDate.equalsIgnoreCase(BudgetFunctions.getDateString()));
        assertEquals("Incorrect starting budget.", 0.0, model.getCurrentBudget().get());
    }

    private List<CategoryEntry> setupCategories() {
        model.addCategory(mostPopularCategory);

        model.queueTransaction(new BudgetEntry(MoneyFactory.createMoneyFromNewDouble(-10), BudgetFunctions.getDateString(), mostPopularCategory));
        model.queueTransaction(new BudgetEntry(MoneyFactory.createMoneyFromNewDouble(-10), BudgetFunctions.getDateString(), mostPopularCategory));
        model.queueTransaction(new BudgetEntry(MoneyFactory.createMoneyFromNewDouble(-10), BudgetFunctions.getDateString(), secondMostPopularCategory));
        model.processWholeQueue();
        return model.getCategoriesSortedByName();
    }

    public void testGetMostPopularCategory() {
        List<CategoryEntry> categories = setupCategories();

        PopularCategoryStrategy strategy = new PopularCategoryStrategy(categories);
        assertEquals("Wrong category returned", mostPopularCategory, strategy.getCategory());
    }

    public void testGetSecondMostPopularCategory() {
        List<CategoryEntry> categories = setupCategories();

        PopularCategoryStrategy strategy = new PopularCategoryStrategy(categories, 1);
        assertEquals("Wrong category returned", secondMostPopularCategory, strategy.getCategory());
    }

    public void testNegativeSkipGetMostPopularCategory() {
        List<CategoryEntry> categories = setupCategories();

        PopularCategoryStrategy strategy = new PopularCategoryStrategy(categories, -10);
        assertEquals("Wrong category returned", mostPopularCategory, strategy.getCategory());
    }

    public void testMassiveSkipGetLeastPopularCategory() {
        List<CategoryEntry> categories = setupCategories();

        PopularCategoryStrategy strategy = new PopularCategoryStrategy(categories, 100);
        assertEquals("Wrong category returned", secondMostPopularCategory, strategy.getCategory());
    }

    public void tearDown() {
        BudgetFunctions.TESTING = false;

        model.clearDatabaseInstance();
    }
}
