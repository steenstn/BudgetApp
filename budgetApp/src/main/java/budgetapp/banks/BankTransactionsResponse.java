package budgetapp.banks;

import com.google.common.base.Optional;

import java.util.List;

public class BankTransactionsResponse {
    private Optional<BankErrorResponse> errorResponse;
    private List<BankTransaction> transactions;

    public BankTransactionsResponse(Optional<BankErrorResponse> errorResponse, List<BankTransaction> transactions) {
        this.errorResponse = errorResponse;
        this.transactions = transactions;
    }

    public Optional<BankErrorResponse> getErrorResponse() {
        return errorResponse;
    }

    public List<BankTransaction> getTransactions() {
        return transactions;
    }
}
