package budgetapp.viewholders;

import budgetapp.main.R;
import budgetapp.util.BudgetEntry;
import budgetapp.util.Money;
import android.view.View;
import android.widget.TextView;


/**
 * ViewHolder for displaying info about a transaction in the Statistics view
 * @author Steen
 *
 */
public class StatEntryViewHolder extends ViewHolder {
	
    private BudgetEntry entry;
    private Type flag;
    private String title;
    
    // What kind of entry it is
    public static enum Type
    {
    	entry,
    	year,
    	month
    }
	public StatEntryViewHolder(BudgetEntry entry)
	{
		this.entry = entry;
		flag = Type.entry;
		this.title = "";
	}
	public StatEntryViewHolder(StatEntryViewHolder viewHolder)
	{
		this.entry = viewHolder.getEntry();
		this.title = viewHolder.getTitle();
		this.flag = viewHolder.getType();
	}
	public StatEntryViewHolder(String theString,Type theType)
	{
		this.entry = new BudgetEntry(-1, new Money(), "", "");
		this.title = theString;
		this.flag = theType;
	}
	
    public BudgetEntry getEntry()
    {
    	return entry;
    }
    
    public Type getType()
    {
    	return flag;
    }
    
    private String getTitle()
    {
    	return title;
    }
    
    private void setEntry(BudgetEntry entry)
    {
    	this.entry = entry;
    }
    private void setType(Type type)
    {
    	this.flag = type;
    }
    private void setTitle(String title)
    {
    	this.title = title;
    }
    
	@Override
	public void printInfo()
	{
		// If this is an entry, print info in all the textviews
		if(getType()==StatEntryViewHolder.Type.entry)
		{
			
			getLeftTextView().setText("Date: " + getEntry().getDate().substring(8));
			getCenterTextView().setText(""+getEntry().getValue());
			// Add a star after the category if this entry has a comment
			getRightTextView().setText(getEntry().getCategory() + (getEntry().getComment().equalsIgnoreCase("") ? "" : " *"));
			
		}
		else
		{
			getLeftTextView().setText(getTitle());
			getCenterTextView().setText("");
			getRightTextView().setText("");
		}
	}
	
	
	@Override
	public void recycle(IViewHolder tempEntry) {
		setEntry(((StatEntryViewHolder) tempEntry).getEntry());
		setTitle(((StatEntryViewHolder) tempEntry).getTitle());
		setType(((StatEntryViewHolder) tempEntry).getType());
		
	}

	@Override
	public IViewHolder copy() {
		return new StatEntryViewHolder(this);

	}

    @Override
	public void setUpConvertView(View convertView) {
		setLeftTextView((TextView)convertView.findViewById(R.id.listLeftTextView));
		setCenterTextView((TextView)convertView.findViewById(R.id.listCenterTextView));
		setRightTextView((TextView)convertView.findViewById(R.id.listRightTextView));
		
	}
   
}
