package budgetapp.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import budgetapp.main.R;
import budgetapp.util.BudgetEntry;

public class TransactionViewHolder extends ViewHolder {

	private BudgetEntry entry;
	
	public TransactionViewHolder(BudgetEntry entry)
	{
		this.entry = entry.clone();
	}
	
	private void setEntry(BudgetEntry entry)
	{
		this.entry = entry;
	}
	
	public BudgetEntry getEntry()
	{
		return entry;
	}

	@Override
	public void printInfo() {
		getLeftTextView().setText(entry.getDate() + ":  "+entry.getValue());
		getCenterTextView().setText("");
		getRightTextView().setText(entry.getCategory());
	}

	@Override
	public void recycle(IViewHolder tempEntry) {
		setEntry(((TransactionViewHolder) tempEntry).getEntry());
	}

	@Override
	public IViewHolder copy() {
		return new TransactionViewHolder(this.entry);
	}

    @Override
	public void setUpConvertView(View convertView) {
		setLeftTextView((TextView)convertView.findViewById(R.id.listLeftTextView));
		setCenterTextView((TextView)convertView.findViewById(R.id.listCenterTextView));
		setRightTextView((TextView)convertView.findViewById(R.id.listRightTextView));
		
	}

}
