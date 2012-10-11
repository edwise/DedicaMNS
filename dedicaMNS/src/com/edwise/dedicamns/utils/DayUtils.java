package com.edwise.dedicamns.utils;

public class DayUtils {

    public static boolean isWeekend(String dayName) {
	return dayName.equalsIgnoreCase("Domingo") || dayName.equalsIgnoreCase("Sabado");
    }
}
