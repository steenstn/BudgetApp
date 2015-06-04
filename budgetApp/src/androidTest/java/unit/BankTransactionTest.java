package unit;


import java.util.List;

import budgetapp.util.BankTransaction;
import budgetapp.util.money.Money;

public class BankTransactionTest extends AbstractIntegrationTest {

    public void testBankTransaction() {
        BankTransaction bankTransaction = new BankTransaction(1, startDate, Money.fromNewDouble(10), "category", "description", 3);

        assertTrue("Adding bank transaction failed", model.addBankTransaction(bankTransaction));
        List<BankTransaction> dbTransactions = model.getBankTransactions();
        BankTransaction bankTransactionDb = dbTransactions.get(0);

        assertEquals("Id was incorrect", bankTransaction.getId(), bankTransactionDb.getId());
        assertEquals("Date was incorrect", bankTransaction.getDate(), bankTransactionDb.getDate());
        assertEquals("Value was incorrect", bankTransaction.getAmount().get(), bankTransactionDb.getAmount().get());
        assertEquals("Description was incorrect", bankTransaction.getDescription(), bankTransactionDb.getDescription());
        assertEquals("CAtegory was incorrect", bankTransaction.getCategory(), bankTransactionDb.getCategory());
        assertEquals("Flags were incorrect", bankTransaction.getFlags(), bankTransactionDb.getFlags());

    }

}
