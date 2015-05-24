package budgetapp.banks.swedbank.client;

import android.util.Base64;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import budgetapp.banks.BankHttpResponse;

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
    public BankHttpResponse post(String endPoint, HttpEntity entity) throws IOException {
        HttpPost postRequest = createPostRequest(endPoint, entity);
        return call(postRequest);
    }

    public BankHttpResponse get(String endPoint) throws IOException {
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
        BasicClientCookie cookie = new BasicClientCookie("dsid", dsId);
        cookie.setDomain(".api.swedbank.se");
        cookie.setPath("/");
        cookie.setVersion(0);
        cs.addCookie(cookie);
    }

    private BankHttpResponse call(HttpUriRequest request) throws IOException {
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
        String responseBody = parseResponse(response);
        return new BankHttpResponse(response.getStatusLine().getStatusCode(), responseBody);
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

    private String parseResponse(HttpResponse response) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
        String output = "";
        StringBuilder sb = new StringBuilder();
        System.out.println("Output from Server .... \n");
        System.out.println("Status: " + response.getStatusLine().getStatusCode());
        while ((output = br.readLine()) != null) {
            System.out.println(output);
            sb.append(output).append("\n");
        }
        return sb.toString();
    }
}
