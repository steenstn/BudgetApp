package budgetapp.util;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Class for a budget entry. Used to keep track of transactions. Implements Parcelable
 * to be able to be sent between Activities
 * 
 * @author Steen
 * 
 */
public class BudgetEntry extends DatabaseEntry implements Parcelable{

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
	
	public BudgetEntry(Parcel in)
	{
		String[] data = new String[6];
		
		in.readStringArray(data);
		
		this.id = Long.parseLong(data[0]);
		this.value=new Money(Double.parseDouble(data[1]));
		this.date=data[2];
		this.category=data[3];
		this.flags=Integer.parseInt(data[4]);
		this.comment = data[5];
		
	}
	
	public BudgetEntry() {
		this.id=-1;
		this.value=new Money(0);
		this.date="";
		this.category="";
		this.flags=0;
		this.comment = "";
	}
	@Override
	public int describeContents()
	{
		return 0;
	}
	
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeStringArray(new String[]{
			String.valueOf(this.id),
			String.valueOf(this.value),
			this.date,
			this.category,
			String.valueOf(this.flags),
			this.comment
		});
	}
	
	public static final Parcelable.Creator<BudgetEntry> CREATOR = new Parcelable.Creator<BudgetEntry>() {
		 
		@Override
		public BudgetEntry createFromParcel(Parcel source) {
			return new BudgetEntry(source);  
		}
		 
		@Override
		public BudgetEntry[] newArray(int size) {
			return new BudgetEntry[size];
		}
	};
	
	public BudgetEntry clone()
	{
		BudgetEntry result = new BudgetEntry();
		result.setId(this.getId());
		result.setDate(this.getDate());
		result.setCategory(this.getCategory());
		result.setValue(this.getValue().get());
		result.setComment(this.getComment());
		return result;
		
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
	
	/**
	 * Gets just the year from the date string
	 * @return The year as a string
	 */
	public String getYear(){
		return date.substring(0, 4);
	}
	
	/**
	 * Gets just the month from the date string, two digits
	 * @return The month as a string
	 */
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
