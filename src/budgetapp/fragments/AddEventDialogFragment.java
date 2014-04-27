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
	EventsActivity activity;
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		activity = (EventsActivity) getActivity();
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    // Get the layout inflater
	    LayoutInflater inflater = activity.getLayoutInflater();
	    // Inflate and set the layout for the dialog
	    view = inflater.inflate(R.layout.dialog_add_event, null);
	 
	    builder.setView(view);
	    
	   
		
	    // Add action buttons
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	   
    		@Override
            public void onClick(DialogInterface dialog, int id) {
    			String name = eventName.getText().toString();
    			String comment = eventName.getText().toString();
    			if(name != null && !name.equalsIgnoreCase("")) {
    				Event event = new Event(0, name, BudgetFunctions.getDateString(), BudgetFunctions.getDateString(), comment, 0);
    				activity.createEvent(event);
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
	@Override
	public void onResume()
	{
		super.onResume();
		eventName = (EditText)view.findViewById(R.id.dialog_event_name);
		eventComment = (EditText)view.findViewById(R.id.dialog_event_comment);
		

	}
	
	
	
}