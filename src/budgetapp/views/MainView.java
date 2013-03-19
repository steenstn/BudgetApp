package budgetapp.views;

import budgetapp.main.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainView extends LinearLayout{
	
	public static interface ViewListener
	{
		public void chooseCategory();
		public void favButtClick(Button id);
		public void favButtLongClick(Button id);
	}
	
	private TextView currentBudgetTextView;
	private EditText editBudgetEditText;
	private Button chooseCategoryButton;
	private Button favButt1,favButt2,favButt3;
	private TextView logLeftTextView;
	private TextView logRightTextView;
	
	private ViewListener viewListener;
	
	public void setViewListener(ViewListener viewListener)
	{
		this.viewListener = viewListener;
	}
	
	public MainView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}
	
	@Override
    protected void onFinishInflate()
    {
    	super.onFinishInflate();
    	
    	currentBudgetTextView = (TextView)findViewById(R.id.textViewCurrentBudget);
    	editBudgetEditText = (EditText)findViewById(R.id.editTextSubtract);
    	chooseCategoryButton = (Button)findViewById(R.id.button_choose_category);
    	favButt1 = (Button)findViewById(R.id.topCategoryButton1);
    	favButt2 = (Button)findViewById(R.id.topCategoryButton2);
    	favButt3 = (Button)findViewById(R.id.topCategoryButton3);
    	logLeftTextView = (TextView)findViewById(R.id.textViewLogLeft);
    	logRightTextView = (TextView)findViewById(R.id.textViewLogRight);
    	
    	
    	setUpListeners();
    	
    	
    	
    }
	
	private void setUpListeners()
	{
		
		chooseCategoryButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				viewListener.chooseCategory();
			}
		});
    	
    	favButt1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				viewListener.favButtClick(favButt1);
			}
		});
    	favButt1.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				viewListener.favButtLongClick(favButt1);
				return true;
			}
		});
    	
    	favButt2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				viewListener.favButtClick(favButt2);
			}
		});
    	favButt2.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				viewListener.favButtLongClick(favButt2);
				return true;
			}
		});
    	
    	favButt3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				viewListener.favButtClick(favButt3);
			}
		});
    	favButt3.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				viewListener.favButtLongClick(favButt3);
				return true;
			}
		});
	}
	

}
