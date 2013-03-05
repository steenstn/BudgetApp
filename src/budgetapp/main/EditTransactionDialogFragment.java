package budgetapp.main;
/**
 * Dialog Fragment for adding a new category
 * 
 */
import budgetapp.util.BudgetEntry;
import budgetapp.util.Money;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;

public class EditTransactionDialogFragment extends DialogFragment {

	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    // Inflate and set the layout for the dialog
	    final View view = inflater.inflate(R.layout.dialog_edit_transaction, null);
	    
	     long theId = getArguments().getLong("id");
	     final BudgetEntry theEntry = new BudgetEntry(theId, new Money(), "", "");
	    builder.setView(view);
	 
	    // Add action buttons
	           builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
	        	   
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   EditText comment = (EditText)view.findViewById(R.id.dialog_add_comment);
	            	   if(MainActivity.datasource.addComment(theEntry, comment.getText().toString())==true)
	            	   {
	            		   Toast.makeText(view.getContext(), "Successfully added "+ comment.getText().toString() , Toast.LENGTH_LONG).show();
	            		   
	            	   }
	            		   else
	            		   Toast.makeText(view.getContext(), "Failed to add "+ comment.getText().toString() , Toast.LENGTH_LONG).show();
	            	      
	               }
	           })
	           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   EditTransactionDialogFragment.this.getDialog().cancel();
	               }
	           });   
	    return builder.create();
	}
}