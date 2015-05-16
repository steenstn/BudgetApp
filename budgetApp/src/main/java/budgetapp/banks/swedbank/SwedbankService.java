package budgetapp.banks.swedbank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import budgetapp.banks.BankErrorResponse;
import budgetapp.banks.BankService;
import budgetapp.banks.BankTransaction;
import budgetapp.banks.BankTransactionsResponse;
import budgetapp.banks.swedbank.beans.OverviewResponse;
import budgetapp.banks.swedbank.beans.ProfileResponse;
import budgetapp.banks.swedbank.beans.Transaction;
import budgetapp.banks.swedbank.beans.TransactionsResponse;
import budgetapp.banks.swedbank.client.SwedbankClient;

public class SwedbankService implements BankService{

    private SwedbankClient client = new SwedbankClient();
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public BankTransactionsResponse getTransactions(String authString) {

        String personalCode = "/identification/personalcode";
        String profile = "/profile/";
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
            Optional<BankErrorResponse> wee = Optional.absent();
            return new BankTransactionsResponse(wee, convertTransactions(t.getTransactions()));
        } catch (MalformedURLException e) {return null;

        } catch (IOException e) {return null;
        } catch (IllegalStateException e) {
            return null;
        } catch (JSONException e) {
            return null;
        }
    }

    private List<BankTransaction> convertTransactions(List<Transaction> transactions) {
        List<BankTransaction> convertedTransactions = new ArrayList<BankTransaction>();
        for(Transaction t : transactions) {
            convertedTransactions.add(new BankTransaction(t.getDate(), t.getAmount(), t.getDescription()));
        }
        return convertedTransactions;
    }
}
