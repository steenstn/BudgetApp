package budgetapp.activities;

import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.views.InstallmentsView;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class InstallmentsActivity extends FragmentActivity {
	
	InstallmentsView view;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = (InstallmentsView)View.inflate(this, R.layout.activity_installments, null);
       // view.setViewListener(viewListener);
        //model = new BudgetModel(this);

        setContentView(view);
       // view.setModel(model);
       // view.update();
        
    }
}
