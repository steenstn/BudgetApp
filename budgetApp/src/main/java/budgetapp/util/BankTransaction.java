package budgetapp.util;

import budgetapp.util.money.Money;

public class BankTransaction {

    private final long id;
    private final String date;
    private final Money amount;
    private final String description;
    private final String category;
    private final int flags;

    public BankTransaction(String date, Money amount, String category, String description) {
        this(-1, date, amount, category, description, 0);
    }

    public BankTransaction(long id, String date, Money amount, String category, String description,int flags) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.flags = flags;
    }

    public BankTransaction clone() {
        return new BankTransaction(id, date, amount, category, description, flags);
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

    public long getId() { return id; }
    public int getFlags() {
        return flags;
    }
}
