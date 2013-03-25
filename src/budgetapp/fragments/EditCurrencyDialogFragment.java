package budgetapp.fragments;
/**
 * Dialog Fragment for adding a new category
 * 
 */
import budgetapp.activities.MainActivity;
import budgetapp.main.R;
import budgetapp.main.R.id;
import budgetapp.main.R.layout;
import budgetapp.util.BudgetEntry;
import budgetapp.util.Money;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;

public class EditCurrencyDialogFragment extends DialogFragment {
	CheckBox checkBox;
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    // Inflate and set the layout for the dialog
	    final View view = inflater.inflate(R.layout.dialog_edit_currency, null);
	    
	    EditText edit = (EditText)view.findViewById(R.id.edit_currency_currency);
	    edit.setText(Money.currency());
	    
	    edit = (EditText)view.findViewById(R.id.edit_currency_exchangerate);
	    edit.setText(""+1);
	    edit.setEnabled(false);
	    edit.setVisibility(View.GONE);
	    checkBox = (CheckBox)view.findViewById(R.id.edit_currency_after_checkbox);
	    checkBox.setChecked(Money.after);
	    builder.setView(view);
	 
	    // Add action buttons
	           builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	        	   
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   EditText currency = (EditText)view.findViewById(R.id.edit_currency_currency);
	            	   Money.setCurrency(currency.getText().toString());
	            	   Money.after = checkBox.isChecked();
	            	   ((MainActivity) getActivity()).saveConfig();
	            	   ((MainActivity) getActivity()).updateView();
	            	   
	               }
	           })
	           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   EditCurrencyDialogFragment.this.getDialog().cancel();
	               }
	           });   
	    return builder.create();
	}
}