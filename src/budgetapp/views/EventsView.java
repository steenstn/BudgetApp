package budgetapp.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.util.BudgetAdapter;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.IBudgetObserver;
import budgetapp.util.Installment;
import budgetapp.util.money.Money;
import budgetapp.util.money.MoneyFactory;
import budgetapp.viewholders.InstallmentViewHolder;
import budgetapp.viewholders.ViewHolder;

public class EventsView extends LinearLayout implements IBudgetObserver{

	public static interface ViewListener
	{
		//public void showInstallmentDialog();
		//public void listViewLongClick(InstallmentViewHolder theEntry);
		//public void listViewClick(InstallmentViewHolder listItem);
	}
	
	private BudgetModel model;
	
	public EventsView(Context context, AttributeSet attrs) {
		super(context,attrs);
	}
	
	public void setModel(BudgetModel model)
	{
		this.model = model;
		model.addObserver(this);
	}
	
	
	@Override
	public void update() {
		
	}

	@Override
    protected void onFinishInflate()
    {
    	super.onFinishInflate();
    }
    	
}
