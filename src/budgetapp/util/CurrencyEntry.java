package budgetapp.util;
/**
 * Class for keeping track of the different currencies that are available
 * @author Steen
 *
 */
public class CurrencyEntry extends DatabaseEntry{
	
	private double rate;
	private String name;
	private String symbol;
	
	public CurrencyEntry(String name, String symbol, double rate, int flags){
		this.name = name;
		this.symbol = symbol;
		this.rate = rate;
		setFlags(flags);
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String n)
	{
		name = n;
	}
	
	public String getSymbol()
	{
		return symbol;
	}
	public void setSymbol(String s)
	{
		symbol = s;
	}
	
	public double getRate()
	{
		return rate;
	}
	public void setRate(double r)
	{
		rate = r;
	}

}
