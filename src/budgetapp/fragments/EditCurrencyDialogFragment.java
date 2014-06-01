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
    private CheckBox symbolAftercheckBox;
    private EditText symbolEditText;
    private EditText exchangeRateEditText;
    private String mode;
    private long currencyId = -1;
    private View view;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_edit_currency, null);

        symbolEditText = (EditText) view.findViewById(R.id.edit_currency_currency);
        exchangeRateEditText = (EditText) view.findViewById(R.id.edit_currency_exchangerate);
        symbolAftercheckBox = (CheckBox) view.findViewById(R.id.edit_currency_after_checkbox);

        mode = this.getTag();
        if (mode.equalsIgnoreCase("edit_currency")) {
            Bundle bundle = this.getArguments();
            currencyId = bundle.getLong("id", -1);

            symbolEditText.setText(bundle.getString("symbol"));
            exchangeRateEditText.setText("" + bundle.getDouble("exchangeRate"));
            symbolAftercheckBox.setChecked(bundle.getBoolean("symbolAfter"));
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
        String symbol = symbolEditText.getText().toString();
        double exchangeRate = 1.0;
        try {
            exchangeRate = Double.parseDouble(exchangeRateEditText.getText().toString());
        } catch (NumberFormatException e) {
            exchangeRate = 1.0;
        }
        int flags = symbolAftercheckBox.isChecked() ? Currency.SHOW_SYMBOL_AFTER : 0;

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

        symbolAftercheckBox = (CheckBox) view.findViewById(R.id.edit_currency_after_checkbox);
        boolean showSymbolAfter = symbolAftercheckBox.isChecked();
        int flags = showSymbolAfter ? Currency.SHOW_SYMBOL_AFTER : 0;
        Currency newCurrency = new Currency(symbol, exchangeRate, flags);

        ((CurrenciesActivity) getActivity()).saveCurrency(newCurrency);
    }
}
