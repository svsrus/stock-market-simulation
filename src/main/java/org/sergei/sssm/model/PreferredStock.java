package org.sergei.sssm.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.sergei.sssm.utils.NumberFormatter;

/**
 * @author - Sergei Shurpenkov
 * @date - 2017.09.30. initial version
 */
public class PreferredStock extends AbstractStock {
	private BigDecimal fixedDividend;

	/**
	 * Constructor initializes stock market company that uses preferred stock.
	 * 
	 * @param stockSymbolCode
	 *            - String of stock symbol code.
	 * @param lastDividend
	 *            - BigDecimal of last dividend value.
	 * @param fixedDividend
	 *            - BigDecimal of fixed dividend value.
	 * @param parValue
	 *            - BigDecimal of par value.
	 * @param initialPrice
	 *            - BigDecimal of price value.
	 */
	public PreferredStock(String stockSymbolCode, final BigDecimal lastDividend, final BigDecimal fixedDividend, final BigDecimal parValue, final BigDecimal initialPrice) {
		super(stockSymbolCode, StockType.PREFERRED, lastDividend, parValue, initialPrice);
		this.fixedDividend = fixedDividend;
	}

	/**
	 * Method calculates Dividend Yield using formula: <br>
	 * Fixed Dividend * Par Value / Price.
	 * 
	 * @return dividendYield - BigDecimal of a calculated Dividend Yield.
	 */
	@Override
	public BigDecimal calculateDividendYield() {
		if (BigDecimal.ZERO.compareTo(super.getPrice()) == 0) {
			return null;
		}
		return this.fixedDividend.multiply(super.getParValue()).divide(super.getPrice(), NumberFormatter.SCALE_4_DECIMAL_DIGITS, RoundingMode.HALF_EVEN);
	}

	/**
	 * @return the fixedDividend formatted as String.
	 */
	public String getFixedDividendFormatted() {
		return NumberFormatter.format(this.fixedDividend);
	}

	/**
	 * Method represents this object as string value.
	 */
	@Override
	public String toString() {
		return new StringBuilder(super.toString()).append("\tFixed Dividend: ").append(getParValueFormatted()).toString();
	}
}