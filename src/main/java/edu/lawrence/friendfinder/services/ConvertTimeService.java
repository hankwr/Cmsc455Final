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
	
	static public Instant StringToInstant(String time) {
		return Instant.from(formatter.parse(time));
	}
	
}