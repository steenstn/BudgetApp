package budgetapp.views;


import java.util.List;

import budgetapp.models.BudgetModel;
import budgetapp.util.entries.CategoryEntry;
import budgetapp.util.graph.PieChartRenderer;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

public class PieChartView extends ImageView{

	private PieChartRenderer pieChartRenderer;
	private List<CategoryEntry> values;
	private double[] valueArray;
	private String[] legendArray;
	
	public PieChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		pieChartRenderer = new PieChartRenderer();
		
		BudgetModel model = new BudgetModel(context);
		values = model.getCategoriesSortedByValue();
		
		// Remove all positive values, we only want spendings
		for(int i = 0; i < values.size(); i++)
		{
			if(values.get(i).getValue().get()>0)
				values.remove(i);
		}
		
		valueArray = new double[values.size()];
		legendArray = new String[values.size()];
		
		for(int i = 0; i < values.size(); i++)
		{
			valueArray[i] = values.get(i).getValue().get();
			legendArray[i] = values.get(i).getCategory();
		}
	}
	
	@Override
	protected void onDraw(Canvas c) {
		super.onDraw(c);
		
		pieChartRenderer.drawGraph(c, 50, 50, 450, 450, valueArray, legendArray);
	}

}
