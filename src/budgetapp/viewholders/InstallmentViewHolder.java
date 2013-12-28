package budgetapp.viewholders;

import budgetapp.util.Installment;


/**
 * ViewHolder for displaying info about an Installment
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
	public void populateViews() {
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
	public IViewHolder copy() {
		return new InstallmentViewHolder(this);
	}
}
