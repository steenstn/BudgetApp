package budgetapp.views;

import budgetapp.util.IBudgetObserver;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

public class StatsView extends LinearLayout implements IBudgetObserver{

	public static interface ViewListener
	{
		public void chooseCategory();
		public void favButtClick(Button id);
		public void favButtLongClick(Button id);
	}
	
	public StatsView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
    protected void onFinishInflate()
    {
    	super.onFinishInflate();
    }

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
