package budgetapp.views;


import budgetapp.activities.GraphActivity;
import budgetapp.models.BudgetModel;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
public class GraphView extends ImageView implements OnTouchListener, OnScaleGestureListener{
	
	private static final int INVALID_POINTER_ID = -1;
	// The ‘active pointer’ is the one currently moving our object.
	private int mActivePointerId = INVALID_POINTER_ID;

	private Context mContext;
	GraphActivity host;
	ScaleGestureDetector scaleDetector = new ScaleGestureDetector(getContext(), this);
	
	Paint blackPaint;
	int sx;
	int sy;
	int pointerIndex = INVALID_POINTER_ID;
	int pointerIndex2 = INVALID_POINTER_ID;
    public float offsetX;
    public float offsetY;
    // Old offset to be able to zoom in without moving around
    float oldOffsetX;
    float oldOffsetY;
    float oldX;
	float oldY;
	float oldX2, oldY2;
	public float xScale = 50;
	float oldDistanceX;
	float oldDistanceY;
	float originX;
	float originY;
	String[] values;
	float globalScale = 100.0f;
	// Number of pixels to scale maximum
	float xScaleMax = 200.0f;
	float xScaleMin = 20.0f;
	
	BudgetModel model = new BudgetModel(this.getContext());
	public float yScale = (float) (0.2f * 50/model.getDailyBudget().get());
	
	// Fix this
	float yScaleMin = (float) (0.05f * 50/model.getDailyBudget().get());
	float yScaleMax = (float) (0.3f * 50/model.getDailyBudget().get());
	
	
	@SuppressWarnings("deprecation")
	public GraphView(Context context) {
		super(context);
		mContext = context;
		host = (GraphActivity) this.getContext();
		Display display = host.getWindowManager().getDefaultDisplay();
		//values = new String[host.values.length];
		//values = host.values;
		
		sx = display.getWidth();
		sy = display.getHeight();
		offsetX = 0.0f;
		offsetY = -sy/2;
		originX = offsetX;
		originY = offsetY;
		oldX=offsetX;
		oldY=offsetY;
		blackPaint = new Paint();
		blackPaint.setColor(Color.BLACK);
		
		
        setOnTouchListener(this);

	}
	
	protected void onDraw(Canvas c) {
		blackPaint.setColor(Color.BLACK);
		if(getResources().getConfiguration().orientation==1)
		{
	    	drawBackground(offsetX,offsetY,sx,sy,c);
		}
		else
		{
			drawBackground(offsetX,offsetY,sy,sx,c);
		}
		host.lineGraph.drawBackground(offsetX,offsetY,sx,sy,c);
		host.lineGraph.drawGraph(offsetX, offsetY, xScale, yScale, c);
		host.lineGraph.drawValues(offsetX, offsetY, xScale, yScale, c);
		
	}
	
