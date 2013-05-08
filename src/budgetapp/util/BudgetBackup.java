package budgetapp.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Class responsible for reading and writing to a backupfile
 * @author Steen
 *
 */

public class BudgetBackup {
	
	/**
	 * Writes a backup file. Overwrites file if it exists
	 * @param entries - The list of entries to write
	 * @param filename - Filename to write to
	 */
	public void writeBackupFile(ArrayList<BudgetEntry> entries, String filename)
	{
		
		try 
		{
	        File myFile = new File(filename);
	        myFile.createNewFile();
	        FileOutputStream fOut = new FileOutputStream(myFile);
	        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
	        
	        for(int i = 0; i < entries.size(); i++)
	        {
	        	myOutWriter.append(entries.get(i).getDate() + "\n");
	        	myOutWriter.append(entries.get(i).getValue() + "\n");
	        	myOutWriter.append(entries.get(i).getCategory() + "\n");
	        	myOutWriter.append(entries.get(i).getComment() + "\n");
	        	myOutWriter.append(entries.get(i).getFlags() + "\n");
	        	
	        }
	        
	        myOutWriter.close();
	        fOut.close();
	        System.out.println("Backup written");
	    } 
		catch (Exception e) 
		{
             System.out.println(e);
	    } 

	}
}
