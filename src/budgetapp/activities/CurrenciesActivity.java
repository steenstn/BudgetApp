package budgetapp.activities;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
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
    private int selectedCurrencyIndex;
    private List<Currency> currencies;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currencies);

        model = new BudgetModel(this);
        model.addObserver(this);
        addCurrencyIfNoOneExist();
        updateList();
        setUpListeners();

        registerForContextMenu(currenciesList);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_currency, menu);

        menu.setHeaderTitle("Currency options");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        CurrencyViewHolder viewHolder = (CurrencyViewHolder) currenciesList.getItemAtPosition(info.position);

        switch (item.getItemId()) {
        case R.id.context_menu_edit:
            showEditCurrencyDialog(viewHolder);
            return true;
        case R.id.context_menu_delete:
            //showRemoveEventDialog(viewHolder);
            model.removeCurrency(viewHolder.getCurrency().getId());
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    private void showEditCurrencyDialog(CurrencyViewHolder viewHolder) {
        long currencyId = viewHolder.getCurrency().getId();
        String currencySymbol = viewHolder.getCurrency().getSymbol();
        double currencyExchangeRate = viewHolder.getCurrency().getExchangeRate();
        boolean currencySymbolAfter = viewHolder.getCurrency().showSymbolAfter();

        Bundle bundle = new Bundle();
        bundle.putLong("id", currencyId);
        bundle.putString("symbol", currencySymbol);
        bundle.putDouble("exchangeRate", currencyExchangeRate);
        bundle.putBoolean("symbolAfter", currencySymbolAfter);
        DialogFragment newFragment = new EditCurrencyDialogFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "edit_currency");
    }

    public void saveCurrency(Currency currency) {
        model.addCurrency(currency);
    }

    public void saveConfig() {
        model.saveConfig();
    }

    public void editCurrency(long id, Currency newCurrency) {
        model.editCurrency(id, newCurrency);
    }

    public void updateList() {
        currenciesList = (ListView) findViewById(R.id.currenciesListView);
        currencies = (model.getCurrencies());
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
                model.saveConfig();
                Toast.makeText(getBaseContext(),
                    "Set active currency to: " + Money.getCurrency() + "\nExchange rate: " + Money.getExchangeRate(),
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

    private void addCurrencyIfNoOneExist() {
        if (model.getCurrencies().size() == 0) {
            int flags = Money.after ? Currency.SHOW_SYMBOL_AFTER : 0;
            Currency initialCurrency = new Currency(Money.getCurrency(), Money.getExchangeRate(), flags);
            model.addCurrency(initialCurrency);
        }
    }
}
