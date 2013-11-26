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

public class BudgetAdapter extends BaseAdapter {
	
	private ArrayList<StatEntryViewHolder> data = new ArrayList<StatEntryViewHolder>();
	private LayoutInflater inflater;
	ViewHolder holder;
	
	public BudgetAdapter(Context theContext ) {
		inflater = (LayoutInflater) theContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	
	public void add(StatEntryViewHolder item)
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
		
		StatEntryViewHolder holder = null;
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.stats_listitem,null);
			holder = new StatEntryViewHolder(data.get(position));
			holder.setLeftTextView((TextView)convertView.findViewById(R.id.listLeftTextView));
			holder.setCenterTextView((TextView)convertView.findViewById(R.id.listCenterTextView));
			holder.setRightTextView((TextView)convertView.findViewById(R.id.listRightTextView));
			
			convertView.setTag(holder);
		}
		else
		{
			StatEntryViewHolder tempEntry = data.get(position);
			holder = (StatEntryViewHolder)convertView.getTag();
			holder.setEntry(tempEntry.getEntry());
			holder.setTitle(tempEntry.getTitle());
			holder.setType(tempEntry.getType());
			
		}
		
		// If this is an entry, print info in all the textviews
		if(holder.getType()==StatEntryViewHolder.Type.entry)
		{
			
			holder.getLeftTextView().setText("Date: " + holder.getEntry().getDate().substring(8));
			holder.getCenterTextView().setText(""+holder.getEntry().getValue());
			// Add a star after the categoryt if this entry has a comment
			holder.getRightTextView().setText(holder.getEntry().getCategory() + (holder.getEntry().getComment().equalsIgnoreCase("") ? "" : " *"));
			
		}
		else
		{
			holder.getLeftTextView().setText(holder.getTitle());
			holder.getCenterTextView().setText("");
			holder.getRightTextView().setText("");
		}
		
		
		return convertView;
		
	}

}
