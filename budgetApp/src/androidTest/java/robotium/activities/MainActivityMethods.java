package robotium.activities;

import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.robotium.solo.Solo;

import budgetapp.main.R;

public class MainActivityMethods {

    private Solo solo;

    public MainActivityMethods(Solo solo) {
        this.solo = solo;
    }

    public double getCurrentBudget() {
        TextView currentBudget = (TextView)solo.getView(R.id.textViewCurrentBudget);
        return Double.parseDouble(currentBudget.getText().toString().replace("kr", "").replace("−", "-"));
    }
    public void makeTransactionWithChooseCategoryButton(double amount, String category) {
        AutoCompleteTextView edit = (AutoCompleteTextView)solo.getView(R.id.editTextSubtract);
        solo.enterText(edit, ""+amount);
        solo.clickOnView(solo.getView(R.id.button_choose_category));
        ListView categories = (ListView)solo.getView(R.id.dialog_choose_category_listview);

        solo.clickInList(findIndexOfCategory(categories, category));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private int findIndexOfCategory(ListView listView, String category) {
        ListAdapter adapter = listView.getAdapter();
        for(int i = 0; i <= adapter.getCount(); i++) {

            if(category.equalsIgnoreCase(adapter.getItem(i).toString())) {
                return i+1;
            }
        }
        throw new RuntimeException("Could not find category " + category + " in category list");
    }
}
