package budgetapp.util;

import java.util.List;

public class BudgetFunctions {
	
	public static int min(int a,int b) 
	{
		if(a<b)
			return a;
		return b;
	}
	// Returns the mean cash flow for n time steps or as many as are available if less
	public static Money getMeanDerivative(List<?> theEntries, int n)
	{
		
		if(theEntries.size()<2) // Just 0 or 1 entries, no derivative yet
			return new Money();
		int i=0;
		
		Money sum = new Money();
		
		
		// Step through days, break if i reaches size or n
		while(i<theEntries.size() && i<n) 
		{
			//Get the value depending on the class
			if(theEntries.get(i) instanceof DayEntry)
				sum=sum.add(((DayEntry) theEntries.get(i)).getTotal());
			if(theEntries.get(i) instanceof BudgetEntry)
				sum=sum.add(((BudgetEntry) theEntries.get(i)).getValue());
			
			i++;
		}
		
		return sum.divide(i);
		
	}
		
	public static Money getWeightedMeanDerivative(List<?> theEntries, int n)
	{
		if(theEntries.size()<2) // Just 0 or 1 entries, no derivative yet
			return new Money();
		int i=0;
		
		double totalWeight = 0;
		Money sum = new Money();
		while(i<theEntries.size() && i<n) 
		{
			totalWeight+= Math.exp(-0.5*(double)i);
			//Get the value depending on the class
			if(theEntries.get(i) instanceof DayEntry)
				sum=sum.add(weight(i,((DayEntry)theEntries.get(i) ).getTotal() ));
			if(theEntries.get(i) instanceof BudgetEntry)
				sum=sum.add(weight(i,((BudgetEntry)theEntries.get(i) ).getValue() ));
			
			i++;
		}
		return sum.divide(totalWeight);
	}
	
	// Weight a value depending on time
	private static Money weight(int n,Money d)
	{
		double weight = Math.exp(-0.5*(double)n);
		double res = weight * d.get();
		return new Money(res);
	}
}
