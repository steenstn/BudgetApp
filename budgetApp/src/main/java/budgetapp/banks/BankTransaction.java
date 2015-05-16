package budgetapp.banks;

public class BankTransaction {

    private String date;
    private String amount;
    private String description;

    public BankTransaction(String date, String amount, String description) {
        this.date = date;
        this.amount = amount;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
