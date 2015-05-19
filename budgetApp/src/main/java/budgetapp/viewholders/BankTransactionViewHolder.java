package budgetapp.viewholders;

import android.widget.Button;
import android.widget.TextView;

import budgetapp.banks.BankTransaction;

public class BankTransactionViewHolder extends ViewHolder {

    private BankTransaction transaction;

    public BankTransactionViewHolder(BankTransaction transaction) {
        this.transaction = transaction.clone();
    }

    @Override
    public void populateViews() {
        TextView leftTextView = (TextView) getFirstView();
        leftTextView.setText(transaction.getDate() + "\n" + transaction.getAmount());

        TextView centerTextView = (TextView) getSecondView();
        centerTextView.setText(transaction.getDescription());

        Button rightButton = (Button) getThirdView();
        rightButton.setText("lol");
    }

    @Override
    public void recycle(IViewHolder tempEntry) {
        setTransaction(((BankTransactionViewHolder)tempEntry).getTransaction());
    }

    @Override
    public IViewHolder copy() {
        return new BankTransactionViewHolder(transaction);
    }


    public BankTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(BankTransaction transaction) {
        this.transaction = transaction;
    }
}
