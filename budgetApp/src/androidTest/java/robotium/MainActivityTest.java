package robotium;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.robotium.solo.Solo;

import budgetapp.activities.MainActivity;
import budgetapp.main.R;
import robotium.activities.MainActivityMethods;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;
    private MainActivityMethods m;
    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        m = new MainActivityMethods(solo);
    }

    public void testAddTransaction() {
        double budgetBefore = m.getCurrentBudget();
        m.makeTransactionWithChooseCategoryButton(1, "Entertainment");
        double budgetAfter = m.getCurrentBudget();

        assertEquals("Current budget was not updated after making a transaction", budgetBefore - 1, budgetAfter);
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
