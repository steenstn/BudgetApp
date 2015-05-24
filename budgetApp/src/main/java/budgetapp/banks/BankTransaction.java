package budgetapp.banks;

import budgetapp.util.money.Money;

public class BankTransaction {

    private String date;
    private Money amount;
    private String description;

    public BankTransaction(String date, Money amount, String description) {
        this.date = date;
        this.amount = amount;
        this.description = description;
    }

    public BankTransaction clone() {
        return new BankTransaction(date, amount, description);
    }
    public String getDate() {
        return date;
    }

    public Money getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
