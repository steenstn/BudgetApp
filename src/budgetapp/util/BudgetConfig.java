package budgetapp.util;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;

/**
 * Class for keeping track of config values. Writes to internal storage
 * @author Steen
 *
 */
public class BudgetConfig {

	// Internal filename for current budget
	private String currentBudgetFileName = "current_budget"; 
	
	private Context context;
	
	// Variables for the config values
	private double varDailyBudget = 0;
	private String varCurrency = "kr";
	private boolean varPrintCurrencyAfter = true;
	private double varExchangeRate = 1;
	
	/**
	 * The available fields in the config
	 */
	public static enum Fields
	{
		dailyBudget, // The daily budget
		currency, // The symbol of the current currency
		printCurrencyAfter, // If the currency symbol should be printed after the cash value
		exchangeRate // The exchange rate of the currency, relative to the user's original currency
	}
	
	public BudgetConfig(Context context)
	{
		this.context = context;
		readConfigFile();
	}
	
	/**
	 * Gets a double value from the class
	 * @param theField - The enum field to fetch
	 * @return A double from the listed enum values
	 * @throws IllegalArgumentException() - If the field entered is not a double in the enum list
	 */
	public double getDoubleValue(BudgetConfig.Fields theField)
	{
		switch(theField)
		{
			case dailyBudget:
				return varDailyBudget;
			case exchangeRate:
				return varExchangeRate;
			default:
				throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Gets a String value from the class
	 * @param theField - The enum field to fetch
	 * @return A double from the listed enum values
	 * @throws IllegalArgumentException() - If the field entered is not a String in the enum list
	 */
	public String getStringValue(BudgetConfig.Fields theField)
	{
		switch(theField)
		{
			case currency:
				return varCurrency;
			default:
				throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Gets a boolean value from the class
	 * @param theField - The enum field to fetch
	 * @return A double from the listed enum values
	 * @throws IllegalArgumentException() - If the field entered is not a boolean in the enum list
	 */
	public boolean getBooleanValue(BudgetConfig.Fields theField)
	{
		switch(theField)
		{
			case printCurrencyAfter:
				return varPrintCurrencyAfter;
			default:
				throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Writes a double to a config variable
	 * @param theField - Enum of the variable to write to
	 * @param theValue - The double to write
	 * @throws IllegalArgumentException() - If the field is not one of the double variables
	 */
	public void writeValue(BudgetConfig.Fields theField, double theValue)
	{
		switch(theField)
		{
			case dailyBudget:
				varDailyBudget = theValue;
				break;
			case exchangeRate:
				varExchangeRate = theValue;
				break;
			default:
				throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Writes a String to a config variable
	 * @param theField - Enum of the variable to write to
	 * @param theValue - The String to write
	 * @throws IllegalArgumentException() - If the field is not one of the String variables
	 */
	public void writeValue(BudgetConfig.Fields theField, String theValue)
	{
		switch(theField)
		{
			case currency:
				varCurrency = theValue;
				break;
			default:
				throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Writes a boolean to a config variable
	 * @param theField - Enum of the variable to write to
	 * @param theValue - The boolean to write
	 * @throws IllegalArgumentException() - If the field is not one of the boolean variables
	 */
	public void writeValue(BudgetConfig.Fields theField, boolean theValue)
	{
		switch(theField)
		{
			case printCurrencyAfter:
				varPrintCurrencyAfter = theValue;
				break;
			default:
				throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Reads the internal config file and parses it into variables, reads until EOF
	 */
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
        				 // Second run, if the user have upgraded from an old version of MrCashManger
        				 // the dailyBudget would be here, try and parse the line directly
        				 if(counter==1) 
        				 {
        					 try
        					 {
        						 varDailyBudget = Double.parseDouble(strLine);
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
        				 done = true;
        			 }
        		 }
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
	 * Parses a string and puts the value in the corresponding variable
	 * @param in
	 */
	private void parseString(String in)
    {
		if(in.startsWith(Fields.dailyBudget.name()+"="))
    	{
    		varDailyBudget = Double.parseDouble(in.substring(Fields.dailyBudget.name().length()+1));
    	}
		else if(in.startsWith(Fields.currency.name()+"="))
		{
    		varCurrency = in.substring(Fields.currency.name().length()+1);
		}
		else if(in.startsWith(Fields.printCurrencyAfter.name()+"="))
		{
			if(in.substring(Fields.printCurrencyAfter.name().length()+1).equalsIgnoreCase("true"))
				varPrintCurrencyAfter = true;
			else
				varPrintCurrencyAfter = false;
		}
		else if(in.startsWith(Fields.exchangeRate.name()+"="))
		{
			varExchangeRate = Double.parseDouble(in.substring(Fields.exchangeRate.name().length()+1));
		}
    }
	
	/**
	 * Save the values to internal file
	 */
	public void saveToFile()
    {
    	DataOutputStream out;
		try
		{
			out = new DataOutputStream(context.openFileOutput(currentBudgetFileName,Context.MODE_PRIVATE));
			
			out.writeUTF(Fields.dailyBudget.name()+"="+varDailyBudget);
			out.writeUTF(Fields.currency.name()+"="+varCurrency);
			out.writeUTF(Fields.printCurrencyAfter.name()+"="+varPrintCurrencyAfter);
			out.writeUTF(Fields.exchangeRate.name()+"="+varExchangeRate);
			
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
    }
	
}
