package budgetapp.main;

/**
 * Class for a budget entry. Used to keep track of transactions.
 * 
 * @author Steen
 * 
 */
public class BudgetEntry extends DatabaseEntry{

	private int value;
	private String date;
	private String category;
	
	public BudgetEntry(int value,String date,String category)
	{
		this.value=value;
		this.date=date;
		this.category=category;
	}
	public BudgetEntry(long id,int value,String date,String category)
	{
		this.id=id;
		this.value=value;
		this.date=date;
		this.category=category;
	}
	
	
	public void setId(long id){
		this.id = id;
	}
	
	public int getValue(){
		return value;
	}
	
	public void setValue(int value){
		this.value = value;
	}
	
	public String getDate(){
		return date;
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
	@Override
	public String toString(){
		return date + ": " + value;
	}
}
