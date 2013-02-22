package budgetapp.main;
/**
 * Dialog Fragment for adding a new category
 * 
 */

import java.util.ArrayList;
import java.util.List;

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
	View view;
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    		
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	   view = inflater.inflate(R.layout.dialog_choose_category, null);
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(view);
	    theList = (ListView) view.findViewById(R.id.dialog_choose_category_listview);
	    
	    // Add action buttons
	           builder
	           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   ChooseCategoryFragment.this.getDialog().cancel();
	                   
	               }
	           });   
	          // System.out.println("itdd is_ " + category.getText().toString());
	           updateCategories();
	    return builder.create();
	}
	
	public void updateCategories()
    {
		final MainActivity main = ((MainActivity) getActivity());
    	// Get the categories for the Spinner
        List<String> categories = main.datasource.getCategoryNames();
        String temp[] = new String[categories.size()+1];
        for(int i=0;i<categories.size();i++)
        {
        	temp[i] = categories.get(i);
        }
        temp[categories.size()] = "Other...";
       
		 // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
        		  android.R.layout.simple_list_item_1, android.R.id.text1, temp);
        
		 // Apply the adapter to the ListView
		 theList.setAdapter(adapter);
		 theList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		   @Override
		   public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
		      Object listItem = theList.getItemAtPosition(position);
		      if(position!=adapter.getCount()-1)
		      {
			      main.subtractFromBudget(adapter,listItem.toString(),null);
			      ChooseCategoryFragment.this.getDialog().cancel();
		      }
		      else if(position==adapter.getCount()-1)
		      {
		    	   DialogFragment newFragment = new OtherCategoryDialogFragment();
		           newFragment.show(main.getSupportFragmentManager(), "other_category");
		           ChooseCategoryFragment.this.getDialog().cancel();
		      }
		   }
		 });
    
		 theList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long arg) {
				if(position!=adapter.getCount()-1)
			    {   
					Object listItem = theList.getItemAtPosition(position);
					main.setChosenCategory(listItem.toString());
					DialogFragment newFragment = new OtherCategoryDialogFragment();
		            newFragment.show(main.getSupportFragmentManager(), "other_category");
		            ChooseCategoryFragment.this.getDialog().cancel();
			    }
				
				return true;
			   }
	});
			
    }
	
    
	
}