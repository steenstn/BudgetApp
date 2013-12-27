package budgetapp.views;

import budgetapp.util.graph.PieChartRenderer;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

public class PieChartView extends ImageView{

	private PieChartRenderer pieChartRenderer;
	
	public PieChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		pieChartRenderer = new PieChartRenderer();
	}
	
	@Override
	protected void onDraw(Canvas c) {
		super.onDraw(c);
		pieChartRenderer.drawGraph(c);
	}

}
