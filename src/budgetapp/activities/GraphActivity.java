package budgetapp.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import budgetapp.models.BudgetModel;
import budgetapp.util.*;
import budgetapp.util.database.BudgetDataSource;
import budgetapp.util.graph.LineGraphRenderer;
import budgetapp.views.GraphView;

public class GraphActivity extends Activity
{
	
	GraphView view;
	public List<DayEntry> entries;
	public String[] values;
	float[] x;// = {0, 30, 60, 90};
    float[] y;// = {200, 100, 350, 100};
    float offsetX = 0;
    float offsetY = 0;
    private BudgetModel model;
    
    public LineGraphRenderer lineGraph;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        entries = new ArrayList<DayEntry>();
        
        view = new GraphView(this);

        
        model = new BudgetModel(this);
       // datasource.open();
        
        entries = model.getSomeDaysTotal(0,BudgetDataSource.ASCENDING);
        x = new float[entries.size()];
        y = new float[entries.size()];
        values = new String[entries.size()];
        
    	
        for(int i=0;i<entries.size();i++)
        {
        	x[i] = i;
        	y[i] = (float)entries.get(i).getValue().get();
        	values[i] = ""+entries.get(i).getValue();
        	//System.out.println("x["+i+"]: " + x[i] + "y["+i+"]: " + days.get(i).getValue());
        }
        lineGraph = new LineGraphRenderer(x,y,values);
        setContentView(view);
    }
    
};