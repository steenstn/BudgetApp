package budgetapp.util;


/**
 *  The class for a cataegoree.
 * @author Steen
 *
 */
public class CategoryEntry extends DatabaseEntry {
	
	private String category; // Name of the category
	private int num; // How many transactions within this category
	private long total; // Total money spent/earned within this category
	
	public CategoryEntry(long id, String category,int num,long total){
		this.id = id;
		this.category = category;
		this.num=num;
		this.total=total;
	}
	public CategoryEntry(long id, String category){
		this.id = id;
		this.category = category;
		this.num=0;
		this.total=0;
	}
	
	public CategoryEntry(String category){
		this.category = category;
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
	
	public void setTotal(long n)
	{
		total = n;
	}
	
	public long getTotal()
	{
		return total;
	}
	
	
	@Override
	public String toString(){
		return category;
	}
}
