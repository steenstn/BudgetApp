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
/*
    public void setUp() {
        solo = new Solo(getInstrumentation(), getActivity());
        solo.unlockScreen();
        m = new MainActivityMethods(solo);
    }

    public void testAddTransaction() {
        double budgetBefore = m.getCurrentBudget();
        String dailyCashFlowBefore = m.getDailyCashflowTextView().getText().toString();
        m.makeTransactionWithChooseCategoryButton(1, "Entertainment");
        double budgetAfter = m.getCurrentBudget();
        String dailyCashFlowAfter = m.getDailyCashflowTextView().getText().toString();

        assertEquals("Current budget was not updated after making a transaction", budgetBefore - 1, budgetAfter);
        assertFalse("Daily cash flow was not updated after transaction", dailyCashFlowBefore.equals(dailyCashFlowAfter));
    }

    public void testFavButtLeft() {
        double budgetBefore = m.getCurrentBudget();
        m.makeTransactionWithFavButt(1, MainActivityMethods.FavButt.LEFT);

        double budgetAfter = m.getCurrentBudget();
        assertEquals("Current budget was not updated after making a transaction", budgetBefore - 1, budgetAfter);
    }

    public void testFavButtCenter() {
        double budgetBefore = m.getCurrentBudget();
        m.makeTransactionWithFavButt(1, MainActivityMethods.FavButt.CENTER);

        double budgetAfter = m.getCurrentBudget();
        assertEquals("Current budget was not updated after making a transaction", budgetBefore - 1, budgetAfter);
    }

    public void testFavButtRight() {
        double budgetBefore = m.getCurrentBudget();
        m.makeTransactionWithFavButt(1, MainActivityMethods.FavButt.RIGHT);

        double budgetAfter = m.getCurrentBudget();
        assertEquals("Current budget was not updated after making a transaction", budgetBefore - 1, budgetAfter);
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
*/
}