	/**
	 * Clears the screen, draws the background (lines and  dates)
	 * @param offsetX
	 * @param offsetY
	 * @param screenWidth
	 * @param screenHeight
	 * @param c
	 */
	public void drawBackground(float offsetX,float offsetY,int screenWidth,int screenHeight,Canvas c)
	{
		blackPaint.setColor(Color.BLACK);
		blackPaint.setStyle(Style.FILL);
		c.drawRect(0, 0, 2*sx, 2*sy, blackPaint);
		
		blackPaint.setColor(Color.WHITE);
		blackPaint.setStyle(Style.STROKE);
		blackPaint.setStrokeWidth(3.0f);
		
    	c.drawLine(offsetX, 0, offsetX, 2*sx, blackPaint);
    	c.drawLine(0, -offsetY, 2*sy, -offsetY, blackPaint);
    	    	
    	
    	// Draw lines on the bottom of the graph
    	// This draws only the amount of lines necessary to fill the screen
    	int numVerticalLines = (int) Math.ceil(screenWidth / xScale);
    	for(float i = 0; i<numVerticalLines;i++)
    	{
    		float drawingX;
    		
    		// Loop around so that the lines are reused
    		drawingX = (offsetX+i*xScale)%(numVerticalLines*xScale);
    		
    		if(drawingX < 0.0f) // Loop around the screen
    		{
    			drawingX+=((float)(numVerticalLines)*xScale);
    		}
    		c.drawLine(drawingX, -offsetY, drawingX, -offsetY+10, blackPaint);
    		
    	}
    	
    	blackPaint.setColor(Color.WHITE);
		blackPaint.setStyle(Style.STROKE);
		blackPaint.setStrokeWidth(1.0f);
		
		// We dont want to display all dates all the time
		// so scale the increment depending on xScale
		float scale = globalScale / xScale;

    	for(int i = 0; i < host.lineGraph.legends.length; i+= 1 + scale)
    	{
    		c.drawText(host.lineGraph.legends[i], offsetX+i*xScale, -offsetY+25.0f, blackPaint);
    	}
	}

	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		scaleDetector.onTouchEvent(event);
		
		
		switch(event.getAction() & MotionEvent.ACTION_MASK)
		{
			case MotionEvent.ACTION_DOWN:
				oldX = (event.getX());
				oldY = (event.getY());
				mActivePointerId = event.getPointerId(0);
			break;
			
			case MotionEvent.ACTION_POINTER_DOWN:
				pointerIndex2 = event.getActionIndex();
				oldX2 = (event.getX(pointerIndex2));
				oldY2 = (event.getY(pointerIndex2));
			break;
			
			case MotionEvent.ACTION_MOVE:
				int numTouch = event.getPointerCount();
				if(numTouch == 1)
				{
					pointerIndex = event.findPointerIndex(mActivePointerId);
					
					float x = event.getX(pointerIndex);
					float y = event.getY(pointerIndex);
					
					
					float dx = x - oldX;
					float dy = y - oldY;
					
					offsetX+= dx;
					offsetY-= dy;
					oldX = x;
					oldY = y;
					
				}
			break;
			
			case MotionEvent.ACTION_UP:
				mActivePointerId = INVALID_POINTER_ID;
			break;
			
			case MotionEvent.ACTION_CANCEL: {
		        mActivePointerId = INVALID_POINTER_ID;
		        break;
		    }
			case MotionEvent.ACTION_POINTER_UP: {
		        // Extract the index of the pointer that left the touch sensor
		        pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) 
		                >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		        final int pointerId = event.getPointerId(pointerIndex);
		        if (pointerId == mActivePointerId) {
		            // This was our active pointer going up. Choose a new
		            // active pointer and adjust accordingly.
		            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
		            oldX = event.getX(newPointerIndex);
		            oldY = event.getY(newPointerIndex);
		            mActivePointerId = event.getPointerId(newPointerIndex);
		        }
		        pointerIndex2 = INVALID_POINTER_ID;
		        break;
		    }
		
		}
		
		this.invalidate();
		return true;
	}
	


	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		
		//Change the xScale and yScale to zoom
		
		xScale += (detector.getCurrentSpan() - detector.getPreviousSpan()) / 10.0f;
		yScale += (detector.getCurrentSpan() - detector.getPreviousSpan()) / 2000.0f;
		
		xScale = clamp(xScale, xScaleMin, xScaleMax);
		yScale = clamp(yScale, yScaleMin, yScaleMax);
		
		offsetX = oldOffsetX * xScale;
		return true;
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		oldOffsetX = offsetX / xScale;
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
		
	}
	
	/**
	 * Clamp a value between two values
	 * @param x - The value to clamp
	 * @param a - Lower limit
	 * @param b - Upper limit
	 * @return - Clamped value between a and b
	 */
	private float clamp(float x, float a, float b)
	{
		if(x < a)
			x = a;
		if(x > b)
			x = b;
		
		return x;
	}
}
