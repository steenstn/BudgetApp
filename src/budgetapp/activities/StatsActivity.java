package budgetapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import budgetapp.fragments.EditTransactionDialogFragment;
import budgetapp.fragments.PieChartFragment;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.util.Event;
import budgetapp.util.entries.BudgetEntry;
import budgetapp.viewholders.StatEntryViewHolder;
import budgetapp.views.StatsView;

public class StatsActivity
    extends FragmentActivity {

    private StatsView theView;
    private BudgetModel model;

    public void removeTransactionEntry(BudgetEntry entry) {
        model.removeTransaction(entry);
    }

    public BudgetModel getModel() {
        return model;
    }

    public void editTransactionEntry(long id, BudgetEntry newEntry) {
        model.editTransaction(id, newEntry);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new BudgetModel(this);
        theView = (StatsView) View.inflate(this, R.layout.activity_stats, null);

        theView.setModel(this.model);
        setContentView(theView);
        long eventId = getIntent().getLongExtra("eventId", -1);

        if (eventId != -1) {
            Event activeEvent = model.getEvent(eventId);
            String eventName = activeEvent.getName();
            this.setTitle(this.getTitle() + " - " + eventName);
        }

        theView.setEventId(eventId);

        theView.setViewListener(viewListener);
        theView.update();

    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_stats, menu);
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        DialogFragment newFragment;

        int id = item.getItemId();
        if (id == R.id.menu_show_linegraph) {
            intent = new Intent(this, GraphActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_show_piechart) {
            newFragment = new PieChartFragment();
            newFragment.show(getSupportFragmentManager(), "show_piechart");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private StatsView.ViewListener viewListener = new StatsView.ViewListener() {

        @Override
        public void spinnerItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            Spinner yearSpinner = (Spinner) findViewById(R.id.spinnerYear);
            Spinner monthSpinner = (Spinner) findViewById(R.id.spinnerMonth);
            Spinner categorySpinner = (Spinner) findViewById(R.id.spinnerCategory);
            // Find what year, month and category that is chosen
            int selectedYear = yearSpinner.getSelectedItemPosition();
            int selectedMonth = monthSpinner.getSelectedItemPosition() - 1; // When there is a "All categories" entry. -1 is needed to get correct month
            String selectedCategory = (String) categorySpinner.getSelectedItem();

            theView.setSelectedYear(selectedYear);
            theView.setSelectedMonth(selectedMonth);
            theView.setSelectedCategory(selectedCategory);
        }

        @Override
        public void listViewLongClick(StatEntryViewHolder theEntry) {
            StatEntryViewHolder listItem = theEntry;
            if (listItem.getType() == StatEntryViewHolder.Type.entry) {
                DialogFragment newFragment = new EditTransactionDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("entry", (Parcelable) listItem.getEntry());

                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "edit_transaction");
            }

        }

        @Override
        public void listViewClick(StatEntryViewHolder theEntry) {
            if (!theEntry.getEntry().getComment().equalsIgnoreCase("")) {
                Toast.makeText(getBaseContext(), theEntry.getEntry().getComment(), Toast.LENGTH_LONG).show();
            }

        }
    };

}
