package budgetapp.util.stats;

import java.util.ArrayList;
import java.util.List;

import budgetapp.util.entries.BudgetEntry;

public class Stats {

    private List<BudgetEntry> transactions;
    protected String name;

    public Stats() {
        transactions = new ArrayList<BudgetEntry>();
    }

    public Stats(String theName) {
        name = theName;
        transactions = new ArrayList<BudgetEntry>();
    }

    public String getName() {
        return name;
    }

    public void addEntry(BudgetEntry theEntry) {
        transactions.add(theEntry);
    }

    public List<BudgetEntry> getTransactions() {
        return transactions;
    }
}
