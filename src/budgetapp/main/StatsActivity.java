package budgetapp.main;

import java.util.List;

import budgetapp.util.BudgetDataSource;
import budgetapp.util.BudgetEntry;
import budgetapp.util.CategoryEntry;
import budgetapp.util.DatabaseEntry;
import budgetapp.util.DayEntry;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class StatsActivity extends Activity{
	
	BudgetDataSource datasource = MainActivity.datasource;
	List<DayEntry> days;
	List<BudgetEntry> entries;
	List<CategoryEntry> categories;
	int numDaysForDerivative = 7;
	int numTransactionsForDerivative = 10;
	int min(int a,int b) 
	{
		if(a<b)
			return a;
		return b;
	}
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        
        // Read in all the data
        entries = datasource.getAllTransactions();
        days = datasource.getAllDays();
        categories = datasource.getCategoriesSorted();
        updateStats();
        updateLog();
        
	}
	
	public void updateStats()
	{
		TextView stats = (TextView)findViewById(R.id.textViewLogStats);
		if(days.size()<2)
		{
			stats.setText("Not enough days for mean derivative\n");
		}
		else
		{
			stats.setText("Mean derivative (" + min(numDaysForDerivative,days.size()) + ") days: ");
			long dayDerivative = getMeanDerivative(days,numDaysForDerivative);
			stats.append(""+dayDerivative + "\n");
		}
		if(entries.size()<2)
		{
			stats.append("Not enough transactions for mean derivative\n");
		}
		else
		{
			stats.append("Mean derivative (" + min(numTransactionsForDerivative,entries.size()) + ") transactions: ");
			long transactionDerivative = getMeanDerivative(entries,numDaysForDerivative);
			stats.append(""+transactionDerivative + "\n");
		}
		
		
	}
	
	// Returns the mean cash flow for n time steps or as many as are available if less
	long getMeanDerivative(List<?> theEntries, int n)
	{
		
		if(theEntries.size()<2) // Just 0 or 1 entries, no derivative yet
			return 0;
		int i=0;
		
		long sum = 0;
		long result = 0;
		
			// Step through days, break if i reaches size or n
			while(i<theEntries.size() && i<n) 
			{
				//Get the value depending on the class
				if(theEntries.get(i) instanceof DayEntry)
					sum+=((DayEntry) theEntries.get(i)).getTotal();
				if(theEntries.get(i) instanceof BudgetEntry)
					sum+=((BudgetEntry) theEntries.get(i)).getValue();
				
				i++;
			}
			result = sum/i;
		
		return result;
		
	}
	public void updateLog()
	{
		
        TextView top = (TextView)findViewById(R.id.textViewLogTop);
        TextView middle = (TextView)findViewById(R.id.textViewLogMiddle);
        TextView bottom = (TextView)findViewById(R.id.textViewLogBottom);
        
        
        top.setMovementMethod(new ScrollingMovementMethod());
        middle.setMovementMethod(new ScrollingMovementMethod());
        bottom.setMovementMethod(new ScrollingMovementMethod());
        for(int i=0;i<entries.size();i++)
        {	
        		top.append(entries.get(i).getDate() + ":    " + entries.get(i).getValue() + "\t\t");
        		top.append(entries.get(i).getCategory() + "\n");	
        }
        
        
        
        
        for(int i=0;i<categories.size();i++) 
        {	
        	middle.append(categories.get(i)+ ": "+ categories.get(i).getTotal() + "\t\tTransactions: ");
        	middle.append(categories.get(i).getNum()+"\n");
        }
        
        for(int i=0;i<days.size();i++) 
        {	
        	bottom.append(days.get(i).getDate()+ ": ");
        		bottom.append(days.get(i).getTotal()+"\n");
        }
	}
}
