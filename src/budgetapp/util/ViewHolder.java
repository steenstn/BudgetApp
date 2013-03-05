package budgetapp.util;

import android.widget.TextView;

public class ViewHolder {
	
	
    public TextView textView;
    public BudgetEntry entry;
    public int flag;
    public String title;
    public static final int ENTRY = 0;
    public static final int YEAR = 1;
    public static final int MONTH = 2;
    
	public ViewHolder(BudgetEntry entry)
	{
		this.entry = entry;
		flag = ENTRY;
		this.title = "";
	}
	public ViewHolder(ViewHolder viewHolder)
	{
		this.entry = viewHolder.entry;
		this.title = viewHolder.title;
		this.flag = viewHolder.flag;
	}
	public ViewHolder(String theString,int theInt)
	{
		this.entry = new BudgetEntry(-1, new Money(), "", "");
		title = theString;
		flag = theInt;
	}
	
    
    public String toString(){
    	switch(flag)
    	{
    	case YEAR:
    	case MONTH:
    		return title;
    	case ENTRY:
    		if(entry!=null)
    		{
	    		String temp = "Date: " + entry.getDate().substring(8) + "\t\t" + entry.getValue();
	    		
	    		if(entry.getValue().get()>-100 && entry.getValue().get()<1000)
	    			temp+="\t";
	    		if(entry.getValue().get()<=-1000)
	    			temp+="\t";
	    		//view.append("\t" + entry.getCategory());
	    		temp+="\t"+entry.getCategory();
	    		// Add comment if there is one
	    		// But only print max 20 characters
	    		String comment = entry.getComment();
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
