package budgetapp.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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

public class InstallmentsView extends LinearLayout implements IBudgetObserver{

	public static interface ViewListener
	{
		public void showInstallmentDialog();
		public void listViewLongClick(InstallmentViewHolder theEntry);
		public void listViewClick(InstallmentViewHolder listItem);
	}
	
	private BudgetModel model;
	private Button addInstallmentButton;
	private ViewListener viewListener;
	private ListView installmentsListView;
	private BudgetAdapter listAdapter;
	
	public InstallmentsView(Context context, AttributeSet attrs) {
		super(context,attrs);

	}
	
	public void setViewListener(ViewListener viewListener)
	{
		this.viewListener = viewListener;
	}
	
	public void setModel(BudgetModel model)
	{
		this.model = model;
		model.addObserver(this);
	}
	
	public ListView getListView() {
		return installmentsListView;
	}
	
	@Override
	public void update() {
		
		Money totalDailyPayments = MoneyFactory.createMoney();
		List<Installment> allInstallments = new ArrayList<Installment>();
		
		allInstallments = model.getInstallments();
		
		listAdapter = new BudgetAdapter(this.getContext(),R.layout.listitem_installment); 
		
		for(int i = 0; i < allInstallments.size(); i++) {
			if(allInstallments.get(i).getRemainingValue().biggerThan(MoneyFactory.createMoney())) {
				model.removeInstallment(allInstallments.get(i).getId());
			}
			else if(!allInstallments.get(i).isPaidOff()) {
				InstallmentViewHolder viewHolder = new InstallmentViewHolder(allInstallments.get(i));
				listAdapter.add(viewHolder);
				totalDailyPayments = totalDailyPayments.add(BudgetFunctions.max(allInstallments.get(i).getRemainingValue(),allInstallments.get(i).getDailyPayment()));
			}
		}
		
		installmentsListView.setAdapter(listAdapter); 
        TextView totalDailyPaymentsTextView = (TextView)findViewById(R.id.textViewTotalDailyPayment);
        totalDailyPaymentsTextView.setText("Total daily payments: " + new Money(totalDailyPayments));
        
	}
	
	private void setUpCheckBoxListener(CheckBox checkBox) {
		//viewHolder.getCheckBox().isChecked();
		/*checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("aiufhwuei");
			}});*/
	}
	@Override
    protected void onFinishInflate()
    {
    	super.onFinishInflate();
    	addInstallmentButton = (Button)findViewById(R.id.buttonAddInstallment);
    	installmentsListView = (ListView)findViewById(R.id.listViewInstallments);

    	addInstallmentButton.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				viewListener.showInstallmentDialog();
			}
		});
    	
    	installmentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long arg) {
				InstallmentViewHolder listItem = (InstallmentViewHolder)installmentsListView.getItemAtPosition(position);
				viewListener.listViewClick(listItem);					 
			     
			}
    		
		});
    	installmentsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view,int position, long arg)
			{
				InstallmentViewHolder listItem = (InstallmentViewHolder)installmentsListView.getItemAtPosition(position);
				viewListener.listViewLongClick(listItem);					 
			     
				return true;
			}
			
		});
    }
    	
}
