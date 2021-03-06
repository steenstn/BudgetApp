package budgetapp.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import budgetapp.fragments.AddInstallmentDialogFragment;
import budgetapp.fragments.EditInstallmentDialogFragment;
import budgetapp.fragments.RemoveInstallmentDialogFragment;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.Installment;
import budgetapp.util.money.Money;
import budgetapp.viewholders.InstallmentViewHolder;
import budgetapp.views.InstallmentsView;

public class InstallmentsActivity
    extends FragmentActivity {

    private InstallmentsView view;
    private BudgetModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = (InstallmentsView) View.inflate(this, R.layout.activity_installments, null);
        view.setViewListener(viewListener);

        setContentView(view);
        model = new BudgetModel(this);
        view.setModel(model);
        view.update();
    }

    public boolean addInstallment(Money totalValue, Money dailyPayment, String category, String comment, boolean active) {
        int flags = 0;
        if (!active) {
            flags = Installment.INSTALLMENT_PAUSED;
        }
        Installment installment = new Installment(totalValue, dailyPayment, BudgetFunctions.getDateString(),
            totalValue.subtract(dailyPayment), category, comment);

        installment.setFlags(flags);
        if (model.addInstallment(installment)) {
            return true;
        } else {
            return false;
        }
    }

    public BudgetModel getModel() {
        return model;
    }

    public boolean removeInstallment(long id) {
        return model.removeInstallment(id);
    }

    private InstallmentsView.ViewListener viewListener = new InstallmentsView.ViewListener() {

        @Override
        public void showInstallmentDialog() {
            DialogFragment newFragment = new AddInstallmentDialogFragment();
            newFragment.show(getSupportFragmentManager(), "add_installment");
        }

        @Override
        public void listViewClick(InstallmentViewHolder listItem) {
            Bundle bundle = new Bundle();
            bundle.putLong("id", listItem.getEntry().getId());
            DialogFragment newFragment = new EditInstallmentDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "edit_installment");
        }

        @Override
        public void listViewLongClick(InstallmentViewHolder listItem) {
            Bundle bundle = new Bundle();
            bundle.putLong("id", listItem.getEntry().getId());
            DialogFragment newFragment = new RemoveInstallmentDialogFragment();
            newFragment.setArguments(bundle);
            newFragment.show(getSupportFragmentManager(), "delete_installment");
        }

    };
}
