package budgetapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import budgetapp.main.R;

public class PreferencesActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.preferences);
        
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        PreferenceScreen dailyBudget = (PreferenceScreen) findPreference("setDailyBudget");
        String menuString = getString(R.string.menu_setdailybudget);
    	CharSequence withBudget = menuString;// + " (" + "model.getDailyBudget()" + ")";
        dailyBudget.setTitle(withBudget);
       // String temp = settings.getString("editCurrency", "fail");
        //System.out.println("res: " + temp);
        
        
    }
}
