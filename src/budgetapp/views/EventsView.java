package budgetapp.views;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.util.BudgetAdapter;
import budgetapp.util.Event;
import budgetapp.util.IBudgetObserver;
import budgetapp.viewholders.EventViewHolder;

public class EventsView
    extends LinearLayout
    implements IBudgetObserver {

    private Button createEventButton;
    private BudgetAdapter listAdapter;
    private ListView eventListView;
    private ViewListener viewListener;

    public static interface ViewListener {
        public void showEventDialog();
        public void listViewClick(EventViewHolder listItem);
    }

    private BudgetModel model;

    public EventsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setModel(BudgetModel model) {
        this.model = model;
        model.addObserver(this);
    }

    public ListView getEventListView() {
        return eventListView;
    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void update() {

        List<Event> allEvents = model.getEvents();
        listAdapter = new BudgetAdapter(this.getContext(), R.layout.listitem_event);
        for (Event e : allEvents) {
            listAdapter.add(new EventViewHolder(e));
        }

        eventListView.setAdapter(listAdapter);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        createEventButton = (Button) findViewById(R.id.buttonAddEvent);
        eventListView = (ListView) findViewById(R.id.listViewEvents);

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.showEventDialog();
            }
        });
        
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                EventViewHolder listItem = (EventViewHolder) eventListView.getItemAtPosition(position);
                viewListener.listViewClick(listItem);

            }

        });

    }

}
