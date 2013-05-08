package budgetapp.activities;

import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class BackupActivity extends Activity{
	
	private BudgetModel model;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        
        model = new BudgetModel(this);
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		// then you use
		String temp = prefs.getString("backupFolder", "/sdcard/mrcashbackup.txt");
		System.out.println("Backing up to " + temp);
		model.saveBackup(temp);
		
	}
}
