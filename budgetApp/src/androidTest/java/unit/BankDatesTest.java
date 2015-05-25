package unit;

import junit.framework.TestCase;

import budgetapp.banks.swedbank.SwedbankDate;

public class BankDatesTest extends TestCase {

    public void testSwedBankDate() {
        String expectedDateString = "2015/02/11 00:00";
        SwedbankDate date = new SwedbankDate("2015-02-11");
        assertEquals("Dates were not equal", expectedDateString, date.toString());
    }
}
