package budgetapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;

public class PreferencesActivity extends PreferenceActivity /*implements OnSharedPreferenceChangeListener*/{
	
	BudgetModel model;
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.preferences);
        
        setUpListeners();
        
    }
    
    @SuppressWarnings("deprecation")
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
    	
    	PreferenceScreen clearAutocomplete = (PreferenceScreen) findPreference("clearAutocompleteValues");
    	clearAutocomplete.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference arg0) {
				model = new BudgetModel(getBaseContext());
				model.clearAutocompleteValues();
				return true;
			}});
    	/*
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
    	*/
    }
/*
	public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
		
		if(key.equalsIgnoreCase("backupFolder"))
		{
			EditTextPreference backupFolder = (EditTextPreference) findPreference("backupFolder");
			backupFolder.setSummary(preferences.getString("backupFolder", Environment.getExternalStorageDirectory().getPath()));
			
		}
		
	}*/
    
}
