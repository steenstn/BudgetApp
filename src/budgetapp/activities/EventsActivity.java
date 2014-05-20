package budgetapp.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import budgetapp.fragments.AddEventDialogFragment;
import budgetapp.fragments.RemoveEventDialogFragment;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.util.Event;
import budgetapp.viewholders.EventViewHolder;
import budgetapp.views.EventsView;

public class EventsActivity
    extends FragmentActivity {

    private EventsView view;
    private BudgetModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = (EventsView) View.inflate(this, R.layout.activity_events, null);
        view.setViewListener(viewListener);

        setContentView(view);
        model = new BudgetModel(this);
        view.setModel(model);
        view.update();

        registerForContextMenu(view.getEventListView());
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_event, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
        case R.id.context_menu_edit:
            // editNote(info.id);
            showEditEventDialog((EventViewHolder) item);
            return true;
        case R.id.context_menu_delete:
            // deleteNote(info.id);
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    private void showEditEventDialog(EventViewHolder listItem) {
        Event event = listItem.getEvent();
        Bundle bundle = new Bundle();
        bundle.putString("chosenEvent", event.getName());
        bundle.putLong("eventId", event.getId());
        DialogFragment newFragment = new RemoveEventDialogFragment();
        newFragment.setArguments(bundle);
        newFragment.show(EventsActivity.this.getSupportFragmentManager(), "remove_event");
    }

    private EventsView.ViewListener viewListener = new EventsView.ViewListener() {

        @Override
        public void showEventDialog() {
            DialogFragment newFragment = new AddEventDialogFragment();
            newFragment.show(getSupportFragmentManager(), "add_event");
        }

        @Override
        public void listViewLongClick(EventViewHolder listItem) {
            /*   Event event = listItem.getEvent();
               Bundle bundle = new Bundle();
               bundle.putString("chosenEvent", event.getName());
               bundle.putLong("eventId", event.getId());
               DialogFragment newFragment = new RemoveEventDialogFragment();
               newFragment.setArguments(bundle);
               newFragment.show(EventsActivity.this.getSupportFragmentManager(), "remove_event");
            */
            openContextMenu(view.getEventListView());
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
