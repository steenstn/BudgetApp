package budgetapp.main;

public class BudgetEntry {
	private long id;
	private int value;
	private String date;
	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public void setValue(int value)
	{
		this.value = value;
	}
	
	public String getDate()
	{
		return date;
	}
	
	public void setDate(String date)
	{
		this.date=date;
	}
	
	@Override
	public String toString()
	{
		return ""+value;
	}
}
