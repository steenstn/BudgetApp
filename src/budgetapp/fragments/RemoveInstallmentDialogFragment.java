package budgetapp.fragments;
/**
 * Dialog fragment for removing a category
 */
import budgetapp.activities.InstallmentsActivity;
import budgetapp.main.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class RemoveInstallmentDialogFragment extends DialogFragment {
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    final View view = inflater.inflate(R.layout.dialog_remove_category, null);
	    builder.setView(view);
	    TextView category = (TextView)view.findViewById(R.id.dialog_category_name);
	    category.setText("Remove installment?");
	    
	    Bundle bundle = this.getArguments();
	    final long installmentId = bundle.getLong("id", -1);
	    
	    final InstallmentsActivity activity = ((InstallmentsActivity) getActivity());
	    // Add action buttons
    	builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
    	   
            @Override
            public void onClick(DialogInterface dialog, int id) {
            	
        	    activity.removeInstallment(installmentId);
        	    
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                RemoveInstallmentDialogFragment.this.getDialog().cancel();
            }
        });   
        	
	    return builder.create();
	}
}