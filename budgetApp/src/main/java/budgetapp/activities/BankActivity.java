package budgetapp.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
            extends AsyncTask<String, Void, List<BankTransaction>> {

        @Override
        protected List<BankTransaction> doInBackground(String... params) {
            BankTransactionsResponse response = swedbankService.getTransactions(params[0]);
            return response.getTransactions();
        }

        @Override
        protected void onPreExecute() {
            TextView t = (TextView)findViewById(R.id.textViewBank);
            EditText e = (EditText)findViewById(R.id.editTextBank);
            t.setText("Getting transactions for " + e.getText().toString().split("\\s+")[0] + "...");

        }

        @Override
        protected void onPostExecute(List<BankTransaction> params) {
            TextView tv = (TextView)findViewById(R.id.textViewBank);
            if(params == null) {
                tv.setText("It done fucked up");
                return;
            }
            StringBuilder sb = new StringBuilder();
            for(BankTransaction t : params) {
                sb.append(t.getDate()).append(":\t").append(t.getAmount()).append("\t").append(t.getDescription()).append("\n");
            }
            tv.setText(sb.toString());
        }


    }
}
