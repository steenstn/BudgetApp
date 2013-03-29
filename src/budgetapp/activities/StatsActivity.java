package budgetapp.activities;


import budgetapp.fragments.EditTransactionDialogFragment;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.util.BudgetEntry;
import budgetapp.util.ViewHolder;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import budgetapp.views.StatsView;
public class StatsActivity extends FragmentActivity{
	
	private StatsView theView;
	private BudgetModel model;
	
	/**
	 * Removes an entry from the model
	 * @param entry - The BudgetEntry to remove
	 */
	public void removeTransactionEntry(BudgetEntry entry)
	{
		model.removeTransaction(entry);
	}
	
	/**
	 * Edits an entry
	 * @param oldEntry - The entry to edit
	 * @param newEntry - The entry to replace the old with
	 */
	public void editTransactionEntry(BudgetEntry oldEntry, BudgetEntry newEntry)
	{
		model.editTransaction(oldEntry, newEntry);
	}
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new BudgetModel(this);
        theView = (StatsView)View.inflate(this, R.layout.activity_stats, null);

        theView.setModel(this.model);
        setContentView(theView);
        
        theView.setViewListener(viewListener);
        theView.update();
        
        
	}
	
	private StatsView.ViewListener viewListener = new StatsView.ViewListener() {
		
		@Override
		public void spinnerItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Spinner yearSpinner =  (Spinner)findViewById(R.id.spinnerYear);
			Spinner monthSpinner =  (Spinner)findViewById(R.id.spinnerMonth);
			Spinner categorySpinner = (Spinner)findViewById(R.id.spinnerCategory);
			// Find what year, month and category that is chosen
			int selectedYear = yearSpinner.getSelectedItemPosition();
			int selectedMonth = monthSpinner.getSelectedItemPosition()-1; // When there is a "All categories" entry. -1 is needed to get correct month
			String selectedCategory = (String) categorySpinner.getSelectedItem();
			
			
			theView.setSelectedYear(selectedYear);
			theView.setSelectedMonth(selectedMonth);
			theView.setSelectedCategory(selectedCategory);
		}
		
		@Override
		public void listViewLongClick(ViewHolder theEntry) {
			 ViewHolder listItem = theEntry;
			 if(listItem.getType() == ViewHolder.Type.entry)
			 {
		    	 DialogFragment newFragment = new EditTransactionDialogFragment();
		    	 Bundle bundle = new Bundle();
		    	 bundle.putParcelable("entry", (Parcelable) listItem.getEntry());
		    	 
		    	 newFragment.setArguments(bundle);
		    	 newFragment.show(getSupportFragmentManager(), "edit_transaction");
		     }
			
		}
		
		@Override
		public void listViewClick(ViewHolder theEntry) {
			if(!theEntry.getEntry().getComment().equalsIgnoreCase(""))
			{	
				Toast.makeText(getBaseContext(), theEntry.getEntry().getComment(), Toast.LENGTH_LONG).show();
			}
			
		}
	};
	
}
