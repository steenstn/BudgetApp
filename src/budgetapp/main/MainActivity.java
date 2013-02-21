package budgetapp.main;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import budgetapp.graph.GraphActivity;
import budgetapp.util.BudgetEntry;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.CategoryEntry;
import budgetapp.util.DayEntry;
import budgetapp.util.Money;
import budgetapp.util.database.BudgetDataSource;
import budgetapp.util.database.TransactionCommand;

import android.os.Bundle;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
public class MainActivity extends FragmentActivity implements OnItemSelectedListener, OnClickListener, OnLongClickListener{

	
	ArrayList<TransactionCommand> tempCom; // A list of TransactionCommand enabling Undo
	public static BudgetDataSource datasource; // The connection to the database
	Money currentBudget = new Money();
	private String currentBudgetFileName = "current_budget"; // Internal filename for current budget
	private boolean logData = true; // If transactions should be logged
	private Money dailyBudget = new Money(); // The daily plus, set to zero until value is read/written in internal file
	public ArrayList<String> allCategories = new ArrayList<String>();
	private String chosenCategory = "";
	int min(int a,int b) 
	{
		if(a<b)
			return a;
		return b;
	}
	
	public String getChosenCategory()
	{
		return chosenCategory;
	}
	public void setChosenCategory(String c)
	{
		chosenCategory = c;
	}
	
	
	public void setDailyBudget(double budget)
	{
		dailyBudget.set(budget);
	}
	
	public Money getDailyBudget()
	{
		return dailyBudget;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datasource = new BudgetDataSource(this);
        datasource.open();
        tempCom = new ArrayList<TransactionCommand>();
        Button b = (Button)findViewById(R.id.topCategoryButton1);
        b.setOnClickListener(this);
        b.setOnLongClickListener(this);
        b = (Button)findViewById(R.id.topCategoryButton2);
        b.setOnClickListener(this);
        b.setOnLongClickListener(this);
        b = (Button)findViewById(R.id.topCategoryButton3);
        b.setOnClickListener(this);
        b.setOnLongClickListener(this);
        
        updateSpinner();
        updateLog();
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
        try
        {
        	 DataInputStream in = new DataInputStream(openFileInput(currentBudgetFileName));
             try
             {
            	 String strLine = in.readUTF();
            	 currentBudget.set(Double.parseDouble(strLine));
            	 try
            	 {
            		 strLine = in.readUTF();
            		 dailyBudget.set(Double.parseDouble(strLine));
            	 }
            	 catch(IOException e)
            	 {
            		 Toast.makeText(this.getBaseContext(),"Daily budget set to 0. Change in menu", Toast.LENGTH_LONG).show();
            	 }
           	
	           	//Close the input stream
	           	in.close();
           	  
             	TextView newBudget = (TextView)findViewById(R.id.textViewCurrentBudget);
             	newBudget.setText(""+currentBudget);
             	updateColor();
            	 
             }
             catch(IOException e)
             {
            	// currentBudget=0;
             }
        }
        catch(NumberFormatException e)
        {
        //	currentBudget=0;
        }
        catch(FileNotFoundException e)
        {
        //	currentBudget=0;
        }
      
         //Add daily budget for all days since last run
        addToBudget();
        updateColor();
        updateButtons();
        
    }
    
