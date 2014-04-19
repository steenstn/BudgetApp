package budgetapp.views;

import java.util.ArrayList;
import java.util.List;

import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.util.BudgetAdapter;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.IBudgetObserver;
import budgetapp.util.database.BudgetDataSource;
import budgetapp.util.entries.BudgetEntry;
import budgetapp.util.entries.CategoryEntry;
import budgetapp.util.entries.DayEntry;
import budgetapp.util.money.Money;
import budgetapp.util.money.MoneyFactory;
import budgetapp.util.stats.CompositeStats;
import budgetapp.util.stats.Stats;
import budgetapp.viewholders.CategoryStatsViewHolder;
import budgetapp.viewholders.StatEntryViewHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class StatsView extends LinearLayout implements IBudgetObserver{

	public static interface ViewListener
	{
		public void spinnerItemSelected(AdapterView<?> parent, View view, int pos, long id);
		public void listViewClick(StatEntryViewHolder theEntry);
		public void listViewLongClick(StatEntryViewHolder theEntry);
	}
	
	private int selectedYear=0;
	private int selectedMonth=0;

	private String selectedCategory = getResources().getString(R.string.all_categories);
	private ArrayList<CompositeStats> years;
	private List<CategoryEntry> categoryStats; // Contains stats for categories
	private ViewListener viewListener;
	private Spinner yearSpinner;
	private Spinner monthSpinner;
	private Spinner categorySpinner;
	private ListView entryList;
	private ListView categoryStatsList;
	private BudgetModel model;
	private BudgetAdapter listAdapter;
	private BudgetAdapter categoryAdapter;
	
	public StatsView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setViewListener(ViewListener viewListener) {
		this.viewListener = viewListener;
	}
	
	public void setModel(BudgetModel model)	{
		this.model = model;
		this.model.addObserver(this);
	}
	
	public void setSelectedYear(int value) {
		if(value!=this.selectedYear) {
			this.selectedYear = value;
			updateViews();
		}
	}
	
	public void setSelectedMonth(int value)
	{
		if(value!=this.selectedMonth)
		{
			this.selectedMonth = value;
			updateViews();
		}
	}
	
	public void setSelectedCategory(String value)
	{
		if(!value.equalsIgnoreCase(this.selectedCategory))
		{
			this.selectedCategory = value;
			updateViews();
		}
	}
	
	private void setUpComposite()
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
		categoryStatsList = (ListView)findViewById(R.id.listViewCategoryStats);
    	
    	listAdapter = new BudgetAdapter(this.getContext());
    	categoryAdapter = new BudgetAdapter(this.getContext());
    	setUpListeners();

    }
	
	/**
	 * Updates the ListView. Clears the stats and the adapter and calls addYear() which in turn calls 
	 * addMonth() which calls addEntry()
	 */
	public void updateList()
	{
		categoryStats = new ArrayList<CategoryEntry>();
		listAdapter = new BudgetAdapter(this.getContext());
		if(years.size()!=0)
		{
			if(selectedYear>-1)
			{
				addYear(selectedYear);
			}
			else
			{
				for(int i = 0; i < years.size(); i++)
				{
					addYear(i);
				}
			}
		}
		entryList.setAdapter(listAdapter);
	}
	
	/**
	 * Calculates the mean derivative and prints the statistics for the categories
	 */
	public void updateStats()
	{
		List<DayEntry> days = model.getSomeDaysTotal(0, BudgetDataSource.DESCENDING);
        List<DayEntry> dayFlow = model.getSomeDays(0,BudgetDataSource.DESCENDING);
        
        categoryAdapter = new BudgetAdapter(this.getContext(), R.layout.listitem_category_stats);
        Money sum = MoneyFactory.createMoney();
        for(int i = 0; i < categoryStats.size(); i++)
        {
        	categoryAdapter.add(new CategoryStatsViewHolder(categoryStats.get(i)));
        	sum = sum.add(categoryStats.get(i).getValue());
        }
        categoryStatsList.setAdapter(categoryAdapter);
        
        
		TextView middleTextView = (TextView)findViewById(R.id.textViewLogTopHead);
		middleTextView.setText("");
		if(days.size()<2)
		{
			middleTextView.append("Not enough days for mean cash flow.\n");
		}
		else
		{
			middleTextView.append("Mean cash flow (" + BudgetFunctions.min(30,dayFlow.size()) + ") days: ");
			Money dayDerivative = BudgetFunctions.getMean(dayFlow,30);
			middleTextView.append(""+dayDerivative + "\n");
		}
		
		middleTextView.append("Total cash flow (" + monthSpinner.getSelectedItem().toString()+"): " + sum);
	}
	
	/**
	 * Appends a year worth of BudgetEntrys to a BudgetAdapter, using addMonth
	 * @param index The index of the year
	 */
	private void addYear(int index)
	{
		ArrayList<Stats> months = (ArrayList<Stats>) years.get(index).getChildren();
		listAdapter.add(new StatEntryViewHolder(years.get(index).getName(),StatEntryViewHolder.Type.year));
    	if(selectedMonth>-1) // A specific month is chosen
    	{
    		addMonth(months,selectedMonth);
    	}
    	else // Print all transactions this year
    	{
        	for(int j=0;j<months.size();j++)
        	{
        		addMonth(months,j);
        	}
    	}
		
	}
	
	/**
	 * Adds a months of BudgetEntrys to a BudgetAdapter, only adds those chosen by selectedCategory, 
	 * or if selectedCategory is set to All categories, adds all
	 * @param months An ArrayList<Stats> containing the months
	 * @param index The index of the month
	 */
	private void addMonth(ArrayList<Stats> months, int index)
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
						listAdapter.add(new StatEntryViewHolder(monthToString(months.get(index).getName()),StatEntryViewHolder.Type.month));
						monthPrinted = true;
					}
					addEntry(entry);
					addStats(entry); // Add the stats to categoryStats
				}
			}
		}
	}
	
	
	
	/**
	 * Adds a BudgetEntry to a specific BudgetAdapter
	 * @param entry The BudgetEntry to append
	 */
	private void addEntry(BudgetEntry entry)
	{
		listAdapter.add(new StatEntryViewHolder(entry));
	}
	
	/**
	 * Update the stats for a category
	 * @param entry The transaction with the data to get stats from
	 */
	private void addStats(BudgetEntry entry)
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
		
		 //Set up the year spinner
		ArrayList<String> yearStartValues = new ArrayList<String>();
		
        
		//yearStartValues.add(getContext().getString(R.string.all_years));
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
		
		updateCategorySpinner();
			 
			 
	}
	
	private void updateMonthSpinner()
	{
		
		//Set up the month spinner
		ArrayList<String> monthStartValues = new ArrayList<String>();
        monthStartValues.add(getContext().getString(R.string.all_months));
        
        if(years.size()!=0)
        {
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
	        	for(int i=1;i<=12;i++)
	        	{
	        		monthStartValues.add(monthToString(""+i));
	        	}
	        }
        }
        
        Spinner spinner = (Spinner) findViewById(R.id.spinnerMonth);
		 // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),   android.R.layout.simple_spinner_item, monthStartValues);
	   
	     // Specify the layout to use when the list of choices appears
		 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 // Apply the adapter to the spinner
		 spinner.setAdapter(adapter);
		 
	}
	
	/**
	 * Gets all categories and adds them to the spinner as well as the "All categories" option
	 */
	private void updateCategorySpinner()
	{
		List<CategoryEntry> categories = new ArrayList<CategoryEntry>();
		categories = model.getCategoriesSortedByValue();
        
		List<String> categoryNames = new ArrayList<String>();
        for(int i=0;i<categories.size();i++)
        	categoryNames.add(categories.get(i).getCategory());
        
		ArrayList<String> categoryStartValues = new ArrayList<String>();
        
		categoryStartValues.add(getContext().getString(R.string.all_categories));
		for(int i=0;i<categoryNames.size();i++)
		{
			categoryStartValues.add(categoryNames.get(i));
		}
		Spinner spinner = (Spinner) findViewById(R.id.spinnerCategory);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),   android.R.layout.simple_spinner_item, categoryStartValues);
		  
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
	
	@Override
	public void update() {
		setUpComposite();
		updateSpinners();
		updateViews();
	}
	
	/**
	 * Updates the views without adding new information and scrolls to top of both views
	 */
	public void updateViews()
	{
		updateList();
		updateStats();
		scrollToTop();
	}
	
	/**
	 * Scroll both views to the top
	 */
	public void scrollToTop()
	{
		ListView transactionList = (ListView) findViewById(R.id.ListViewLogTop);
		transactionList.scrollTo(0,0);
		ListView categoryList = (ListView)findViewById(R.id.listViewCategoryStats);
		categoryList.scrollTo(0,0);
	}
	
	/**
	 * Set up listeners for the Spinners and the ListView
	 */
	private void setUpListeners()
	{
		
		entryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
		    public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
		       StatEntryViewHolder listItem = (StatEntryViewHolder)entryList.getItemAtPosition(position);
		       viewListener.listViewClick(listItem);
		    }
	 	});
		
		entryList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view,int position, long arg)
			{
				StatEntryViewHolder listItem = (StatEntryViewHolder)entryList.getItemAtPosition(position);
				viewListener.listViewLongClick(listItem);					 
			     
				return true;
			}
			
		});
		yearSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			{
				viewListener.spinnerItemSelected(parent, view, pos, id);
				updateMonthSpinner();
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

	/**
	 * Parses a string and gets the correct month from Resources. String must be
	 * 1-12
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
