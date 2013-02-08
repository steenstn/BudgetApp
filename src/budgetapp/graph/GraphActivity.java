package budgetapp.graph;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import budgetapp.util.*;

public class GraphActivity extends Activity
{
	BudgetDataSource datasource;
	List<DayEntry> days;
	
    LineGraphRenderer drawView;
    float[] x;// = {0, 30, 60, 90};
    float[] y;// = {200, 100, 350, 100};
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  days = new List<DayEntry>();
        datasource = new BudgetDataSource(this);
        datasource.open();
        days = datasource.getAllDaysTotal();
        x = new float[days.size()];
        y = new float[days.size()];
    	float xScale = 40.0f;
    	float yScale = 0.10f;
        
    	
        for(int i=days.size()-1;i>=0;i--)
        {
        	
        	x[i] = i*xScale;
        	y[i] = days.get(i).getValue()*yScale;
        	
        }
        drawView = new LineGraphRenderer(this,x,y);
        drawView.setBackgroundColor(Color.BLACK);
        setContentView(drawView);
        drawView.drawGraph(0,0,0,0);

    }
};