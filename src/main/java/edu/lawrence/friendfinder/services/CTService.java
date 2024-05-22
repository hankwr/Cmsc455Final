package edu.lawrence.friendfinder.services;

// Java-level includes [Utility]
import java.util.regex.PatternSyntaxException;

// Java-level includes [Time]
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.Instant;
import java.time.ZoneId;

public class CTService {
	
	/**
	 * time format examples: 
	 * 12:04 PM, 5/21/2024 CST
	 * 11:59 AM, 5/16/2024 PST
	 * 8:45 PM, 6/03/2024 UCT
	 * 
	 * leading zeros in front of hour will be removed automatically
	 */
	private static final String pattern = "h:mm a, M/dd/yyyy z";
	private static final String regexPattern = "^\\d{1,2}:\\d{2} [A-Z]{2}, \\d{1,2}/\\d{2}/\\d{4} [A-Z]{1,3}$";
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.of("UTC"));
	private static final String formattedEpoch = formatter.format(Instant.EPOCH);
	
	
	
	/**
	 * Formats a java.time.Instant into a String (h:mm a, M/dd/yyyy z), assumes UTC timezone
	 * @param time the Instant object to convert
	 * @return the formatted String version of the instant
	 */
	static public String instToStr(Instant time) {
		String ret;
		
		try {
		ret = formatter.format(time);
		}catch (Exception e) {
			e.printStackTrace();
			return formattedEpoch;
		}
		
		return ret;
	}
	
	/**
	 * Formats a java.time.Instant into a String of format (h:mm a, M/dd/yyyy z)
	 * @param time time the Instant object to convert
	 * @param zone the specified timezone to display in the String
	 * @return the formatted String version of the instant
	 */
	static public String instToStr(Instant time, String zone) {
		String ret;
		
		try{
			ret = formatter.withZone(ZoneId.of(zone, ZoneId.SHORT_IDS)).format(time);
		} catch (Exception e) {
			e.printStackTrace();
			return formattedEpoch;
		}
		
		return ret;
	}
	
	/**
	 * Parses a formatted String into a java.time.Instant, formats to UTC timezone
	 * @param time the formatted String representation of time (h:mm a, M/dd/yyyy z)
	 * @return the parsed Instant object, returns EPOCH date if failed
	 */
	static public Instant strToInst(String time) {
		Instant ret;
		
		boolean status = false;
		try { // My intellisense freaks out about unreachable code without this tryCatch block
			status = time.matches(regexPattern);
		} catch (PatternSyntaxException e){
			e.printStackTrace();
			ret = Instant.EPOCH;
		}
			
		if(!status) // Checks to make sure formatting is of correct shape
			ret = Instant.EPOCH;
		
		
		try { // Use tryCatch block to verify that Instant is valid (Feb 30th would be invalid, for example)
			ret = Instant.from(formatter.withResolverStyle(ResolverStyle.STRICT).parse(time));
		} catch (DateTimeParseException e) {
			ret = Instant.EPOCH;
		} catch (Exception e) {
			e.printStackTrace();
			ret = Instant.EPOCH;
		}
			
		return ret;
	}
	
	/**
	 * Parses a formatted String into a java.time.Instant, formats to specified timezone
	 * @param time the formatted String representation of time (h:mm a, M/dd/yyyy z)
	 * @param zone the specified zone abbreviation (UTC, CST, PST, etc.)
	 * @return the parsed Instant object, returns EPOCH date if failed
	 */
	static public Instant strToInst(String time, String zone) {
		Instant ret;
		
		boolean status = false;
		try { // My intellisense freaks out about unreachable code without this tryCatch block
			status = time.matches(regexPattern);
		} catch (PatternSyntaxException e){
			e.printStackTrace();
			ret = Instant.EPOCH;
		}
			
		if(!status) // Checks to make sure formatting is of correct shape
			ret = Instant.EPOCH;
		
		
		try { // Use tryCatch block to verify that Instant is valid (Feb 30th would be invalid, for example)
			ret = Instant.from(formatter.withResolverStyle(ResolverStyle.STRICT).withZone(ZoneId.of(zone, ZoneId.SHORT_IDS)).parse(time));
		} catch (DateTimeParseException e) {
			ret = Instant.EPOCH;
		} catch (Exception e) {
			e.printStackTrace();
			ret = Instant.EPOCH;
		}
			
		return ret;
	}

	/**
	 * Checks to see if formatted String timestamp is valid in both format and content
	 * @param time the input String timestamp to check
	 * @return 
	 */
	static public boolean checkFormat(String time) {
		
		boolean status;
		
		try { // Intellisense freaks out about unreachable code without this tryCatch block
			status = time.matches(regexPattern);
		} catch (PatternSyntaxException e){
			e.printStackTrace();
			return false;
		}
			
		if(!status) // Checks to make sure rough formatting is correct
			return status;
		
		try { // Use tryCatch block to verify that Instant is valid (Feb 30th would be invalid, for example)
			Instant.from(formatter.withResolverStyle(ResolverStyle.STRICT).parse(time));
		} catch (DateTimeParseException e) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
			
		return true;
	}
	
}