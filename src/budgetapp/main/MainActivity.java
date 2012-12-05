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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.database.sqlite.*;

public class MainActivity extends Activity {

	private BudgetDataSource datasource;
	int currentBudget = 0;
	
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
        
        try{
        	  // Open the file that is the first 
        	  // command line parameter
        	  FileInputStream fstream = new FileInputStream("/mnt/sdcard/budget/budget.txt");
        	  // Get the object of DataInputStream
        	  DataInputStream in = new DataInputStream(fstream);
        	  BufferedReader br = new BufferedReader(new InputStreamReader(in));
        	  String strLine = br.readLine();
        	  currentBudget = Integer.parseInt(strLine);
        	  //Read File Line By Line
        	  
        	  //Close the input stream
        	  in.close();
        	  
              	TextView newBudget = (TextView)findViewById(R.id.textViewCurrentBudget);
              	EditText resultText = (EditText)findViewById(R.id.editTextSubtract);
              	resultText.requestFocus();
              	newBudget.setText(""+currentBudget);
              	if(currentBudget<0)
            		newBudget.setTextColor(Color.rgb(255,255-min(255,Math.abs(currentBudget/5)),255-min(255,Math.abs(currentBudget/5))));
            	else
            		newBudget.setTextColor(Color.rgb(255-min(255,Math.abs(currentBudget/5)),255,255-min(255,Math.abs(currentBudget/5))));
        	  }
    	catch(NumberFormatException e)
    	{
    		currentBudget=0;
    	}
		catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
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
       
      // BudgetEntry entry = datasource.createEntry(100, "yeah");
    	
    	
    	
    	
    	EditText resultText = (EditText)findViewById(R.id.editTextSubtract);
    	String result = resultText.getText().toString();
    	try
    	{
        	int resultInt = Integer.parseInt(result);
        	TextView newBudget = (TextView)findViewById(R.id.textViewCurrentBudget);
        	currentBudget-=resultInt;
        	newBudget.setText(""+currentBudget);
        	
        	// Add to database
        	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        	Calendar cal = Calendar.getInstance();
        	BudgetEntry entry = datasource.createEntry(resultInt*-1, dateFormat.format(cal.getTime()));
        	
        	//Set color
        	if(currentBudget<0)
        		newBudget.setTextColor(Color.rgb(255,255-min(255,Math.abs(currentBudget/5)),255-min(255,Math.abs(currentBudget/5))));
        	else
        		newBudget.setTextColor(Color.rgb(255-min(255,Math.abs(currentBudget/5)),255,255-min(255,Math.abs(currentBudget/5))));
    	  
        	
        		  // Create file 
        		  FileWriter fstream = new FileWriter("/mnt/sdcard/budget/budget.txt");
        		  BufferedWriter out = new BufferedWriter(fstream);
        		  out.write(""+currentBudget);
        		  //Close the output stream
        		  out.close();
        		  resultText.setText("");
        		  updateLog();
    	}
    	catch(NumberFormatException e)
    	{
    		System.out.println(e);
    	}
    	catch (Exception e){//Catch exception if any
  		  System.err.println("Error: " + e.getMessage());
  		}
    		
    }
}
