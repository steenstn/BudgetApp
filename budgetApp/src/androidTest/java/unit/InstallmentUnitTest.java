package unit;

import junit.framework.TestCase;

import budgetapp.util.Installment;
import budgetapp.util.money.Money;

public class InstallmentUnitTest extends TestCase {

    public void testCalculateDailyPaymentNegativeInstallment() {
        Installment installment = new Installment(Money.fromNewDouble(-100), Money.fromNewDouble(-10),"1012/01/01 00:00", Money.zero(), "cat", "");
        Money expectedDailyPayment = Money.fromNewDouble(-10);
        assertEquals("Incorrect daily payment calculated", expectedDailyPayment.get(), installment.calculateDailyPayment().get());
    }

    public void testCalculateDailyPaymentNegativeInstallmentAlmostPaid() {
        Installment installment = new Installment(Money.fromNewDouble(-100), Money.fromNewDouble(-1000),"1012/01/01 00:00", Money.zero(), "cat", "");
        Money expectedDailyPayment = Money.fromNewDouble(-100);
        assertEquals("Incorrect daily payment calculated", expectedDailyPayment.get(), installment.calculateDailyPayment().get());
    }

    public void testCalculateDailyPaymentPositiveInstallment() {
        Installment installment = new Installment(Money.fromNewDouble(100), Money.fromNewDouble(10),"1012/01/01 00:00", Money.zero(), "cat", "");
        installment.setFlags(Installment.INSTALLMENT_POSITIVE);
        Money expectedDailyPayment = Money.fromNewDouble(10);
        assertEquals("Incorrect daily payment calculated", expectedDailyPayment.get(), installment.calculateDailyPayment().get());
    }

    public void testCalculateDailyPaymentPositiveInstallmentAlmostPaidOff() {
        Installment installment = new Installment(Money.fromNewDouble(100), Money.fromNewDouble(1000),"1012/01/01 00:00", Money.zero(), "cat", "");
        installment.setFlags(Installment.INSTALLMENT_POSITIVE);
        Money expectedDailyPayment = Money.fromNewDouble(100);
        assertEquals("Incorrect daily payment calculated", expectedDailyPayment.get(), installment.calculateDailyPayment().get());
    }

}
