package budgetapp.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import budgetapp.fragments.ChooseCategoryFragment;
import budgetapp.fragments.ChoosePriceFragment;
import budgetapp.fragments.DailyBudgetFragment;
import budgetapp.fragments.EditCurrencyDialogFragment;
import budgetapp.fragments.OtherCategoryDialogFragment;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.util.entries.BudgetEntry;
import budgetapp.util.money.Money;
import budgetapp.util.money.MoneyFactory;
import budgetapp.views.MainView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.content.Intent;
import android.content.SharedPreferences;
public class MainActivity extends FragmentActivity {

	public ArrayList<String> allCategories = new ArrayList<String>();
	private String chosenCategory = "";
	private MainView view;
	private BudgetModel model;
	private SharedPreferences settings;
	public Money dailyFlow = MoneyFactory.createMoney();
	private ProcessQueueTask processQueueTask;
	
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
		model.setDailyBudget(MoneyFactory.createMoneyFromNewDouble(budget));
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
	
	public List<Double> getAutoCompleteValues(String category)
	{
		return model.getAutocompleteValues(category);
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
    public void onResume() {
    	super.onResume();
    	settings = PreferenceManager.getDefaultSharedPreferences(this);
    	
    	model.addDailyBudget();
    	model.payOffInstallments();

		boolean autoComplete = settings.getBoolean("enableAutoCompleteValues", false);
    	if(autoComplete) {
    		view.setUpAutocompleteValues();
    	}
    	else {
    		view.setUpEmptyAutocompleteValues();
    	}
    	
    	processQueue();
    	view.update();
    }
    
    public void processQueue() {
    	if(model.getRemainingItemsInQueue() > 0)
    	{
    		if(processQueueTask == null || processQueueTask.getStatus()!= AsyncTask.Status.RUNNING) {
    			processQueueTask = new ProcessQueueTask();
    			processQueueTask.execute();
    		}
    	}
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
    public void subtractFromBudget(String theValue, String theCategory, String theComment) {
    	
    	try
    	{
    		double value = Double.parseDouble(theValue);
		
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	    	Calendar cal = Calendar.getInstance();
	    	String dateString = dateFormat.format(cal.getTime());
	    	BudgetEntry entry = new BudgetEntry(MoneyFactory.createMoneyFromNewDouble(value*-1), dateString,theCategory,theComment);
	    	model.queueTransaction(entry);
	    	processQueue();
	    	EditText resultText = (EditText)findViewById(R.id.editTextSubtract);
	    	resultText.setText("");
	    	
    	}
    	catch(NumberFormatException e)
    	{
    		// Bad/no input, don't do anything. No biggie.
    	}
		
    }
    
    public String getEnteredValue()
    {
    	EditText resultText = (EditText)findViewById(R.id.editTextSubtract);
    	String result = resultText.getText().toString();
    	return result;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
	private MainView.VieswListener viewListener = new MainView.ViewListener() {
		
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
			String enteredValue = getEnteredValue();
			if(enteredValue.equalsIgnoreCase(""))
			{
				String category = pressedButton.getText().toString();
				List<Double> prices = getAutoCompleteValues(category);
				if(prices.size()!=0)
				{
					Bundle bundle = new Bundle();
			    	bundle.putString("category", category);
			    	 
					DialogFragment newFragment = new ChoosePriceFragment();
			    	newFragment.setArguments(bundle);
			    	newFragment.show(getSupportFragmentManager(), "choose_price");
				}
			}
			else
			{
				subtractFromBudget(enteredValue, pressedButton.getText().toString(),null);
			}
		}
		
		@Override
		public void chooseCategory() {
			DialogFragment newFragment = new ChooseCategoryFragment();
	    	newFragment.show(getSupportFragmentManager(), "choose_category");
		}
	};
	
	private class ProcessQueueTask extends AsyncTask<Void,Void,Void>
	{
		ProgressBar progressBar;
		boolean progressBarVisible = false;
		@Override
		protected void onPreExecute()
		{
			EditText editText = (EditText)findViewById(R.id.editTextSubtract);
			editText.setEnabled(false);
			progressBar = (ProgressBar)findViewById(R.id.progressBarQueue);
			progressBar.setMax(model.getQueueSize());
			System.out.println("size " + model.getQueueSize());
			if(progressBar.getMax() > 10)
			{
				progressBarVisible = true;
				progressBar.setVisibility(View.VISIBLE);
			}
			else
				progressBar.setVisibility(View.GONE);
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			for(int i = 0; i < model.getQueueSize(); i++) {
				model.processQueueItem();
				if(progressBarVisible) {
					progressBar.incrementProgressBy(1);
					publishProgress();
				}
			}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Void... arg0) {
			model.notifyObservers();
		}
		
		@Override
		protected void onPostExecute(Void params) {
			EditText editText = (EditText)findViewById(R.id.editTextSubtract);
			editText.setEnabled(true);
			if(progressBar.getVisibility() == View.VISIBLE) {
				progressBar.setVisibility(View.GONE);
			}
		}
		
	}
    
}
