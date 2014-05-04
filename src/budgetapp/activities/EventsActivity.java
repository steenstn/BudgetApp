package budgetapp.activities;

import budgetapp.fragments.AddEventDialogFragment;
import budgetapp.fragments.AddInstallmentDialogFragment;
import budgetapp.fragments.RemoveCategoryDialogFragment;
import budgetapp.fragments.RemoveEventDialogFragment;
import budgetapp.fragments.RemoveInstallmentDialogFragment;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.util.Event;
import budgetapp.viewholders.EventViewHolder;
import budgetapp.viewholders.InstallmentViewHolder;
import budgetapp.views.EventsView;
import budgetapp.views.InstallmentsView;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
	
	public void createEvent(Event event) {
		model.addEvent(event);
	}
	
	public void editEvent(long id, Event newEvent) {
		model.editEvent(id, newEvent);
	}
	
	public void removeEvent(long id) {
		model.removeEvent(id);
	}
	
	private EventsView.ViewListener viewListener = new EventsView.ViewListener() {

		@Override
		public void showEventDialog() {
			DialogFragment newFragment = new AddEventDialogFragment();
        	newFragment.show(getSupportFragmentManager(), "add_event");
		}

		@Override
		public void listViewLongClick(EventViewHolder listItem) {
			Event event = listItem.getEvent();
			Bundle bundle = new Bundle();
			bundle.putString("chosenEvent", event.getName());
			bundle.putLong("eventId", event.getId());
			DialogFragment newFragment = new RemoveEventDialogFragment();
			newFragment.setArguments(bundle);
        	newFragment.show(EventsActivity.this.getSupportFragmentManager(), "remove_event");
        	
		}

		@Override
		public void listViewClick(EventViewHolder listItem) {
			long eventId = listItem.getEvent().getId();
			Bundle bundle = new Bundle();
			bundle.putLong("id", eventId);
			DialogFragment newFragment = new AddEventDialogFragment();
			newFragment.setArguments(bundle);
        	newFragment.show(getSupportFragmentManager(), "edit_event");
		}

		
	};

}
