package budgetapp.util;

import android.widget.TextView;

/**
 * Abstract class for ViewHolders, just contains three TextViews
 * @author Steen
 *
 */
public abstract class ViewHolder implements IViewHolder {
	
    private TextView leftTextView;
    private TextView centerTextView;
    private TextView rightTextView;
    
    protected TextView getLeftTextView()
    {
    	return leftTextView;
    }
    
    protected TextView getCenterTextView()
    {
    	return centerTextView;
    }
    
    protected TextView getRightTextView()
    {
    	return rightTextView;
    }
    
    protected void setLeftTextView(TextView textView)
    {
    	this.leftTextView = textView;
    }
    protected void setCenterTextView(TextView textView)
    {
    	this.centerTextView = textView;
    }
    protected void setRightTextView(TextView textView)
    {
    	this.rightTextView = textView;
    }

    
   
}
