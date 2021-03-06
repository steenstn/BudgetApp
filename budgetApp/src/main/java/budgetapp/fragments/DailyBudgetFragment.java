package budgetapp.fragments;

/**
 * Dialog Fragment for adding a new category
 * 
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import budgetapp.activities.MainActivity;
import budgetapp.main.R;
import budgetapp.util.money.Money;

public class DailyBudgetFragment
    extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        final View view = inflater.inflate(R.layout.dialog_set_dailybudget, null);
        TextView currentBudget = (TextView) view.findViewById(R.id.dialog_dailybudget_currentbudget);
        currentBudget.append(" " + ((MainActivity) getActivity()).getDailyBudget());
        builder.setView(view);

        // Add action buttons
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

                EditText newBudget = (EditText) view.findViewById(R.id.dialog_dailybudget);

                try {
                    double theBudget = Double.parseDouble(newBudget.getText().toString());
                    ((MainActivity) getActivity()).setDailyBudget(theBudget);

                    Money dailyBudget = ((MainActivity) getActivity()).getDailyBudget();
                    Toast.makeText(view.getContext(), "Daily budget set to " + dailyBudget, Toast.LENGTH_LONG).show();
                } catch (NumberFormatException e) {

                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DailyBudgetFragment.this.getDialog().cancel();
            }
        });

        return builder.create();
    }
}
