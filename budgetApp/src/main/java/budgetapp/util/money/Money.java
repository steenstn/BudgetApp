package budgetapp.util.money;


public class Money {

    private double value;
    public static boolean after = true;
    private static String currency = "kr";
    private static double exchangeRate = 1;

    Money() {
        value = 0;
    }

    Money(double x) {
        value = x * exchangeRate;
    }

    public Money(Money m) {
        value = m.get();
    }

    public static Money fromNewDouble(double d) {
        return new Money(d);
    }

    public static Money zero() {
        return new Money();
    }

    /**
     * Use this when a double in the system (e.g. from database) should become a money
     * @param d
     * @return
     */
    public static Money fromExistingDouble(double d) {
        return new Money(d / Money.getExchangeRate());
    }

    public static void setCurrency(String c) {
        currency = c;
    }

    public static String getCurrency() {
        return currency;
    }

    public static double getExchangeRate() {
        return exchangeRate;
    }

    public static void setExchangeRate(double val) {
        exchangeRate = val;
    }

    public double get() {
        return value;
    }

    public Money add(Money x) {
        return new Money((this.value + x.get()) / exchangeRate);
    }

    public Money subtract(Money x) {
        return new Money((this.value - x.get()) / exchangeRate);
    }

    public Money divide(double x) {
        if (x != 0) {
            return new Money((this.value / x) / exchangeRate);
        } else {
            return new Money();
        }
    }

    public Money divide(Money x) {
        return new Money((value / x.get()) / exchangeRate);
    }

    public Money makeNegative() {
        return new Money((Math.abs(value) / exchangeRate) * -1);
    }

    public Money makePositive() {
        return new Money(Math.abs(value) / exchangeRate);
    }

    public Money multiply(double x) {
        return new Money((value * x) / exchangeRate);
    }

    public Money multiply(Money x) {
        return new Money((this.value * x.get()) / exchangeRate);
    }

    public boolean biggerThan(Money x) {
        return value > x.get();
    }

    public boolean biggerThanOrEquals(Money x) {
        return value >= x.get();
    }

    public boolean smallerThan(Money x) {
        return value < x.get();
    }

    public boolean smallerThanOrEquals(Money x) {
        return value <= x.get();
    }

    public boolean equals(Money x) {
        return value == x.get();
    }

    public boolean almostZero() {
        return Math.abs(value) < 0.000001;
    }

    /**
     * Returns a string of the value with some formatting to get the minus sign at the right place and not print out
     * decimals where it's not needed.
     */
    @Override
    public String toString() {
        double fixedValue = value / exchangeRate;
        if (after) {
            if (frac(fixedValue) < 0.01)
                return String.format("%.0f " + currency, fixedValue);
            else
                return String.format("%.2f " + currency, fixedValue);
        } else {
            if (frac(fixedValue) < 0.01)
                return (fixedValue < 0.0 ? "-" : "") + String.format(currency + "%.0f ", Math.abs(fixedValue));
            else
                return (fixedValue < 0.0 ? "-" : "") + String.format(currency + "%.2f", Math.abs(fixedValue));
        }
    }

    private double frac(double d) {
        return d - Math.floor(d);
    }

}
