package unit;

import java.util.List;

import budgetapp.models.BudgetModel;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.Installment;
import budgetapp.util.database.BudgetDataSource;
import budgetapp.util.entries.BudgetEntry;
import budgetapp.util.entries.DayEntry;
import budgetapp.util.money.Money;
import budgetapp.util.money.MoneyFactory;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class RemoveTransactionTest extends AndroidTestCase{
	
	BudgetModel model;
	RenamingDelegatingContext mockContext;
	String prefix = "test";
	String startDate = "2022/01/02 00:00";
	
	public void setUp()
	{
		BudgetFunctions.TESTING = true;
		mockContext = new RenamingDelegatingContext(getContext(), getContext(), prefix);

        model = new BudgetModel(mockContext);
        model.clearDatabaseInstance();
        model = new BudgetModel(mockContext);

        BudgetFunctions.theDate = startDate;
		Money.setExchangeRate(1.0);
		model.setDailyBudget(Money.zero());
		
		//model.createTransaction(new BudgetEntry(MoneyFactory.createMoney(),BudgetFunctions.getDateString(),"test"));
		
		assertEquals("Incorrect starting budget.", 0.0,model.getCurrentBudget().get());
		assertEquals("Incorrect startDate", startDate, BudgetFunctions.theDate);
	
	}
	
	public void testRemoveTransaction()
	{
		int numTransactions = 3;
		double value = 100;
		for(int i = 0; i < numTransactions; i++) {
			model.queueTransaction(new BudgetEntry(MoneyFactory.createMoneyFromNewDouble(value), BudgetFunctions.getDateString(), "test"));
			addDays(1);
		}
        model.processWholeQueue();
		assertEquals("Incorrect current budget after adding transactions", value * numTransactions, model.getCurrentBudget().get());
		
		List<BudgetEntry> transactions = model.getSomeTransactions(0, BudgetDataSource.DESCENDING);
		BudgetEntry entry = transactions.get(0);
		
		model.removeTransaction(entry);
		String dateRemoved = entry.getDate();
		assertEquals("Incorrect current budget after removing transaction", value * (numTransactions - 1), model.getCurrentBudget().get());
		List<DayEntry> dayFlow = model.getSomeDays(0, BudgetDataSource.ASCENDING);
		
		assertEquals("Incorrect value in daily flow", 0.0, dayFlow.get(0).getValue().get());
		
	}
	
	public void testUndoTransction() {
		double value = 100;
		model.queueTransaction(new BudgetEntry(MoneyFactory.createMoneyFromNewDouble(value), BudgetFunctions.getDateString(), "test"));
		model.processWholeQueue();
		assertEquals("Incorrect value after adding transaction", value, model.getCurrentBudget().get());
		model.undoLatestTransaction();
		assertEquals("Incorrect value after undo", 0.0, model.getCurrentBudget().get());
	}
	
	public void testRemoveTransactionHighExchangeRate()
	{
		int numTransactions = 3;
		double value = 100;
		double exchangeRate = 2;
		Money.setExchangeRate(exchangeRate);
		
		for(int i = 0; i < numTransactions; i++) {
			model.queueTransaction(new BudgetEntry(MoneyFactory.createMoneyFromNewDouble(value), BudgetFunctions.getDateString(), "test"));
			addDays(1);
		}
        model.processWholeQueue();
		assertEquals("Incorrect current budget after adding transactions", value * exchangeRate * numTransactions, model.getCurrentBudget().get());
		
		List<BudgetEntry> transactions = model.getSomeTransactions(0, BudgetDataSource.DESCENDING);
		BudgetEntry entry = transactions.get(0);
		
		model.removeTransaction(entry);
		
		assertEquals("Incorrect current budget after removing transaction", value * exchangeRate * (numTransactions - 1), model.getCurrentBudget().get());
		
	}
	
	public void testRemoveTransactionLowExchangeRate()
	{
		int numTransactions = 3;
		double value = 100;
		double exchangeRate = .2;
		Money.setExchangeRate(exchangeRate);
		
		for(int i = 0; i < numTransactions; i++) {
			model.queueTransaction(new BudgetEntry(MoneyFactory.createMoneyFromNewDouble(value), BudgetFunctions.getDateString(), "test"));
			addDays(1);
		}
        model.processWholeQueue();
		assertEquals("Incorrect current budget after adding transactions", value * exchangeRate * numTransactions, model.getCurrentBudget().get());
		
		List<BudgetEntry> transactions = model.getSomeTransactions(0, BudgetDataSource.DESCENDING);
		BudgetEntry entry = transactions.get(0);
		
		model.removeTransaction(entry);
		
		assertEquals("Incorrect current budget after removing transaction", value * exchangeRate * (numTransactions - 1), model.getCurrentBudget().get());
		
	}
	
	public void testRemoveTransactionAfterInstallment()
	{
		double installmentTotalValue = -10;
		double installmentDailyPayment = -3;
		double installmentAmountPaid = 0;

		int numberOfDays = 12;
		
		Installment installment = new Installment(MoneyFactory.createMoneyFromNewDouble(installmentTotalValue), MoneyFactory.createMoneyFromNewDouble(installmentDailyPayment),
				BudgetFunctions.getDateString(), MoneyFactory.createMoneyFromNewDouble(installmentAmountPaid), "test", "testComment");
		
		assertEquals("Could not add installment.", model.addInstallment(installment), true);
		List<Installment> installments = model.getInstallments();
		long installmentId = installments.get(0).getId();
		
		
		addDays(numberOfDays);
		assertEquals("Wrong number of days added.", model.queueAddDailyBudget(), numberOfDays);
		model.queuePayOffInstallments();
		model.processWholeQueue();
		assertEquals("Incorrect current budget after paying of installments",
				installmentTotalValue, model.getCurrentBudget().get());
		
		
		model.removeInstallment(installmentId);
		
		BudgetEntry entry = new BudgetEntry();
		entry.setId(installments.get(0).getTransactionId());
		model.removeTransaction(entry);
		assertEquals("Incorrect current budget after removing entry",
				0.0, model.getCurrentBudget().get());
		
		List<DayEntry> dayFlow = model.getSomeDays(0, BudgetDataSource.ASCENDING);
		
		for(int i = 0; i < dayFlow.size(); i++)
		{
			assertEquals("Daily flow not correct for day (" + i + "): " + dayFlow.get(i).getDate(), 
					0.0, dayFlow.get(i).getValue().get());
		}

		assertEquals("Incorrect current budget.", 0.0, model.getCurrentBudget().get());
		
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
