package io.github.rowak.recipesappserver.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ConsoleOutput {
	public static String format(String str) {
		return getTimeString() + " " + str;
	}
	
	private static String getTimeString() {
		Date time = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
		return sdf.format(time);
	}
}
