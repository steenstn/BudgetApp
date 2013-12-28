package budgetapp.fragments;
/**
 * Dialog Fragment for adding a new category
 * 
 */

import java.util.List;

import budgetapp.activities.MainActivity;
import budgetapp.main.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChooseCategoryFragment extends DialogFragment {
	
	private ListView theList;
	private View view;
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    view = inflater.inflate(R.layout.dialog_choose_category, null);
	    
	    builder.setView(view);
	    theList = (ListView) view.findViewById(R.id.dialog_choose_category_listview);

	    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
	    {
	        public void onClick(DialogInterface dialog, int id) {
	    	    ChooseCategoryFragment.this.getDialog().cancel(); 
	        }
        });   
	   
	    updateCategories();
	    setUpListeners();
	    return builder.create();
	}
	
	/**
	 * Adds all categories to the ListView and the "Other..." option
	 */
	private void updateCategories()
    {
        List<String> categories = ((MainActivity)getActivity()).getCategoryNames();
        String temp[] = new String[categories.size()+1];
        for(int i=0;i<categories.size();i++)
        {
        	temp[i] = categories.get(i);
        }
        temp[categories.size()] = "Other...";
       
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
        		  android.R.layout.simple_list_item_1, android.R.id.text1, temp);
        
		 theList.setAdapter(adapter);
    }
	
	/**
	 * Set up the listeners for the ListView, click and longclick listeners
	 */
	private void setUpListeners()
	{
		theList.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
				Object listItem = theList.getItemAtPosition(position);
				if(position!=adapter.getCount()-1)
				{
					String enteredValue = ((MainActivity)getActivity()).getEnteredValue();
					if(enteredValue.equalsIgnoreCase(""))
					{
						String category = listItem.toString();
						List<Double> prices = ((MainActivity)getActivity()).getAutoCompleteValues(category);
						if(prices.size()!=0)
						{
					    	 
							Bundle bundle = new Bundle();
					    	bundle.putString("category",category);
					    	 
							DialogFragment newFragment = new ChoosePriceFragment();
					    	newFragment.setArguments(bundle);
					    	newFragment.show(((MainActivity)getActivity()).getSupportFragmentManager(), "choose_price");
					    	
					    	ChooseCategoryFragment.this.getDialog().cancel();
						}
				    	
					}
					else
					{
						((MainActivity)getActivity()).subtractFromBudget(enteredValue, listItem.toString(),null);
						ChooseCategoryFragment.this.getDialog().cancel();
					}
				}
				else if(position==adapter.getCount()-1)
				{
					DialogFragment newFragment = new OtherCategoryDialogFragment();
					newFragment.show(((MainActivity)getActivity()).getSupportFragmentManager(), "other_category");
					ChooseCategoryFragment.this.getDialog().cancel();
				}
			}
	 	});
	    
		theList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long arg) {
				if(position!=adapter.getCount()-1)
			    {   
					Object listItem = theList.getItemAtPosition(position);
					((MainActivity)getActivity()).setChosenCategory(listItem.toString());
					DialogFragment newFragment = new OtherCategoryDialogFragment();
		            newFragment.show(((MainActivity)getActivity()).getSupportFragmentManager(), "other_category");
		            ChooseCategoryFragment.this.getDialog().cancel();
			    }
				
				return true;
		   }
		});
	}
	
    
	
}