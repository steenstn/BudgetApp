package budgetapp.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;



public class BudgetBackup {
	
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
	        	myOutWriter.append(entries.get(i).toString() + "\n");
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
