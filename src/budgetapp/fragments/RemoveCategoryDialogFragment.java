package budgetapp.fragments;
/**
 * Dialog fragment for removing a category
 */
import budgetapp.activities.CategoriesActivity;
import budgetapp.main.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RemoveCategoryDialogFragment extends DialogFragment {
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    final View view = inflater.inflate(R.layout.dialog_remove_category, null);
	    builder.setView(view);
	    EditText category = (EditText)view.findViewById(R.id.dialog_category_name);
	    category.setText(getArguments().getString("chosenCategory"));
	    final CategoriesActivity activity = ((CategoriesActivity) getActivity());
	    // Add action buttons
    	builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
    	   
            @Override
            public void onClick(DialogInterface dialog, int id) {
        	    EditText category = (EditText)view.findViewById(R.id.dialog_category_name);
        	    
        	    if(activity.removeCategory(category.getText().toString())==true)
        	    {
        		    Toast.makeText(view.getContext(), "Successfully removed "+ category.getText().toString() , Toast.LENGTH_LONG).show();
        	    }
        	    else
        		    Toast.makeText(view.getContext(), "Could not remove "+ category.getText().toString(), Toast.LENGTH_LONG).show();
        	    
        	    activity.updateList();
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                RemoveCategoryDialogFragment.this.getDialog().cancel();
            }
        });   
        	
	    return builder.create();
	}
}