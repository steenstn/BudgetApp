package budgetapp.banks.swedbank.beans;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileResponse {

    @JsonProperty("hasSwedbankProfile")
    private boolean swedbankProfile;

    @JsonProperty("hasSavingbankProfile")
    private boolean savingbankProfile;

    @JsonProperty
    private List<Bank> banks;

    public boolean isSwedbankProfile() {
        return swedbankProfile;
    }

    public void setSwedbankProfile(boolean swedbankProfile) {
        this.swedbankProfile = swedbankProfile;
    }

    public boolean isSavingbankProfile() {
        return savingbankProfile;
    }

    public void setSavingbankProfile(boolean savingbankProfile) {
        this.savingbankProfile = savingbankProfile;
    }

    public List<Bank> getBanks() {
        if (banks == null) {
            banks = new ArrayList<Bank>();
        }
        return banks;
    }

    public void setBanks(List<Bank> banks) {
        this.banks = banks;
    }
}