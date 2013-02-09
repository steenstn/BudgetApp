package budgetapp.graph;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import budgetapp.main.R;
import budgetapp.util.*;

public class GraphActivity extends Activity
{
	
	GraphView view;
	public List<BudgetEntry> entries;
	public String[] values;
	float[] x;// = {0, 30, 60, 90};
    float[] y;// = {200, 100, 350, 100};
    float offsetX = 0;
    float offsetY = 0;
    public BudgetDataSource datasource;
    
    LineGraphRenderer lineGraph;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        entries = new ArrayList<BudgetEntry>();
        
        view = new GraphView(this);

        
        datasource = new BudgetDataSource(this);
        datasource.open();
        
        entries = datasource.getAllTransactions(datasource.ASCENDING);
        x = new float[entries.size()];
        y = new float[entries.size()];
        values = new String[entries.size()];
        
    	
        for(int i=0;i<entries.size();i++)
        {
        	x[i] = i;
        	y[i] = entries.get(i).getValue();
        	values[i] = ""+entries.get(i).getValue();
        	//System.out.println("x["+i+"]: " + x[i] + "y["+i+"]: " + days.get(i).getValue());
        }
        lineGraph = new LineGraphRenderer(x,y,values);
        setContentView(view);
    }
    
};