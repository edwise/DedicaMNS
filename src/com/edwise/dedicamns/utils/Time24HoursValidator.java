/**
 * 
 */
package com.edwise.dedicamns.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author edwise
 * 
 */
public class Time24HoursValidator {

	private Pattern patternWithMinutes;
	private Pattern patternWithOutMinutes;
	private Matcher matcher;

	private static Time24HoursValidator instance = null;

	private static final String TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
	private static final String TIME24HOURS_WITHOUTMINUTES_PATTERN = "[01]?[0-9]|2[0-3]";

	private Time24HoursValidator() {
		patternWithMinutes = Pattern.compile(TIME24HOURS_PATTERN);
		patternWithOutMinutes = Pattern.compile(TIME24HOURS_WITHOUTMINUTES_PATTERN);
	}

	public static boolean validateTime(String time) {
		if (Time24HoursValidator.instance == null) {
			Time24HoursValidator.instance = new Time24HoursValidator();
		}

		return Time24HoursValidator.instance.internalValidate(time);
	}

	/**
	 * Validate time in 24 hours format with regular expression
	 * 
	 * @param time
	 *            time address for validation
	 * @return true valid time fromat, false invalid time format
	 */
	private boolean internalValidate(final String time) {
		matcher = patternWithMinutes.matcher(time);
		if (!matcher.matches()) {
			matcher = patternWithOutMinutes.matcher(time);
		}
		return matcher.matches();
	}
}