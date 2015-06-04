package unit;


import budgetapp.util.BankTransaction;
import budgetapp.util.money.Money;

public class BankTransactionTest extends AbstractIntegrationTest {

    public void testBankTransaction() {
        BankTransaction bankTransaction = new BankTransaction(startDate, Money.fromNewDouble(10), "desc", "category", 3);

        assertTrue("Adding bank transaction failed", model.addBankTransaction(bankTransaction));
    }

}
