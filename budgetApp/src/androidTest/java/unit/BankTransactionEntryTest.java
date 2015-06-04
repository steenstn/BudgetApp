package unit;


import java.util.List;

import budgetapp.banks.BankTransaction;
import budgetapp.util.BankTransactionEntry;
import budgetapp.util.money.Money;

public class BankTransactionEntryTest extends AbstractIntegrationTest {

    public void testBankTransaction() {
        BankTransactionEntry bankTransaction = new BankTransactionEntry(1, startDate, Money.fromNewDouble(10), "category", "description", 3);

        assertTrue("Adding bank transaction failed", model.addBankTransaction(bankTransaction));
        List<BankTransactionEntry> dbTransactions = model.getBankTransactions();
        BankTransactionEntry bankTransactionDb = dbTransactions.get(0);

        assertEquals("Id was incorrect", bankTransaction.getId(), bankTransactionDb.getId());
        assertEquals("Date was incorrect", bankTransaction.getDate(), bankTransactionDb.getDate());
        assertEquals("Value was incorrect", bankTransaction.getAmount().get(), bankTransactionDb.getAmount().get());
        assertEquals("Description was incorrect", bankTransaction.getDescription(), bankTransactionDb.getDescription());
        assertEquals("Category was incorrect", bankTransaction.getCategory(), bankTransactionDb.getCategory());
        assertEquals("Flags were incorrect", bankTransaction.getFlags(), bankTransactionDb.getFlags());

    }

    public void testFindBankTransaction() {
        BankTransactionEntry bankTransaction = new BankTransactionEntry(1, startDate, Money.fromNewDouble(10), "category", "description", 3);
        assertTrue("Adding bank transaction failed", model.addBankTransaction(bankTransaction));

        BankTransaction existingTransaction = new BankTransaction(startDate, Money.fromNewDouble(10), "description");
        BankTransaction nonexistingTransaction1 = new BankTransaction("2022/01/02 00:01", Money.fromNewDouble(10), "description");
        BankTransaction nonexistingTransaction2 = new BankTransaction(startDate, Money.fromNewDouble(11), "description");
        BankTransaction nonexistingTransaction3 = new BankTransaction(startDate, Money.fromNewDouble(10), "wdescription");

        assertTrue("Transaction was not found", model.isBankTransactionProcessed(existingTransaction));
        assertFalse("Transaction was incorrectly found", model.isBankTransactionProcessed(nonexistingTransaction1));
        assertFalse("Transaction was incorrectly found", model.isBankTransactionProcessed(nonexistingTransaction2));
        assertFalse("Transaction was incorrectly found", model.isBankTransactionProcessed(nonexistingTransaction3));

    }

}
