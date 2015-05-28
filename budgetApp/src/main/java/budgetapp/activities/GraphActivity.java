package budgetapp.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import budgetapp.models.BudgetModel;
import budgetapp.util.database.BudgetDataSource;
import budgetapp.util.entries.DayEntry;
import budgetapp.util.graph.LineGraphRenderer;
import budgetapp.util.money.MoneyFactory;
import budgetapp.views.GraphView;

public class GraphActivity
    extends Activity {

    GraphView view;
    private List<DayEntry> entries;
    private String[] values;
    float[] x;// = {0, 30, 60, 90};
    float[] y;// = {200, 100, 350, 100};
    float offsetX = 0;
    float offsetY = 0;

    String[] legends;
    private BudgetModel model;

    public LineGraphRenderer lineGraph;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        entries = new ArrayList<DayEntry>();

        view = new GraphView(this);

        model = new BudgetModel(this);
        // datasource.open();

        entries = model.getSomeDays(0, BudgetDataSource.ASCENDING);
        x = new float[entries.size()];
        y = new float[entries.size()];
        values = new String[entries.size()];
        legends = new String[entries.size()];

        x[0] = 0;
        y[0] = (float) entries.get(0).getValue().get();
        values[0] = "" + entries.get(0).getValue();
        legends[0] = entries.get(0).getDate().substring(0,10);

        for (int i = 1; i < entries.size(); i++) {
            x[i] = i;
            y[i] = y[i-1] + (float) entries.get(i).getValue().get();
            values[i] = "" + (MoneyFactory.convertDoubleToMoney(y[i]));
            legends[i] = entries.get(i).getDate().substring(1,10);
        }
        lineGraph = new LineGraphRenderer(x, y, values, legends);

        setContentView(view);

        // Move the view to the end of the graph
        view.offsetX = -(entries.size() - 8) * view.xScale;

    }

};
