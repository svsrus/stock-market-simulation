package org.sergei.sssm.model;

/**
 * @author - Sergei Shurpenkov
 * @date - 2017.09.30. initial version
 */
public enum StockType {
	COMMON("Common"), PREFERRED("Preferred");

	private String description;

	/**
	 * Constructor initializes description attribute.
	 * 
	 * @param description
	 *            - String stock type description value.
	 */
	private StockType(final String description) {
		this.description = description;
	}

	/**
	 * Method returns true if this object represents COMMON enum.
	 * 
	 * @return boolean.
	 */
	public boolean isCommon() {
		return this == COMMON;
	}

	/**
	 * Method returns true if this object represents PREFERRED enum.
	 * 
	 * @return boolean.
	 */
	public boolean isPreferred() {
		return this == PREFERRED;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
}