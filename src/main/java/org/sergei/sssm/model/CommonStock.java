package org.sergei.sssm.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.sergei.sssm.utils.NumberFormatter;

/**
 * @author - Sergei Shurpenkov
 * @date - 2017.09.30. initial version
 */
public class CommonStock extends AbstractStock {

	/**
	 * Constructor initializes class attributes.
	 * 
	 * @param stockSymbolCode
	 *            - String of stock symbol code.
	 * @param stockType
	 *            - StockType the type of stock.
	 * @param lastDividend
	 *            - BigDecimal of last dividend value.
	 * @param parValue
	 *            - BigDecimal of par value.
	 * @param initialPrice
	 *            - BigDecimal of price value.
	 */
	public CommonStock(final String stockSymbolCode, final BigDecimal lastDividend, final BigDecimal parValue, final BigDecimal initialPrice) {
		super(stockSymbolCode, StockType.COMMON, lastDividend, parValue, initialPrice);
	}

	/**
	 * Method calculates Dividend Yield using formula: Last Dividend / Price. <br>
	 * If price is zero, null is returned.
	 * 
	 * @return dividendYield - BigDecimal of a calculated Dividend Yield.
	 */
	@Override
	public BigDecimal calculateDividendYield() {
		if (BigDecimal.ZERO.compareTo(super.getPrice()) == 0) {
			return null;
		}
		return super.getLastDividend().divide(super.getPrice(), NumberFormatter.SCALE_4_DECIMAL_DIGITS, RoundingMode.HALF_EVEN);
	}

	/**
	 * Method represents this object as string value.
	 */
	@Override
	public String toString() {
		return new StringBuilder(super.toString()).append("\t\t\t").toString();
	}
}