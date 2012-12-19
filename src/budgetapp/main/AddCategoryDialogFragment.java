package budgetapp.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddCategoryDialogFragment extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    final View view = inflater.inflate(R.layout.dialog_add_category, null);
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(view);
	   // final EditText category=(EditText)inflater.inflate(R.layout.dialog_add_category, null).findViewById(R.id.dialog_category_name);

//category.setText("heeyeye");

	    // Add action buttons
	           builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
	        	   
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   //MainActivity.datasource.addCategory(category.toString());
	            	   EditText category = (EditText)view.findViewById(R.id.dialog_category_name);
	            	 // System.out.println("it is_ " + category.getText().toString());
	            	  MainActivity.datasource.addCategory(category.getText().toString());
	            	   //String new_category = (String)findViewById(R.id.dialog_category_name);
	            	  // Toast.makeText(getContext(), "The planet is", Toast.LENGTH_LONG).show();   
	               }
	           })
	           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   AddCategoryDialogFragment.this.getDialog().cancel();
	               }
	           });   
	          // System.out.println("itdd is_ " + category.getText().toString());
	    return builder.create();
	}
}