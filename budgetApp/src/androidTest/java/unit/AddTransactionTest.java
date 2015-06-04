package unit;

import java.util.List;

import budgetapp.models.BudgetModel;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.database.BudgetDataSource;
import budgetapp.util.entries.BudgetEntry;
import budgetapp.util.money.Money;
import budgetapp.util.money.MoneyFactory;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;


public class AddTransactionTest extends AbstractIntegrationTest{

	public void testAddTransaction()
	{
		double value = 100;
		BudgetEntry entry = new BudgetEntry(MoneyFactory.createMoneyFromNewDouble(value), BudgetFunctions.getDateString(), "test","comment",2);
		assertEquals("Value of entry not correct", value, entry.getValue().get());
		assertTrue("Category not correct.", entry.getCategory().equalsIgnoreCase("test"));
		assertTrue("Comment not correct.", entry.getComment().equalsIgnoreCase("comment"));
		assertEquals("Flags not correct", 2, entry.getFlags());
		model.queueTransaction(entry);
		model.processWholeQueue();
		List<BudgetEntry> transactions = model.getSomeTransactions(1, BudgetDataSource.DESCENDING);
		entry = transactions.get(0);
		
		assertEquals("Value of entry not correct after adding transaction. Found " + entry.getCategory() + " date " + entry.getDate(), value, entry.getValue().get());
		assertEquals("Value of current budget not correct", value, model.getCurrentBudget().get());
		assertTrue("Category not correct after adding transaction.", entry.getCategory().equalsIgnoreCase("test"));
		assertTrue("Comment not correct after adding transaction.", entry.getComment().equalsIgnoreCase("comment"));
		assertEquals("Flags not correct after adding transaction", 2, entry.getFlags());
		
	}
	
	public void testAddTransactionHighExchangeRate()
	{
		double value = 100;
		double exchangeRate = 2;
		Money.setExchangeRate(exchangeRate);
		
		BudgetEntry startEntry = new BudgetEntry(MoneyFactory.createMoneyFromNewDouble(value), BudgetFunctions.getDateString(), "test");
		assertEquals("Value of entry incorrect", value * exchangeRate, startEntry.getValue().get());
		model.queueTransaction(startEntry);
		model.processWholeQueue();
		List<BudgetEntry> transactions = model.getSomeTransactions(1, BudgetDataSource.DESCENDING);
		BudgetEntry entry = transactions.get(0);
		
		assertEquals("Value of entry not correct after adding transaction", value * exchangeRate, entry.getValue().get());
		assertEquals("Value of current budget not correct", value, model.getCurrentBudget().get() / Money.getExchangeRate());
	}
	
	public void testAddTransactionLowExchangeRate()
	{
		double value = 100;
		double exchangeRate = .2;
		Money.setExchangeRate(exchangeRate);
		
		model.queueTransaction(new BudgetEntry(MoneyFactory.createMoneyFromNewDouble(value), BudgetFunctions.getDateString(), "test"));
		model.processWholeQueue();
		List<BudgetEntry> transactions = model.getSomeTransactions(1, BudgetDataSource.DESCENDING);
		BudgetEntry entry = transactions.get(0);
		
		assertEquals("Value of entry not correct after adding transaction", value * exchangeRate, entry.getValue().get());
		assertEquals("Value of current budget not correct", value * exchangeRate, model.getCurrentBudget().get());
	}

    public void testDateSorting() {
        String[] expectedSortedDates = {
                "2026/11/12 16:23",
                "2026/10/13 18:28",
                "2025/12/23 20:51",
                "2025/11/12 16:23",
                "2025/10/12 16:23"
        };

        for(int i = 0; i < expectedSortedDates.length; i++) {
            model.queueTransaction(new BudgetEntry(MoneyFactory.createMoneyFromNewDouble(i+1), expectedSortedDates[i], "number " + i));
        }

        model.processWholeQueue();

        List<BudgetEntry> descTransactions = model.getSomeTransactions(0, BudgetDataSource.DESCENDING);
        for(int i = 0; i < expectedSortedDates.length; i++) {
            BudgetEntry b = descTransactions.get(i);
            assertEquals("Entry " + i+ " had wrong date. Found " + b.getCategory(), expectedSortedDates[i], b.getDate());
        }

     }


}
