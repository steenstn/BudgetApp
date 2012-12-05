package budgetapp.main;



import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.content.ClipData.Item;
import android.content.Context;
import android.database.sqlite.*;

public class MainActivity extends Activity {

	private BudgetDataSource datasource;
	int currentBudget = 0;
	private String currentBudgetFileName = "current_budget"; // Internal file for current budget
	private boolean logData = true; // If transactions should be logged
	int min(int a,int b)
	{
		if(a<b)
			return a;
		return b;
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
         try
        {
        	 DataInputStream in = new DataInputStream(openFileInput(currentBudgetFileName));
             try
             {
            	 String strLine = in.readUTF();
            	 currentBudget = Integer.parseInt(strLine);
           	
           	  //Close the input stream
           	  in.close();
           	  
             	TextView newBudget = (TextView)findViewById(R.id.textViewCurrentBudget);
             	EditText resultText = (EditText)findViewById(R.id.editTextSubtract);
             	//	resultText.requestFocus();
             	newBudget.setText(""+currentBudget);
             	if(currentBudget<0)
               		newBudget.setTextColor(Color.rgb(255,255-min(255,Math.abs(currentBudget/5)),255-min(255,Math.abs(currentBudget/5))));
               	else
               		newBudget.setTextColor(Color.rgb(255-min(255,Math.abs(currentBudget/5)),255,255-min(255,Math.abs(currentBudget/5))));
           	  
            	 
             }
             catch(IOException e)
             {
            	 currentBudget=0;
             }
        }
        catch(NumberFormatException e)
        {
        	currentBudget=0;
        }
        catch(FileNotFoundException e)
        {
        	currentBudget=0;
        }
      
        	  
        datasource = new BudgetDataSource(this);
        datasource.open();
        
        //List all budget entries
        updateLog();
       
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void updateLog()
    {
    	List<BudgetEntry> entries = datasource.getAllEntries();
        TextView temp = (TextView)findViewById(R.id.textViewLog);
        temp.setText("");
        for(int i=entries.size()-1;i>=entries.size()-10;i--)
        {	
        	if(i>=0)
        		temp.append(entries.get(i).getDate() + ":    " + entries.get(i).getValue() + "\n");
        }
    	
    }
    
    public void subtractFromBudget(View view) {
       
    	EditText resultText = (EditText)findViewById(R.id.editTextSubtract);
    	String result = resultText.getText().toString();
    	
    	try
    	{
    		int resultInt = Integer.parseInt(result);
        	TextView newBudget = (TextView)findViewById(R.id.textViewCurrentBudget);
        	currentBudget-=resultInt;
        	newBudget.setText(""+currentBudget);
        	
        	// Add to database if logging is set
        	if(logData)
        	{
	        	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	        	Calendar cal = Calendar.getInstance();
	        	BudgetEntry entry = datasource.createEntry(resultInt*-1, dateFormat.format(cal.getTime()));
        	}
        	//Set color
        	if(currentBudget<0)
        		newBudget.setTextColor(Color.rgb(255,255-min(255,Math.abs(currentBudget/5)),255-min(255,Math.abs(currentBudget/5))));
        	else
        		newBudget.setTextColor(Color.rgb(255-min(255,Math.abs(currentBudget/5)),255,255-min(255,Math.abs(currentBudget/5))));
    	  
        	resultText.setText("");
        	updateLog();
    		DataOutputStream out = new DataOutputStream(openFileOutput(currentBudgetFileName,Context.MODE_PRIVATE));
    		out.writeUTF(""+currentBudget);
    		
    	}
    	catch(IOException e)
    	{
    		System.out.println("Error: "+e);
    	}
    	
    		
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_logdata:
                logData=!logData;
                item.setChecked(logData);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
}
