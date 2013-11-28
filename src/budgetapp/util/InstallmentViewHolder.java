package budgetapp.util;

import budgetapp.main.R;
import android.view.View;
import android.widget.TextView;


/**
 * Class used in the ListView. Contains a TextView and a BudgetEntry that is printed in 
 * the TextView
 * @author Steen
 *
 */
public class InstallmentViewHolder extends ViewHolder {
	
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
    
    public String getTitle()
    {
    	return title;
    }
    
    public void setEntry(Installment entry)
    {
    	this.entry = entry;
    }
    
    public void setTitle(String title)
    {
    	this.title = title;
    }
	@Override
	public void printInfo() {
		getLeftTextView().setText(getEntry().getCategory());
		getCenterTextView().setText(""+getEntry().getRemainingValue().makePositive() + "/"
				+ getEntry().getTotalValue().makePositive());
		
		double daysLeft = (getEntry().getRemainingValue().divide(getEntry().getDailyPayment())).get();
		int numDaysLeft = (int) Math.ceil(daysLeft);
		getRightTextView().setText(""+numDaysLeft);
		
	}
	
	@Override
	public void recycle(IViewHolder tempEntry) {
		setEntry(((InstallmentViewHolder) tempEntry).getEntry());
		setTitle(((InstallmentViewHolder) tempEntry).getTitle());
				
	}
	@Override
	public void setUpConvertView(View convertView) {
		setLeftTextView((TextView)convertView.findViewById(R.id.listLeftTextView));
		setCenterTextView((TextView)convertView.findViewById(R.id.listCenterTextView));
		setRightTextView((TextView)convertView.findViewById(R.id.listRightTextView));
		
	}
	@Override
	public IViewHolder copy() {
		return new InstallmentViewHolder(this);
	}
   
}
