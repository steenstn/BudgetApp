package budgetapp.util;


/**
 * Class for a budget entry. Used to keep track of transactions.
 * 
 * @author Steen
 * 
 */
public class BudgetEntry extends DatabaseEntry{

	private Money value; // How large the transaction was
	private String date; // The date it was done
	private String category; // What category the transaction had
	private String comment; // Possible comment for the transaction
	private int flags;
	
	public BudgetEntry(Money value,String date,String category)
	{
		this.value=new Money(value);
		this.date=date;
		this.category=category;
		this.flags=0;
		this.comment = "";
	}
	public BudgetEntry(Money value,String date,String category,String comment)
	{
		this.value=new Money(value);
		this.date=date;
		this.category=category;
		this.flags=0;
		this.comment = comment;
	}
	public BudgetEntry(long id,Money value,String date,String category)
	{
		this.id=id;
		this.value=new Money(value);
		this.date=date;
		this.flags=0;
		this.category=category;
		this.comment = "";
	}
	public BudgetEntry(long id,Money value,String date,String category,int flags)
	{
		this.id=id;
		this.value=new Money(value);
		this.date=date;
		this.category=category;
		this.flags=flags;
		this.comment = "";
	}
	
	public BudgetEntry(long id,Money value,String date,String category,int flags,String comment)
	{
		this.id=id;
		this.value=new Money(value);
		this.date=date;
		this.category=category;
		this.flags=flags;
		this.comment = comment;
	}
	
	
	public void setId(long id){
		this.id = id;
	}
	
	public Money getValue(){
		return value;
	}
	
	public void setValue(Money value){
		this.value = value;
	}
	public void setValue(double x){
		this.value.set(x);
	}
	
	public String getDate(){
		return date;
	}
	
	public String getYear(){
		return date.substring(0, 4);
	}
	
	public String getMonth(){
		return date.substring(5,7);
	}
	public void setDate(String date){
		this.date=date;
	}
	
	public void setCategory(String category){
		this.category=category;
	}
	
	public String getCategory(){
		return category;
	}
	
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	
	public String getComment()
	{
		if(comment!=null)
			return comment;
		else
			return "";
	}
	@Override
	public String toString(){
		return "ID: " + id + " val: "+ value + " date: " + date + " cat: " + category + " flags: " +flags;
	}
}
