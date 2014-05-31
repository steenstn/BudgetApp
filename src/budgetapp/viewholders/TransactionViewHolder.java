package budgetapp.viewholders;

import budgetapp.util.entries.BudgetEntry;

public class TransactionViewHolder
    extends ViewHolder {

    private BudgetEntry entry;

    public TransactionViewHolder(BudgetEntry entry) {
        this.entry = entry.clone();
    }

    private void setEntry(BudgetEntry entry) {
        this.entry = entry;
    }

    public BudgetEntry getEntry() {
        return entry;
    }

    @Override
    public void populateViews() {
        getLeftTextView().setText(entry.getDate() + ":  " + entry.getValue());
        getCenterTextView().setText("");
        getRightTextView().setText(entry.getCategory());
    }

    @Override
    public void recycle(IViewHolder tempEntry) {
        setEntry(((TransactionViewHolder) tempEntry).getEntry());
    }

    @Override
    public IViewHolder copy() {
        return new TransactionViewHolder(this.entry);
    }

}
