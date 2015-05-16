package budgetapp.banks.swedbank.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

    @JsonProperty
    private boolean selectedForQuickBalance;

    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String balance;

    @JsonProperty
    private String currency;

    @JsonProperty
    private String fullyFormattedNumber;

    @JsonProperty
    private String accountNumber;

    @JsonProperty
    private String clearingNumber;

    public boolean isSelectedForQuickBalance() {
        return selectedForQuickBalance;
    }

    public void setSelectedForQuickBalance(boolean selectedForQuickBalance) {
        this.selectedForQuickBalance = selectedForQuickBalance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBalance() {
        return balance != null ? balance : "0";
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency != null ? currency : "";
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFullyFormattedNumber() {
        return fullyFormattedNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getClearingNumber() {
        return clearingNumber;
    }
}
