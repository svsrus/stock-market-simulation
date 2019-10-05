package org.sergei.sssm.exeption;

/**
 * @author - Sergei Shurpenkov
 * @date - 2017.10.01. initial version
 */
@SuppressWarnings("serial")
public class StockMarketSystemException extends RuntimeException {
	private final String extendedMessage;

	/**
	 * Exception constructor calls for super constructor passing exeption parameter
	 * and initializes this classes attribute.
	 * 
	 * @param exception
	 *            - Exception.
	 * @param extendedMessage
	 *            - String extended message.
	 */
	public StockMarketSystemException(Exception exception, String extendedMessage) {
		super(exception);
		this.extendedMessage = extendedMessage;
	}

	/**
	 * Method represents this object as string value.
	 */
	@Override
	public String toString() {
		return new StringBuilder(super.toString()).append(" - Extended message: ").append(this.extendedMessage).toString();
	}
}