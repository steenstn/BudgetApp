package budgetapp.banks.swedbank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import budgetapp.banks.BankConnectionException;
import budgetapp.banks.BankErrorResponse;
import budgetapp.banks.BankHttpResponse;
import budgetapp.banks.BankService;
import budgetapp.banks.BankTransaction;
import budgetapp.banks.BankTransactionsResponse;
import budgetapp.banks.swedbank.beans.OverviewResponse;
import budgetapp.banks.swedbank.beans.PersonalCodeResponse;
import budgetapp.banks.swedbank.beans.ProfileResponse;
import budgetapp.banks.swedbank.beans.Transaction;
import budgetapp.banks.swedbank.beans.TransactionsResponse;
import budgetapp.banks.swedbank.client.SwedbankClient;
import budgetapp.util.BudgetFunctions;
import budgetapp.util.money.Money;
import budgetapp.util.money.MoneyFactory;

public class SwedbankService implements BankService{

    private SwedbankClient client = new SwedbankClient();
    private ObjectMapper mapper = new ObjectMapper();
    private static final String swedbankDateFormat = "yyyy-MM-dd";

    @Override
    public BankTransactionsResponse getTransactions(String authString) {

        String personalCode = "/identification/personalcode";
        String profile = "/profile/";
        String birthDate;
        String password;

        try {
            birthDate = authString.split("\\s+")[0];
            password = authString.split("\\s+")[1];
            StringEntity s = new StringEntity(
                    "{\"userId\": \""+birthDate+"\", \"useEasyLogin\": false, \"password\": \""+password + "\", \"generateEasyLoginId\": false}");

            BankHttpResponse loginResponse = client.post(personalCode, s);
            if(loginResponse.getStatus() == 401) {
                throw new BankConnectionException("Login for " + birthDate + " failed");
            }

            BankHttpResponse prof = client.get(profile);
            ProfileResponse profil = parseOrThrowException(prof, ProfileResponse.class);

            client.post(profile + profil.getBanks().get(0).getPrivateProfile().getId(), new StringEntity(""));

            BankHttpResponse accounts = client.get("/engagement/overview");
            OverviewResponse or = parseOrThrowException(accounts, OverviewResponse.class);

            String accountId = or.getTransactionAccounts().get(0).getId();

            BankHttpResponse res = client.get("/engagement/transactions/" + accountId);
            TransactionsResponse t = parseOrThrowException(res, TransactionsResponse.class);

            client.shutdown();
            Optional<String> wee = Optional.absent();
            return new BankTransactionsResponse(wee, convertTransactions(t.getTransactions()));
        } catch (Exception e) {
             Optional<String> error = Optional.of(e.getMessage());
             return new BankTransactionsResponse(error, new ArrayList<BankTransaction>());
         } finally {
            client.shutdown();
        }
    }

    private List<BankTransaction> convertTransactions(List<Transaction> transactions) throws ParseException {
        List<BankTransaction> convertedTransactions = new ArrayList<BankTransaction>();
        for(Transaction t : transactions) {
            Money m = MoneyFactory.createMoneyFromNewDouble(parseAmountString(t.getAmount()));
            String convertedDate = BudgetFunctions.timeStampConverter(swedbankDateFormat, t.getDate(), "yyyy/MM/dd") + " 00:00";
            convertedTransactions.add(new BankTransaction(convertedDate, m, t.getDescription()));
        }
        return convertedTransactions;
    }

    private double parseAmountString(String amount) {
        return Double.parseDouble(amount.replaceAll("\\s+", "").replaceAll(",",".").trim());
    }

    private <T>  T parseOrThrowException(BankHttpResponse response, Class<T> responseType) {
        System.out.println("Parsing to " + responseType.getSimpleName());
        if(response.getStatus() != 200) {
            throw new BankConnectionException("Call not successful: " + response.getBody());
        }
        try {
            return mapper.readValue(response.getBody(), responseType);
        } catch (IOException e) {
            throw new BankConnectionException("Something went wrong: " + response.getBody());
        }
    }
}
