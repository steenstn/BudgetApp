package budgetapp.util;

import java.util.List;

public class BudgetFunctions {
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
					sum.add(((DayEntry) theEntries.get(i)).getTotal());
				if(theEntries.get(i) instanceof BudgetEntry)
					sum.add(((BudgetEntry) theEntries.get(i)).getValue());
				
				i++;
			}
			
			return sum.divide(i);
			
		}
		
		public static Money getWeightedMeanDerivative(List<?> theEntries, int n)
		{
			if(theEntries.size()<2) // Just 0 or 1 entries, no derivative yet
				return new Money();
			int i=0;
			
			Money sum = new Money();
			while(i<theEntries.size() && i<n) 
			{
				//Get the value depending on the class
				if(theEntries.get(i) instanceof DayEntry)
					sum.add(weight(i,((DayEntry)theEntries.get(i) ).getTotal() ));
				if(theEntries.get(i) instanceof BudgetEntry)
					sum.add(weight(i,((BudgetEntry)theEntries.get(i) ).getValue() ));
				
				i++;
			}
			return sum.divide(i);
		}
		// Weight a value depending on time
		private static Money weight(int n,Money d)
		{
			double res = Math.exp(-0.1*(double)n) * d.get();
			return new Money(res);
		}
}
