package budgetapp.activities;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import budgetapp.fragments.AddCategoryDialogFragment;
import budgetapp.fragments.ChooseCategoryFragment;
import budgetapp.fragments.DailyBudgetFragment;
import budgetapp.fragments.EditCurrencyDialogFragment;
import budgetapp.fragments.OtherCategoryDialogFragment;
import budgetapp.fragments.RemoveCategoryDialogFragment;
import budgetapp.main.R;
import budgetapp.main.R.array;
import budgetapp.main.R.id;
import budgetapp.main.R.layout;
import budgetapp.main.R.menu;
import budgetapp.models.BudgetModel;
import budgetapp.util.BudgetEntry;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.CategoryEntry;
import budgetapp.util.DayEntry;
import budgetapp.util.IBudgetObserver;
import budgetapp.util.Money;
import budgetapp.util.database.BudgetDataSource;
import budgetapp.util.database.TransactionCommand;
import budgetapp.views.MainView;

import android.os.Bundle;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.DialogFragment;
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
public class MainActivity extends FragmentActivity {

	
	ArrayList<TransactionCommand> tempCom; // A list of TransactionCommand enabling Undo
	public static BudgetDataSource datasource; // The connection to the database
	Money currentBudget = new Money();
	private String currentBudgetFileName = "current_budget"; // Internal filename for current budget
	private Money dailyBudget = new Money(); // The daily plus, set to zero until value is read/written in internal file
	public ArrayList<String> allCategories = new ArrayList<String>();
	private String chosenCategory = "";
	private MainView view;
	private BudgetModel model;
	
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
        view = (MainView)View.inflate(this, R.layout.activity_main, null);
        view.setViewListener(viewListener);

        model = new BudgetModel(this);
        setContentView(view);
        view.setModel(model);
        view.update();
        datasource = new BudgetDataSource(this);
        tempCom = new ArrayList<TransactionCommand>();
       
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
        readConfigFile();
        update();
        
    }
    
    
    public void readConfigFile()
    {
    	try
        {
        	 DataInputStream in = new DataInputStream(openFileInput(currentBudgetFileName));
        	 
        	 String strLine;
        	 boolean done = false;
        	 int counter = 0;
        		 while(!done)
        		 {
        			 try
        			 {
        				 strLine = in.readUTF();
        				 // Second run, reading dailyBudget in old config file,
        				 // try and parse it directly
        				 if(counter==1) 
        				 {
        					 try
        					 {
        						 double tempDailyBudget = Double.parseDouble(strLine);
        						 dailyBudget.set(tempDailyBudget);
        					 }
        					 catch(NumberFormatException e)
        					 {
        						 System.out.println("No double");
        					 }
        				 }
        				 parseString(strLine);
        				 counter++;
        				
        			 }
        			 catch(IOException e)
        			 {
        				 System.out.println("Done reading config");
        				 done = true;
        			 }
        		 }
            	 
	           	//Close the input stream
            	 in.close();
   
        }
        catch(FileNotFoundException e)
        {
        	
        } 
    	catch (IOException e) 
        {
			e.printStackTrace();
		}
    }
    
    private void parseString(String in)
    {
    	//String array with all values in config
    	String[] values = getResources().getStringArray(R.array.config_values_array);
    		
		if(in.startsWith(values[0]+"=")) // dailyBudget
    	{
    		dailyBudget.set(Double.parseDouble(in.substring(values[0].length()+1)));
    	}
		else if(in.startsWith(values[1]+"=")) // currency
		{
    		Money.setCurrency(in.substring(values[1].length()+1));
		}
		else if(in.startsWith(values[2]+"=")) // printCurrencyAfter
		{
			if(in.substring(values[2].length()+1).equalsIgnoreCase("true"))
				Money.after = true;
			else
				Money.after = false;
		}
		else if(in.startsWith(values[3]+"=")) // exchangeRate
		{
			System.out.println(values[3] + "=" + in);
		}
    	
    	
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
    	MenuItem item = menu.findItem(R.id.menu_setdailybudget);
    	CharSequence withBudget = "Set daily budget" + " (" + dailyBudget + ")";
    	item.setTitle(withBudget);
    	return true;
    }
    
    public void subtractFromBudget(View view,String theCategory, String theComment) {
       
    	EditText resultText = (EditText)findViewById(R.id.editTextSubtract);
    	String result = resultText.getText().toString();
    	
		double resultInt = Double.parseDouble(result);
		
    	if(resultInt!=0)
    	{
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	    	Calendar cal = Calendar.getInstance();
	    	
	    	BudgetEntry entry = new BudgetEntry(new Money(resultInt*-1), dateFormat.format(cal.getTime()),theCategory,theComment);
	    	model.createTransaction(entry);
	        
    	}
    	resultText.setText("");
		
    }
    
    // Saves the current budget to the internal file
    public void saveToFile()
    {
    	DataOutputStream out;
		try {
			out = new DataOutputStream(openFileOutput(currentBudgetFileName,Context.MODE_PRIVATE));
			String[] values = getResources().getStringArray(R.array.config_values_array);
	    	
			out.writeUTF(values[0]+"="+dailyBudget.get());
			
			out.writeUTF(values[1]+"="+Money.currency());
			out.writeUTF(values[2]+"="+Money.after);
			out.writeUTF(values[3]+"="+1);
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
		        model.undoLatestTransaction();
	        	return true;
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
            case R.id.menu_editcurrency:
            	newFragment = new EditCurrencyDialogFragment();
            	newFragment.show(getSupportFragmentManager(), "edit_currency");
            	return true;
            case R.id.menu_statistics:
            	intent = new Intent(this,StatsActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_showgraph: // Wait for it!
            	intent = new Intent(this,GraphActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


	
	public void update()
	{
		saveToFile();
	}

	private MainView.ViewListener viewListener = new MainView.ViewListener() {
		
		@Override
		public void favButtLongClick(Button id) {
			Button pressedButton = id;
	    	chosenCategory = (String) pressedButton.getText();
	    	DialogFragment newFragment = new OtherCategoryDialogFragment();
	    	newFragment.show(getSupportFragmentManager(), "other_category");
	    	
		}
		
		@Override
		public void favButtClick(Button id) {
			Button pressedButton = id;
	    	subtractFromBudget(id,pressedButton.getText().toString(),null);
		}
		
		@Override
		public void chooseCategory() {
			DialogFragment newFragment = new ChooseCategoryFragment();
	    	newFragment.show(getSupportFragmentManager(), "choose_category");
		}
	};
	
    
}
