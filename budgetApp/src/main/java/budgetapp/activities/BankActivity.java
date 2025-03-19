package budgetapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Optional;

import java.util.List;

import budgetapp.banks.BankTransaction;
import budgetapp.banks.BankTransactionsResponse;
import budgetapp.fragments.AddInstallmentDialogFragment;
import budgetapp.fragments.ChooseCategoryBankTransactionFragment;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.util.BankTransactionEntry;
import budgetapp.util.BudgetAdapter;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.Installment;
import budgetapp.util.entries.BudgetEntry;
import budgetapp.util.money.Money;
import budgetapp.viewholders.BankTransactionViewHolder;
import budgetapp.views.BankView;

public class BankActivity extends FragmentActivity {

    private ListView transactionsListView;
    private BankView view;
    private BudgetModel model;
    private int chosenTransactionPosition;
    private BudgetAdapter listAdapter;
    private SharedPreferences settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = (BankView) View.inflate(this, R.layout.activity_banks, null);
        view.setViewListener(viewListener);
        model = new BudgetModel(getApplicationContext());

        model = new BudgetModel(this);

        setContentView(view);
        view.setModel(model);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        if(settings.getBoolean("rememberPersonalCode", false)) {
            EditText e = (EditText)findViewById(R.id.editTextBank);
            String personalCode = settings.getString("personalCode", "");
            e.setText(personalCode);
        }

        transactionsListView = (ListView) findViewById(R.id.listViewBankTransactions);
    }

    public BudgetModel getModel() {
        return model;
    }
    public void addTransaction(BudgetEntry entry) {
        model.queueTransaction(entry);
        model.processQueueItem();
        BankTransactionEntry bankTransaction = new BankTransactionEntry(entry.getDate(), entry.getValue(), entry.getCategory(), entry.getComment());
        model.addBankTransaction(bankTransaction);

        listAdapter.remove(chosenTransactionPosition);
        transactionsListView.setAdapter(listAdapter);

        Toast.makeText(this, "Added " + bankTransaction.getDescription() + " as " + bankTransaction.getCategory(), Toast.LENGTH_LONG).show();

    }

    private BankView.ViewListener viewListener = new BankView.ViewListener() {
        @Override
        public void transactionListItemClicked(BankTransactionViewHolder transaction, int positionInList) {
            DialogFragment newFragment = new ChooseCategoryBankTransactionFragment();
            Bundle transactionBunduru = new Bundle();
            List<String> categories = model.getCategoryNames();
            transactionBunduru.putStringArray("categories", categories.toArray(new String[categories.size()]));
            BankTransaction bt = transaction.getTransaction();
            chosenTransactionPosition = positionInList;
            BudgetEntry entry = new BudgetEntry(bt.getAmount(), bt.getDate(), "", bt.getDescription());
            transactionBunduru.putParcelable("entry", entry);
            newFragment.setArguments(transactionBunduru);

            newFragment.show(getSupportFragmentManager(), "choose_category");
        }

        @Override
        public void createInstallmentOfTransaction(BankTransactionViewHolder transaction, int positionInList) {
            DialogFragment newFragment = new AddInstallmentDialogFragment();
            Bundle transactionBunduru = new Bundle();
            BankTransaction bt = transaction.getTransaction();
            chosenTransactionPosition = positionInList;
            transactionBunduru.putString("description", bt.getDescription());
            transactionBunduru.putDouble("amount", bt.getAmount().get());
            newFragment.setArguments(transactionBunduru);
            newFragment.show(getSupportFragmentManager(), "add_installment");
        }
    };


    public void doIt(View v) {
        EditText e = (EditText)findViewById(R.id.editTextBank);
        Button b = (Button)v;
        SharedPreferences.Editor editor = settings.edit();
        String savedPersonalCode = settings.getBoolean("rememberPersonalCode", false) ? e.getText().toString() : "";
        editor.putString("personalCode", savedPersonalCode);
        editor.commit();

        new GetTransactionsTask().execute(e.getText().toString(), b.getText().toString());
    }

    private class GetTransactionsTask
            extends AsyncTask<String, Void, BankTransactionsResponse> {

        @Override
        protected BankTransactionsResponse doInBackground(String... params) {
            return new BankTransactionsResponse(Optional.of(""), List.of());
        }

        @Override
        protected void onPreExecute() {
            TextView t = (TextView)findViewById(R.id.textViewBank);
            EditText e = (EditText)findViewById(R.id.editTextBank);
            t.setText("Getting transactions for " + e.getText().toString().split("\\s+")[0] + "...");
        }

        @Override
        protected void onPostExecute(BankTransactionsResponse params) {
            TextView tv = (TextView)findViewById(R.id.textViewBank);
            if(params == null) {
                tv.setText("It done fucked up");
                return;
            }
            if(params.getErrorResponse().isPresent()) {
                tv.setText(params.getErrorResponse().get());
                return;
            }
            tv.setText("New transactions");
            listAdapter = new BudgetAdapter(getApplicationContext(), R.layout.listitem_banktransaction);
            List<BankTransaction> newTransactions = params.getTransactions();
            for (BankTransaction t : newTransactions) {
                if(!model.isBankTransactionProcessed(t)) {
                    listAdapter.add(new BankTransactionViewHolder(t));
                }
            }

            transactionsListView.setAdapter(listAdapter);

        }

    }

    public boolean addInstallment(Money totalValue, Money dailyPayment, String category, String comment, boolean active) {
        int flags = 0;
        if (!active) {
            flags = Installment.INSTALLMENT_PAUSED;
        }
        Installment installment = new Installment(totalValue, dailyPayment, BudgetFunctions.getDateString(),
                totalValue.subtract(dailyPayment), category, comment);

        installment.setFlags(flags);
        return model.addInstallment(installment);
    }
}
