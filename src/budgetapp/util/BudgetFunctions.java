package budgetapp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Class containing functions used throughout the application
 * @author Steen
 *
 */
public class BudgetFunctions {
	
	public static String theDate = "2012/01/01 00:00";
	public static boolean TESTING = false;
	/**
	 * Returns the lowest value of a and b
	 * @param a - First int to compare
	 * @param b - Second int to compare
	 * @return - The lowest value of a and b
	 */
	public static int min(int a,int b) 
	{
		if(a<b)
			return a;
		return b;
	}
	
	public static double min(double a, double b) {
		
		if(a<b)
			return a;
		return b;
	}
	
	public static double max(double a, double b) {
		
		if(a>b)
			return a;
		return b;
	}
	
	public static boolean almostEquals(double a, double b)
	{
		if(Math.abs(a-b) < 0.00001)
			return true;
		else
			return false;
	}
	
	/**
	 * Gets today's date in a string format
	 * @return - Todays date in "yyyy/MM/dd HH:mm"
	 */
	public static String getDateString()
	{
		if(TESTING == true)
		{
	    	return theDate;			
		}
		else
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	    	Calendar cal = Calendar.getInstance();
	    	String dateString = dateFormat.format(cal.getTime());
	    	return dateString;
		}
	}
		
	public static int getYear()
	{
		String dateString = getDateString();
		return Integer.parseInt(dateString.substring(0, 4));
	}
	
	public static int getMonth()
	{
		String dateString = getDateString();
		return Integer.parseInt(dateString.substring(5, 7))-1; // January = 0 in calendar
	}
	
	public static int getDay()
	{
		String dateString = getDateString();
		return Integer.parseInt(dateString.substring(8,10));
	}
	/**
	 * Calculates the mean value for n entries or as many as are available if less
	 * @param theEntries - List of the entries to calculate
	 * @param n - Number of entries to use
	 * @return - The mean for the entries
	 */
	public static Money getMean(List<? extends DatabaseEntry> theEntries, int n)
	{
		
		if(theEntries.size()<2) // Just 0 or 1 entries, no derivative yet
			return new Money();
		int i=0;
		
		Money sum = new Money();
		
		// Step through days, break if i reaches size or n
		while(i<theEntries.size() && i<n) 
		{
			sum=sum.add((theEntries.get(i)).getValue());
			
			i++;
		}
		
		return sum.divide(i);
		
	}
	
	/**
	 * Calculates a weighted mean value for n entries or as many as are available if less
	 * @param theEntries - List of the entries to calculate
	 * @param n - Number of entries to use
	 * @return - The weighted mean
	 */
	public static Money getWeightedMean(List<? extends DatabaseEntry> theEntries, int n)
	{
		if(theEntries.size()<2) // Just 0 or 1 entries, no derivative yet
			return new Money();
		int i=0;
		
		double totalWeight = 0;
		Money sum = new Money();
		while(i<theEntries.size() && i<n) 
		{
			totalWeight+= Math.exp(-0.5*(double)i);
			
			sum=sum.add(weight(i,theEntries.get(i).getValue()));
			
			i++;
		}
		return sum.divide(totalWeight);
	}
	
	/**
	 * Weight a value depending on time
	 * @param n - Timesteps to weigh with
	 * @param d - The value to weigh
	 * @return - The weighted value
	 */
	private static Money weight(int n,Money d)
	{
		double weight = Math.exp(-0.5*(double)n);
		double res = weight * d.get();
		return new Money(res);
	}

}
