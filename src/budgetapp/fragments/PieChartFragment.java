package budgetapp.fragments;
/**
 * Dialog Fragment for adding a new category
 * 
 */
import budgetapp.main.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.DialogFragment;

public class PieChartFragment extends DialogFragment {

	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    // Inflate and set the layout for the dialog
	    final View view = inflater.inflate(R.layout.dialog_piechart, null);
	    
	    builder.setView(view);
	 
	    // Add action buttons
        builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
	        	   
        	@Override
	        public void onClick(DialogInterface dialog, int id) {
	            PieChartFragment.this.getDialog().cancel();
	        }
	    });
	           
	    return builder.create();
	}
}