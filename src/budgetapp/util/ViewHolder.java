package budgetapp.util;

import android.widget.TextView;

/**
 * Class used in the ListView. Contains a TextView and a BudgetEntry that is printed in 
 * the TextView
 * @author Steen
 *
 */
public class ViewHolder {
	
    private TextView textView;
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
	public ViewHolder(BudgetEntry entry)
	{
		this.entry = entry;
		flag = Type.entry;
		this.title = "";
	}
	public ViewHolder(ViewHolder viewHolder)
	{
		this.entry = viewHolder.getEntry();
		this.title = viewHolder.getTitle();
		this.flag = viewHolder.getType();
	}
	public ViewHolder(String theString,Type theType)
	{
		this.entry = new BudgetEntry(-1, new Money(), "", "");
		this.title = theString;
		this.flag = theType;
	}
	/*private TextView textView;
    private BudgetEntry entry;
    private Type flag;
    private String title;
    */
    public BudgetEntry getEntry()
    {
    	return entry;
    }
    
    public TextView getTextView()
    {
    	return textView;
    }
    
    public Type getType()
    {
    	return flag;
    }
    
    public String getTitle()
    {
    	return title;
    }
    
    public void setTextView(TextView textView)
    {
    	this.textView = textView;
    }
    public void setEntry(BudgetEntry entry)
    {
    	this.entry = entry;
    }
    public void setType(Type type)
    {
    	this.flag = type;
    }
    public void setTitle(String title)
    {
    	this.title = title;
    }
    public String toString(){
    	switch(flag)
    	{
    	case year:
    	case month:
    		return title;
    	case entry:
    		if(this.entry!=null)
    		{
	    		String temp = "Date: " + this.entry.getDate().substring(8) + "\t\t" + this.entry.getValue();
	    		
	    		if(this.entry.getValue().get()>-100 && this.entry.getValue().get()<1000)
	    			temp+="\t";
	    		if(this.entry.getValue().get()<=-1000)
	    			temp+="\t";
	    		//view.append("\t" + entry.getCategory());
	    		temp+="\t"+this.entry.getCategory();
	    		// Add comment if there is one
	    		// But only print max 20 characters
	    		String comment = this.entry.getComment();
	    		if(comment!=null && !comment.equalsIgnoreCase(""))
	    		{
	    			temp+=" *";
	    		}
	    		return temp;
    		}
    	}
    	return "Error in ViewHolder.toString()";
    }
   
}
