package com.edwise.dedicamns.utils;

public class DayUtils {

    public static boolean isWeekend(String dayName) {
	return dayName.equalsIgnoreCase("Dom") || dayName.equalsIgnoreCase("Sáb");
    }

    public static String replaceAcutes(String name) {
	String nameWithoutAcutes = name;
	if (name.contains("acute;")) {
	    nameWithoutAcutes = nameWithoutAcutes.replace("&aacute;", "á");
	    nameWithoutAcutes = nameWithoutAcutes.replace("&eacute;", "é");
	}

	return nameWithoutAcutes;
    }
}
