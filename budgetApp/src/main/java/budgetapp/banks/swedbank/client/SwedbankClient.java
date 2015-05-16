package budgetapp.banks.swedbank.client;

import android.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class SwedbankClient {

    private CookieStore cookieStore;
    private String baseUrl = "https://auth.api.swedbank.se/TDE_DAP_Portal_REST_WEB/api/v2";
    private DefaultHttpClient httpClient;

    public SwedbankClient() {
        httpClient = new DefaultHttpClient();
        cookieStore = new BasicCookieStore();
        httpClient.setCookieStore(cookieStore);
    }

    public void shutdown() {
        httpClient.getConnectionManager().shutdown();
    }
    public JSONObject post(String endPoint, HttpEntity entity) throws IOException, JSONException {
        HttpPost postRequest = createPostRequest(endPoint, entity);
        return call(postRequest);
    }

    public JSONObject get(String endPoint) throws IOException, JSONException {
        HttpGet getRequest = createGetRequest(endPoint);
        return call(getRequest);
    }
    private HttpPost createPostRequest(String urlEndPoint, HttpEntity entity) {
        String dsId = dsId();
        HttpPost request = new HttpPost(baseUrl + urlEndPoint + dsIdUrl(dsId));
        request.setHeader("Authorization", getAuth());
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/json");

        request.setEntity(entity);
        addCookie(cookieStore, dsId);
        return request;
    }

    private HttpGet createGetRequest(String urlEndPoint) {
        String dsId = dsId();
        HttpGet request = new HttpGet(baseUrl + urlEndPoint + dsIdUrl(dsId));
        request.setHeader("Authorization", getAuth());
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/json");

        addCookie(cookieStore, dsId);
        return request;
    }

    private static void addCookie(CookieStore cs, String dsId) {
        BasicClientCookie cookie2 = new BasicClientCookie("dsid", dsId);
        cookie2.setDomain(".api.swedbank.se");
        cookie2.setPath("/");
        cookie2.setVersion(0);
        cs.addCookie(cookie2);
    }

    private JSONObject call(HttpUriRequest request) throws IOException,
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
}
