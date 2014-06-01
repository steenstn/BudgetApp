package budgetapp.activities;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import budgetapp.fragments.EditCurrencyDialogFragment;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.util.BudgetAdapter;
import budgetapp.util.Currency;
import budgetapp.util.IBudgetObserver;
import budgetapp.util.money.Money;
import budgetapp.viewholders.CurrencyViewHolder;

public class CurrenciesActivity
    extends FragmentActivity
    implements IBudgetObserver {

    private ListView currenciesList;
    private BudgetModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currencies);

        model = new BudgetModel(this);
        model.addObserver(this);
        updateList();
        setUpListeners();
    }

    public void saveCurrency(Currency currency) {
        model.addCurrency(currency);
    }

    public void saveConfig() {
        model.saveConfig();
    }

    public void updateList() {
        currenciesList = (ListView) findViewById(R.id.currenciesListView);
        List<Currency> currencies = (model.getCurrencies());
        BudgetAdapter listAdapter = new BudgetAdapter(this.getBaseContext(), R.layout.listitem_currency);
        for (Currency c : currencies) {
            listAdapter.add(new CurrencyViewHolder(c));
        }
        currenciesList.setAdapter(listAdapter);
    }

    private void setUpListeners() {
        currenciesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {

                Currency currency = ((CurrencyViewHolder) currenciesList.getItemAtPosition(position)).getCurrency();
                Money.setCurrency(currency.getSymbol());
                Money.setExchangeRate(currency.getExchangeRate());
                Money.after = currency.showSymbolAfter();
                Toast.makeText(getBaseContext(),
                    "Active currency: " + Money.getCurrency() + " Exchange rate: " + Money.getExchangeRate(),
                    Toast.LENGTH_LONG).show();
            }
        });

        Button addCategory = (Button) findViewById(R.id.addCurrencyBtn);

        addCategory.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                DialogFragment newFragment = new EditCurrencyDialogFragment();
                newFragment.show(CurrenciesActivity.this.getSupportFragmentManager(), "add_currency");

            }

        });

    }

    @Override
    public void update() {
        updateList();
    }
}
