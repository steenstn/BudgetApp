package budgetapp.util;

public class Installment {
	
	public static final int INSTALLMENT_PAID = 1;
	public static final int INSTALLMENT_ACTIVE = 2;
	private long id;
	private long transactionId;
	private Money totalValue;
	private Money dailyPayment;
	private Money amountPaid;
	private String category;
	private String comment; 
	private String dateLastPaid;
	private int flags;
	
	
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
		this.flags = 0;
	}
	
	public Installment(long id, long transactionId, Money totalValue, Money dailyPayment,
			String dateLastPaid, Money amountPaid, String category, String comment, int flags)
	{
		this.id = id;
		this.transactionId = transactionId;
		this.totalValue = totalValue;
		this.dailyPayment = dailyPayment;
		this.amountPaid = amountPaid;
		this.dateLastPaid = dateLastPaid;
		this.category = category;
		this.comment = comment;
		this.flags = flags;
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
		this.flags = 0;
	}
	
	public boolean isPaidOff() {
		return (flags & INSTALLMENT_PAID) == INSTALLMENT_PAID;
	}
	
	public boolean isActive() {
		return (flags & INSTALLMENT_ACTIVE) == INSTALLMENT_ACTIVE;
	}
	
	
	public void setTransactionId(long id) {
		this.transactionId = id;
	}
	public void setTotalValue(Money value) {
		this.totalValue = value; 
	}
	
	public void setdailyPayment(Money value) {
		this.dailyPayment = value;
	}
	
	public void setFlags(int flags) {
		this.flags = flags;
	}
	
	public long getId() { return id; }
	public long getTransactionId() { return transactionId; }
	public Money getTotalValue() { return totalValue; }
	public Money getDailyPayment() { return dailyPayment; }
	public Money getAmountPaid() { return amountPaid; }
	public Money getRemainingValue() { return totalValue.subtract(amountPaid); } 
	public String getCategory() { return category; }
	public String getComment() { return comment; }
	public String getDateLastPaid() { return dateLastPaid; }
	public int getFlags() { return flags; }
	
}
