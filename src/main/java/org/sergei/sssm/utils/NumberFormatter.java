package org.sergei.sssm.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author - Sergei Shurpenkov
 * @date - 2017.09.30. initial version
 */
public class NumberFormatter {
	public static final int SCALE_4_DECIMAL_DIGITS = 4;
	private static final NumberFormat decimalFormatter = new DecimalFormat("#0.0000");
	private static final String EMPTY_VALUE = "";

	/**
	 * Private constructor throws illegal state exception in case of instantiation
	 * execution.
	 */
	private NumberFormatter() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Method scales value to 2 decimal positions, rounds using half even rounding
	 * mode and converts value to string.
	 * 
	 * @param value
	 *            - BigDecimal input value.
	 * 
	 * @return stringValue - converted return value.
	 */
	public static String format(final BigDecimal value) {
		if (value != null) {
			return decimalFormatter.format(value);
		}
		return EMPTY_VALUE;
	}
}