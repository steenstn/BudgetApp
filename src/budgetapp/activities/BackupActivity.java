package budgetapp.activities;

import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

public class BackupActivity extends Activity{
	
	private BudgetModel model;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        
        model = new BudgetModel(this);
       
	}
	
	public void saveBackup(View v)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		String temp = prefs.getString("backupFolder", Environment.getExternalStorageDirectory().getPath() + "mrcashbackup.txt");
		System.out.println("Backing up to " + temp);
		System.out.println("env: " + Environment.getExternalStorageDirectory().getPath());
		model.saveBackup(temp);
	}
	
	public void readBackup(View v)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String temp = prefs.getString("backupFolder", Environment.getExternalStorageDirectory().getPath() + "mrcashbackup.txt");
		
		if(model.readAndMergeBackup(temp))
		{
			Toast.makeText(this, "Successfully read backup file", Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(this, "Could not read backup file", Toast.LENGTH_LONG).show();
		}
	}
}
