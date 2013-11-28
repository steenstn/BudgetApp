package budgetapp.util;
/**
 * Adapter class for putting classes that implements IViewHolder to a listview
 */
import java.util.ArrayList;

import budgetapp.main.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BudgetAdapter extends BaseAdapter {
	
	private ArrayList<IViewHolder> data = new ArrayList<IViewHolder>();
	private LayoutInflater inflater;
	
	public BudgetAdapter(Context theContext ) {
		inflater = (LayoutInflater) theContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void add(IViewHolder item)
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
		
		IViewHolder holder = null;
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.stats_listitem,null);
			holder = data.get(position).copy();
			
			holder.setUpConvertView(convertView);
			convertView.setTag(holder);
		}
		else
		{
			IViewHolder tempEntry = data.get(position);
			holder = (IViewHolder) convertView.getTag();
			holder.recycle(tempEntry);
			
		}
		
		holder.printInfo();
		return convertView;
		
	}

}
