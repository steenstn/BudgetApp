package budgetapp.views;

import java.util.ArrayList;
import java.util.List;

import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.util.BudgetAdapter;
import budgetapp.util.BudgetEntry;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.CategoryEntry;
import budgetapp.util.DayEntry;
import budgetapp.util.IBudgetObserver;
import budgetapp.util.Money;
import budgetapp.util.ViewHolder;
import budgetapp.util.database.BudgetDataSource;
import budgetapp.util.stats.CompositeStats;
import budgetapp.util.stats.Stats;
import android.content.Context;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
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
		public void listViewClick(ViewHolder theEntry);
		public void listViewLongClick(ViewHolder theEntry);
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
		this.model.addObserver(this);
	}
	public void setSelectedYear(int value)
	{
		if(value!=this.selectedYear)
		{
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
        
        TextView stats = (TextView)findViewById(R.id.textViewLogStats);
		stats.setMovementMethod(new ScrollingMovementMethod());
		if(days.size()<2)
		{
			stats.setText("Not enough days for mean derivative\n");
		}
		else
		{
			stats.setText("Mean derivative (" + BudgetFunctions.min(30,dayFlow.size()) + ") days: ");
			Money dayDerivative = BudgetFunctions.getWeightedMeanDerivative(dayFlow,30);
			stats.append(""+dayDerivative + "\n");
		}
		stats.append(Html.fromHtml("<b>Category statistics</b><br />"));
		CategoryEntry entry;
		int length = 0;
		System.out.println("size: " + categoryStats.size());
		for(int i=0;i<categoryStats.size();i++) // Get longest name
		{
			entry = categoryStats.get(i);
			if(entry.getCategory().length()>length)
			{
				length = entry.getCategory().length();
			}
		}
		length++;
		Money sum = new Money(); // Calculate the total cash flow for the selected time span
		for(int i=0;i<categoryStats.size();i++)
		{
			entry = categoryStats.get(i);
			
			stats.append(entry.getCategory()+":");
			for(int j=0;j<Math.floor((length-entry.getCategory().length()+1)/3)+1;j++) // Fancy pancy formatting
				stats.append("\t");
			stats.append(""+entry.getNum());
			stats.append("\t");
			if(entry.getNum()<10)
				stats.append("\t");
			stats.append(" Total: " + entry.getTotal() + "\n");
			sum = sum.add(entry.getTotal());
			
		}
		
		stats.append("Total cash flow: " + sum);
	}
	
	/**
	 * Appends a year worth of BudgetEntrys to a BudgetAdapter, using addMonth
	 * @param index The index of the year
	 */
	private void addYear(int index)
	{
		ArrayList<Stats> months = (ArrayList<Stats>) years.get(index).getChildren();
		listAdapter.add(new ViewHolder(years.get(index).getName(),ViewHolder.Type.year));
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
						listAdapter.add(new ViewHolder(monthToString(months.get(index).getName()),ViewHolder.Type.month));
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
		listAdapter.add(new ViewHolder(entry));
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
		ListView listView = (ListView) findViewById(R.id.ListViewLogTop);
		listView.scrollTo(0,0);
		TextView textView = (TextView)findViewById(R.id.textViewLogStats);
		textView.scrollTo(0,0);
	}
	
	/**
	 * Set up listeners for the Spinners and the ListView
	 */
	private void setUpListeners()
	{
		
		entryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
		    public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
		       ViewHolder listItem = (ViewHolder)entryList.getItemAtPosition(position);
		       viewListener.listViewClick(listItem);
		    }
	 	});
		
		entryList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view,int position, long arg)
			{
				ViewHolder listItem = (ViewHolder)entryList.getItemAtPosition(position);
				viewListener.listViewLongClick(listItem);					 
			     
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
