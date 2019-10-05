package org.sergei.sssm.model;

/**
 * @author - Sergei Shurpenkov
 * @date - 2017.10.04. initial version
 */
public enum StockOrderTransactionType {
	BUY("Buy"), SELL("Sell");

	private String code;

	/**
	 * Constructor initializes classes code attribute.
	 * 
	 * @param description
	 *            - String stock order transaction type code value.
	 */
	private StockOrderTransactionType(final String code) {
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
}