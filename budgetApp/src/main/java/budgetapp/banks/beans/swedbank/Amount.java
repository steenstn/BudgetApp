package budgetapp.banks.beans.swedbank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Amount {
    @JsonProperty
    private String amount;

    @JsonProperty
    private String currencyCode;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
