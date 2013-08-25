package budgetapp.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.util.IBudgetObserver;
import budgetapp.util.Installment;
import budgetapp.util.InstallmentAdapter;
import budgetapp.util.InstallmentViewHolder;
import budgetapp.util.Money;
import budgetapp.util.ViewHolder;
import budgetapp.views.MainView.ViewListener;

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
	
	public InstallmentsView(Context context, AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
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
	
	@Override
	public void update() {
		List<Installment> allInstallments = new ArrayList<Installment>();
		
		allInstallments = model.getInstallments();
		
		InstallmentAdapter listAdapter = new InstallmentAdapter(this.getContext()); 
		
		for(int i = 0; i < allInstallments.size(); i++)
		{
			if(allInstallments.get(i).getRemainingValue().get() > 0) // This installment is paid for
				model.removeInstallment(allInstallments.get(i).getId());
			else
				listAdapter.add(new InstallmentViewHolder(allInstallments.get(i)));
		}
		installmentsListView.setAdapter(listAdapter); 
        
		
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
