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
	private static class ViewHolder {
		public ViewHolder(BudgetEntry entry)
		{
			this.entry = entry;
		}
		
        public TextView textView;
        public BudgetEntry entry;
        public String toString(){
        	String temp = "Date: " + entry.getDate().substring(8) + "\t\t" + entry.getValue();
    		
    		if(entry.getValue().get()>-100 && entry.getValue().get()<1000)
    			temp+="\t";
    		if(entry.getValue().get()<=-1000)
    			temp+="\t";
    		//view.append("\t" + entry.getCategory());
    		temp+="\t"+entry.getCategory();
    		// Add comment if there is one
    		// But only print max 20 characters
    		String comment = entry.getComment();
    		if(comment!=null && !comment.equalsIgnoreCase(""))
    		{
    			temp+=" *";
    		}
    		return temp;
        }
    }
	private ArrayList<BudgetEntry> data = new ArrayList<BudgetEntry>();
	private LayoutInflater inflater;
	
	public BudgetAdapter(Context theContext ) {
		inflater = (LayoutInflater) theContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void add(final BudgetEntry item)
	{
		data.add(item);
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
		System.out.println("getView " + position + " " + convertView);
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
			BudgetEntry tempEntry = data.get(position);
			holder = (ViewHolder)convertView.getTag();
			holder.entry = tempEntry;
		}
		holder.textView.setText(holder.toString());
		return convertView;
		
	}

}
