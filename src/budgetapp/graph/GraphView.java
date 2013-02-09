package budgetapp.graph;

import java.util.List;

import budgetapp.util.BudgetDataSource;
import budgetapp.util.DayEntry;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Build;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
public class GraphView extends ImageView implements OnTouchListener{
	
	private static final int INVALID_POINTER_ID = -1;
	// The �active pointer� is the one currently moving our object.
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
	float xScale = 40;
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
		
		offsetX = 40.0f;
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
    	blackPaint.setStrokeWidth(1.0f);
    	blackPaint.setColor(Color.GRAY);
    	for(int i = 0; i<100;i++)
    	{
    		c.drawLine(offsetX+i*xScale, 0, offsetX+i*xScale, 2*sy, blackPaint);
    		
    		c.drawLine(0, -offsetY-i/(yScale/10), 2*sx, -offsetY-i/(yScale/10), blackPaint);
    		
    	}
	}

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.ECLAIR)
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
				oldDistanceX = (event.getX(pointerIndex)-event.getX(pointerIndex2));
				oldDistanceY = (event.getY(pointerIndex)-event.getY(pointerIndex2));
			break;
			
			case MotionEvent.ACTION_MOVE:
				 pointerIndex = event.findPointerIndex(mActivePointerId);
				float x = event.getX(pointerIndex);
				float y = event.getY(pointerIndex);
				
				
				float dx = x - oldX;
				float dy = y - oldY;
				
				offsetX+= dx;
				offsetY-= dy;
				oldX = x;
				oldY = y;
				if(pointerIndex2!=INVALID_POINTER_ID)
				{
					float distanceX =(x-event.getX(pointerIndex2))/200.0f;
					float distanceY =(y-event.getY(pointerIndex2))/20000.0f;
					
					float distdx = distanceX - oldDistanceX;
					float distdy = distanceY - oldDistanceY;
					
					xScale+=distdx/10.0f;
					yScale+=distdy/1000.0f;
					
					oldDistanceX = distanceX;
					oldDistanceY = distanceY;
					
				}
				//System.out.println("Index: " + pointerIndex);
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
