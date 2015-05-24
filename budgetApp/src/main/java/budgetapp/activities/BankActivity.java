package budgetapp.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import budgetapp.banks.BankTransaction;
import budgetapp.banks.BankTransactionsResponse;
import budgetapp.banks.swedbank.SwedbankService;
import budgetapp.main.R;
import budgetapp.util.BudgetAdapter;
import budgetapp.viewholders.BankTransactionViewHolder;

public class BankActivity extends FragmentActivity {

    private SwedbankService swedbankService = new SwedbankService();
    private ListView transactionsListView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banks);

        transactionsListView = (ListView) findViewById(R.id.listViewBankTransactions);
    }

    private void setUpListeners() {
        transactionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    public void doIt(View v) {
        EditText e = (EditText)findViewById(R.id.editTextBank);
        Button b = (Button)v;

        new GetTransactionsTask().execute(e.getText().toString(), b.getText().toString());

    }

    private class GetTransactionsTask
            extends AsyncTask<String, Void, BankTransactionsResponse> {

        @Override
        protected BankTransactionsResponse doInBackground(String... params) {
            return swedbankService.getTransactions(params[0]);
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
            tv.setText("Transactions");
            BudgetAdapter listAdapter = new BudgetAdapter(getApplicationContext(), R.layout.listitem_banktransaction);
            for (BankTransaction t : params.getTransactions()) {
                listAdapter.add(new BankTransactionViewHolder(t));
            }
            transactionsListView.setAdapter(listAdapter);

        }


    }
}
