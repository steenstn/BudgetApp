package budgetapp.util.graph;

import java.util.ArrayList;
import java.util.List;

import budgetapp.util.CategoryEntry;
import budgetapp.util.Money;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

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
		currentPaint.setTextSize(18);
	}
	
	public void drawGraph(Canvas c, float x, float y, float width, float height,  double[] values, String[] legends)
	{
		double sum = 0;
		for(int i =0; i < values.length; i++) {
			sum += values[i];
		}

		float startAngle = 0;
		float sweepAngle;
		for(int i = 0; i < values.length; i++) {
			
			currentPaint.setARGB(255, i*50, 100, 255-i*20);
			sweepAngle = calculateSweepingAngle(values[i],sum);
			c.drawArc(new RectF(x,y,width,height), startAngle, sweepAngle, true, currentPaint);

			c.drawText(legends[i], x+width, i*20, currentPaint);
			startAngle += sweepAngle;
			
		}
	}
	
	private float calculateSweepingAngle(double value, double total)
	{
		return (float)((value/total) * 360);
	}
}
