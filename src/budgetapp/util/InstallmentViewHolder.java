package budgetapp.util;

import android.widget.TextView;

/**
 * Class used in the ListView. Contains a TextView and a BudgetEntry that is printed in 
 * the TextView
 * @author Steen
 *
 */
public class InstallmentViewHolder {
	
    private TextView leftTextView;
    private TextView centerTextView;
    private TextView rightTextView;
    private Installment entry;
    private String title;
    
	public InstallmentViewHolder(Installment entry)
	{
		this.entry = entry;
		this.title = "";
	}
	public InstallmentViewHolder(InstallmentViewHolder viewHolder)
	{
		this.entry = viewHolder.getEntry();
		this.title = viewHolder.getTitle();
	}
	
    public Installment getEntry()
    {
    	return entry;
    }
    
    public TextView getLeftTextView()
    {
    	return leftTextView;
    }
    
    public TextView getCenterTextView()
    {
    	return centerTextView;
    }
    
    public TextView getRightTextView()
    {
    	return rightTextView;
    }
    
    public String getTitle()
    {
    	return title;
    }
    
    public void setLeftTextView(TextView textView)
    {
    	this.leftTextView = textView;
    }
    public void setCenterTextView(TextView textView)
    {
    	this.centerTextView = textView;
    }
    public void setRightTextView(TextView textView)
    {
    	this.rightTextView = textView;
    }
    
    public void setEntry(Installment entry)
    {
    	this.entry = entry;
    }
    public void setTitle(String title)
    {
    	this.title = title;
    }
    
   
}
