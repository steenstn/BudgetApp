package budgetapp.util;

public class Currency {

    private String symbol;
    private long id;
    private int flags;
    private double exchangeRate;
    public static final int SHOW_SYMBOL_AFTER = 1;
    public static final int CURRENCY_ACTIVE = 2;

    public Currency(long id, String symbol, double exchangeRate, int flags) {
        this.id = id;
        this.symbol = symbol;
        this.exchangeRate = exchangeRate;
        this.flags = flags;
    }

    public boolean isActive() {
        return (flags & CURRENCY_ACTIVE) == CURRENCY_ACTIVE;
    }

    public void setActive(boolean value) {
        if (value) {
            flags = flags | CURRENCY_ACTIVE;
        } else {
            flags = flags & (flags ^ CURRENCY_ACTIVE);
        }
    }

    public boolean showSymbolAfter() {
        return (flags & SHOW_SYMBOL_AFTER) == SHOW_SYMBOL_AFTER;
    }

    public void setShowSymbolAfter(boolean value) {
        if (value) {
            flags = flags | SHOW_SYMBOL_AFTER;
        } else {
            flags = flags & (flags ^ SHOW_SYMBOL_AFTER);
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

}
