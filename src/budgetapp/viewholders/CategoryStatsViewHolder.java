package budgetapp.viewholders;

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
        getLeftTextView().setText(getEntry().getCategory());
        getCenterTextView().setText("" + getEntry().getNum());
        getRightTextView().setText("" + getEntry().getValue());
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
