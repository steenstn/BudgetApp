package budgetapp.fragments;
/**
 * Dialog Fragment for adding a new category
 * 
 */
import budgetapp.activities.MainActivity;
import budgetapp.main.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;

public class AddCategoryDialogFragment extends DialogFragment {

	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
    
		final View view = inflater.inflate(R.layout.dialog_add_category, null);
		final MainActivity activity = (MainActivity)getActivity();
    
		builder.setView(view);
 
		builder.setPositiveButton("Add", new DialogInterface.OnClickListener() 
		{   
			@Override
			public void onClick(DialogInterface dialog, int id) 
			{
				EditText category = (EditText)view.findViewById(R.id.dialog_category_name);
				if(activity.addCategory(category.getText().toString())==true)
				{
				    Toast.makeText(view.getContext(), "Successfully added "+ category.getText().toString() , Toast.LENGTH_LONG).show();
				}
				else
					Toast.makeText(view.getContext(), "Failed to add "+ category.getText().toString() , Toast.LENGTH_LONG).show();
			}
		})
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
        {
		    public void onClick(DialogInterface dialog, int id) 
		    {
		        AddCategoryDialogFragment.this.getDialog().cancel();
		    }
        });   

    return builder.create();
	}
}