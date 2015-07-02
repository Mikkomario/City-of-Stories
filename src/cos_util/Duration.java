package cos_util;

import flow_recording.ObjectFormatException;

/**
 * Duration is a time period that consists of hours, minutes and seconds
 * @author Mikko Hilpinen
 * @since 2.7.2015
 */
public class Duration implements Comparable<Duration>
{
	// ATTRIBUTES	-----------------------
	
	private int hours, minutes, seconds;
	
	
	// CONSTRUCTOR	-----------------------
	
	/**
	 * Creates a new duration
	 * @param hours The hours in duration
	 * @param minutes The minutes in duration (in addition to the hours)
	 * @param seconds The seconds in duration (in addition to the hours and minutes)
	 */
	public Duration(int hours, int minutes, int seconds)
	{
		initialize(hours, minutes, seconds);
	}
	
	/**
	 * Creates a new duration
	 * @param minutes The minutes in duration
	 * @param seconds The seconds in duration (in addition to the minutes)
	 */
	public Duration(int minutes, int seconds)
	{
		initialize(0, minutes, seconds);
	}
	
	/**
	 * Creates a new duration
	 * @param seconds The seconds in duration
	 */
	public Duration(int seconds)
	{
		initialize(0, 0, seconds);
	}
	
	/**
	 * Creates a new duration by parsing it from a string
	 * @param s The string the duration is parsed from
	 * @throws ObjectFormatException If the string was malformed
	 */
	public Duration(String s) throws ObjectFormatException
	{
		// The correct format is hh:mm:ss, where zeroes can be omitted
		String[] parts = s.split("\\:");
		try
		{
			int seconds = 0;
			int minutes = 0;
			int hours = 0;
			int index = parts.length - 1;
			
			seconds = Integer.parseInt(parts[index]);
			if (parts.length > 1)
				minutes = Integer.parseInt(parts[--index]);
			if (parts.length > 2)
				hours = Integer.parseInt(parts[--index]);
			
			initialize(hours, minutes, seconds);
		}
		catch (NumberFormatException e)
		{
			throw new ObjectFormatException("Can't parse a duration from " + s);
		}
	}
	
	
	// IMPLEMENTED METHODS	---------------------

	@Override
	public int compareTo(Duration o)
	{
		return getTotalSeconds() - o.getTotalSeconds();
	}
	
	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();
		if (getHours() != 0)
			s.append(getHours() + ":");
		if (getTotalMinutes() != 0)
			s.append(getMinutes() + ":");
		s.append(getSeconds());
		
		return s.toString();
	}
	
	
	// GETTERS & SETTERS	------------------
	
	/**
	 * @return The amount of hours in the duration
	 */
	public int getHours()
	{
		return this.hours;
	}
	
	/**
	 * @return The amount of minutes in the duration (besides complete hours)
	 */
	public int getMinutes()
	{
		return this.minutes;
	}
	
	/**
	 * @return The amount of seconds in the duration (besides the complete hours and minutes)
	 */
	public int getSeconds()
	{
		return this.seconds;
	}
	
	/**
	 * @return The amount of minutes in the duration in total
	 */
	public int getTotalMinutes()
	{
		return getHours() * 60 + getMinutes();
	}
	
	/**
	 * @return The amount of seconds in the duration in total
	 */
	public int getTotalSeconds()
	{
		return getTotalMinutes() * 60 + getSeconds();
	}

	
	// OTHER METHODS	-------------------
	
	private void initialize(int hours, int minutes, int seconds)
	{
		this.seconds = seconds % 60;
		this.minutes = (seconds / 60 + minutes) % 60;
		this.hours = (minutes + seconds / 60) / 60 + hours;
	}
}
