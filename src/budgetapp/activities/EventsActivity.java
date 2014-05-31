package budgetapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
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

        registerForContextMenu(view.getEventListView());
    }

    @Override
    public void onResume() {
        super.onResume();
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_event, menu);

        menu.setHeaderTitle("Event options");

        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        EventViewHolder clickedViewHolder = (EventViewHolder) ((ListView) v).getItemAtPosition(info.position);

        MenuItem activationItem = menu.findItem(R.id.context_menu_activate_event);
        if (clickedViewHolder.getEvent().isActive()) {
            activationItem.setTitle("Deactivate event");
        } else {
            activationItem.setTitle("Activate event");
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        ListView eventList = view.getEventListView();
        EventViewHolder viewHolder = (EventViewHolder) eventList.getItemAtPosition(info.position);

        switch (item.getItemId()) {
        case R.id.context_menu_edit:
            showEditEventDialog(viewHolder);
            return true;
        case R.id.context_menu_delete:
            showRemoveEventDialog(viewHolder);
            return true;
        case R.id.context_menu_activate_event:
            changeActivationStatus(viewHolder.getEvent());
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    private void changeActivationStatus(Event event) {
        Event newEvent = new Event(event);
        newEvent.setActive(!newEvent.isActive());
        model.editEvent(event.getId(), newEvent);
    }

    private void showEditEventDialog(EventViewHolder listItem) {
        long eventId = listItem.getEvent().getId();
        Bundle bundle = new Bundle();
        bundle.putLong("id", eventId);
        DialogFragment newFragment = new AddEventDialogFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "edit_event");
    }

    private void showRemoveEventDialog(EventViewHolder listItem) {
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
        public void listViewClick(EventViewHolder listItem) {
            Intent intent = new Intent(getBaseContext(), StatsActivity.class);
            Event event = listItem.getEvent();
            intent.putExtra("eventId", event.getId());
            startActivity(intent);
        }

    };

}
