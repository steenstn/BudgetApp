package budgetapp.fragments;
/**
 * Dialog Fragment for adding a new category
 * 
 */

import java.util.Calendar;
import java.util.List;

import budgetapp.activities.EventsActivity;
import budgetapp.activities.InstallmentsActivity;
import budgetapp.main.R;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.Event;
import budgetapp.util.money.MoneyFactory;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;

public class AddEventDialogFragment extends DialogFragment {

	View view;
	EditText eventName;
	EditText eventComment;
	CheckBox eventActive;
	EventsActivity activity;
	Event eventToEdit;
	String mode;
	String name;
	String comment;
	int flags;
	long eventId;
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		activity = (EventsActivity) getActivity();
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    // Get the layout inflater
	    LayoutInflater inflater = activity.getLayoutInflater();
	    // Inflate and set the layout for the dialog
	    view = inflater.inflate(R.layout.dialog_add_event, null);
	 
	    builder.setView(view);
	    mode = this.getTag();
	    
	    // Add action buttons
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	   
    		@Override
            public void onClick(DialogInterface dialog, int id) {
    			
    			if(mode.equalsIgnoreCase("add_event")) {
    				parseFields();
					if(name != null && !name.equalsIgnoreCase("")) {
						Event event = new Event(0, name, BudgetFunctions.getDateString(), BudgetFunctions.getDateString(), comment, flags);
						activity.createEvent(event);
					}
    			}
    			else if(mode.equalsIgnoreCase("edit_event")) {
    				parseFields();
    				if(name != null && !name.equalsIgnoreCase("")) {
						Event newEvent = new Event(0, name, BudgetFunctions.getDateString(), BudgetFunctions.getDateString(), comment, flags);
						activity.editEvent(eventId, newEvent);
					}
    			}
    			
    		}
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AddEventDialogFragment.this.getDialog().cancel();
            }
        });   
    	
	    return builder.create();
	}
	
	private void parseFields() {
		name = eventName.getText().toString();
		comment = eventComment.getText().toString();
		flags = eventActive.isChecked() ? 1 : 0;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		eventName = (EditText)view.findViewById(R.id.dialog_event_name);
		eventComment = (EditText)view.findViewById(R.id.dialog_event_comment);
		eventActive = (CheckBox)view.findViewById(R.id.dialog_event_checkbox_active);
		
		if(mode.equalsIgnoreCase("edit_event")) {
			Bundle bundle = this.getArguments();
		    eventId = bundle.getLong("id", -1);
			Event event = activity.getModel().getEvent(eventId);
			
			eventName.setText(event.getName());
			eventComment.setText(event.getComment());
			eventActive.setChecked(event.isActive());
		}

	}
	
	
	
}