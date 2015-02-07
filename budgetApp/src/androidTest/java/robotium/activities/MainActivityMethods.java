package robotium.activities;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.robotium.solo.Solo;

import java.util.logging.Logger;

import budgetapp.main.R;

public class MainActivityMethods {

    Logger LOG = Logger.getLogger("MainActivityMethods");
    private Solo solo;
    public enum FavButt {LEFT, CENTER, RIGHT}

    public MainActivityMethods(Solo solo) {
        this.solo = solo;
    }

    public double getCurrentBudget() {
        TextView currentBudget = (TextView)solo.getView(R.id.textViewCurrentBudget);
        LOG.info("Current budget: " + currentBudget.getText());
        return Double.parseDouble(currentBudget.getText().toString().replace("kr", "").replace("−", "-"));
    }
    public void makeTransactionWithChooseCategoryButton(double amount, String category) {
        enterValueInEditTextSubtract(amount);
        solo.clickOnView(solo.getView(R.id.button_choose_category));
        ListView categories = (ListView)solo.getView(R.id.dialog_choose_category_listview);

        solo.clickInList(findIndexOfCategory(categories, category));
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void enterValueInEditTextSubtract(double amount) {
        AutoCompleteTextView edit = (AutoCompleteTextView)solo.getView(R.id.editTextSubtract);
        solo.enterText(edit, ""+amount);
        LOG.info("Entered " + amount + " into subtractEditText");
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

    public String makeTransactionWithFavButt(double amount, FavButt favButt) {
        enterValueInEditTextSubtract(amount);
        Button resultingButton = null;
        switch(favButt) {
            case LEFT:
                resultingButton = (Button)solo.getView(R.id.topCategoryButton2);
                break;
            case CENTER:
                resultingButton = (Button)solo.getView(R.id.topCategoryButton1);
                break;
            case RIGHT:
                resultingButton = (Button)solo.getView(R.id.topCategoryButton3);
                break;
            default:
                throw new IllegalArgumentException("Invalid " + favButt);
        }
        LOG.info("Pressing on FavButt " + resultingButton.getId() + "(" + resultingButton.getText() + ")");
        solo.clickOnView(resultingButton);
        return resultingButton.getText().toString();
    }
}
