package budgetapp.util;
/**
 * Money class. Instead of using doubles directly, this class is used to get more flexibility
 * @author Steen
 *
 */
public class Money {
	
	private double value;
	public static boolean after = false;
	private static String currency = "£";
	
	public Money()
	{
		value = 0;
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
	
	/**
	 * Returns a string of the value with some formatting to get the minus sign
	 * at the right place and not print out decimals where it's not needed
	 */
	public String toString()
	{
		if(after)
		{
			if(frac(value)<0.01)
				return String.format("%.0f "+currency,value);
			else
				return String.format("%.2f "+currency,value);
		}
		else
		{
			if(frac(value)<0.01)
				return (value<0.0 ? "-" : "") + String.format(currency+"%.0f ",Math.abs(value));
			else
				return (value<0.0 ? "-" : "") + String.format(currency + "%.2f",Math.abs(value));
		}
	}
	
	double frac(double d)
	{
		return d-Math.floor(d);
	}
	

}
