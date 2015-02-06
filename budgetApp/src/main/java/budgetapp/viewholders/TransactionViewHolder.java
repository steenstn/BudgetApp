package budgetapp.viewholders;

import android.widget.TextView;
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
        TextView leftTextView = (TextView) getFirstView();
        leftTextView.setText(entry.getDate() + ":  " + entry.getValue());

        TextView centerTextView = (TextView) getSecondView();
        centerTextView.setText("");

        TextView rightTextView = (TextView) getThirdView();
        rightTextView.setText(entry.getCategory());

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
