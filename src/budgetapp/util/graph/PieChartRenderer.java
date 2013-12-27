package budgetapp.util.graph;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class PieChartRenderer {
	
	Paint currentPaint;
	Paint textPaint;
	public PieChartRenderer()
	{
		textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(18);
		currentPaint = new Paint();
		currentPaint.setColor(Color.GREEN);
		currentPaint.setAntiAlias(true);
	}
	
	public void drawGraph(Canvas c)
	{
		c.drawCircle(50, 50, 40, currentPaint);
		c.drawText("Aw yeah", 30, 30, textPaint);
	}
}
