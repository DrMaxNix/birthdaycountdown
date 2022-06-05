package de.drmaxnix.birthdaycountdown;

import android.icu.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Birthday {
	private final int age;
	private final int[] date;
	
	/*
		CONSTRUCTOR: Get new birthday object
	*/
	private Birthday(int age, int[] date){
		this.age = age;
		this.date = date;
	}
	
	/*
		HELPER: Find next birthday and return new birthday object
	*/
	public static Birthday find_next(int[] birthdate){
		// FIND DATE //
		final Calendar now = Calendar.getInstance();
		
		// try this year
		GregorianCalendar birthday_try = new GregorianCalendar(now.get(Calendar.YEAR), birthdate[1], birthdate[2]);
		
		// already in past? use next year!
		if(birthday_try.getTime().getTime() < now.getTime().getTime()){
			birthday_try = new GregorianCalendar(now.get(Calendar.YEAR) + 1, birthdate[1], birthdate[2]);
		}
		
		
		// GET VALUES //
		int year = birthday_try.get(Calendar.YEAR);
		int month = birthday_try.get(Calendar.MONTH);
		int day = birthday_try.get(Calendar.DAY_OF_MONTH);
		
		
		// GET AGE //
		int age = year - birthdate[0];
		
		
		// RETURN NEW BIRTHDAY OBJECT //
		return new Birthday(age, new int[]{year, month, day});
	}
	
	/*
		GETTER: Get age
	*/
	public int age(){
		return age;
	}
	
	/*
		GETTER: Get millis left till birthday
	*/
	public double millis_left(){
		// GET DATES //
		// objects
		Date date_now = new Date();
		Date date_birthday;
		
		// load birthday date
		date_birthday = new GregorianCalendar(date[0], date[1], date[2]).getTime();
		
		
		// GET TIME DIFFERENCE //
		// millis
		double date_diff_millis = date_birthday.getTime() - date_now.getTime();
		
		// make sure it's not negative
		date_diff_millis = Math.max(date_diff_millis, 0);
		
		
		// RETURN //
		return date_diff_millis;
	}
}
