package budgetapp.util;


/**
 *  The class for a cataegoree. Has number of entries and total money spent on it
 * @author Steen
 *
 */
public class CategoryEntry extends DatabaseEntry {
	
	private String category; // Name of the category
	private int num; // How many transactions within this category
	private Money value; // Total money spent/earned within this category
	
	public CategoryEntry(long id, String category,int num,Money total,int flags){
		setId(id);
		this.category = category;
		this.num=num;
		this.value = new Money(total);
		setFlags(flags);
	}
	
	public CategoryEntry(long id, String category,int num,Money total){
		setId(id);
		this.category = category;
		this.num=num;
		this.value=new Money(total);
		setFlags(0);
	}
	public CategoryEntry(long id, String category){
		setId(id);
		this.category = category;
		this.num=0;
		this.value=new Money();
		setFlags(0);
	}
	
	public CategoryEntry(String category){
		setId(0);
		this.category = category;
		this.value=new Money();
		this.num=0;
		setFlags(0);
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
	
	public void setValue(Money n)
	{
		value = n;
	}
	
	public Money getValue()
	{
		return value;
	}
	public void addToTotal(Money n){
		value = value.add(n);
	}
	
	
	@Override
	public String toString(){
		return category;
	}
}
