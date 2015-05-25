package unit;

import java.util.List;

import budgetapp.models.BudgetModel;
import budgetapp.util.entries.*;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.database.BudgetDataSource;
import budgetapp.util.money.Money;
import budgetapp.util.money.MoneyFactory;
import unit.HelperFunctions;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class EditTransactionTest extends AndroidTestCase{
	
	BudgetModel model;
	RenamingDelegatingContext mockContext;
	String prefix = "test";
    String initalDate = "2022/01/01 00:00";
	String startDate = "2022/01/01 00:01";
	
	public void setUp()
	{
		BudgetFunctions.TESTING = true;
		mockContext = new RenamingDelegatingContext(getContext(), getContext(), prefix);

        model = new BudgetModel(mockContext);
        model.clearDatabaseInstance();
        model = new BudgetModel(mockContext);

        BudgetFunctions.theDate = initalDate;
		Money.setExchangeRate(1.0);
		model.setDailyBudget(MoneyFactory.createMoney());
		
		model.queueTransaction(new BudgetEntry(MoneyFactory.createMoney(),BudgetFunctions.getDateString(),"test"));
        BudgetFunctions.theDate = startDate;
		assertEquals("Incorrect starting budget.", 0.0,model.getCurrentBudget().get());
		assertEquals("Incorrect startDate", startDate, BudgetFunctions.theDate);
	
	}
	
	public void testEditTransaction() {
		double originalValue = 100;
		double newValue = 200;
		int numDays = 5;
		
		double expectedValue = (numDays - 1) * originalValue + newValue;
		
		for(int i = 0; i < numDays; i++) {
			model.queueTransaction(new BudgetEntry(MoneyFactory.createMoneyFromNewDouble(originalValue),BudgetFunctions.getDateString(),"test"));
			addDays(1);
		}
        model.processWholeQueue();
		// Change value of the first transaction
		List<BudgetEntry> entries = model.getSomeTransactions(1, BudgetDataSource.DESCENDING);
		BudgetEntry entry = entries.get(0);
        assertEquals("Incorrect value on transaction got" + entry.getDate(), originalValue, entry.getValue().get());
		BudgetEntry newEntry = entry.clone();
		
		newEntry.setValue(MoneyFactory.createMoneyFromNewDouble(newValue));
		
		model.editTransaction(entry.getId(), newEntry);

        BudgetEntry editedEntry = model.getTransaction(entry.getId());
		
		assertEquals("Wrong value after editing transaction.", newValue,  editedEntry.getValue().get());
		assertEquals("Current budget wrong after editing transaction", expectedValue, model.getCurrentBudget().get());
	}
	
	public void testEditTransactionHighExchangeRate()
	{
		double originalValue = 100;
		double newValue = 200;
		double exchangeRate = 2;
		int numDays = 3;
		
		double expectedValue = ((numDays - 1) * originalValue + newValue) * exchangeRate;
		System.out.println("expected: " + expectedValue);
		Money.setExchangeRate(exchangeRate);
		for(int i = 0; i < numDays; i++) {
			model.queueTransaction(new BudgetEntry(MoneyFactory.createMoneyFromNewDouble(originalValue),BudgetFunctions.getDateString(),"test"));

			addDays(1);
		}
        model.processWholeQueue();
		// Change value of the first transaction
		List<BudgetEntry> entries = model.getSomeTransactions(1, BudgetDataSource.DESCENDING);
		BudgetEntry entry = entries.get(0);
		BudgetEntry newEntry = entry.clone();
		
		newEntry.setValue(MoneyFactory.createMoneyFromNewDouble(newValue));
		
		model.editTransaction(entry.getId(), newEntry);
		
		entries = model.getSomeTransactions(1, BudgetDataSource.DESCENDING);
		entry = entries.get(0);
		
		assertEquals("Wrong value after changing exchange rate.", newValue * exchangeRate,  entry.getValue().get());
		assertEquals("Current budget wrong after editing transaction", expectedValue, model.getCurrentBudget().get());
	}
	

	public void tearDown()
	{
		BudgetFunctions.TESTING = false;
		model.clearDatabaseInstance();
	}
	
	private void addDays(int n)
	{
		BudgetFunctions.theDate = HelperFunctions.addDays(BudgetFunctions.theDate, n);
	}

}
