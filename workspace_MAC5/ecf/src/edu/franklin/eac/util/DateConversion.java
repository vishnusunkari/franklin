package edu.franklin.eac.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConversion {
	
	public Date formatStringToDate(String dateStr) {
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MM/dd/yyyy");
		Date dateObj = null;
		try	{
			dateObj = formatter.parse(dateStr);          
		} catch (ParseException e){
			e.printStackTrace();
		}
		System.out.println("Date Object is : " + dateObj); //using the date object from earlier
		return dateObj;
	}
	public String formatDateToString(Date dateObj) {
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MM/dd/yyyy");
		String dateStr = formatter.format(dateObj);
		System.out.println("Date String is : " + dateStr); //using the date object from earlier
		return dateStr;
	}

}