    // Colors the currentBudget text depending on the size of the current budget
    public void updateColor()
    {
    	List<DayEntry> days = datasource.getSomeDays(7,BudgetDataSource.DESCENDING);
    	Money derivative = (BudgetFunctions.getMeanDerivative(days,7));
    	TextView newBudget = (TextView)findViewById(R.id.textViewCurrentBudget);
    	
    	
    	double maxValue = (double)dailyBudget.get();
    	double floatDerivative = derivative.get() / maxValue;
    	// Choose transfer function, sqrt if negative for fast red value,
    	// x^2 if positive for slow green value
    	if(floatDerivative<0)
    		floatDerivative=-Math.sqrt(Math.abs(floatDerivative));
    	else
    		floatDerivative=floatDerivative*floatDerivative;
    	
    	floatDerivative = floatDerivative * 255;
    	
    	
    	
    	int coloringFactor = min(255,Math.abs((int)floatDerivative));
    	if(derivative.get()<0)
    		newBudget.setTextColor(Color.rgb(255,255-coloringFactor,255-coloringFactor));
    	else
    		newBudget.setTextColor(Color.rgb(255-coloringFactor,255,255-coloringFactor));
	  
    }
    
    // Updates the spinner with all available categories
    public void updateSpinner()
    {
    	// Get the categories for the Spinner
        List<String> categories = datasource.getCategoryNames();
        
        //Clear allCategories if there is anything in it
        allCategories.clear();
        // Put the category names in an ArrayList to get them into the spinner
        allCategories.add("Choose category");
        for(int i=0;i<categories.size();i++)
        {
        	allCategories.add(categories.get(i));
        }
        allCategories.add("Other...");
        Spinner spinner = (Spinner) findViewById(R.id.categories_spinner);
		 // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, allCategories);
	   
	     // Specify the layout to use when the list of choices appears
		 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 // Apply the adapter to the spinner
		 spinner.setAdapter(adapter);
		 spinner.setOnItemSelectedListener(this);
			
    }
    public void chooseCategory()
    {
    //	ListFragment newFragment = new ChooseCategoryFragment();
    	//newFragment.show(getSupportFragmentManager(), "choose_category");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    // Updates the log of transactions, categories and daysums
    public void updateLog()
    {
    	List<BudgetEntry> entries = datasource.getSomeTransactions(5,BudgetDataSource.DESCENDING);
        TextView left = (TextView)findViewById(R.id.textViewLogLeft);
        TextView right = (TextView)findViewById(R.id.textViewLogRight);
        left.setText(Html.fromHtml("<b>Latest transactions</b><br />"));
        right.setText(Html.fromHtml("<b>Category</><br />"));
        for(int i=0;i<entries.size();i++)
        {	
        		left.append(entries.get(i).getDate() + ":    " + entries.get(i).getValue() + "\n");
        		right.append(entries.get(i).getCategory() + "\n");
        		
        }
        
        List<DayEntry> days = datasource.getSomeDays(7,BudgetDataSource.DESCENDING);
        left.append("\n");
        left.append(Html.fromHtml("<b>Daily cash flow</b><br />"));
        for(int i=0;i<days.size();i++) 
        {	
        	left.append(days.get(i).getDate()+ ": ");
        		left.append(days.get(i).getTotal()+"\n");
        }
        
    }
    
    void updateButtons()
    {
    	int numButtons = 3;
    	ArrayList<Button> theButtons = new ArrayList<Button>();
    	theButtons.add((Button)findViewById(R.id.topCategoryButton1));
    	theButtons.add((Button)findViewById(R.id.topCategoryButton2));
    	theButtons.add((Button)findViewById(R.id.topCategoryButton3));
    	List<CategoryEntry> categories = datasource.getCategoriesSortedByNum();
    	//Remove categories with positive total
    	int i;
		for(i=0;i<categories.size();i++)
		{
			if(categories.get(i).getTotal().get()>0 || categories.get(i).getNum()<2)
			{	
				categories.remove(i);
				i--;
			}
		}
    	int index = categories.size()-1; // Start at the last entry
    	
    	for(i = 0;i<min(numButtons,categories.size());i++)
    	{
    		theButtons.get(i).setText(categories.get(index).getCategory());
    		theButtons.get(i).setVisibility(View.VISIBLE);
    		index--;
    	}
    	
    	for(;i<numButtons;i++)
    	{
    		theButtons.get(i).setVisibility(View.GONE);
    	}
    	
    }
    
    
    public void subtractFromBudget(View view,String theCategory, String theComment) {
       
    	EditText resultText = (EditText)findViewById(R.id.editTextSubtract);
    	String result = resultText.getText().toString();
    	
    	try
    	{
    		addToBudget(); // Check so that all dailies has come in
    		double resultInt = Double.parseDouble(result);
        	if(resultInt==0)
        		throw new NumberFormatException();
        
    		TextView newBudget = (TextView)findViewById(R.id.textViewCurrentBudget);
        	
        	currentBudget.subtract(resultInt);
        	newBudget.setText(""+currentBudget);
        	
        	// Add to database if logging is set
        	if(logData)
        	{
	        	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	        	Calendar cal = Calendar.getInstance();
	        	
	        	BudgetEntry entry = new BudgetEntry(new Money(resultInt*-1), dateFormat.format(cal.getTime()),theCategory,theComment);
	        	tempCom.add(new TransactionCommand(datasource,entry));
	        	tempCom.get(tempCom.size()-1).execute();
	        	
        	}
        	resultText.setText("");
        	//Set color
        	updateAll();
    		
    	}
    	catch(NumberFormatException e)
    	{
    		resultText.setText("");
    		System.out.println("Error: "+e);
    	}
    	
    		
    }
    public void onClick(View v)
    {
    	//Toast.makeText(this, "Click",Toast.LENGTH_SHORT).show();
    	Button pressedButton = (Button)v;
    	subtractFromBudget(v,pressedButton.getText().toString(),null);
    }
    @Override
	public boolean onLongClick(View v) {
    	Button pressedButton = (Button)v;
    	chosenCategory = (String) pressedButton.getText();
    	DialogFragment newFragment = new OtherCategoryDialogFragment();
    	newFragment.show(getSupportFragmentManager(), "other_category");
    	
		return true;
	}
    public void buttonPressed(View v)
    {
    	Button pressedButton = (Button)v;
    	subtractFromBudget(v,pressedButton.getText().toString(),null);
    }
    
    // Adds the daily plus sum for all days missing since the last time the program was run
    public void addToBudget()
    {
    	
    	List<DayEntry> lastDay = datasource.getSomeDays(1,datasource.DESCENDING);
    	
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		
    	
    	if(!lastDay.isEmpty())
    	{
    		SimpleDateFormat compareFormat = new SimpleDateFormat("yyyy/MM/dd");
    		
    		
	    	String lastDayString = lastDay.get(0).getDate();
	    	Calendar lastDayCalendar = Calendar.getInstance();
	    	// Convert the string to a Calendar time. Subtract 1 from month because month 0 = January
	    	// Set HH:mm to 00:00
	    	lastDayCalendar.set(Integer.parseInt(lastDayString.substring(0, 4)),Integer.parseInt(lastDayString.substring(5, 7))-1,Integer.parseInt(lastDayString.substring(8, 10)),0,0);
	    	
	    	System.out.println("Last day: " + dateFormat.format(lastDayCalendar.getTime()));
	    	lastDayCalendar.add(Calendar.DAY_OF_MONTH, 1); // We want to start counting from the first day without transactions

	    	// Step up to the day before tomorrow
	    	Calendar nextDay = Calendar.getInstance();
	    	nextDay.add(Calendar.DAY_OF_MONTH,1);
	    	
	    	System.out.println("Next day: " + dateFormat.format(nextDay.getTime()));
	    	Calendar tempDate = (Calendar)lastDayCalendar.clone();
	    	int numDays = 0;
	    	Money totalMoney= new Money();
	    	while(tempDate.before(nextDay))
	    	{
	    		if(!compareFormat.format(tempDate.getTime()).equalsIgnoreCase(compareFormat.format(nextDay.getTime())))
	    		{
	    			System.out.println("Day to add: " + dateFormat.format(tempDate.getTime()));
	    			BudgetEntry entry = new BudgetEntry(new Money(dailyBudget), dateFormat.format(tempDate.getTime()),"Income");
		        	tempCom.add(new TransactionCommand(datasource,entry));
		        	tempCom.get(tempCom.size()-1).execute();
		        	currentBudget.add(dailyBudget);
		        	totalMoney.add(dailyBudget);
		        	numDays++;
		        	TextView newBudget = (TextView)findViewById(R.id.textViewCurrentBudget);
		        	newBudget.setText(""+currentBudget);
	    		}
	    		
	    		tempDate.add(Calendar.DAY_OF_MONTH,1);	
	    	}
	    	saveToFile();
	    	if(numDays>0)
	    		Toast.makeText(this.getBaseContext(), "Added " + totalMoney + " to budget (" + numDays + " day"+((numDays>1)? "s" : "") +")" , Toast.LENGTH_LONG).show();
	    	
	    	updateLog();
    	}
    	else // Add a transaction of 0
    	{
    		Calendar tempDate = Calendar.getInstance();
    		
        	datasource.updateDaySum(new BudgetEntry(new Money(), dateFormat.format(tempDate.getTime()),"Income"));
        

        	updateLog();
    	}
    	
    }
    
    // Saves the current budget to the internal file
    public void saveToFile()
    {
    	DataOutputStream out;
		try {
			out = new DataOutputStream(openFileOutput(currentBudgetFileName,Context.MODE_PRIVATE));
			out.writeUTF(""+currentBudget.get());
			out.writeUTF(""+dailyBudget.get());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
    	Intent intent;
    	DialogFragment newFragment;
        switch (item.getItemId()) {
	        case R.id.menu_undo:
	        	if(!tempCom.isEmpty() && tempCom.get(tempCom.size()-1).unexecute()==true)
	        	{
		        	TextView newBudget = (TextView)findViewById(R.id.textViewCurrentBudget);
		        	currentBudget.subtract(tempCom.get(tempCom.size()-1).getEntry().getValue().get());
		        	tempCom.remove(tempCom.size()-1);
		        	newBudget.setText(""+currentBudget);
		        	updateAll();
	        	}
	        	return true;
	        
	        /*
            case R.id.menu_logdata: // Change logging data status (Not visible as standard
                logData=!logData;
                item.setChecked(logData);
                return true;*/
            case R.id.menu_addcategory:
            	newFragment = new AddCategoryDialogFragment();
                newFragment.show(getSupportFragmentManager(), "add_category");
                return true;
            case R.id.menu_removecategory:
            	newFragment = new RemoveCategoryDialogFragment();
            	newFragment.show(getSupportFragmentManager(), "remove_category");
            	return true;
            case R.id.menu_setdailybudget:
            	newFragment = new DailyBudgetFragment();
            	newFragment.show(getSupportFragmentManager(), "set_dailybudget");
            	return true;
            case R.id.menu_statistics:
            	intent = new Intent(this,StatsActivity.class);
                startActivity(intent);
                return true;
            /*case R.id.menu_showgraph: // Wait for it!
            	intent = new Intent(this,GraphActivity.class);
                startActivity(intent);
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
	{
		//Toast.makeText(parent.getContext(), "The planet is " +parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
		String theCategory = parent.getItemAtPosition(pos).toString();
		if(pos!=0 && pos!=parent.getCount()-1) // Don't add "Choose category" or "Other..."
		{
			subtractFromBudget(parent,theCategory,null);
		}
		if(pos==parent.getCount()-1)
		{
			DialogFragment newFragment = new OtherCategoryDialogFragment();
        	newFragment.show(getSupportFragmentManager(), "other_category");
		}
		parent.setSelection(0); // Reset to "Choose category"
		
	}
	
	public void updateAll()
	{
		updateLog();
		updateSpinner();
		updateButtons();
		updateColor();
		saveToFile();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
		
	
	}

	
    
}
