package budgetapp.util.stats;

import java.util.ArrayList;
import java.util.List;

import budgetapp.util.BudgetEntry;

public class CompositeStats extends Stats {
	
	private List<Stats> children;
	public static final int MONTH = 0;
	public CompositeStats()
	{
		children = new ArrayList<Stats>();
	}
	public CompositeStats(String theName)
	{
		name = theName;
		children = new ArrayList<Stats>();
	}
	
	public List<Stats> getChildren(){
		return children;
	}
	public void addEntry(BudgetEntry theEntry,int mode)
	{
		boolean added = false;
		for(int i=0;i<children.size();i++)
		{
			if(children.get(i).getName().equalsIgnoreCase(theEntry.getMonth())) // Add the entry to the month
			{
				children.get(i).addEntry(theEntry);
				added = true;
				
			}
		}
		if(!added) // Month not yet added, add it
		{
			children.add(new Stats(theEntry.getMonth()));
			children.get(children.size()-1).addEntry(theEntry);
		}
	}



}
