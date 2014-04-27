package budgetapp.activities;

import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.viewholders.EventViewHolder;
import budgetapp.viewholders.InstallmentViewHolder;
import budgetapp.views.EventsView;
import budgetapp.views.InstallmentsView;
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
        view.setViewListener(viewListener);
        
        setContentView(view);
        model = new BudgetModel(this);
        view.setModel(model);
        view.update();
    }
	
	
	
	public BudgetModel getModel() {
		return model;
	}
	
	private EventsView.ViewListener viewListener = new EventsView.ViewListener() {

		@Override
		public void showEventDialog() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void listViewLongClick(EventViewHolder listItem) {
			long id = listItem.getEvent().getId();
			model.removeEvent(id);
		}

		
	};

}
