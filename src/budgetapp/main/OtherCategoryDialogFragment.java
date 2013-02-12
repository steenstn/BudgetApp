package budgetapp.main;
/**
 * Dialog Fragment for adding a new category
 * 
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;

public class OtherCategoryDialogFragment extends DialogFragment {

	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    // Inflate and set the layout for the dialog
	    final View view = inflater.inflate(R.layout.dialog_other_category, null);
	 
	    builder.setView(view);
	 
	    // Add action buttons
	           builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	        	   
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   EditText category = (EditText)view.findViewById(R.id.dialog_other_category_name);
	            	   EditText comment = (EditText)view.findViewById(R.id.dialog_other_category_comment);
	            	   
	            	   if(!category.getText().toString().equalsIgnoreCase(""))
	            		   ((MainActivity) getActivity()).subtractFromBudget(view,category.getText().toString(),comment.getText().toString()); 
	            	   else
	            		   Toast.makeText(view.getContext(), "Please enter category name" , Toast.LENGTH_LONG).show();
	               }
	           })
	           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   OtherCategoryDialogFragment.this.getDialog().cancel();
	               }
	           });   
	          // System.out.println("itdd is_ " + category.getText().toString());
	    return builder.create();
	}
}