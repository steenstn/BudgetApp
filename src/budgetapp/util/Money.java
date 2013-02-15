package budgetapp.util;
/**
 * Money class. Instead of using doubles directly, this class is used to get more flexibility
 * @author Steen
 *
 */
public class Money {
	
	private double value;
	public static boolean after;
	private static String currency = "kr";
	
	public Money()
	{
		value = 0;
		after = true;
	}
	public Money(double x)
	{
		value = x;
	}
	public Money(Money m)
	{
		value = m.get();
	}
	public void setCurrency(String c)
	{
		currency = c;
	}
	
	public String currency()
	{
		return currency;
	}
	public void set(double val)
	{
		value = val;
	}
	public void set(Money val)
	{
		value = val.get();
	}
	public double get()
	{
		return value;
	}
	
	public Money add(double x)
	{
		value+=x;
		return this;
	}
	public Money add(Money x)
	{
		value+=x.get();
		return this;
	}
	public Money subtract(double x)
	{
		value-=x;
		return this;
	}
	public Money subtract(Money x)
	{
		value-=x.get();
		return this;
	}
	public Money divide(double x)
	{
		if(x!=0)
			value/=x;
		return this;
	}
	public Money divide(Money x)
	{
		if(x.get()!=0)
			value/=x.get();
		return this;
	}
	public Money multiply(double x)
	{
		value*=x;
		return this;
	}
	public Money multiply(Money x)
	{
		value*=x.get();
		return this;
	}
	
	public String toString()
	{
		if(after)
		{
			return ("" + value + " " + currency);
		}
		else
		{
			return currency + value;
		}
	}
	

}
