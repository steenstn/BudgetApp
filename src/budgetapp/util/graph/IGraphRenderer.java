package budgetapp.util.graph;

import android.graphics.Canvas;

public interface IGraphRenderer {
	
	
	public void drawGraph(float x, float y, float xScale, float yScale, Canvas c);
	
	public void drawValues(float x, float y,float xScale, float yScale, Canvas c);
	
	public void drawGraphAndValues(float x, float y, float xScale, float yScale,String[] values, Canvas c);

}
