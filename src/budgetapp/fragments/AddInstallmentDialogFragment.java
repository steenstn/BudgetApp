package budgetapp.fragments;
/**
 * Dialog Fragment for adding a new category
 * 
 */
import java.util.List;

import budgetapp.activities.InstallmentsActivity;
import budgetapp.main.R;
import budgetapp.util.Money;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

public class AddInstallmentDialogFragment extends DialogFragment {

	View view;
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final InstallmentsActivity activity = (InstallmentsActivity) getActivity();
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    // Get the layout inflater
	    LayoutInflater inflater = activity.getLayoutInflater();
	    // Inflate and set the layout for the dialog
	    view = inflater.inflate(R.layout.dialog_add_installment, null);
	 
	    builder.setView(view);
	 
	    // Add action buttons
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	   
    		@Override
            public void onClick(DialogInterface dialog, int id) {
    			
    			EditText categoryEditText = (EditText)view.findViewById(R.id.dialog_installment_category);
    			EditText totalValueEditText = (EditText)view.findViewById(R.id.dialog_installment_total_value);
    			EditText dailyPaymentEditText = (EditText)view.findViewById(R.id.dialog_installment_daily_payment);
    			EditText commentEditText = (EditText)view.findViewById(R.id.dialog_installment_comment);
    			
    			// Get all information an do error checking
    			try
    			{
    				String category = categoryEditText.getText().toString();
    				System.out.println(category);
    				if(category.equalsIgnoreCase(""))
    					throw new Exception("Must add category");
    				
    				double totalValue = Double.parseDouble(totalValueEditText.getText().toString());
    				if(totalValue <= 0)
    					throw new Exception("Invalid total value");
    				
    				totalValue *= -1;
    				
    				double dailyPayment = Double.parseDouble(dailyPaymentEditText.getText().toString());
    				
    				if(dailyPayment <= 0)
    					throw new Exception("Invalid daily payment");
    				
    				dailyPayment *= -1;
    				
    				if(dailyPayment < totalValue)
    					throw new Exception("dailyayment larger than total value");
    				
    				String comment = commentEditText.getText().toString();
    				activity.addInstallment(new Money(totalValue), new Money(dailyPayment), category, comment);
    				
    			}
    			catch(Exception e)
    			{
    				e.printStackTrace();
    			}
        	  }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AddInstallmentDialogFragment.this.getDialog().cancel();
            }
        });   
    	
	    return builder.create();
	}
	@Override
	public void onResume()
	{
		super.onResume();
		/*EditText category = (EditText)view.findViewById(R.id.dialog_other_category_name);
		String chosenCategory = ((MainActivity) getActivity()).getChosenCategory();
		if(chosenCategory.length()!=0)
		{	
		//	category.setText(chosenCategory);
		//	((MainActivity) getActivity()).setChosenCategory("");
			//EditText comment = (EditText)view.findViewById(R.id.dialog_other_category_comment);
		//	comment.requestFocus();
		}*/
		
	}
	
	/**
	 * Adds the category to the database if it does not exist
	 */
	/*private void addCategory(String theCategory)
	{
		List<String> allCategories = ((MainActivity) getActivity()).getCategoryNames();
    	boolean categoryExists = false;
    	for(int i = 0; i < allCategories.size(); i++)
    	{
    		if(allCategories.get(i).equalsIgnoreCase(theCategory))
    		{
    			categoryExists = true;
    			break;
    		}
    	}
    	
    	if(!categoryExists)
    	{
    		((MainActivity) getActivity()).addCategory(theCategory);
    	}
	}*/
}