package budgetapp.viewholders;

import android.graphics.Color;
import android.widget.TextView;
import budgetapp.util.Currency;

public class CurrencyViewHolder
    extends ViewHolder {

    private Currency currency;
    private TextView leftTextView;
    private TextView centerTextView;

    public CurrencyViewHolder(Currency currency) {
        this.currency = currency;
    }

    public CurrencyViewHolder(CurrencyViewHolder viewHolder) {
        this.currency = viewHolder.getCurrency();
    }

    @Override
    public void populateViews() {
        leftTextView = (TextView) getFirstView();
        centerTextView = (TextView) getSecondView();

        leftTextView.setText(currency.getSymbol());
        if (currency.isActive()) {
            leftTextView.setShadowLayer(10, 0, 0, Color.rgb(135, 240, 255));
            leftTextView.setTextColor(Color.WHITE);
        } else {
            leftTextView.setTextColor(Color.LTGRAY);
            leftTextView.setShadowLayer(0, 0, 0, Color.rgb(135, 240, 255));
        }
        centerTextView.setText("Exchange rate: " + currency.getExchangeRate());

    }

    @Override
    public void recycle(IViewHolder tempEntry) {
        setCurrency(((CurrencyViewHolder) tempEntry).getCurrency());
    }

    @Override
    public IViewHolder copy() {
        return new CurrencyViewHolder(this);
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

}
