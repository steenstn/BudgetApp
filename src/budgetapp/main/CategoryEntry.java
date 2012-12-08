package budgetapp.main;
/**
 *  The class for a cataegory.
 * @author Steen
 *
 */
public class CategoryEntry extends DatabaseEntry {
	
	private String category;
	
	public CategoryEntry(long id, String category){
		this.id = id;
		this.category = category;
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
	
	@Override
	public String toString(){
		return category;
	}
}
