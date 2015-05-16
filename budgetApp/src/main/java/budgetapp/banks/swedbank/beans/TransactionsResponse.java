package budgetapp.banks.swedbank.beans;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionsResponse {
    @JsonProperty
    private List<Transaction> transactions;

    @JsonProperty
    private List<Transaction> reservedTransactions;

    public List<Transaction> getTransactions() {
        if (transactions == null) {
            transactions = new ArrayList<Transaction>();
        }
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Transaction> getReservedTransactions() {
        if (reservedTransactions == null) {
            reservedTransactions = new ArrayList<Transaction>();
        }
        return reservedTransactions;
    }

    public void setReservedTransactions(List<Transaction> reservedTransactions) {
        this.reservedTransactions = reservedTransactions;
    }
}

