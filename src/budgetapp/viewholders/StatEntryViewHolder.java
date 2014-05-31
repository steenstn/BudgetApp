package budgetapp.viewholders;

import java.text.ParseException;
import java.util.Locale;

import budgetapp.util.BudgetFunctions;
import budgetapp.util.entries.BudgetEntry;
import budgetapp.util.money.MoneyFactory;

/**
 * ViewHolder for displaying info about a transaction in the Statistics view
 * @author Steen
 *
 */
public class StatEntryViewHolder
    extends ViewHolder {

    private BudgetEntry entry;
    private Type flag;
    private String title;

    public static enum Type {
        entry,
        year,
        month
    }

    public StatEntryViewHolder(BudgetEntry entry) {
        this.entry = entry;
        flag = Type.entry;
        this.title = "";
    }

    public StatEntryViewHolder(StatEntryViewHolder viewHolder) {
        this.entry = viewHolder.getEntry();
        this.title = viewHolder.getTitle();
        this.flag = viewHolder.getType();
    }

    public StatEntryViewHolder(String theString, Type theType) {
        this.entry = new BudgetEntry(-1, MoneyFactory.createMoney(), "", "");
        this.title = theString;
        this.flag = theType;
    }

    public BudgetEntry getEntry() {
        return entry;
    }

    public Type getType() {
        return flag;
    }

    private String getTitle() {
        return title;
    }

    private void setEntry(BudgetEntry entry) {
        this.entry = entry;
    }

    private void setType(Type type) {
        this.flag = type;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void populateViews() {
        // If this is an entry, print info in all the textviews
        if (getType() == StatEntryViewHolder.Type.entry) {

            String dateString = "";
            try {
                String inputTimeStamp = getEntry().getDate();

                final String inputFormat = "yyyy/MM/dd HH:mm";
                final String outputFormat = "EEE dd HH:mm";

                dateString = BudgetFunctions.timeStampConverter(inputFormat, inputTimeStamp, outputFormat);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            String outputDateString = dateString.substring(0, 1).toUpperCase(Locale.US) + dateString.substring(1);
            getLeftTextView().setText(outputDateString);
            getCenterTextView().setText("" + getEntry().getValue());
            // Add a star after the category if this entry has a comment
            getRightTextView().setText(
                getEntry().getCategory() + (getEntry().getComment().equalsIgnoreCase("") ? "" : " *"));

        } else {
            getLeftTextView().setText(getTitle());
            getCenterTextView().setText("");
            getRightTextView().setText("");
        }
    }

    @Override
    public void recycle(IViewHolder tempEntry) {
        setEntry(((StatEntryViewHolder) tempEntry).getEntry());
        setTitle(((StatEntryViewHolder) tempEntry).getTitle());
        setType(((StatEntryViewHolder) tempEntry).getType());

    }

    @Override
    public IViewHolder copy() {
        return new StatEntryViewHolder(this);
    }
}
