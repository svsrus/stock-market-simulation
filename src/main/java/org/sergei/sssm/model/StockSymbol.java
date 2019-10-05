package org.sergei.sssm.model;

/**
 * @author - Sergei Shurpenkov
 * @date - 2017.09.30. initial version
 */
public enum StockSymbol {
	TEA("TEA"), POP("POP"), ALE("ALE"), GIN("GIN"), JOE("JOE");

	private String code;

	/**
	 * Constructor initializes classes code attribute.
	 * 
	 * @param description
	 *            - String stock symbol code value.
	 */
	private StockSymbol(final String code) {
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
}