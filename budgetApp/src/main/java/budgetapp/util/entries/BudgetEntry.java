package budgetapp.util.entries;

import android.os.Parcel;
import android.os.Parcelable;
import budgetapp.util.money.Money;
import budgetapp.util.money.MoneyFactory;

public class BudgetEntry
    extends DatabaseEntry
    implements Parcelable {

    private String date;
    private String category;
    private String comment;

    public BudgetEntry(BudgetEntry other) {
        this.setValue(other.getValue());
        this.date = other.getDate();
        this.category = other.getCategory();
        setFlags(other.getFlags());
        this.comment = other.getComment();
    }

    public BudgetEntry(Money value, String date, String category) {
        this.setValue(value);
        this.date = date;
        this.category = category;
        setFlags(0);
        this.comment = "";
    }

    public BudgetEntry(Money value, String date, String category, String comment) {
        this.setValue(value);
        this.date = date;
        this.category = category;
        setFlags(0);
        this.comment = comment;
    }

    public BudgetEntry(Money value, String date, String category, String comment, int flags) {
        this.setValue(value);
        this.date = date;
        this.category = category;
        setFlags(flags);
        this.comment = comment;
    }

    public BudgetEntry(long id, Money value, String date, String category) {
        setId(id);
        this.setValue(value);
        this.date = date;
        setFlags(0);
        this.category = category;
        this.comment = "";
    }

    public BudgetEntry(long id, Money value, String date, String category, int flags) {
        setId(id);
        this.setValue(value);
        this.date = date;
        this.category = category;
        setFlags(flags);
        this.comment = "";
    }

    public BudgetEntry(long id, Money value, String date, String category, int flags, String comment) {
        setId(id);
        this.setValue(value);
        this.date = date;
        this.category = category;
        setFlags(flags);
        this.comment = comment;
    }

    public BudgetEntry(Parcel in) {
        String[] data = new String[6];

        in.readStringArray(data);

        setId(Long.parseLong(data[0]));
        this.setValue(MoneyFactory.convertDoubleToMoney(Double.parseDouble(data[1])));
        this.date = data[2];
        this.category = data[3];
        setFlags(Integer.parseInt(data[4]));
        this.comment = data[5];

    }

    public BudgetEntry() {
        setId(-1);
        this.setValue(Money.zero());
        this.date = "";
        this.category = "";
        setFlags(0);
        this.comment = "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] { String.valueOf(this.getId()), String.valueOf(this.getValue()), this.date,
                this.category, String.valueOf(getFlags()), this.comment });
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

    public BudgetEntry clone() {
        BudgetEntry result = new BudgetEntry();
        result.setId(this.getId());
        result.setDate(this.getDate());
        result.setCategory(this.getCategory());
        result.setValue(this.getValue());
        result.setComment(this.getComment());
        result.setFlags(this.getFlags());
        return result;
    }

    public String getDate() {
        return date;
    }

    public String getYear() {
        return date.substring(0, 4);
    }

    public String getMonth() {
        return date.substring(5, 7);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        if (comment != null) {
            return comment;
        } else {
            return "";
        }
    }
}
