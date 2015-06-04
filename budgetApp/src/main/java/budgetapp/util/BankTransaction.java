package budgetapp.util;

import budgetapp.util.money.Money;

public class BankTransaction {

    private final String date;
    private final Money amount;
    private final String description;
    private final String category;
    private final int flags;

    public BankTransaction(String date, Money amount, String description, String category) {
        this(date, amount, description, category, 0);
    }

    public BankTransaction(String date, Money amount, String description, String category, int flags) {
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.flags = flags;
    }

    public BankTransaction clone() {
        return new BankTransaction(date, amount, description, category, flags);
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

    public String getCategory() {
        return category;
    }

    public int getFlags() {
        return flags;
    }
}
