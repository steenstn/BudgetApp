package budgetapp.banks;

public abstract class BankDate {
    protected String dateString;

    public BankDate(String date) {
        dateString = date;
    }

    @Override
    public abstract String toString();
}
