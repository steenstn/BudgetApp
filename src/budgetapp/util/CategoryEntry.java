package budgetapp.util;


/**
 *  The class for a cataegoree. Has number of entries and total money spent on it
 * @author Steen
 *
 */
public class CategoryEntry extends DatabaseEntry {
	
	private String category; // Name of the category
	private int num; // How many transactions within this category
	private Money total; // Total money spent/earned within this category
	
	public CategoryEntry(long id, String category,int num,Money total,int flags){
		this.id = id;
		this.category = category;
		this.num=num;
		this.total= new Money(total);
		this.flags=flags;
	}
	public CategoryEntry(long id, String category,int num,Money total){
		this.id = id;
		this.category = category;
		this.num=num;
		this.total=new Money(total);
	}
	public CategoryEntry(long id, String category){
		this.id = id;
		this.category = category;
		this.num=0;
		this.total=new Money();
	}
	
	public CategoryEntry(String category){
		this.category = category;
		this.total=new Money();
		this.num=0;
	}
	
	public void setCategory(String category){
		this.category = category;
	}
	public String getCategory(){
		return category;
	}
	
	public void setNum(int n){
		num=n;
	}
	public int getNum(){
		return num;
	}
	public void addToNum(int n){
		num+=n;
	}
	
	public void setTotal(Money n)
	{
		total = n;
	}
	
	public Money getTotal()
	{
		return total;
	}
	public void addToTotal(Money n){
		total = total.add(n);
	}
	
	
	@Override
	public String toString(){
		return category;
	}
}
