package budgetapp.banks;

public interface BankService {
    BankTransactionsResponse getTransactions(String authString);

}
