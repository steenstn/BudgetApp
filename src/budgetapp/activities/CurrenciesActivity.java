package budgetapp.activities;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import budgetapp.fragments.EditCurrencyDialogFragment;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;
import budgetapp.util.Currency;

public class CurrenciesActivity
    extends FragmentActivity {

    ListView currenciesList;
    BudgetModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currencies);

        model = new BudgetModel(this);

        updateList();
        setUpListeners();
    }

    public void saveConfig() {
        model.saveConfig();
    }

    public void updateList() {
        currenciesList = (ListView) findViewById(R.id.currenciesListView);
        List<Currency> currencies = (model.getCurrencies());
        String temp[] = new String[currencies.size()];
        for (int i = 0; i < currencies.size(); i++) {
            temp[i] = currencies.get(i).getSymbol();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
            android.R.id.text1, temp);

        currenciesList.setAdapter(adapter);
    }

    private void setUpListeners() {
        currenciesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {

                /*Object item = currenciesList.getItemAtPosition(position);
                String theCategory = item.toString();
                Bundle bundle = new Bundle();
                bundle.putString("chosenCurrency", theCategory);*/
                DialogFragment newFragment = new EditCurrencyDialogFragment();
                //newFragment.setArguments(bundle);
                newFragment.show(CurrenciesActivity.this.getSupportFragmentManager(), "edit_currency");

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
}
