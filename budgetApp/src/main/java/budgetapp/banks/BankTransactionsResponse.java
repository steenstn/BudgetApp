package budgetapp.banks;

import com.google.common.base.Optional;

import java.util.List;

public class BankTransactionsResponse {
    private Optional<String> errorResponse;
    private List<BankTransaction> transactions;

    public BankTransactionsResponse(Optional<String> errorResponse, List<BankTransaction> transactions) {
        this.errorResponse = errorResponse;
        this.transactions = transactions;
    }

    public Optional<String> getErrorResponse() {
        return errorResponse;
    }

    public List<BankTransaction> getTransactions() {
        return transactions;
    }
}
