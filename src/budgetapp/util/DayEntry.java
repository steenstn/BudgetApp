package budgetapp.util;
/**
 * The class for storing the total money spent/earned for a day.
 * 
 * @author Steen
 *
 */

public class DayEntry extends DatabaseEntry{
	private long total;
	private String date;
	
	public DayEntry(String date,int total)
	{
		this.total=total;
		this.date=date;
	}
	public DayEntry(String date,long total)
	{
		this.total=total;
		this.date=date;
	}
	public DayEntry(long id,String date,long total)
	{
		this.id=id;
		this.total=total;
		this.date=date;
	}
	public DayEntry(long id,String date,long total,int flags)
	{
		this.id=id;
		this.total=total;
		this.date=date;
		this.flags=flags;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public long getTotal(){
		return total;
	}
	
	public void setTotal(int total){
		this.total = total;
	}
	
	public String getDate(){
		return date;
	}
	
	public void setDate(String date){
		this.date=date;
	}
}
