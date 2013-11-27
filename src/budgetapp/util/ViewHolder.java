package budgetapp.util;

import android.view.View;
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

    
   
}
