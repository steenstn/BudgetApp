package budgetapp.banks.swedbank;

import budgetapp.banks.BankDate;

public class SwedbankDate extends BankDate {
    public SwedbankDate(String date) {
        super(date);
    }

    @Override
    public String toString() {
        return dateString.replaceAll("-","/") + " 00:00";
    }
}
