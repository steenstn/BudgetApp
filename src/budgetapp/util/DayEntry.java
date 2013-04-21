package budgetapp.util;
/**
 * The class for storing the total money spent/earned for a day.
 * 
 * @author Steen
 *
 */

public class DayEntry extends DatabaseEntry{
	private Money total;
	private String date;
	
	public DayEntry(String date,Money total)
	{
		this.total=new Money(total);
		this.date=date;
	}
	public DayEntry(long id,String date,Money total)
	{
		setId(id);
		this.total=new Money(total);
		this.date=date;
	}
	public DayEntry(long id,String date,Money total,int flags)
	{
		setId(id);
		this.total=new Money(total);
		this.date=date;
		setFlags(flags);
	}
	
	public Money getTotal(){
		return total;
	}
	
	public Money getValue(){
		return total;
	}
	
	public void setTotal(Money total){
		this.total = total;
	}
	
	public String getDate(){
		return date;
	}
	
	public void setDate(String date){
		this.date=date;
	}
}
