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
	private double var_dailyBudget = 0;
	private String var_currency = "kr";
	private boolean var_printCurrencyAfter = true;
	private double var_exchangeRate = 1;
	
	public static enum fields
	{
		dailyBudget,
		currency,
		printCurrencyAfter,
		exchangeRate
	}
	
	public BudgetConfig(Context context)
	{
		this.context = context;
		readConfigFile();
	}
	
	public double getDoubleValue(BudgetConfig.fields theField)
	{
		switch(theField)
		{
			case dailyBudget:
				return var_dailyBudget;
			case exchangeRate:
				return var_exchangeRate;
			default:
					throw new IllegalArgumentException();
		}
		
	}
	
	public String getStringValue(BudgetConfig.fields theField)
	{
		switch(theField)
		{
			case currency:
				return var_currency;
			default:
					throw new IllegalArgumentException();
		}
		
	}
	
	public boolean getBooleanValue(BudgetConfig.fields theField)
	{
		switch(theField)
		{
			case printCurrencyAfter:
				return var_printCurrencyAfter;
			default:
					throw new IllegalArgumentException();
		}
		
	}
	public boolean writeValue(BudgetConfig.fields theField, double theValue)
	{
		switch(theField)
		{
			case dailyBudget:
				var_dailyBudget = theValue;
				break;
			case exchangeRate:
				var_exchangeRate = theValue;
				break;
			default:
				return false;
		}
		
		return true;
	}
	
	public boolean writeValue(BudgetConfig.fields theField, String theValue)
	{
		switch(theField)
		{
			case currency:
				var_currency = theValue;
				break;
			default:
				return false;
		}
		
		return true;
	}
	
	public boolean writeValue(BudgetConfig.fields theField, boolean theValue)
	{
		switch(theField)
		{
			case printCurrencyAfter:
				var_printCurrencyAfter = theValue;
				break;
			default:
				return false;
		}
		
		return true;
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
        						 var_dailyBudget = Double.parseDouble(strLine);
        					 }
        					 catch(NumberFormatException e)
        					 {

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
	/**
	 * Parses a string and acts accordingly
	 * @param in
	 */
	private void parseString(String in)
    {
    	
		if(in.startsWith(fields.dailyBudget.name()+"=")) // dailyBudget
    	{
    		var_dailyBudget = Double.parseDouble(in.substring(fields.dailyBudget.name().length()+1));
    	}
		else if(in.startsWith(fields.currency.name()+"=")) // currency
		{
    		var_currency = in.substring(fields.currency.name().length()+1);
		}
		else if(in.startsWith(fields.printCurrencyAfter.name()+"=")) // printCurrencyAfter
		{
			if(in.substring(fields.printCurrencyAfter.name().length()+1).equalsIgnoreCase("true"))
				var_printCurrencyAfter = true;
			else
				var_printCurrencyAfter = false;
		}
		else if(in.startsWith(fields.exchangeRate.name()+"=")) // exchangeRate
		{
			System.out.println(fields.exchangeRate.name() + "=" + in);
		}
    	
    }
	
	public void saveToFile()
    {
    	DataOutputStream out;
		try {
			out = new DataOutputStream(context.openFileOutput(currentBudgetFileName,Context.MODE_PRIVATE));
			
			out.writeUTF(fields.dailyBudget.name()+"="+var_dailyBudget);
			out.writeUTF(fields.currency.name()+"="+var_currency);
			out.writeUTF(fields.printCurrencyAfter.name()+"="+var_printCurrencyAfter);
			out.writeUTF(fields.exchangeRate.name()+"="+1);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
}
