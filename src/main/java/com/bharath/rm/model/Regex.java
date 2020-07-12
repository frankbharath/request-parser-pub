package com.bharath.rm.model;

/**
 * @author bharath
 * @version 1.0
 * Creation time: Jun 17, 2020 8:29:45 PM
 * This model is used to create regex object that will be used in SecurityXMLHandler
 */
public class Regex {
	
	/** Holds the regex name. */
	private String regexName;
	
	/** Holds regex value. */
	private String regexValue;
	
	/**
	 * Instantiates a new regex.
	 *
	 * @param regexName the regex name
	 * @param regexValue the regex value
	 */
	public Regex(String regexName, String regexValue) {
		this.regexName = regexName;
		this.regexValue = regexValue;
	}
	
	/**
	 * Gets the regex name.
	 *
	 * @return the regexName
	 */
	public String getRegexName() {
		return regexName;
	}
	
	/**
	 * Gets the regex value.
	 *
	 * @return the regexValue
	 */
	public String getRegexValue() {
		return regexValue;
	}
}
