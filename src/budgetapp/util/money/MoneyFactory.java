package budgetapp.util.money;

public class MoneyFactory {

	/**
	 * Use this when creating a money from e.g. input where it should be multiplied with the exchange rate
	 * @param d
	 * @return
	 */
	public static Money createMoneyFromNewDouble(double d) {
		return new Money(d);
	}
	
	public static Money createMoney() {
		return new Money();
	}
	
	/**
	 * Use this when a double in the system (e.g. from database) should become a money
	 * @param d
	 * @return
	 */
	public static Money convertDoubleToMoney(double d) {
		return new Money(d / Money.getExchangeRate());
	}
}
