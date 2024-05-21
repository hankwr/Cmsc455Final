package edu.lawrence.friendfinder.services;

// Java-level includes [Time]
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ConvertTimeService {
	
	/**
	 * time format examples: 
	 * 12:04 pm, Tue 05/21/2024 CST
	 * 11:59 am, Thu 05/16/2024 PST
	 */
	private static final String pattern = "hh:mm a, EEE MM:dd:yyyy z";
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.of("UTC"));
	
	/**
	 * Formats a java.time.Instant into a String, assumes UTC timezone
	 * @param time the Instant object to convert
	 * @return the formatted String version of the instant
	 */
	static public String InstantToString(Instant time) {
		String ret = formatter.format(time);
	
		return ret;
	}
	
	/**
	 * Formats a java.time.Instant into a String
	 * @param time time the Instant object to convert
	 * @param zone the specified timezone to display in the String
	 * @return the formatted String version of the instant
	 */
	static public String InstantToString(Instant time, String zone) {
		String ret = formatter.withZone(ZoneId.of(zone, ZoneId.SHORT_IDS)).format(time);
		
		return ret;
	}
	
	/**
	 * Parses a formatted String into a java.time.Instant, formats to UTC timezone
	 * @param time the formatted String representation of time (hh:mm a, MM:dd:yyyy z)
	 * @return the parsed Instant object
	 */
	static public Instant StringToInstant(String time) {
		Instant ret = Instant.from(formatter.parse(time));
		
		return ret;
	}
	
	/**
	 * Parses a formatted String into a java.time.Instant, formats to specified timezone
	 * @param time the formatted String representation of time (hh:mm a, MM:dd:yyyy z)
	 * @param zone the specified zone abbreviation (UTC, CST, PST, etc.)
	 * @return the parsed Instant object
	 */
	static public Instant StringToInstant(String time, String zone) {
		Instant ret = Instant.from(formatter.withZone(ZoneId.of(zone, ZoneId.SHORT_IDS)).parse(time));
		
		return ret;
	}
	
}