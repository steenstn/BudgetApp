package budgetapp.fragments;

/**
 * Dialog Fragment for adding a new category
 * 
 */
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import budgetapp.activities.StatsActivity;
import budgetapp.main.R;
import budgetapp.util.Event;
import budgetapp.util.MultiSpinner;
import budgetapp.util.entries.BudgetEntry;
import budgetapp.util.money.Money;
import budgetapp.util.money.MoneyFactory;

public class EditTransactionDialogFragment
    extends DialogFragment {

    MultiSpinner eventSpinner;
    StatsActivity activity;
    BudgetEntry theEntry;
    List<String> activeEventNames;
    List<Event> linkedEvents;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        final View view = inflater.inflate(R.layout.dialog_edit_transaction, null);

        theEntry = getArguments().getParcelable("entry");
        builder.setView(view);
        TextView date = (TextView) view.findViewById(R.id.dialog_edit_transaction_date_textview);
        date.setText(theEntry.getDate());

        final EditText category = (EditText) view.findViewById(R.id.dialog_edit_transaction_category_edittext);
        category.setText(theEntry.getCategory());

        final EditText value = (EditText) view.findViewById(R.id.dialog_edit_transaction_value_edittext);
        value.setText("" + theEntry.getValue().get() / Money.getExchangeRate());

        final EditText comment = (EditText) view.findViewById(R.id.dialog_add_comment);
        comment.setText(theEntry.getComment());

        final CheckBox checkBox = (CheckBox) view
            .findViewById(R.id.dialog_edit_transaction_delete_transaction_checkbox);

        eventSpinner = (MultiSpinner) view.findViewById(R.id.dialog_edit_transaction_event_spinner);

        activity = ((StatsActivity) getActivity());

        setUpSpinner();
        // Add action buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

                if (checkBox.isChecked()) {
                    activity.removeTransactionEntry(theEntry);
                    Toast.makeText(view.getContext(), "Transaction deleted", Toast.LENGTH_LONG).show();

                } else {
                    // See if the new entry is ok
                    long newId;
                    String newDate;
                    String newCategory;
                    double newValue;
                    int newFlags;
                    String newComment;

                    try {
                        newId = theEntry.getId();
                        if (newId < 0)
                            throw new Exception();

                        newDate = theEntry.getDate();
                        if (newDate.equalsIgnoreCase(""))
                            throw new Exception();

                        newCategory = category.getText().toString();
                        if (newCategory.equalsIgnoreCase(""))
                            throw new Exception();

                        newValue = Double.parseDouble(value.getText().toString());
                        if (newValue == 0)
                            throw new Exception();

                        newFlags = theEntry.getFlags();

                        newComment = comment.getText().toString(); // Comment
                                                                   // can be
                                                                   // empty, no
                                                                   // biggie

                        BudgetEntry newEntry = new BudgetEntry(newId, MoneyFactory.createMoneyFromNewDouble(newValue),
                            newDate, newCategory, newFlags, newComment);

                        activeEventNames = eventSpinner.getSelectedItems();
                        if (activeEventNames.size() != 0) {
                            activity.getModel().removeTransactionFromEvents(newId);
                            for (String eventName : activeEventNames) {
                                long eventId = activity.getModel().getIdFromEventName(eventName);
                                activity.getModel().linkTransactionToEvent(newId, eventId);
                            }
                        } else {
                            activity.getModel().removeTransactionFromEvents(newId);
                        }

                        activity.editTransactionEntry(theEntry.getId(), newEntry);

                        Toast.makeText(view.getContext(), "Successfully edited transaction", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(view.getContext(), "Could not edit transaction", Toast.LENGTH_LONG).show();
                    }
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditTransactionDialogFragment.this.getDialog().cancel();
            }
        });
        return builder.create();
    }

    private void setUpSpinner() {
        List<Event> events = activity.getModel().getEvents();

        ArrayList<String> eventNames = new ArrayList<String>();
        for (Event e : events) {
            eventNames.add(e.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(eventSpinner.getContext(),
            android.R.layout.simple_spinner_item, eventNames);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        eventSpinner.setItems(eventNames);

        if (linkedEvents == null) {
            linkedEvents = activity.getModel().getLinkedEventsFromTransactionId(theEntry.getId());
        }
        for (Event event : linkedEvents) {
            eventSpinner.setChecked(event.getName(), true);
        }

    }
}
