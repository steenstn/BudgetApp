package budgetapp.util;


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
    
   
}
