package budgetapp.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import budgetapp.banks.BankTransaction;
import budgetapp.banks.BankTransactionsResponse;
import budgetapp.banks.swedbank.SwedbankService;
import budgetapp.banks.swedbank.beans.OverviewResponse;
import budgetapp.banks.swedbank.beans.ProfileResponse;
import budgetapp.banks.swedbank.beans.Transaction;
import budgetapp.banks.swedbank.beans.TransactionsResponse;
import budgetapp.banks.swedbank.client.SwedbankClient;
import budgetapp.main.R;
import budgetapp.util.BudgetAdapter;
import budgetapp.util.Event;
import budgetapp.viewholders.BankTransactionViewHolder;
import budgetapp.viewholders.EventViewHolder;

public class BankActivity extends FragmentActivity {

    private SwedbankService swedbankService = new SwedbankService();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banks);

    }
    public void doIt(View v) {
        EditText e = (EditText)findViewById(R.id.editTextBank);
        Button b = (Button)v;

        new JsonTask().execute(e.getText().toString(), b.getText().toString());

    }

    private class JsonTask
            extends AsyncTask<String, Void, BankTransactionsResponse> {

        @Override
        protected BankTransactionsResponse doInBackground(String... params) {
            BankTransactionsResponse response = swedbankService.getTransactions(params[0]);
            return response;
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

            BudgetAdapter listAdapter = new BudgetAdapter(getApplicationContext(), R.layout.listitem_banktransaction);
            for (BankTransaction t : params.getTransactions()) {
                listAdapter.add(new BankTransactionViewHolder(t));
            }
            ListView transactionsListView = (ListView) findViewById(R.id.listViewBankTransactions);
            transactionsListView.setAdapter(listAdapter);
           /* StringBuilder sb = new StringBuilder();
            for(BankTransaction t : params.getTransactions()) {
                sb.append(t.getDate()).append(":\t").append(t.getAmount()).append("\t").append(t.getDescription()).append("\n");
            }
            tv.setText(sb.toString());*/
        }


    }
}
