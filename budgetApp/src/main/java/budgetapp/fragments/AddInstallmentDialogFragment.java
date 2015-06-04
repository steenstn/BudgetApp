package budgetapp.fragments;


import java.util.Calendar;
import java.util.List;

import budgetapp.activities.BankActivity;
import budgetapp.activities.InstallmentsActivity;
import budgetapp.main.R;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.money.MoneyFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;

public class AddInstallmentDialogFragment extends DialogFragment {

	View view;
	AutoCompleteTextView categoryEditText;
	EditText totalValueEditText;
	EditText dailyPaymentEditText;
	EditText commentEditText;
	DatePicker datePicker;
	Button getDateButton;
	CheckBox activeCheckBox;
	Activity activity;
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		activity = getActivity();
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
    			try {
    				String category = categoryEditText.getText().toString();
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
    					throw new Exception("daily payment larger than total value");

    				boolean active = activeCheckBox.isChecked();
    				String comment = commentEditText.getText().toString();
                    if(activity instanceof InstallmentsActivity) {
                        ((InstallmentsActivity)activity).addInstallment(MoneyFactory.createMoneyFromNewDouble(totalValue),
                                MoneyFactory.createMoneyFromNewDouble(dailyPayment), category, comment, active);
                    }else if(activity instanceof BankActivity) {
                        ((BankActivity)activity).addInstallment(MoneyFactory.createMoneyFromNewDouble(totalValue),
                                MoneyFactory.createMoneyFromNewDouble(dailyPayment), category, comment, active);
                    }
    			}
    			catch(Exception e) {
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
		

		categoryEditText = (AutoCompleteTextView)view.findViewById(R.id.dialog_installment_category);
		totalValueEditText = (EditText)view.findViewById(R.id.dialog_installment_total_value);
		dailyPaymentEditText = (EditText)view.findViewById(R.id.dialog_installment_daily_payment);
		commentEditText = (EditText)view.findViewById(R.id.dialog_installment_comment);
		getDateButton = (Button)view.findViewById(R.id.dialog_installment_get_date);
		datePicker = (DatePicker)view.findViewById(R.id.dialog_installment_date_picker);
		activeCheckBox = (CheckBox)view.findViewById(R.id.activeCheckBox);
		
		setUpAutoCompleteValues();
		setUpWatchers();
        populateFields();

	}

    private void populateFields() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            String description = bundle.getString("description");
            double amount = bundle.getDouble("amount", 0);

            totalValueEditText.setText("" + amount);
            commentEditText.setText(description);
        }
    }
	
	private void setUpAutoCompleteValues()
	{
        List<String> categories = null;
        if(activity instanceof InstallmentsActivity) {
            categories = ((InstallmentsActivity)activity).getModel().getCategoryNames();
        } else if(activity instanceof BankActivity) {
            categories = ((BankActivity)activity).getModel().getCategoryNames();
        }
		// Get a reference to the AutoCompleteTextView in the layout
    	
		
    	ArrayAdapter<String> adapter = 
        new ArrayAdapter<String>(activity.getBaseContext(), android.R.layout.simple_list_item_1, categories);
    	categoryEditText.setAdapter(adapter);
	}
	
	private void setUpWatchers()
	{
		datePicker.init(BudgetFunctions.getYear(), BudgetFunctions.getMonth(), BudgetFunctions.getDay(),
			new OnDateChangedListener(){

				@Override
				public void onDateChanged(DatePicker datePicker, int year,
						int month, int day) {
					Calendar today = Calendar.getInstance();
					
					Calendar date = Calendar.getInstance();
					date.set(year, month, day);
					long targetDateMillisecs = date.getTimeInMillis();
					long todayMillisecs = today.getTimeInMillis();
					
					int days = (int) Math.ceil(((targetDateMillisecs - todayMillisecs) / (1000 * 60 * 60 * 24)));
					try
					{
						double totalValue = Double.parseDouble(totalValueEditText.getText().toString());
						double dailyPay = totalValue / days;
						dailyPay = (double)Math.round(dailyPay * 100) / 100;
						String dailyPayString = String.valueOf(dailyPay);
						if(!dailyPayString.contains("-") && !dailyPayString.contains("E"))
							dailyPaymentEditText.setText(String.valueOf(dailyPay));
						else
							dailyPaymentEditText.setText("");
					}
					catch(NumberFormatException e)
					{
					
					}
				}
			});
		TextWatcher dailyPaymentWatcher = new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
				
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				updateDatePicker();
			}
			
		};
		
		getDateButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				updateDatePicker();
			}});
		
		totalValueEditText.addTextChangedListener(dailyPaymentWatcher);
	}
	
	private void updateDatePicker()
	{
		try
		{
			double totalValue = Double.parseDouble(totalValueEditText.getText().toString());
			double dailyPayment = Double.parseDouble(dailyPaymentEditText.getText().toString());
			
			
			int daysLeft = calculateDaysLeft(totalValue, dailyPayment);
			
			Calendar date = Calendar.getInstance();
			date.setLenient(true);
			date.add(Calendar.DATE, daysLeft);
			datePicker.updateDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		}
		catch(NumberFormatException e)
		{
			
		}
	}
	private int calculateDaysLeft(double totalValue, double dailyPayment)
	{
		return (int)Math.ceil(totalValue / dailyPayment);
	}
	
	
}