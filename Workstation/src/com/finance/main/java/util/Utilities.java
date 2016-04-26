package com.finance.main.java.util;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Utilities
{
	public static Date addDaysToSqlDate(Date date, int days)
	{
		return new Date(date.getTime() + (days * 24 * 3600 * 1000L));
	}
	
	public static Date subtractDaysFromSqlDate(Date date, int days)
	{
		return new Date(date.getTime() - (days * 24 * 3600 * 1000L));
	}
	
	public static String sqlDateToString(Date date)
	{
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		return formatter.format(date);
	}
	
	public static java.util.Date stringToUtilDate(String date) throws ParseException
	{
		return new SimpleDateFormat("yyyy-MM-dd").parse(date);
	}
	
	public static Date stringToSqlDate(String date, String format)
	{
		java.util.Date utilDate = new java.util.Date();
		
		try
		{
			utilDate = new SimpleDateFormat(format).parse(date);
		}
		catch(Exception e)
		{
			System.out.println("stringToSqlDate(): "+e.getLocalizedMessage());
		}
		
		return (utilDate != null) ? new Date(utilDate.getTime()) : null;
	}
	
	public static Date stringToSqlDate(String date)
	{
		return stringToSqlDate(date, "yyyy-MM-dd");
	}

	/**
	 * Returns the differences between two dates in terms of days
	 * @param start The starting date of the given range
	 * @param end The end date of the given range
	 * @return The number of days difference between the two days
	 */
	public static int dateDiff(Date start, Date end)
	{
		return (int) ((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24));
	}// dateDiff(Date,Date)
}
