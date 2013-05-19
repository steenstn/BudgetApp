package budgetapp.views;


import budgetapp.activities.GraphActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
public class GraphView extends ImageView implements OnTouchListener{
	
	private static final int INVALID_POINTER_ID = -1;
	// The ‘active pointer’ is the one currently moving our object.
	private int mActivePointerId = INVALID_POINTER_ID;

	private Context mContext;
	GraphActivity host;
	
	Paint blackPaint;
	int sx;
	int sy;
	int pointerIndex = INVALID_POINTER_ID;
	int pointerIndex2 = INVALID_POINTER_ID;
    float offsetX;
    float offsetY;
    float oldX;
	float oldY;
	float oldX2, oldY2;
	float xScale = 50;
	float yScale = 0.1f;
	float oldDistanceX;
	float oldDistanceY;
	float originX;
	float originY;
	String[] values;
	
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
    	//blackPaint.setStrokeWidth(1.0f);
    	//blackPaint.setColor(Color.GRAY);

    	
    	
    	// Draw lines 
    	int numVerticalLines = (int) Math.ceil(screenWidth / xScale);
    	int numHorizontalLines = (int) Math.ceil(screenHeight / yScale);
    	for(float i = 0; i<numVerticalLines;i++)
    	{
    		float drawingX, drawingY;
    		// Loop around so that the lines are reused
    		drawingX = (offsetX+i*xScale)%(numVerticalLines*xScale);
    		drawingY = (-offsetY-i/(yScale/10.0f))%(numHorizontalLines*yScale);
    		
    		if(drawingX < 0.0f)
    		{
    			drawingX+=((float)(numVerticalLines)*xScale);
    		}
    		c.drawLine(drawingX, -offsetY, drawingX, -offsetY+10, blackPaint);
    		//c.drawLine(0, drawingY, 2*sx, drawingY, blackPaint);
    		
    		//c.drawLine(0, -offsetY-i/(yScale/10), 2*sx, -offsetY-i/(yScale/10), blackPaint);
    		
    	}
    	
    	blackPaint.setColor(Color.WHITE);
		blackPaint.setStyle(Style.STROKE);
		blackPaint.setStrokeWidth(1.0f);
		
		// We dont want to display all dates all the time
		// so scale the increment depending on xScale
		float scale = 100 / xScale;

    	for(int i = 0; i < host.lineGraph.legends.length; i+=1 + scale)
    	{
    		c.drawText(host.lineGraph.legends[i], offsetX+i*xScale, -offsetY+25.0f, blackPaint);
    	}
	}

	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		
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
				else if(numTouch == 2)
				{

					int id1 = event.getPointerId(0);
					int id2 = event.getPointerId(1);
					float x1 = event.getX(id1);
					float y1 = event.getY(id1);
					float x2 = event.getX(id2);
					float y2 = event.getY(id2);
					
					float dx1 = x1 - oldX;
					float dy1 = y1 - oldY;
					
					float dx2 = x2 - oldX2;
					float dy2 = y2 - oldY2;
					
					
					
					float distanceX =(x1-x2)/200.0f;
					float distanceY =(y1-y2)/20000.0f;
					
					float distdx = distanceX - oldDistanceX;
					float distdy = distanceY - oldDistanceY;
					
					xScale+=(dx1-dx2)/10.0f;
					yScale+=(dy1-dy2)/1000.0f;
					
					oldDistanceX = distanceX;
					oldDistanceY = distanceY;
					oldX = x1;
					oldY = y1;
					oldX2 = x2;
					oldY2 = y2;
					//System.out.println("Multitouch!");
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
		/*switch(event.getAction() & MotionEvent.ACTION_MASK)
		{
			case MotionEvent.ACTION_DOWN:
				oldX = (event.getX());
				oldY = (event.getY());
			break;
			
			case MotionEvent.ACTION_MOVE:
				offsetX+= event.getX() - oldX;
				offsetY-= event.getY() - oldY;
				oldX = (event.getX());
				oldY = (event.getY());
			break;
			
			case MotionEvent.ACTION_UP:
			
			break;
				
		}*/
		
		
		this.invalidate();
		return true;
	}

}
