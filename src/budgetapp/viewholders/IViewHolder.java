package budgetapp.viewholders;

import android.view.View;

/**
 * Interface for ViewHolders
 * @author Steen
 *
 */
public interface IViewHolder {
	
	public abstract void setUpConvertView(View convertView);
	
    public abstract void populateViews();
    
    public abstract void recycle(IViewHolder tempEntry);
    
    public abstract IViewHolder copy();
}
