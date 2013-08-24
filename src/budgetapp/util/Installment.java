package budgetapp.util;

public class Installment {
	
	private long id;
	private long transactionId;
	private Money totalValue;
	private Money dailyPayment;
	private Money amountPaid;
	private String category;
	private String comment; 
	private String dateLastPaid;
	
	/*String DATABASE_CREATE_TABLE_INSTALLMENTS = "create table "
	+ TABLE_INSTALLMENTS + "(" + COLUMN_ID
	+ " integer primary key autoincrement, "
	+ COLUMN_TRANSACTION_ID + " integer, "
	+ COLUMN_VALUE + " double, " + COLUMN_DATE_LAST_PAID + " text, "
	+ COLUMN_REMAINING_AMOUNT + " double);";*/
	
	public Installment(long id, long transactionId, Money totalValue, Money dailyPayment,
			String dateLastPaid, Money amountPaid, String category, String comment)
	{
		this.id = id;
		this.transactionId = transactionId;
		this.totalValue = totalValue;
		this.dailyPayment = dailyPayment;
		this.amountPaid = amountPaid;
		this.dateLastPaid = dateLastPaid;
		this.category = category;
		this.comment = comment;
	}
	
	public Installment(Money totalValue, Money dailyPayment,
			String dateLastPaid, Money amountPaid, String category, String comment)
	{
		this.id = -1;
		this.transactionId = -1;
		this.totalValue = totalValue;
		this.dailyPayment = dailyPayment;
		this.amountPaid = amountPaid;
		this.dateLastPaid = dateLastPaid;
		this.category = category;
		this.comment = comment;
	}
	
	public void setTransactionId(long id)
	{
		this.transactionId = id;
	}
	public void setTotalValue(Money value) 
	{
		this.totalValue = value; 
	}
	
	public void setdailyPayment(Money value) 
	{
		this.dailyPayment = value;
	}
	
	public long getId() { return id; }
	public long getTransactionId() { return transactionId; }
	public Money getTotalValue() { return totalValue; }
	public Money getDailyPayment() { return dailyPayment; }
	public Money getAmountPaid() { return amountPaid; }
	public Money getRemainingValue() { return new Money(totalValue.get() - amountPaid.get()); } 
	public String getCategory() { return category; }
	public String getComment() { return comment; }
	public String getDateLastPaid() { return dateLastPaid; }
	
}
