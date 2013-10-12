package budgetapp.util;
/**
 * Money class. Instead of using doubles directly, this class is used to get more flexibility.
 * @author Steen
 *
 */
public class Money {
	
	private double value;
	public static boolean after = true;
	private static String currency = "kr";
	private static double exchangeRate = 1;
	
	public Money()
	{
		value = 0;
	}
	
	public Money(double x)
	{
		value = x * exchangeRate;
	}
	public Money(Money m)
	{
		value = m.get();
	}
	public static void setCurrency(String c)
	{
		currency = c;
	}
	
	public static String getCurrency()
	{
		return currency;
	}
	
	public static double getExchangeRate()
	{
		return exchangeRate;
	}
	
	public static void setExchangeRate(double val)
	{
		exchangeRate = val;
	}
	public double get()
	{
		return value;
	}
	public Money add(Money x)
	{
		return new Money((this.value + x.get()) / exchangeRate);
	}
	
	public Money subtract(double x)
	{
		return new Money(this.value - x);
	}
	public Money subtract(Money x)
	{
		return new Money((this.value - x.get()) / exchangeRate);
	}
	public Money divide(double x)
	{
		if(x!=0)
			return new Money((this.value/x) / exchangeRate);
		else
			return new Money();
	}
	/*
	public double divide(Money x)
	{
		return this.value()/x.value();
	}*/
	
	public Money divide(Money x)
	{
		return new Money((value / x.get()) / exchangeRate);
	}
	
	public Money makeNegative()
	{
		return new Money((Math.abs(value) * -1) / exchangeRate);
	}
	
	public Money makePositive()
	{
		return new Money(Math.abs(value) / exchangeRate);
	}
	
	public Money multiply(int x)
	{
		return new Money((value * (double)x) / exchangeRate);
	}
	
	public Money multiply(Money x)
	{
		return new Money((this.value * x.get()) / exchangeRate);
	}
	
	public boolean biggerThan(Money x)
	{
		if(value > x.get())
			return true;
		else 
			return false;
	}
	
	public boolean biggerThanOrEquals(Money x)
	{
		if(value >= x.get())
			return true;
		else 
			return false;
	}
	
	public boolean smallerThan(Money x)
	{
		if(value < x.get())
			return true;
		else
			return false;
	}
	
	public boolean smallerThanOrEquals(Money x)
	{
		if(value <= x.get())
			return true;
		else
			return false;
	}
	
	public boolean equals(Money x)
	{
		if(value == x.get())
			return true;
		else
			return false;
	}
	
	public boolean almostZero()
	{
		if(value < 0.000001)
			return true;
		else
			return false;
	}
	/**
	 * Returns a string of the value with some formatting to get the minus sign
	 * at the right place and not print out decimals where it's not needed.
	 */
	public String toString()
	{
		double fixedValue = value/exchangeRate;
		if(after)
		{
			if(frac(fixedValue)<0.01)
				return String.format("%.0f "+currency,fixedValue);
			else
				return String.format("%.2f "+currency,fixedValue);
		}
		else
		{
			if(frac(fixedValue)<0.01)
				return (fixedValue<0.0 ? "-" : "") + String.format(currency+"%.0f ",Math.abs(fixedValue));
			else
				return (fixedValue<0.0 ? "-" : "") + String.format(currency + "%.2f",Math.abs(fixedValue));
		}
	}
	
	private double frac(double d)
	{
		return d-Math.floor(d);
	}
	

}
