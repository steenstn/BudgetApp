package budgetapp.activities;

import java.io.IOException;

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
		
		try
		{
			model.saveBackup(temp);
			Toast.makeText(this, "Successfully wrote backup file", Toast.LENGTH_LONG).show();
		}
		catch(NumberFormatException e)
		{
			Toast.makeText(this, "Error: Error parsing value", Toast.LENGTH_LONG).show();
		}
		catch(IOException e)
		{
			Toast.makeText(this, "Error: Could not open file", Toast.LENGTH_LONG).show();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Toast.makeText(this, "Unknown Error: Could not complete backup", Toast.LENGTH_LONG).show();
		}
	}
	
	public void readBackup(View v)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String temp = prefs.getString("backupFolder", Environment.getExternalStorageDirectory().getPath() + "mrcashbackup.txt");
		
		try
		{
			model.readAndMergeBackup(temp);
			Toast.makeText(this, "Successfully read backup file", Toast.LENGTH_LONG).show();
		}
		catch(NumberFormatException e)
		{
			Toast.makeText(this, "Error: Error parsing value", Toast.LENGTH_LONG).show();
		}
		catch(IOException e)
		{
			Toast.makeText(this, "Error: Could not open file", Toast.LENGTH_LONG).show();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Toast.makeText(this, "Unknown Error: Could not complete backup", Toast.LENGTH_LONG).show();
		}
	}
}
