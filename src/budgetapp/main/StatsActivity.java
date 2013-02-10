package budgetapp.main;

import java.util.ArrayList;
import java.util.List;

import budgetapp.util.BudgetEntry;
import budgetapp.util.CategoryEntry;
import budgetapp.util.DayEntry;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.database.BudgetDataSource;
import budgetapp.util.stats.*;
public class StatsActivity extends Activity implements OnItemSelectedListener{
	
	BudgetDataSource datasource = MainActivity.datasource;
	List<DayEntry> days;
	List<DayEntry> dayFlow;
	List<BudgetEntry> entries;
	List<CategoryEntry> categories;
	List<String> categoryNames;
	ArrayList<CompositeStats> years;
	int selectedYear=0;
	int selectedMonth=-1;
	String selectedCategory = "";
	int numDaysForDerivative = 7;
	int numTransactionsForDerivative = 10;
	int min(int a,int b) 
	{
		if(a<b)
			return a;
		return b;
	}
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        
        // Read in all the data
        entries = datasource.getAllTransactions(BudgetDataSource.ASCENDING);
        days = datasource.getAllDaysTotal(BudgetDataSource.DESCENDING);
        dayFlow = datasource.getAllDays(BudgetDataSource.DESCENDING);
        categories = datasource.getCategoriesSorted();
        //updateStats();
        categoryNames = new ArrayList<String>();
        for(int i=0;i<categories.size();i++)
        	categoryNames.add(categories.get(i).getCategory());
        
        
        years = new ArrayList<CompositeStats>();
        int size = entries.size();
        BudgetEntry entry;
        int entryIndex = 0;
        int yearIndex = 0;
        
        // Set up the composite
        while(entryIndex<size)
        {
        	entry = entries.get(entryIndex);
        	String year = entry.getYear();
        	if(years.isEmpty() || !years.get(yearIndex).getName().equalsIgnoreCase(year))
        	{
	        	//Add a year
	        	years.add(new CompositeStats(year));
        	}
        	years.get(yearIndex).addEntry(entry,CompositeStats.MONTH);
        	
        	entryIndex++;
        }
        
        
        Spinner spinner = (Spinner) findViewById(R.id.spinnerMonth);
        spinner.setOnItemSelectedListener(this);
        spinner = (Spinner) findViewById(R.id.spinnerCategory);
        spinner.setOnItemSelectedListener(this);
        spinner = (Spinner) findViewById(R.id.spinnerYear);
        
        spinner.setOnItemSelectedListener(this);

