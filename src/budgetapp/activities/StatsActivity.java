package budgetapp.activities;

import java.util.ArrayList;
import java.util.List;

import budgetapp.fragments.EditTransactionDialogFragment;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.util.BudgetAdapter;
import budgetapp.util.BudgetEntry;
import budgetapp.util.CategoryEntry;
import budgetapp.util.DayEntry;
import budgetapp.util.Money;
import budgetapp.util.ViewHolder;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.database.BudgetDataSource;
import budgetapp.util.stats.*;
import budgetapp.views.StatsView;
public class StatsActivity extends FragmentActivity{
	
	private StatsView theView;
	List<DayEntry> days;
	List<DayEntry> dayFlow;
	List<BudgetEntry> entries;
	List<CategoryEntry> categories;
	List<CategoryEntry> categoryStats; // Contains stats for categories
	List<String> categoryNames;
	ListView top;
	BudgetAdapter ad;
	ArrayList<BudgetEntry> allEntries = new ArrayList<BudgetEntry>();
	ArrayList<CompositeStats> years;
	
	int numDaysForDerivative = 30;
	int numTransactionsForDerivative = 10;
	ViewHolder selectedViewHolder;
	int selectedViewHolderIndex;
	private BudgetModel model;
	
	public void removeTransactionEntry(BudgetEntry entry)
	{
		model.removeTransaction(entry);
	}
	
	public void editTransactionEntry(BudgetEntry oldEntry, BudgetEntry newEntry)
	{
		model.editTransaction(oldEntry, newEntry);
	}
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theView = (StatsView)View.inflate(this, R.layout.activity_stats, null);
        setContentView(theView);
        model = new BudgetModel(this);
        theView.setModel(model);
        theView.setViewListener(viewListener);
        
        theView.setUpComposite();
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
			 if(listItem.flag == ViewHolder.ENTRY)
			 {
		    	 DialogFragment newFragment = new EditTransactionDialogFragment();
		    	 Bundle bundle = new Bundle();
		    	 bundle.putParcelable("entry", (Parcelable) listItem.entry);
		    	 
		    	 newFragment.setArguments(bundle);
		    	
		    	 newFragment.show(getSupportFragmentManager(), "edit_transaction");
		     }
			
		}
		
		@Override
		public void listViewClick(ViewHolder theEntry) {
			if(!theEntry.entry.getComment().equalsIgnoreCase(""))
			{	
				Toast.makeText(getBaseContext(), theEntry.entry.getComment(), Toast.LENGTH_LONG).show();
			}
			
		}
	};
	
}
