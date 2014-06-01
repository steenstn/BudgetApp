package budgetapp.fragments;

/**
 * Dialog Fragment for adding a new category
 * 
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import budgetapp.activities.CurrenciesActivity;
import budgetapp.main.R;
import budgetapp.util.Currency;

public class EditCurrencyDialogFragment
    extends DialogFragment {
    private CheckBox checkBox;
    private String mode;
    private long currencyId = -1;
    private View view;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        view = inflater.inflate(R.layout.dialog_edit_currency, null);
        mode = this.getTag();
        if (mode.equalsIgnoreCase("edit_currency")) {
            Bundle bundle = this.getArguments();
            currencyId = bundle.getLong("id", -1);

            EditText edit = (EditText) view.findViewById(R.id.edit_currency_currency);
            edit.setText(bundle.getString("symbol"));

            edit = (EditText) view.findViewById(R.id.edit_currency_exchangerate);
            edit.setText("" + bundle.getDouble("exchangeRate"));

            checkBox = (CheckBox) view.findViewById(R.id.edit_currency_after_checkbox);
            checkBox.setChecked(bundle.getBoolean("symbolAfter"));
        }
        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

                if (mode.equalsIgnoreCase("edit_currency")) {
                    editCurrency();
                } else if (mode.equalsIgnoreCase("add_currency")) {
                    addCurrency();
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditCurrencyDialogFragment.this.getDialog().cancel();
            }
        });

        return builder.create();
    }

    public void editCurrency() {
        Bundle bundle = this.getArguments();

        String symbol = bundle.getString("symbol");
        double exchangeRate = bundle.getDouble("exchangeRate");
        int flags = bundle.getBoolean("symbolAfter") ? Currency.SHOW_SYMBOL_AFTER : 0;

        Currency newCurrency = new Currency(symbol, exchangeRate, flags);

        ((CurrenciesActivity) getActivity()).editCurrency(currencyId, newCurrency);
    }

    public void addCurrency() {

        EditText temp = (EditText) view.findViewById(R.id.edit_currency_currency);
        String symbol = temp.getText().toString();

        double exchangeRate = 1;
        try {
            temp = (EditText) view.findViewById(R.id.edit_currency_exchangerate);
            double value = Double.parseDouble(temp.getText().toString());
            if (value > 0) {
                exchangeRate = value;
            }
        } catch (NumberFormatException e) {
            exchangeRate = 1;
        }

        checkBox = (CheckBox) view.findViewById(R.id.edit_currency_after_checkbox);
        boolean showSymbolAfter = checkBox.isChecked();
        int flags = showSymbolAfter ? Currency.SHOW_SYMBOL_AFTER : 0;
        Currency newCurrency = new Currency(symbol, exchangeRate, flags);

        ((CurrenciesActivity) getActivity()).saveCurrency(newCurrency);
    }
}
