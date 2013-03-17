package budgetapp.util;

import java.util.ArrayList;

import budgetapp.main.R;

import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BudgetAdapter extends BaseAdapter {
	
	private ArrayList<ViewHolder> data = new ArrayList<ViewHolder>();
	private LayoutInflater inflater;
	
	public BudgetAdapter(Context theContext ) {
		inflater = (LayoutInflater) theContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	
	public void add(ViewHolder item)
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
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.stats_listitem,null);
			holder = new ViewHolder(data.get(position));
			holder.textView = (TextView)convertView.findViewById(android.R.id.text1);
			convertView.setTag(holder);
		}
		else
		{
			ViewHolder tempEntry = data.get(position);
			holder = (ViewHolder)convertView.getTag();
			holder.entry = tempEntry.entry;
			holder.title = tempEntry.title;
			holder.flag = tempEntry.flag;
			
		}
		holder.textView.setText(holder.toString());
		return convertView;
		
	}

}
