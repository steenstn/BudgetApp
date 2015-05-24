package budgetapp.banks;

public class BankHttpResponse {
    private int status;
    private String body;

    public BankHttpResponse(int status, String body) {
        this.status = status;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }
}
