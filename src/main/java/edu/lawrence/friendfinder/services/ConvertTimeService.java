package edu.lawrence.friendfinder.services;

// Java-level includes [Time]
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class ConvertTimeService {
	
	/**
	 * time format examples: 
	 * 12:04 pm, Tue 05/21/2024 CST
	 * 11:59 am, Thu 05/16/2024 PST
	 */
	private static final String pattern = "hh:mm a, EEE MM:dd:yyyy z";
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
	
	static public String InstantToString(Instant time) {
		return formatter.format(time);
	}
	
	// Uncertain of how best to pass requested timezone
	// May need helper method for parsing zoneId from string
	static public Instant StringToInstant(String time /*, ZoneId timezone*/) {
		Instant ret = Instant.from(formatter/*.withZone(timezone)*/.parse(time));
				
		return ret;
	}
	
}