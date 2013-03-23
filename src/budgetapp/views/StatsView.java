package budgetapp.views;

import java.util.ArrayList;
import java.util.List;

import budgetapp.fragments.EditTransactionDialogFragment;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.util.BudgetAdapter;
import budgetapp.util.BudgetEntry;
import budgetapp.util.CategoryEntry;
import budgetapp.util.IBudgetObserver;
import budgetapp.util.ViewHolder;
import budgetapp.util.database.BudgetDataSource;
import budgetapp.util.stats.CompositeStats;
import budgetapp.util.stats.Stats;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class StatsView extends LinearLayout implements IBudgetObserver{

	public static interface ViewListener
	{
		public void spinnerItemSelected(AdapterView<?> parent, View view, int pos, long id);
		public void listViewClick(Button id);
		public void listViewLongClick(Button id);
	}
	
	private int selectedYear=0;
	private int selectedMonth=0;

	String selectedCategory = "";
	
	private ArrayList<CompositeStats> years;
	List<CategoryEntry> categoryStats; // Contains stats for categories
	private ViewListener viewListener;
	private Spinner yearSpinner;
	private Spinner monthSpinner;
	private Spinner categorySpinner;
	private ListView entryList;
	private BudgetModel model;
	BudgetAdapter listAdapter;
	
	public StatsView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setViewListener(ViewListener viewListener)
	{
		this.viewListener = viewListener;
	}
	
	public void setModel(BudgetModel model)
	{
		this.model = model;
	}
	public void setSelectedYear(int value)
	{
		if(value!=this.selectedYear)
		{
			this.selectedYear = value;
			updateLog();
		}
	}
	
	public void setSelectedMonth(int value)
	{
		if(value!=this.selectedMonth)
		{
			this.selectedMonth = value;
			updateLog();
		}
	}
	
	public void setSelectedCategory(String value)
	{
		if(!value.equalsIgnoreCase(this.selectedCategory))
		{
			this.selectedCategory = value;
			updateLog();
		}
	}
	
	public void setUpComposite()
	{
		years = new ArrayList<CompositeStats>();
        BudgetEntry entry;
        int entryIndex = 0;
        int yearIndex = -1;
        
        List<BudgetEntry> entries = new ArrayList<BudgetEntry>();
        entries = model.getSomeTransactions(0, BudgetDataSource.DESCENDING);
		// Set up the composite
        while(entryIndex<entries.size())
        {
        	entry = entries.get(entryIndex);

        	String year = entry.getYear();
        	if(years.isEmpty() || !years.get(yearIndex).getName().equalsIgnoreCase(year))
        	{
	        	//Add a year
	        	years.add(new CompositeStats(year));
	        	yearIndex++;
        	}
        	years.get(yearIndex).addEntry(entry,CompositeStats.MONTH);
        	
        	entryIndex++;
        }
	}
	@Override
    protected void onFinishInflate()
    {
    	super.onFinishInflate();
    	
    	yearSpinner = (Spinner)findViewById(R.id.spinnerYear);
    	monthSpinner = (Spinner)findViewById(R.id.spinnerMonth);
    	categorySpinner = (Spinner)findViewById(R.id.spinnerCategory);
    	entryList = (ListView)findViewById(R.id.ListViewLogTop);
    	
    	listAdapter = new BudgetAdapter(this.getContext());
    	setUpListeners();
    }
	
	public void updateLog()
	{
		categoryStats = new ArrayList<CategoryEntry>();
		listAdapter = new BudgetAdapter(this.getContext());
		printYear(0);
		entryList.setAdapter(listAdapter);
		//updateSpinners();
	}
	/**
	 * Appends a year worth of BudgetEntrys to a TextView, using printMonth
	 * @param view The TextView to append to
	 * @param index The index of the year
	 */
	public void printYear(int index)
	{
		
		ArrayList<Stats> months = (ArrayList<Stats>) years.get(index).getChildren();
		//list.add(years.get(index).getName());
    	if(selectedMonth>-1) // A specific month is chosen
    	{
    		printMonth(months,selectedMonth);
    	}
    	else // Print all transactions this year
    	{
        	for(int j=0;j<months.size();j++)
        	{
        		printMonth(months,j);
        	}
    	}
		
	}
	
	/**
	 * Appends a months of BudgetEntrys to a TextView, only appends those chosen by selectedCategory, 
	 * or if selectedCategory is set to All categories, appends all
	 * @param view The TextView to append to
	 * @param months An ArrayList<Stats> containing the months
	 * @param index The index of the month
	 */
	public void printMonth(ArrayList<Stats> months, int index)
	{
		boolean monthPrinted = false;
		if(index<months.size())
		{
			for(int k=0;k<months.get(index).getTransactions().size();k++)
			{
				BudgetEntry entry = months.get(index).getTransactions().get(k);
				// Print the entry if it has the correct category
				// or if all categories are set to be printed
				if(selectedCategory.equalsIgnoreCase(entry.getCategory()) || selectedCategory.equalsIgnoreCase(getResources().getString(R.string.all_categories)))
			{
					if(!monthPrinted)
					{
						//list.add(monthToString(months.get(index).getName()));
						listAdapter.add(new ViewHolder(monthToString(months.get(index).getName()),ViewHolder.MONTH));
						monthPrinted = true;
					}
					printEntry(entry);
					//addStats(entry); // Add the stats to categoryStats
				}
			}
		}
	}
	
	
	
	/**
	 * Appends a BudgetEntry to a specific TextView
	 * @param view The view to append to
	 * @param entry The BudgetEntry to append
	 */
	public void printEntry(BudgetEntry entry)
	{
		listAdapter.add(new ViewHolder(entry));
	}
	
	/**
	 * Update the stats for a category
	 * @param entry The transaction with the data to get stats from
	 */
	public void addStats(BudgetEntry entry)
	{
		boolean added = false;
		for(int i=0;i<categoryStats.size();i++)
		{
			if(categoryStats.get(i).getCategory().equalsIgnoreCase(entry.getCategory()))
			{
				categoryStats.get(i).addToNum(1);
				categoryStats.get(i).addToTotal(entry.getValue());
				added = true;
				break;
			}
		}
		if(!added) // No entry for this category yet, add a new one
		{
			CategoryEntry newEntry = new CategoryEntry(0, entry.getCategory(),1,entry.getValue());
			categoryStats.add(newEntry);
		}
	}
	public void updateSpinners()
	{
		List<CategoryEntry> categories = new ArrayList<CategoryEntry>();
		categories = model.getCategoriesSortedByValue();
        
		List<String> categoryNames = new ArrayList<String>();
        for(int i=0;i<categories.size();i++)
        	categoryNames.add(categories.get(i).getCategory());
        
		 //Set up the year spinner
		ArrayList<String> yearStartValues = new ArrayList<String>();
		
        ArrayList<String> categoryStartValues = new ArrayList<String>();
        
		//yearStartValues.add(getString(R.string.all_years));
        for(int i=0;i<years.size();i++)
        {
        	yearStartValues.add(years.get(i).getName());
        }
        Spinner spinner = (Spinner) findViewById(R.id.spinnerYear);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),   android.R.layout.simple_spinner_item, yearStartValues);
		   
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		 
		 
		updateMonthSpinner();
		// Set up the category spinner
		categoryStartValues.add(getContext().getString(R.string.all_categories));
		for(int i=0;i<categoryNames.size();i++)
		{
			categoryStartValues.add(categoryNames.get(i));
		}
		spinner = (Spinner) findViewById(R.id.spinnerCategory);
		// Create an ArrayAdapter using the string array and a default spinner layout
		adapter = new ArrayAdapter<String>(this.getContext(),   android.R.layout.simple_spinner_item, categoryStartValues);
		  
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
			 
			 
	}
	
	public void updateMonthSpinner()
	{
		//Set up the month spinner
		ArrayList<String> monthStartValues = new ArrayList<String>();
        monthStartValues.add(getContext().getString(R.string.all_months));
        
       
        if(selectedYear>-1)
        {
        	ArrayList<Stats> months = new ArrayList<Stats>();
        	months = (ArrayList<Stats>) years.get(selectedYear).getChildren();
        	
        	for(int i=0;i<months.size();i++)
        	{
        		monthStartValues.add(monthToString(months.get(i).getName()));
        	}
        }
        else
        {
        	monthStartValues.clear();
        }
        	
        
        Spinner spinner = (Spinner) findViewById(R.id.spinnerMonth);
		 // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),   android.R.layout.simple_spinner_item, monthStartValues);
	   
	     // Specify the layout to use when the list of choices appears
		 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 // Apply the adapter to the spinner
		 spinner.setAdapter(adapter);
		 
	}
	
	
	@Override
	public void update() {
		updateLog();
		updateSpinners();
		//updateMonthSpinner();

		ListView listView = (ListView) findViewById(R.id.ListViewLogTop);
		listView.scrollTo(0,0);
		TextView textView = (TextView)findViewById(R.id.textViewLogStats);
		textView.scrollTo(0,0);
	}
	
	private void setUpListeners()
	{
		entryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    @Override
		    public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
		       ViewHolder listItem = (ViewHolder)entryList.getItemAtPosition(position);
		       if(!listItem.entry.getComment().equalsIgnoreCase(""))
		       {	
		    	   Toast.makeText(view.getContext(), listItem.entry.getComment(), Toast.LENGTH_LONG).show();
		       }
		    }
	 	});
		
		entryList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view,int position, long arg)
			{
				ViewHolder listItem = (ViewHolder)entryList.getItemAtPosition(position);
	 		   // selectedViewHolder = listItem;
	 		   // selectedViewHolderIndex = position;
				if(listItem.flag == ViewHolder.ENTRY)
				{
				  //  DialogFragment newFragment = new EditTransactionDialogFragment();
				   // Bundle bundle = new Bundle();
				   // bundle.putParcelable("entry", (Parcelable) listItem.entry);
				    
				   // newFragment.setArguments(bundle);
				  //  newFragment.show(getSupportFragmentManager(), "edit_transaction");
				    //  Toast.makeText(view.getContext(), listItem.entry.getComment(), Toast.LENGTH_LONG).show();
				}
	 		    return true;
			}
			
		});
		yearSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			{
				viewListener.spinnerItemSelected(parent, view, pos, id);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		monthSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			{
				viewListener.spinnerItemSelected(parent, view, pos, id);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
	
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			{
				viewListener.spinnerItemSelected(parent, view, pos, id);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
	}

	/***
	 * Parses a string and gets the correct month from Resources
	 * @param in The string to parse
	 * @return The month as a string
	 */
	private String monthToString(String in)
	{
		String[] months = getResources().getStringArray(R.array.months_array);
		int month = Integer.parseInt(in) - 1;
		return months[month];
	}
	
}
