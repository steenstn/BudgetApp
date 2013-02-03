package budgetapp.util;

import java.util.List;

public class BudgetFunctions {
	// Returns the mean cash flow for n time steps or as many as are available if less
		public static long getMeanDerivative(List<?> theEntries, int n)
		{
			
			if(theEntries.size()<2) // Just 0 or 1 entries, no derivative yet
				return 0;
			int i=0;
			
			long sum = 0;
			long result = 0;
			
				// Step through days, break if i reaches size or n
				while(i<theEntries.size() && i<n) 
				{
					//Get the value depending on the class
					if(theEntries.get(i) instanceof DayEntry)
						sum+=((DayEntry) theEntries.get(i)).getTotal();
					if(theEntries.get(i) instanceof BudgetEntry)
						sum+=((BudgetEntry) theEntries.get(i)).getValue();
					
					i++;
				}
				result = sum/i;
			
			return result;
			
		}
}
