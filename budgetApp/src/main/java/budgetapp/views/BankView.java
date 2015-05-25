package budgetapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.viewholders.BankTransactionViewHolder;

public class BankView extends LinearLayout {

    private BudgetModel model;
    private ViewListener viewListener;
    private ListView transactionList;

    public static interface ViewListener {

        void transactionListItemClicked(BankTransactionViewHolder transaction);

    }

    public BankView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        transactionList = (ListView)findViewById(R.id.listViewBankTransactions);
        setUpListeners();
    }

    private void setUpListeners() {
        transactionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BankTransactionViewHolder item = (BankTransactionViewHolder)transactionList.getItemAtPosition(position);
                viewListener.transactionListItemClicked(item);
            }
        });
    }


    public void setModel(BudgetModel model) {
        this.model = model;
    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }
}
