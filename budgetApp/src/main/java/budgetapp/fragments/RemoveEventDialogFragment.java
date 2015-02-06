package budgetapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import budgetapp.activities.EventsActivity;
import budgetapp.main.R;

public class RemoveEventDialogFragment
    extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_remove_event, null);
        builder.setView(view);
        final String chosenEvent = getArguments().getString("chosenEvent");
        final long eventId = getArguments().getLong("eventId");
        TextView category = (TextView) view.findViewById(R.id.dialog_event_name);
        category.setText("Remove event '" + chosenEvent + "'? Transactions logged to this event will not be deleted.");
        final EventsActivity activity = ((EventsActivity) getActivity());

        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                activity.removeEvent(eventId);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                RemoveEventDialogFragment.this.getDialog().cancel();
            }
        });

        return builder.create();
    }
}
