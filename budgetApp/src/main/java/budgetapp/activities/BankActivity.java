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

import budgetapp.banks.swedbank.beans.OverviewResponse;
import budgetapp.banks.swedbank.beans.ProfileResponse;
import budgetapp.banks.swedbank.beans.Transaction;
import budgetapp.banks.swedbank.beans.TransactionsResponse;
import budgetapp.banks.swedbank.client.SwedbankClient;
import budgetapp.main.R;

public class BankActivity extends FragmentActivity {

    private ObjectMapper mapper = new ObjectMapper();
    private SwedbankClient client = new SwedbankClient();

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
            extends AsyncTask<String, Void, List<Transaction>> {


        @Override
        protected List<Transaction> doInBackground(String... params) {

            String personalCode = "/identification/personalcode";
            String profile = "/profile/";
            String authString = params[0];
            String birthDate = authString.split("\\s+")[0];
            String password = authString.split("\\s+")[1];
            try {

                StringEntity s = new StringEntity(
                        "{\"userId\": \""+birthDate+"\", \"useEasyLogin\": false, \"password\": \""+password + "\", \"generateEasyLoginId\": false}");

                client.post(personalCode, s);

                JSONObject prof = client.get(profile);
                ProfileResponse profil = mapper.readValue(prof.toString(), ProfileResponse.class);

                client.post(profile + profil.getBanks().get(0).getPrivateProfile().getId(), new StringEntity(""));

                JSONObject accounts = client.get("/engagement/overview");
                OverviewResponse or = mapper.readValue(accounts.toString(), OverviewResponse.class);

                String accountId = or.getTransactionAccounts().get(0).getId();

                JSONObject res = client.get("/engagement/transactions/" + accountId);
                TransactionsResponse t = mapper.readValue(res.toString(), TransactionsResponse.class);

                client.shutdown();
                return t.getTransactions();
            } catch (MalformedURLException e) {return null;

            } catch (IOException e) {return null;
            } catch (IllegalStateException e) {
                return null;
            } catch (JSONException e) {
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            TextView t = (TextView)findViewById(R.id.textViewBank);
            EditText e = (EditText)findViewById(R.id.editTextBank);
            t.setText("Getting transactions for " + e.getText().toString().split("\\s+")[0] + "...");

        }

        @Override
        protected void onPostExecute(List<Transaction> params) {
            TextView tv = (TextView)findViewById(R.id.textViewBank);
            if(params == null) {
                tv.setText("It done fucked up");
                return;
            }
            StringBuilder sb = new StringBuilder();
            for(Transaction t : params) {
                sb.append(t.getDate()).append(":\t").append(t.getAmount()).append("\t").append(t.getDescription()).append("\n");
            }
            tv.setText(sb.toString());
        }


    }
}
