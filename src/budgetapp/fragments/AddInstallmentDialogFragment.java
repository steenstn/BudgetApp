package budgetapp.fragments;
/**
 * Dialog Fragment for adding a new category
 * 
 */
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import budgetapp.activities.InstallmentsActivity;
import budgetapp.main.R;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.Money;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;

public class AddInstallmentDialogFragment extends DialogFragment {

	View view;
	EditText categoryEditText;
	EditText totalValueEditText;
	EditText dailyPaymentEditText;
	EditText commentEditText;
	DatePicker datePicker;
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
		

		categoryEditText = (EditText)view.findViewById(R.id.dialog_installment_category);
		totalValueEditText = (EditText)view.findViewById(R.id.dialog_installment_total_value);
		dailyPaymentEditText = (EditText)view.findViewById(R.id.dialog_installment_daily_payment);
		commentEditText = (EditText)view.findViewById(R.id.dialog_installment_comment);
		
		
		setUpWatchers();		
	}
	
	private void setUpWatchers()
	{
		datePicker = (DatePicker)view.findViewById(R.id.dialog_installment_date_picker);
		datePicker.init(BudgetFunctions.getYear(), BudgetFunctions.getMonth(), BudgetFunctions.getDay(),
			new OnDateChangedListener(){

				@Override
				public void onDateChanged(DatePicker datePicker, int year,
						int month, int day) {
					
				}
			});
		TextWatcher dailyPaymentWatcher = new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try
				{
					double totalValue = Double.parseDouble(totalValueEditText.getText().toString());
					double dailyPayment = Double.parseDouble(dailyPaymentEditText.getText().toString());
				
					int daysLeft = calculateDaysLeft(totalValue, dailyPayment);
					
					Calendar today = Calendar.getInstance();
					long todayInMillisecs = today.getTimeInMillis();
					long endTimeInMillisecs = todayInMillisecs + (daysLeft * 1000 * 60 * 60 * 24);
					Calendar endDate = Calendar.getInstance();
					endDate.setTimeInMillis(endTimeInMillisecs);
					
					datePicker.updateDate(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));
				}
				catch(NumberFormatException e)
				{
					
				}
			}
			
		};
		
		totalValueEditText.addTextChangedListener(dailyPaymentWatcher);
		dailyPaymentEditText.addTextChangedListener(dailyPaymentWatcher);
		
		
	}
	
	private int calculateDaysLeft(double totalValue, double dailyPayment)
	{
		return (int)Math.ceil(totalValue / dailyPayment);
	}
	
	
}