package budgetapp.util;

import static budgetapp.util.FlagHandler.isFlagSet;
import static budgetapp.util.FlagHandler.setFlag;
import static budgetapp.util.FlagHandler.unsetFlag;
import budgetapp.util.money.Money;

public class Installment {

    public static final int INSTALLMENT_PAID = 1;
    public static final int INSTALLMENT_PAUSED = 2;
    public static final int INSTALLMENT_POSITIVE = 4;
    private long id;
    private long transactionId;
    private Money totalValue;
    private Money dailyPayment;
    private Money amountPaid;
    private String category;
    private String comment;
    private String dateLastPaid;
    private int flags;

    public Installment(long id, long transactionId, Money totalValue, Money dailyPayment, String dateLastPaid,
            Money amountPaid, String category, String comment) {
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

    public Installment(long id, long transactionId, Money totalValue, Money dailyPayment, String dateLastPaid,
            Money amountPaid, String category, String comment, int flags) {
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

    public Installment(Money totalValue, Money dailyPayment, String dateLastPaid, Money amountPaid, String category,
            String comment) {
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
        return isFlagSet(flags, INSTALLMENT_PAID);
    }

    public boolean isPaused() {
        return isFlagSet(flags, INSTALLMENT_PAUSED);
    }

    public void setPaidOff(boolean value) {
        if (value) {
            flags = setFlag(flags, INSTALLMENT_PAID);
        } else {
            flags = unsetFlag(flags, INSTALLMENT_PAID);
        }
    }

    public void setPaused(boolean value) {
        if (value) {
            flags = setFlag(flags, INSTALLMENT_PAUSED);
        } else {
            flags = unsetFlag(flags, INSTALLMENT_PAUSED);
        }
    }

    public Money calculateDailyPayment() {
        if(!FlagHandler.isFlagSet(flags, INSTALLMENT_POSITIVE)) {
            Money remaining = totalValue.add(amountPaid);
            return BudgetFunctions.max(remaining, dailyPayment);
        } else {
            Money remaining = totalValue.subtract(amountPaid);
            return BudgetFunctions.min(remaining, dailyPayment);
        }
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

    public void setDateLastPaid(String date) {
        this.dateLastPaid = date;
    }

    public long getId() {
        return id;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public Money getTotalValue() {
        return totalValue;
    }

    public Money getDailyPayment() {
        return dailyPayment;
    }

    public Money getAmountPaid() {
        return amountPaid;
    }

    public Money getRemainingValue() {
        return totalValue.subtract(amountPaid);
    }

    public String getCategory() {
        return category;
    }

    public String getComment() {
        return comment;
    }

    public String getDateLastPaid() {
        return dateLastPaid;
    }

    public int getFlags() {
        return flags;
    }

}
