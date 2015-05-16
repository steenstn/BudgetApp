package budgetapp.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;

import budgetapp.banks.beans.swedbank.OverviewResponse;
import budgetapp.banks.beans.swedbank.ProfileResponse;
import budgetapp.banks.beans.swedbank.Transaction;
import budgetapp.banks.beans.swedbank.TransactionsResponse;
import budgetapp.main.R;

public class BankActivity extends FragmentActivity {

    CookieStore cs = new BasicCookieStore();
    String baseUrl = "https://auth.api.swedbank.se/TDE_DAP_Portal_REST_WEB/api/v2";
    DefaultHttpClient httpClient = new DefaultHttpClient();
    ObjectMapper mapper = new ObjectMapper();

    private  HttpPost createPostRequest(String urlEndPoint, HttpEntity entity) {
        String dsId = dsId();
        HttpPost request = new HttpPost(baseUrl + urlEndPoint + dsIdUrl(dsId));
        request.setHeader("Authorization", getAuth());

        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/json");

        request.setEntity(entity);
        addCookie(cs, dsId);
        return request;
    }

    private HttpGet createGetRequest(String urlEndPoint) {
        String dsId = dsId();
        HttpGet request = new HttpGet(baseUrl + urlEndPoint + dsIdUrl(dsId));
        request.setHeader("Authorization", getAuth());
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/json");

        addCookie(cs, dsId);
        return request;
    }

    private static void addCookie(CookieStore cs, String dsId) {
        BasicClientCookie cookie2 = new BasicClientCookie("dsid", dsId);
        cookie2.setDomain(".api.swedbank.se");
        cookie2.setPath("/");
        cookie2.setVersion(0);
        cs.addCookie(cookie2);
    }

    public JSONObject call(HttpUriRequest request) throws IOException,
            IllegalStateException, JSONException {
        System.out.println("Calling headers");
        for (Header h : request.getAllHeaders()) {
            System.out.println(h.getName() + " " + h.getValue());
        }
        System.out.println("Calling " + request.getURI().toString());
        HttpResponse response = httpClient.execute(request);

        System.out.println(response.toString());
        System.out.println("Response headers");
        for (Header h : response.getAllHeaders()) {
            System.out.println(h);
        }
        return printResult(response);
    }
    private String getAuth() {
        String uuid = UUID.randomUUID().toString();
        String auth = "HithYAGrzi8fu73j:" + uuid;
        try {
            return new String(Base64.encode(auth.getBytes("UTF-8"), Base64.NO_WRAP));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String dsIdUrl(String dsId) {
        return "?dsid=" + dsId;
    }

    private  String dsId() {
        return RandomStringUtils.randomAlphabetic(4).toLowerCase()
                + RandomStringUtils.randomAlphabetic(4).toUpperCase();
    }

    private JSONObject printResult(HttpResponse response) throws IllegalStateException, IOException, JSONException {

        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
        String output = "";
        JSONObject json = null;
        StringBuilder sb = new StringBuilder();
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
            sb.append(output).append("\n");
        }
        return new JSONObject(sb.toString());
    }
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

                httpClient.setCookieStore(cs);
                StringEntity s = new StringEntity(
                        "{\"userId\": \""+birthDate+"\", \"useEasyLogin\": false, \"password\": \""+password + "\", \"generateEasyLoginId\": false}");

                HttpPost loginRequest = createPostRequest(personalCode, s);
                call(loginRequest);

                HttpGet profileRequest = createGetRequest(profile);
                JSONObject prof = call(profileRequest);
                ProfileResponse profil = mapper.readValue(prof.toString(), ProfileResponse.class);
                HttpPost profile2Request = createPostRequest(profile + profil.getBanks().get(0).getPrivateProfile().getId(), new StringEntity(""));
                call(profile2Request);

                HttpGet accountsReq = createGetRequest("/engagement/overview");
                JSONObject accounts = call(accountsReq);
                OverviewResponse or = mapper.readValue(accounts.toString(), OverviewResponse.class);

                String accountId = or.getTransactionAccounts().get(0).getId();
                HttpGet transactions = createGetRequest("/engagement/transactions/" + accountId);
                JSONObject res = call(transactions);
                TransactionsResponse t = mapper.readValue(res.toString(), TransactionsResponse.class);

                httpClient.getConnectionManager().shutdown();
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
            t.setText("Calling shit...");

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
