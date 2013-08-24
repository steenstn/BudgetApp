package budgetapp.activities;

import budgetapp.fragments.AddInstallmentDialogFragment;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.Installment;
import budgetapp.util.InstallmentViewHolder;
import budgetapp.util.Money;
import budgetapp.views.InstallmentsView;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;
public class InstallmentsActivity extends FragmentActivity {
	
	private InstallmentsView view;
	private BudgetModel model;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = (InstallmentsView)View.inflate(this, R.layout.activity_installments, null);
        view.setViewListener(viewListener);
        //model = new BudgetModel(this);
        
        setContentView(view);
        model = new BudgetModel(this);
        view.setModel(model);
        view.update();
        
    }
	
	public boolean addInstallment(Money totalValue, Money dailyPayment, String category, String comment)
	{
		//(Money totalValue, Money dailyPayment,
		//		String dateLastPaid, Money remainingValue, String category, String comment)
		Installment installment = new Installment(totalValue, dailyPayment, BudgetFunctions.getDateString(),
			new Money(totalValue.get()-dailyPayment.get()), category, comment);
		
		if(model.addInstallment(installment) == true)
			return true;
		else
			return false;
	}
	// Set up a ViewListener for the view
	private InstallmentsView.ViewListener viewListener = new InstallmentsView.ViewListener() {

		@Override
		public void showInstallmentDialog() {
			DialogFragment newFragment = new AddInstallmentDialogFragment();
        	newFragment.show(getSupportFragmentManager(), "add_installment");
        	
		}

		@Override
		public void listViewClick(InstallmentViewHolder listItem) {
			Toast.makeText(getBaseContext(), "Daily pay: " + listItem.getEntry().getDailyPayment() + ", Remaining: " + listItem.getEntry().getRemainingValue(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void listViewLongClick(InstallmentViewHolder theEntry) {
			model.removeInstallment(theEntry.getEntry().getId());
		}

		
	};
}