       	updateSpinners();
        updateLog();
		 
        
        
	}
	
	public void updateStats()
	{
		TextView stats = (TextView)findViewById(R.id.textViewLogStats);
		
		if(days.size()<2)
		{
			stats.setText("Not enough days for mean derivative\n");
		}
		else
		{
			stats.setText("Mean derivative (" + min(numDaysForDerivative,dayFlow.size()) + ") days: ");
			long dayDerivative = BudgetFunctions.getMeanDerivative(dayFlow,numDaysForDerivative);
			stats.append(""+dayDerivative + "\n");
		}
		if(entries.size()<2)
		{
			stats.append("Not enough transactions for mean derivative\n");
		}
		else
		{
			stats.append("Mean derivative (" + min(numTransactionsForDerivative,entries.size()) + ") transactions: ");
			long transactionDerivative = BudgetFunctions.getMeanDerivative(entries,numDaysForDerivative);
			stats.append(""+transactionDerivative + "\n");
		}
		
		
	}
	
	
	public void updateSpinners()
	{
		 //Set up the year spinner
		ArrayList<String> yearStartValues = new ArrayList<String>();
		ArrayList<String> monthStartValues = new ArrayList<String>();
        ArrayList<String> categoryStartValues = new ArrayList<String>();
        
		//yearStartValues.add(getString(R.string.all_years));
        for(int i=0;i<years.size();i++)
        {
        	yearStartValues.add(years.get(i).getName());
        }
        Spinner spinner = (Spinner) findViewById(R.id.spinnerYear);
		 // Create an ArrayAdapter using the string array and a default spinner layout
         ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, yearStartValues);
	   
	     // Specify the layout to use when the list of choices appears
		 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 // Apply the adapter to the spinner
		 spinner.setAdapter(adapter);
		 
		 
		//Set up the month spinner
     
        monthStartValues.add(getString(R.string.all_months));
        
       
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
        	
        
        spinner = (Spinner) findViewById(R.id.spinnerMonth);
		 // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, monthStartValues);
	   
	     // Specify the layout to use when the list of choices appears
		 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 // Apply the adapter to the spinner
		 spinner.setAdapter(adapter);
		 
		 // Set up the category spinner
		 categoryStartValues.add(getString(R.string.all_categories));
	     for(int i=0;i<categoryNames.size();i++)
	     {
	    	 categoryStartValues.add(categoryNames.get(i));
	     }
         spinner = (Spinner) findViewById(R.id.spinnerCategory);
		 // Create an ArrayAdapter using the string array and a default spinner layout
         adapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, categoryStartValues);
	   
	     // Specify the layout to use when the list of choices appears
		 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 // Apply the adapter to the spinner
		 spinner.setAdapter(adapter);
			 
			 
	}
	
	public void printEntry(TextView view,BudgetEntry entry)
	{
		view.append("Date: " + entry.getDate().substring(8) + "  Category: " + entry.getCategory()+"\n");
	}
	
	public void printMonth(TextView view, ArrayList<Stats> months, int index)
	{
		view.append(monthToString(months.get(index).getName())+"\n");
		for(int k=0;k<months.get(index).getTransactions().size();k++)
		{
			BudgetEntry entry = months.get(index).getTransactions().get(k);
			// Print the entry if it has the correct category
			// or if all categories are set to be printed
			if(selectedCategory.equalsIgnoreCase(entry.getCategory())|| selectedCategory.equalsIgnoreCase(getResources().getString(R.string.all_categories)))
			printEntry(view,entry);
			
		}
	}
	
	public void printYear(TextView view, int index)
	{
		ArrayList<Stats> months = (ArrayList<Stats>) years.get(selectedYear).getChildren();
    	if(selectedMonth>-1) // A specific month is chosen
    	{
    		printMonth(view,months,selectedMonth);
    	}
    	else // Print all transactions this year
    	{
        	for(int j=0;j<months.size();j++)
        	{
        		printMonth(view,months,j);
        	}
    	}
	}
	
	public void updateLog()
	{
		
        TextView top = (TextView)findViewById(R.id.textViewLogTop);
        BudgetEntry entry;
        
        top.setText("");
        top.setMovementMethod(new ScrollingMovementMethod());
        if(selectedYear>-1) // A specific year is chosen
        {
	        printYear(top,selectedYear);
	        
        }
        else // Print all years
        {
        	//for(int i=0;i<years.size();i++)
        		//printYear(top,i);
        }
	}
	
	public String monthToString(String in)
	{
		String[] months = getResources().getStringArray(R.array.months_array);
		int month = Integer.parseInt(in) - 1;
		return months[month];
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		
		Spinner yearSpinner =  (Spinner)findViewById(R.id.spinnerYear);
		Spinner monthSpinner =  (Spinner)findViewById(R.id.spinnerMonth);
		Spinner categorySpinner = (Spinner)findViewById(R.id.spinnerCategory);
		
		selectedYear = yearSpinner.getSelectedItemPosition();
		selectedMonth = monthSpinner.getSelectedItemPosition()-1;
		selectedCategory = (String) categorySpinner.getSelectedItem();
		
		updateLog();
		System.out.println("selectedYear: " + selectedYear);
		System.out.println("selectedMonth: " + selectedMonth);
		System.out.println("selectedCategory: " + selectedCategory);
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
