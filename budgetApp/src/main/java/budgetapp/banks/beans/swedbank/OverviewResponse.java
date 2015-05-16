package budgetapp.banks.beans.swedbank;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OverviewResponse {

    @JsonProperty
    private CreditCard creditCard;

    @JsonProperty
    private List<Account> transactionAccounts;

    @JsonProperty
    private List<Account> savingAccounts;

    @JsonProperty("loanAccounts")
    private List<Account> loanAccounts;

    @JsonProperty
    private List<Account> transactionDisposalAccounts;

    @JsonProperty
    private List<Account> savingDisposalAccounts;

    @JsonProperty
    private List<CardAccount> cardAccounts;

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public List<Account> getTransactionAccounts() {
        if (transactionAccounts == null) {
            transactionAccounts = new ArrayList<Account>();
        }
        return transactionAccounts;
    }

    public void setTransactionAccounts(List<Account> transactionAccounts) {
        this.transactionAccounts = transactionAccounts;
    }

    public List<Account> getSavingAccounts() {
        if (savingAccounts == null) {
            savingAccounts = new ArrayList<Account>();
        }
        return savingAccounts;
    }

    public void setSavingAccounts(List<Account> savingAccounts) {
        this.savingAccounts = savingAccounts;
    }

    public List<Account> getLoanAccounts() {
        if (loanAccounts == null) {
            loanAccounts = new ArrayList<Account>();
        }
        return loanAccounts;
    }

    public void setLoanAccounts(List<Account> loanAccounts) {
        this.loanAccounts = loanAccounts;
    }

    public List<Account> getTransactionDisposalAccounts() {
        if (transactionDisposalAccounts == null) {
            transactionDisposalAccounts = new ArrayList<Account>();
        }
        return transactionDisposalAccounts;
    }

    public void setTransactionDisposalAccounts(List<Account> transactionDisposalAccounts) {
        this.transactionDisposalAccounts = transactionDisposalAccounts;
    }

    public List<Account> getSavingDisposalAccounts() {
        if (savingDisposalAccounts == null) {
            savingDisposalAccounts = new ArrayList<Account>();
        }
        return savingDisposalAccounts;
    }

    public void setSavingDisposalAccounts(List<Account> savingDisposalAccounts) {
        this.savingDisposalAccounts = savingDisposalAccounts;
    }

    public List<CardAccount> getCardAccounts() {
        if (cardAccounts == null) {
            cardAccounts = new ArrayList<CardAccount>();
        }
        return cardAccounts;
    }

    public void setCardAccounts(List<CardAccount> cardAccounts) {
        this.cardAccounts = cardAccounts;
    }
}
