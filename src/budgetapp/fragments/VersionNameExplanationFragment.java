package budgetapp.fragments;

import budgetapp.main.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public class VersionNameExplanationFragment extends DialogFragment {
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    final View view = inflater.inflate(R.layout.dialog_version_name_explanation, null);
	    builder.setView(view);
	    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	   
            @Override
            public void onClick(DialogInterface dialog, int id) {
                VersionNameExplanationFragment.this.getDialog().cancel();
            }
        });   
        	
	    return builder.create();
	}
}