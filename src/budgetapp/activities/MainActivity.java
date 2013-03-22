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
import budgetapp.util.BudgetConfig;
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
		model.setDailyBudget(new Money(budget));
	}
	
	public Money getDailyBudget()
	{
		return model.getDailyBudget();
	}
	
	public boolean addCategory(String category)
	{
		return model.addCategory(category);
	}
	public boolean removeCategory(String category)
	{
		return model.removeCategory(category);
	}
	
	public List<String> getCategoryNames()
	{
		return model.getCategoryNames();
	}
	
	public void removeTransactionEntry(BudgetEntry entry)
	{
		model.removeTransaction(entry);
	}
	
	public void editTransactionEntry(BudgetEntry oldEntry, BudgetEntry newEntry)
	{
		model.editTransaction(oldEntry, newEntry);
	}
	
	public void updateView()
	{
		view.update();
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = (MainView)View.inflate(this, R.layout.activity_main, null);
        view.setViewListener(viewListener);
        model = new BudgetModel(this);

        setContentView(view);
        view.setModel(model);
       
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	model.addDailyBudget();
    	
        
    }
    
    public void saveConfig()
    {
    	model.saveConfig();
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
    	CharSequence withBudget = "Set daily budget" + " (" + model.getDailyBudget() + ")";
    	item.setTitle(withBudget);
    	return true;
    }
    
    public void subtractFromBudget(View view,String theCategory, String theComment) {
       
    	EditText resultText = (EditText)findViewById(R.id.editTextSubtract);
    	String result = resultText.getText().toString();
    	try
    	{
    		double value = Double.parseDouble(result);
		
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	    	Calendar cal = Calendar.getInstance();
	    	String dateString = dateFormat.format(cal.getTime());
	    	BudgetEntry entry = new BudgetEntry(new Money(value*-1), dateString,theCategory,theComment);
	    	model.createTransaction(entry);
	    	resultText.setText("");
    	}
    	catch(NumberFormatException e)
    	{
    		// Bad/no input, don't do anything.
    	}
		
    }
    
    // Saves the current budget to the internal file
    
    
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
