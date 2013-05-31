package budgetapp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;


/**
 * Class responsible for reading and writing to a backup file
 * @author Steen
 *
 */

public class BudgetBackup {
	
	ProgressDialog progressBar;
	Handler progressHandler = new Handler();
	int progressStatus = 0;
	Context context;
	ArrayList<BudgetEntry> theList = new ArrayList<BudgetEntry>();
	FileInputStream is;
	InputStreamReader streamReader;
	BufferedReader bufferedReader;
	File inputFile;
	
	public BudgetBackup(Context context)
	{
		this.context = context;
	}
	
	/**
	 * Writes a backup file. Overwrites file if it exists
	 * @param entries - The list of entries to write
	 * @param filename - Filename to write to
	 */
	public boolean writeBackupFile(ArrayList<BudgetEntry> entries, String filename) throws Exception
	{
	        File myFile = new File(filename);
	        myFile.createNewFile();
	        FileOutputStream fOut = new FileOutputStream(myFile);
	        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
	        
	        for(int i = 0; i < entries.size(); i++)
	        {
	        	myOutWriter.append(entries.get(i).getDate() + "\n");
	        	myOutWriter.append(""+entries.get(i).getValue().get() + "\n");
	        	myOutWriter.append(entries.get(i).getCategory() + "\n");
	        	myOutWriter.append(entries.get(i).getComment() + "\n");
	        	myOutWriter.append(entries.get(i).getFlags() + "\n");
	        	
	        }
	        
	        myOutWriter.close();
	        fOut.close();
	        return true;
	     
	}
	
	/**
	 * Reads a file with entries and returns a list with them
	 * @param filename - The file to read
	 * @return - A list of all transactions
	 * @throws Exception 
	 */
	public ArrayList<BudgetEntry> readBackupFile(final String filename) throws Exception 
	{	
		inputFile = new File(filename);
				
		is = new FileInputStream(inputFile);
					
		streamReader = new InputStreamReader(is);
		bufferedReader = new BufferedReader(streamReader);
		
		progressBar = new ProgressDialog(context);
		progressBar.setCancelable(true);
		progressBar.setMessage("Reading backup file...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressBar.setProgress(0);
		progressBar.setMax(countLines(filename) / 6);

		progressBar.show();
		
		
		
		Thread readingThread = new Thread(new Runnable() {
			String date = "", value = "", category = "", comment = "", flags = "";
			
			public void run() {
				while(true)
				{
					
					try {
						date = bufferedReader.readLine();
					
					
						value = bufferedReader.readLine();
						category = bufferedReader.readLine();
						comment = bufferedReader.readLine();
						flags = bufferedReader.readLine();
					} catch (IOException e) {						
						e.printStackTrace();
						break;
					}
					if(date == null)
						break;
					theList.add(createEntry(date, value, category, comment, flags));
					System.out.println("Added entry");
					progressStatus++;
					progressHandler.post(new Runnable() {
						public void run() {
						  progressBar.setProgress(progressStatus);
						}
					  });
				}

			}
		});
		readingThread.start();
		System.out.println("Current thread: " + Thread.currentThread());
		readingThread.join();

		progressBar.dismiss();
		bufferedReader.close();
		
		streamReader.close();
		is.close();
		
		return theList;
	}
	
	/**
	 * Counts and returns the number of lines in a file
	 * @param filename - The file to read
	 * @return - Number of lines in the file
	 * @throws Exception
	 */
	private int countLines(String filename) throws Exception
	{
		int numRows = 0;
		FileInputStream is;
		InputStreamReader streamReader;
		BufferedReader bufferedReader;
		
		File inputFile = new File(filename);
		if(!inputFile.isFile())
			throw new Exception();
		
		is = new FileInputStream(inputFile);
		streamReader = new InputStreamReader(is);
		bufferedReader = new BufferedReader(streamReader);
		
		String temp = "";
		while((temp = bufferedReader.readLine()) != null)
		{
			numRows++;
		}
		bufferedReader.close();
		streamReader.close();
		is.close();
		return numRows;
	}
	
	/**
	 * Creates a BudgetEntry from strings
	 * @param date - The date
	 * @param value - The value 
	 * @param category - The category
	 * @param comment - The comment
	 * @param flags - The flags
	 * @return - A new BudgetEntry
	 */
	private BudgetEntry createEntry(String date, String value, String category, String comment,String flags)
	{
		double theValue = Double.parseDouble(value);
		int theFlags = Integer.parseInt(flags);
		return new BudgetEntry(new Money(theValue), date, category, comment, theFlags);
	}
}
