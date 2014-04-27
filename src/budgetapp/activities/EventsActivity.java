package budgetapp.activities;

import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.views.EventsView;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class EventsActivity extends FragmentActivity {
	
	private EventsView view;
	private BudgetModel model;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = (EventsView)View.inflate(this, R.layout.activity_events, null);
        
        setContentView(view);
        model = new BudgetModel(this);
        view.setModel(model);
        view.update();
    }
	
	
	
	public BudgetModel getModel() {
		return model;
	}
	
}
