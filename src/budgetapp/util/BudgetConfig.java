package budgetapp.util;


import android.content.Context;

public class BudgetConfig {

	private String currentBudgetFileName = "current_budget"; // Internal filename for current budget
	private Context context;
	
	public BudgetConfig(Context context)
	{
		this.context = context;
	}
	
}
