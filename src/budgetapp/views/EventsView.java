package budgetapp.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.util.BudgetAdapter;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.Event;
import budgetapp.util.IBudgetObserver;
import budgetapp.util.Installment;
import budgetapp.util.money.Money;
import budgetapp.util.money.MoneyFactory;
import budgetapp.viewholders.EventViewHolder;
import budgetapp.viewholders.InstallmentViewHolder;
import budgetapp.viewholders.ViewHolder;

public class EventsView extends LinearLayout implements IBudgetObserver{

	private Button createEventButton;
	private BudgetAdapter listAdapter;
	private ListView eventListView;
	
	public static interface ViewListener
	{
		public void showEventDialog();
		//public void listViewLongClick(InstallmentViewHolder theEntry);
		//public void listViewClick(InstallmentViewHolder listItem);
	}
	
	private BudgetModel model;
	
	public EventsView(Context context, AttributeSet attrs) {
		super(context,attrs);
	}
	
	public void setModel(BudgetModel model)
	{
		this.model = model;
		model.addObserver(this);
	}
	
	
	@Override
	public void update() {

		List<Event> allEvents = model.getEvents();
		listAdapter = new BudgetAdapter(this.getContext(),R.layout.listitem_event); 
		for(Event e : allEvents) {
			listAdapter.add(new EventViewHolder(e));
		}
		
		eventListView.setAdapter(listAdapter);
		
	}

	@Override
    protected void onFinishInflate()
    {
    	super.onFinishInflate();
    	
    	createEventButton = (Button)findViewById(R.id.buttonAddEvent);
    	eventListView = (ListView)findViewById(R.id.listViewEvents);
    	
    	createEventButton.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				//viewListener.showInstallmentDialog();
				Event event = new Event(0, "yeah", "2013-01-10", "2013-02-02", "comment", 3);
				model.addEvent(event);
			}
		});
    }
    	
}
