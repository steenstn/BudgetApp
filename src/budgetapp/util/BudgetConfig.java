package budgetapp.util;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import android.content.Context;

public class BudgetConfig {

	private String currentBudgetFileName = "current_budget"; // Internal filename for current budget
	private Context context;
	private BudgetModel model;
	
	public BudgetConfig(Context context, BudgetModel model)
	{
		this.context = context;
		this.model = model;
		readConfigFile();
	}
	
	public void readConfigFile()
    {
    	try
        {
        	 DataInputStream in = new DataInputStream(context.openFileInput(currentBudgetFileName));
        	 
        	 String strLine;
        	 boolean done = false;
        	 int counter = 0;
        		 while(!done)
        		 {
        			 try
        			 {
        				 strLine = in.readUTF();
        				 // Second run, reading dailyBudget in old config file,
        				 // try and parse it directly
        				 if(counter==1) 
        				 {
        					 try
        					 {
        						 double tempDailyBudget = Double.parseDouble(strLine);
        						 model.setDailyBudget(new Money(tempDailyBudget));
        					 }
        					 catch(NumberFormatException e)
        					 {
        						 System.out.println("No double");
        					 }
        				 }
        				 parseString(strLine);
        				 counter++;
        				
        			 }
        			 catch(IOException e)
        			 {
        				 System.out.println("Done reading config");
        				 done = true;
        			 }
        		 }
            	 
	           	//Close the input stream
            	 in.close();
   
        }
        catch(FileNotFoundException e)
        {
        	
        } 
    	catch (IOException e) 
        {
			e.printStackTrace();
		}
    }
	
	private void parseString(String in)
    {
    	//String array with all values in config
    	String[] values = context.getResources().getStringArray(R.array.config_values_array);
    		
		if(in.startsWith(values[0]+"=")) // dailyBudget
    	{
    		model.setDailyBudget(new Money(Double.parseDouble(in.substring(values[0].length()+1))));
    	}
		else if(in.startsWith(values[1]+"=")) // currency
		{
    		Money.setCurrency(in.substring(values[1].length()+1));
		}
		else if(in.startsWith(values[2]+"=")) // printCurrencyAfter
		{
			if(in.substring(values[2].length()+1).equalsIgnoreCase("true"))
				Money.after = true;
			else
				Money.after = false;
		}
		else if(in.startsWith(values[3]+"=")) // exchangeRate
		{
			System.out.println(values[3] + "=" + in);
		}
    	
    }
	
	public void saveToFile()
    {
    	DataOutputStream out;
		try {
			out = new DataOutputStream(context.openFileOutput(currentBudgetFileName,Context.MODE_PRIVATE));
			String[] values = context.getResources().getStringArray(R.array.config_values_array);
	    	
			out.writeUTF(values[0]+"="+model.getDailyBudget().get());
			
			out.writeUTF(values[1]+"="+Money.currency());
			out.writeUTF(values[2]+"="+Money.after);
			out.writeUTF(values[3]+"="+1);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
}
