package budgetapp.viewholders;

import budgetapp.util.CategoryEntry;


/**
 * ViewHolder for displaying info about a category in the statistics view
 * @author Steen
 *
 */
public class CategoryStatsViewHolder extends ViewHolder {
	
    private CategoryEntry entry;
    
	public CategoryStatsViewHolder(CategoryEntry entry)
	{
		this.entry = entry;
	}
	public CategoryStatsViewHolder(CategoryStatsViewHolder viewHolder)
	{
		this.entry = viewHolder.getEntry();
	}
    public CategoryEntry getEntry()
    {
    	return entry;
    }
    
    public void setEntry(CategoryEntry entry)
    {
    	this.entry = entry;
    }
    
	@Override
	public void populateViews()
	{
		getLeftTextView().setText(getEntry().getCategory());
		getCenterTextView().setText(""+getEntry().getNum());
		// Add a star after the category if this entry has a comment
		getRightTextView().setText(""+getEntry().getValue());
	
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
