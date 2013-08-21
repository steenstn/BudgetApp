package budgetapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.util.IBudgetObserver;

public class InstallmentsView extends LinearLayout implements IBudgetObserver{

	public InstallmentsView(Context context, AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	@Override
    protected void onFinishInflate()
    {
    	super.onFinishInflate();
    }
    	
}
