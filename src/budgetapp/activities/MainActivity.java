package budgetapp.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import budgetapp.fragments.ChooseCategoryFragment;
import budgetapp.fragments.DailyBudgetFragment;
import budgetapp.fragments.EditCurrencyDialogFragment;
import budgetapp.fragments.OtherCategoryDialogFragment;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.util.BudgetEntry;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.Money;
import budgetapp.views.MainView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
public class MainActivity extends FragmentActivity {

	public ArrayList<String> allCategories = new ArrayList<String>();
	private String chosenCategory = "";
	private MainView view;
	private BudgetModel model;
	public Money dailyFlow = new Money();
	
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
	
	public void editTransactionEntry(long id, BudgetEntry newEntry)
	{
		model.editTransaction(id, newEntry);
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
        view.update();
        
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	
    	// Add daily budget and Toast the result if anything was added
    	new DailyBudgetAddTask().execute();
        view.setUpAutocompleteValues();
    	view.update();
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
    	String menuString = getString(R.string.menu_setdailybudget);
    	CharSequence withBudget = menuString + " (" + model.getDailyBudget() + ")";
    	item.setTitle(withBudget);
    	return true;
    }
    
    /**
     * Gets the value from the EditText and subtract the value from current budget
     * @param theCategory - The category to add to the entry
     * @param theComment - The comment to add to the entry
     */
    public void subtractFromBudget(String theCategory, String theComment) {
       
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
    		// Bad/no input, don't do anything. No biggie.
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
            case R.id.menu_installments:
            	intent = new Intent(this,InstallmentsActivity.class);
            	startActivity(intent);
            	return true;
            case R.id.menu_showgraph:
            	intent = new Intent(this,GraphActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_preferences:
            	intent = new Intent(this,PreferencesActivity.class);
            	startActivity(intent);
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // Set up a ViewListener for the MainView
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
	    	subtractFromBudget(pressedButton.getText().toString(),null);
		}
		
		@Override
		public void chooseCategory() {
			DialogFragment newFragment = new ChooseCategoryFragment();
	    	newFragment.show(getSupportFragmentManager(), "choose_category");
		}
	};
	
	private class DailyBudgetAddTask extends AsyncTask<Void,Void,Integer>
	{
		@Override
		protected void onPreExecute()
		{
			EditText editText = (EditText)findViewById(R.id.editTextSubtract);
			editText.setEnabled(false);
			dailyFlow = new Money();
		}
		@Override
		protected Integer doInBackground(Void... params) {
			int result = model.addDailyBudget();
			return result;
		}
		
		@Override
		protected void onPostExecute(Integer daysAdded)
		{
			EditText editText = (EditText)findViewById(R.id.editTextSubtract);
			editText.setEnabled(true);
			if(daysAdded>0)
	    	{
	    		dailyFlow = getDailyBudget().multiply(daysAdded);
	    	}
			view.update();

	    	new PayInstallmentsTask().execute();
		}
	}
	
	private class PayInstallmentsTask extends AsyncTask<Void,Void,Void>
	{
		@Override
		protected void onPreExecute()
		{
			EditText editText = (EditText)findViewById(R.id.editTextSubtract);
			editText.setEnabled(false);
		}
		@Override
		protected Void doInBackground(Void... params) {
			Money temp = model.payOffInstallments();
			dailyFlow = dailyFlow.add(temp);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void params)
		{
			EditText editText = (EditText)findViewById(R.id.editTextSubtract);
			editText.setEnabled(true);
			if(!dailyFlow.almostZero())
			{
				Toast.makeText(getApplicationContext(), "Cash flow since last time: " + dailyFlow, Toast.LENGTH_LONG).show();
			}
			view.update();
		}
	}
		
    
}
