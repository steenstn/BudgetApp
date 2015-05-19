package budgetapp.banks;

public class BankErrorResponse {
    private int status;
    private String message;

    public BankErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
