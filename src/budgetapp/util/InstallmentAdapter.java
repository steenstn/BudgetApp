package budgetapp.util;
/**
 * Adapter class for adding a transaction entry to a ListView
 */
import java.util.ArrayList;

import budgetapp.main.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InstallmentAdapter extends BaseAdapter {
	
	private ArrayList<InstallmentViewHolder> data = new ArrayList<InstallmentViewHolder>();
	private LayoutInflater inflater;
	
	public InstallmentAdapter(Context theContext ) {
		inflater = (LayoutInflater) theContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	
	public void add(InstallmentViewHolder item)
	{
		data.add(item);
	}
	
	public void remove(int pos)
	{
		data.remove(pos);
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position)
	{
		return data.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}
	
	/**
	 * Overridden function for getting a View from the ListView, uses recycling to 
	 * save resources
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		
		InstallmentViewHolder holder = null;
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.installment_listitem,null);
			holder = new InstallmentViewHolder(data.get(position));
			holder.setLeftTextView((TextView)convertView.findViewById(R.id.listLeftTextView));
			holder.setCenterTextView((TextView)convertView.findViewById(R.id.listCenterTextView));
			holder.setRightTextView((TextView)convertView.findViewById(R.id.listRightTextView));
			
			convertView.setTag(holder);
		}
		else
		{
			InstallmentViewHolder tempEntry = data.get(position);
			holder = (InstallmentViewHolder)convertView.getTag();
			holder.setEntry(tempEntry.getEntry());
			holder.setTitle(tempEntry.getTitle());
			
		}
		
		
		holder.getLeftTextView().setText(holder.getEntry().getCategory());
		holder.getCenterTextView().setText(""+holder.getEntry().getRemainingValue().makePositive() + "/"
				+ holder.getEntry().getTotalValue().makePositive());
		
		double daysLeft = (holder.getEntry().getRemainingValue().divide(holder.getEntry().getDailyPayment())).get();
		int numDaysLeft = (int) Math.ceil(daysLeft);
		holder.getRightTextView().setText(""+numDaysLeft);
			
		return convertView;
		
	}

}
