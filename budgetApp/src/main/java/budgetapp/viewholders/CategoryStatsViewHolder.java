package budgetapp.viewholders;

import android.widget.TextView;
import budgetapp.util.entries.CategoryEntry;

public class CategoryStatsViewHolder
    extends ViewHolder {

    private CategoryEntry entry;

    public CategoryStatsViewHolder(CategoryEntry entry) {
        this.entry = entry;
    }

    public CategoryStatsViewHolder(CategoryStatsViewHolder viewHolder) {
        this.entry = viewHolder.getEntry();
    }

    public CategoryEntry getEntry() {
        return entry;
    }

    public void setEntry(CategoryEntry entry) {
        this.entry = entry;
    }

    @Override
    public void populateViews() {
        TextView leftTextView = (TextView) getFirstView();
        leftTextView.setText(getEntry().getCategory());

        TextView centerTextView = (TextView) getSecondView();
        centerTextView.setText("" + getEntry().getNum());

        TextView rightTextView = (TextView) getThirdView();
        rightTextView.setText("" + getEntry().getValue());
    }

    @Override
    public void recycle(IViewHolder tempEntry) {
        setEntry(((CategoryStatsViewHolder) tempEntry).getEntry());
    }

    @Override
    public IViewHolder copy() {
        return new CategoryStatsViewHolder(this);
    }

}
