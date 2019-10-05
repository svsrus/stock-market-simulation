package org.sergei.sssm.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author - Sergei Shurpenkov
 * @date - 2017.10.04. initial version
 */
public class TimeFormatter {
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd. HH:mm:ss:SSS");

	/**
	 * Private constructor throws illegal state exception in case of instantiation
	 * execution.
	 */
	private TimeFormatter() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Method formats date using following pattern: yyyy.MM.dd. HH:mm:ss:SSS.
	 * 
	 * @param timestamp
	 *            - long parameter to format.
	 * 
	 * @return String formatted timestamp.
	 */
	public static String format(final long timestamp) {
		return format(new Timestamp(timestamp));
	}

	/**
	 * Method formats date using following pattern: yyyy.MM.dd. HH:mm:ss:SSS.
	 * 
	 * @param timestamp
	 *            - Timestamp parameter to format.
	 * 
	 * @return String formatted timestamp.
	 */
	public static String format(final Timestamp timestamp) {
		return dateFormat.format(timestamp);
	}
}