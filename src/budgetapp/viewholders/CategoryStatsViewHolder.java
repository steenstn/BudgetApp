package budgetapp.viewholders;

import budgetapp.main.R;
import budgetapp.util.CategoryEntry;
import android.view.View;
import android.widget.TextView;


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
	public void printInfo()
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

    @Override
	public void setUpConvertView(View convertView) {
		setLeftTextView((TextView)convertView.findViewById(R.id.listLeftTextView));
		setCenterTextView((TextView)convertView.findViewById(R.id.listCenterTextView));
		setRightTextView((TextView)convertView.findViewById(R.id.listRightTextView));
		
	}
   
}
