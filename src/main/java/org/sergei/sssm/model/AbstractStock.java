package org.sergei.sssm.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.sergei.sssm.utils.NumberFormatter;

/**
 * @author - Sergei Shurpenkov
 * @date - 2017.09.30. initial version
 */
public abstract class AbstractStock {
	private static final String PE_RATIO_NOT_APPLICABLE = "N/A";
	private StockSymbol stockSymbol;
	private StockType stockType;
	private BigDecimal lastDividend;
	private BigDecimal parValue;
	private BigDecimal price;

	/**
	 * Constructor initializes class attributes.
	 * 
	 * @param stockSymbolCode
	 *            - String of stock symbol code.
	 * @param stockType
	 *            - StockType the type of stock.
	 * @param lastDividend
	 *            - BigDecimal of a last stock dividend.
	 * @param parValue
	 *            - BigDecimal of a stock par value.
	 * @param initialPrice
	 *            - BigDecimal of a initial stock price.
	 */
	public AbstractStock(final String stockSymbolCode, final StockType stockType, final BigDecimal lastDividend, final BigDecimal parValue, final BigDecimal initialPrice) {
		this.stockSymbol = StockSymbol.valueOf(stockSymbolCode);
		this.stockType = stockType;
		this.lastDividend = lastDividend;
		this.parValue = parValue;
		this.price = initialPrice;
	}

	/**
	 * Method calculates Dividend Yield for a given stock.
	 * 
	 * @return dividendYield - BigDecimal of a calculated Dividend Yield.
	 */
	public abstract BigDecimal calculateDividendYield();

	/**
	 * Method calculates Dividend Yield for a given stock and formats result as
	 * String, if a divident yield is null, N/A is returned.
	 * 
	 * @return dividendYieldFormatted - String of a calculated Dividend Yield.
	 */
	public String calculateDividendYieldFormatted() {
		final BigDecimal calculateDividendYield = calculateDividendYield();
		if (calculateDividendYield == null) {
			return PE_RATIO_NOT_APPLICABLE;
		}
		return NumberFormatter.format(calculateDividendYield);
	}

	/**
	 * Method calculates P/E Ratio for a given stock, if a Dividend Yield is null or
	 * zero, N/A is returned.
	 * 
	 * @return dividendYield - BigDecimal of a calculated P/E Ratio.
	 */
	public BigDecimal calculatePERatio() {
		final BigDecimal dividendYield = this.calculateDividendYield();
		if (dividendYield == null || BigDecimal.ZERO.compareTo(dividendYield) == 0) {
			return null;
		}
		return price.divide(dividendYield, NumberFormatter.SCALE_4_DECIMAL_DIGITS, RoundingMode.HALF_EVEN);
	}

	/**
	 * Method calculates P/E Ratio for a given stock and returns a formatted value,
	 * if a Dividend Yield is zero, N/A is returned.
	 * 
	 * @return dividendYield - String of a calculated P/E Ratio.
	 */
	public String calculatePERatioFormatted() {
		final BigDecimal peRatio = this.calculatePERatio();
		if (peRatio == null) {
			return PE_RATIO_NOT_APPLICABLE;
		}
		return NumberFormatter.format(peRatio);
	}

	/**
	 * @return the stockTypeDescription
	 */
	public String getStockTypeDescription() {
		return this.stockType.getDescription();
	}

	/**
	 * @return the lastDividend formatted as String.
	 */
	public String getLastDividendFormatted() {
		return NumberFormatter.format(this.lastDividend);
	}

	/**
	 * @return the parValue formatted as String.
	 */
	public String getParValueFormatted() {
		return NumberFormatter.format(this.parValue);
	}

	/**
	 * @return the price formatted as String.
	 */
	public String getPriceFormatted() {
		return NumberFormatter.format(this.price);
	}

	/**
	 * @return the stockSymbol
	 */
	public StockSymbol getStockSymbol() {
		return stockSymbol;
	}

	/**
	 * @return the lastDividend
	 */
	public BigDecimal getLastDividend() {
		return lastDividend;
	}

	/**
	 * @return the parValue
	 */
	public BigDecimal getParValue() {
		return parValue;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * Method represents this object as string value.
	 */
	@Override
	public String toString() {
		return new StringBuilder("Stock symbol: ").append(this.stockSymbol.getCode()).append("\tType: ").append(this.stockType.getDescription()).append("\tLast Dividend: ")
				.append(getLastDividendFormatted()).append("\tPar Value: ").append(getParValueFormatted()).append("\tPrice: ").append(getPriceFormatted()).toString();
	}
}