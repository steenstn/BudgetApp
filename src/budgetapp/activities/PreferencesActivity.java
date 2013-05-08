package budgetapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
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

public class PreferencesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{
	
	private BudgetModel model;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        model = new BudgetModel(this);
        addPreferencesFromResource(R.xml.preferences);
        
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        
        setUpListeners();
        
        
        
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
    	
    	PreferenceScreen backup = (PreferenceScreen) findPreference("manageBackup");
    	backup.setOnPreferenceClickListener(new OnPreferenceClickListener() {
       
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				Intent intent = new Intent(PreferencesActivity.this,BackupActivity.class);
	    		startActivity(intent);
				return true;
			}
        });
    	getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
		
		if(key.equalsIgnoreCase("backupFolder"))
		{
			EditTextPreference backupFolder = (EditTextPreference) findPreference("backupFolder");
			backupFolder.setSummary(preferences.getString("backupFolder", "/sdcard/"));
			
		}
		
	}
    
}
