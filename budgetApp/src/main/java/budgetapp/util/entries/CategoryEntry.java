package budgetapp.util.entries;

import budgetapp.util.money.Money;
import budgetapp.util.money.MoneyFactory;

public class CategoryEntry
    extends DatabaseEntry {

    private String category;
    private int numTransactions; 
    private Money value; 

    public CategoryEntry(long id, String category, int num, Money total, int flags) {
        setId(id);
        this.category = category;
        this.numTransactions = num;
        this.value = new Money(total);
        setFlags(flags);
    }

    public CategoryEntry(long id, String category, int num, Money total) {
        setId(id);
        this.category = category;
        this.numTransactions = num;
        this.value = new Money(total);
        setFlags(0);
    }

    public CategoryEntry(long id, String category) {
        setId(id);
        this.category = category;
        this.numTransactions = 0;
        this.value = Money.zero();
        setFlags(0);
    }

    public CategoryEntry(String category) {
        setId(0);
        this.category = category;
        this.value = Money.zero();
        this.numTransactions = 0;
        setFlags(0);
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setNum(int n) {
        numTransactions = n;
    }

    public int getNum() {
        return numTransactions;
    }

    public void addToNum(int n) {
        numTransactions += n;
    }

    public void setValue(Money n) {
        value = n;
    }

    public Money getValue() {
        return value;
    }

    public void addToTotal(Money n) {
        value = value.add(n);
    }

    @Override
    public String toString() {
        return category;
    }
}
