package budgetapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import android.view.View;
import budgetapp.fragments.AddCategoryDialogFragment;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;

public class PreferencesActivity extends PreferenceActivity {
	
	private BudgetModel model;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        model = new BudgetModel(this);
        addPreferencesFromResource(R.xml.preferences);
        
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        //PreferenceScreen dailyBudget = (PreferenceScreen) findPreference("setDailyBudget");
        //String menuString = getString(R.string.menu_setdailybudget);
    	//CharSequence withBudget = menuString + " (" + model.getDailyBudget() + ")";
        //dailyBudget.setTitle(withBudget);
        
        setUpListeners();
        
        
       // String temp = settings.getString("editCurrency", "fail");
        //System.out.println("res: " + temp);
        
        
    }
    
    private void setUpListeners()
    {
    	
    	PreferenceScreen categories = (PreferenceScreen) findPreference("manageCategories");
    	categories.setOnPreferenceClickListener(new OnPreferenceClickListener() {
       
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				Intent intent = new Intent(PreferencesActivity.this,CategoriesActivity.class);
	    		startActivity(intent);
				return true;
			}
        });
    }
    
}
