package de.drmaxnix.birthdaycountdown;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
	SharedPreferences settings_store;
	
	int[] birthdate = new int[3];
	
	TextView countdown_days;
	TextView countdown_hours;
	TextView countdown_minutes;
	
	LinearLayout date_select_container;
	TextView date_select_text;
	
	LinearLayout milestone_last_birthday_container;
	LinearLayout milestone_350_container;
	LinearLayout milestone_300_container;
	LinearLayout milestone_250_container;
	LinearLayout milestone_200_container;
	LinearLayout milestone_150_container;
	LinearLayout milestone_100_container;
	LinearLayout milestone_75_container;
	LinearLayout milestone_50_container;
	LinearLayout milestone_30_container;
	LinearLayout milestone_15_container;
	LinearLayout milestone_7_container;
	LinearLayout milestone_3_container;
	LinearLayout milestone_1_container;
	LinearLayout milestone_next_birthday_container;
	
	TextView milestone_last_birthday_countdown;
	TextView milestone_350_countdown;
	TextView milestone_300_countdown;
	TextView milestone_250_countdown;
	TextView milestone_200_countdown;
	TextView milestone_150_countdown;
	TextView milestone_100_countdown;
	TextView milestone_75_countdown;
	TextView milestone_50_countdown;
	TextView milestone_30_countdown;
	TextView milestone_15_countdown;
	TextView milestone_7_countdown;
	TextView milestone_3_countdown;
	TextView milestone_1_countdown;
	TextView milestone_next_birthday_countdown;
	
	TextView milestone_last_birthday_age;
	TextView milestone_next_birthday_age;
	
	LinearLayout about_website_button;
	
	Handler update_handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		// OPEN SETTINGS STORE //
		// get object
		settings_store = this.getSharedPreferences("settings", 0);
		
		// load birthdate
		birthdate[0] = settings_store.getInt("birthdate.year", -1);
		birthdate[1] = settings_store.getInt("birthdate.month", -1);
		birthdate[2] = settings_store.getInt("birthdate.day", -1);
		
		
		// BIND VIEWS //
		countdown_days = findViewById(R.id.countdown_days);
		countdown_hours = findViewById(R.id.countdown_hours);
		countdown_minutes = findViewById(R.id.countdown_minutes);
		
		date_select_container = findViewById(R.id.date_select_container);
		date_select_text = findViewById(R.id.date_select_text);
		
		milestone_last_birthday_container = findViewById(R.id.milestone_last_birthday_container);
		milestone_350_container = findViewById(R.id.milestone_350_container);
		milestone_300_container = findViewById(R.id.milestone_300_container);
		milestone_250_container = findViewById(R.id.milestone_250_container);
		milestone_200_container = findViewById(R.id.milestone_200_container);
		milestone_150_container = findViewById(R.id.milestone_150_container);
		milestone_100_container = findViewById(R.id.milestone_100_container);
		milestone_75_container = findViewById(R.id.milestone_75_container);
		milestone_50_container = findViewById(R.id.milestone_50_container);
		milestone_30_container = findViewById(R.id.milestone_30_container);
		milestone_15_container = findViewById(R.id.milestone_15_container);
		milestone_7_container = findViewById(R.id.milestone_7_container);
		milestone_3_container = findViewById(R.id.milestone_3_container);
		milestone_1_container = findViewById(R.id.milestone_1_container);
		milestone_next_birthday_container = findViewById(R.id.milestone_next_birthday_container);
		
		milestone_last_birthday_countdown = findViewById(R.id.milestone_last_birthday_countdown);
		milestone_350_countdown = findViewById(R.id.milestone_350_countdown);
		milestone_300_countdown = findViewById(R.id.milestone_300_countdown);
		milestone_250_countdown = findViewById(R.id.milestone_250_countdown);
		milestone_200_countdown = findViewById(R.id.milestone_200_countdown);
		milestone_150_countdown = findViewById(R.id.milestone_150_countdown);
		milestone_100_countdown = findViewById(R.id.milestone_100_countdown);
		milestone_75_countdown = findViewById(R.id.milestone_75_countdown);
		milestone_50_countdown = findViewById(R.id.milestone_50_countdown);
		milestone_30_countdown = findViewById(R.id.milestone_30_countdown);
		milestone_15_countdown = findViewById(R.id.milestone_15_countdown);
		milestone_7_countdown = findViewById(R.id.milestone_7_countdown);
		milestone_3_countdown = findViewById(R.id.milestone_3_countdown);
		milestone_1_countdown = findViewById(R.id.milestone_1_countdown);
		milestone_next_birthday_countdown = findViewById(R.id.milestone_next_birthday_countdown);
		
		milestone_last_birthday_age = findViewById(R.id.milestone_last_birthday_age);
		milestone_next_birthday_age = findViewById(R.id.milestone_next_birthday_age);
		
		about_website_button = findViewById(R.id.about_website_button);
		
		
		// SET VERSION TEXT //
		try {
			// get version of the app
			PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
			String version = pInfo.versionName;
			
			// set text
			TextView version_text = findViewById(R.id.version);
			version_text.setText(version);
			
		} catch(PackageManager.NameNotFoundException e){
			e.printStackTrace();
		}
		
		
		// INITIALIZE DATE SELECT //
		// date picker dialog
		DatePickerDialog.OnDateSetListener date_select_picker = (view, year, month, day) -> {
			// UPDATE SETTINGS-STORE //
			// get editor object
			SharedPreferences.Editor editor = settings_store.edit();
			
			// store
			editor.putInt("birthdate.year", year);
			editor.putInt("birthdate.month", month);
			editor.putInt("birthdate.day", day);
			
			// apply changes
			editor.apply();
			
			
			// RELOAD VALUES //
			birthdate[0] = year;
			birthdate[1] = month;
			birthdate[2] = day;
			
			
			// UPDATE UI //
			// birth date
			date_select_update();
			
			// overview countdown
			update_overview_countdown();
			
			// milestones
			update_milestones();
		};
		
		// on-click listener
		date_select_container.setOnClickListener(view -> {
			// get default values for date picker
			int default_year;
			int default_month;
			int default_day;
			
			if(birthdate[0] > -1 && birthdate[1] > -1 && birthdate[2] > -1){
				// use stored date
				default_year = birthdate[0];
				default_month = birthdate[1];
				default_day = birthdate[2];
				
			} else {
				// no date set yet, use today
				final Calendar now = Calendar.getInstance();
				default_year = now.get(Calendar.YEAR);
				default_month = now.get(Calendar.MONTH);
				default_day = now.get(Calendar.DAY_OF_MONTH);
			}
			
			// open date picker dialog
			new DatePickerDialog(MainActivity.this, date_select_picker, default_year, default_month, default_day).show();
		});
		
		// load date into textview
		date_select_update();
		
		
		// WEBSITE-BUTTON ONCLICK CALLBACK //
		about_website_button.setOnClickListener(v -> this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://birthdaycountdown.drmaxnix.de"))));
		
		
		// START PAGE UPDATE HANDLER //
		update_handler.post(update_runnable);
	}
	
	
	/*
		HELPER: Update value of date select text
	*/
	private void date_select_update(){
		if(birthdate[0] > -1 && birthdate[1] > -1 && birthdate[2] > -1){
			// get date object (month starts with 0)
			Date date = new GregorianCalendar(birthdate[0], birthdate[1], birthdate[2]).getTime();
			
			// format date
			SimpleDateFormat date_format = new SimpleDateFormat("dd MMM yyyy", Locale.US);
			String date_text_formatted = date_format.format(date);
			
			// display
			date_select_text.setText(date_text_formatted);
			
		} else {
			// no date set yet, make it a button
			date_select_text.setText(R.string.select_date_button);
		}
	}
	
	
	/*
		RUNNABLE: Update page
	*/
	Runnable update_runnable = new Runnable(){
		public void run(){
			// UPDATE STUFF //
			// overview countdown
			update_overview_countdown();
			
			// milestones
			update_milestones();
			
			
			// CALL AGAIN AFTER DELAY //
			update_handler.postDelayed(this, 1000);
		}
	};
	
	/*
		HELPER: Update overview countdown
	*/
	private void update_overview_countdown(){
		// MAYBE DISPLAY PLACEHOLDER //
		if(birthdate[0] <= -1 || birthdate[1] <= -1 || birthdate[2] <= -1){
			countdown_days.setText(R.string.countdown_days_placeholder);
			countdown_hours.setText(R.string.countdown_hours_placeholder);
			countdown_minutes.setText(R.string.countdown_minutes_placeholder);
			return;
		}
		
		
		// GET COUNTDOWN //
		// find next birthday
		Birthday birthday = Birthday.find_next(birthdate);
		
		// format millis left
		String[] countdown = format_countdown(birthday.millis_left());
		
		// set texts
		countdown_days.setText(countdown[0]);
		countdown_hours.setText(countdown[1]);
		countdown_minutes.setText(countdown[2]);
	}
	
	/*
		HELPER: Update milestones
	*/
	private void update_milestones(){
		// PRELOAD RESOURCES //
		// colors
		int color_theme = ContextCompat.getColor(this, R.color.theme);
		int color_theme_dark = ContextCompat.getColor(this, R.color.theme_dark);
		int color_gray_dark = ContextCompat.getColor(this, R.color.gray_dark);
		
		// mini-countdown format
		String milestone_mini_countdown = getResources().getString(R.string.milestone_mini_countdown);
		
		// milestone list elements
		TreeMap<Integer, LinearLayout> container_list = new TreeMap<>();
		TreeMap<Integer, TextView> countdown_list = new TreeMap<>();
		container_list.put(350, milestone_350_container); countdown_list.put(350, milestone_350_countdown);
		container_list.put(300, milestone_300_container); countdown_list.put(300, milestone_300_countdown);
		container_list.put(250, milestone_250_container); countdown_list.put(250, milestone_250_countdown);
		container_list.put(200, milestone_200_container); countdown_list.put(200, milestone_200_countdown);
		container_list.put(150, milestone_150_container); countdown_list.put(150, milestone_150_countdown);
		container_list.put(100, milestone_100_container); countdown_list.put(100, milestone_100_countdown);
		container_list.put(75, milestone_75_container); countdown_list.put(75, milestone_75_countdown);
		container_list.put(50, milestone_50_container); countdown_list.put(50, milestone_50_countdown);
		container_list.put(30, milestone_30_container); countdown_list.put(30, milestone_30_countdown);
		container_list.put(15, milestone_15_container); countdown_list.put(15, milestone_15_countdown);
		container_list.put(7, milestone_7_container); countdown_list.put(7, milestone_7_countdown);
		container_list.put(3, milestone_3_container); countdown_list.put(3, milestone_3_countdown);
		container_list.put(1, milestone_1_container); countdown_list.put(1, milestone_1_countdown);
		container_list.put(0, milestone_next_birthday_container); countdown_list.put(0, milestone_next_birthday_countdown);
		
		
		// CLEAR IF BIRTH DATE NOT SET YET //
		if(birthdate[0] <= -1 || birthdate[1] <= -1 || birthdate[2] <= -1){
			// last and next birthday
			milestone_last_birthday_age.setText(format_ordinal(0));
			milestone_next_birthday_age.setText(format_ordinal(1));
			
			// elements
			for(Integer milestone : container_list.descendingKeySet()){
				// get views
				LinearLayout container = container_list.get(milestone);
				TextView countdown = countdown_list.get(milestone);
				assert container != null;
				assert countdown != null;
				
				// clear
				container.setBackgroundColor(color_gray_dark);
				countdown.setText("");
			}
			
			// ignore the rest
			return;
		}
		
		
		// GET VALUES //
		// find next birthday
		Birthday birthday = Birthday.find_next(birthdate);
		
		// get millis left
		double millis_left = birthday.millis_left();
		
		// convert to days
		int date_diff_days = (int)Math.floor(millis_left / DateUtils.DAY_IN_MILLIS);
		
		// make diff really big if you're unborn
		if(birthday.age() <= 0){
			date_diff_days = 999;
		}
		
		// get ages
		int next_birthday_age = Math.max(1, birthday.age());
		int last_birthday_age = next_birthday_age - 1;
		
		
		// FILL IN LAST AND NEXT BIRTHDAY //
		// last
		milestone_last_birthday_age.setText(format_ordinal(last_birthday_age));
		if(birthday.age() > 0){
			milestone_last_birthday_container.setBackgroundColor(color_theme);
			milestone_last_birthday_countdown.setText(R.string.milestone_completed);
			
		} else {
			milestone_last_birthday_container.setBackgroundColor(color_theme_dark);
			milestone_last_birthday_countdown.setText("");
		}
		
		// next
		milestone_next_birthday_age.setText(format_ordinal(next_birthday_age));
		
		
		// CHECK ALL //
		boolean found_next = false;
		for(Integer milestone : container_list.descendingKeySet()){
			// get views
			LinearLayout container = container_list.get(milestone);
			TextView countdown = countdown_list.get(milestone);
			assert container != null;
			assert countdown != null;
			
			// not born yet?
			if(birthday.age() <= 0){
				container.setBackgroundColor(color_gray_dark);
				countdown.setText("");
				continue;
			}
			
			// completed?
			if(date_diff_days < milestone){
				container.setBackgroundColor(color_theme);
				countdown.setText(R.string.milestone_completed);
				continue;
			}
			
			// next to complete?
			if(!found_next){
				container.setBackgroundColor(color_theme_dark);
				
				String[] countdown_string = format_countdown(millis_left - (DateUtils.DAY_IN_MILLIS * milestone), false);
				String[] significant = get_most_significant_segment(countdown_string);
				countdown.setText(String.format(milestone_mini_countdown, significant[0], significant[1]));
				
				found_next = true;
				continue;
			}
			
			// default
			container.setBackgroundColor(color_gray_dark);
			countdown.setText("");
		}
	}
	
	/*
		TOOL: Convert millis to displayable days, hours and minutes
	*/
	private String[] format_countdown(double millis_left){
		return format_countdown(millis_left, true);
	}
	private String[] format_countdown(double millis_left, boolean fill_up_with_zeroes){
		// GET VALUES //
		// make up for the missing seconds display
		millis_left += DateUtils.MINUTE_IN_MILLIS;
		
		// days
		int date_diff_days = (int)Math.floor(millis_left / DateUtils.DAY_IN_MILLIS);
		millis_left -= (date_diff_days * DateUtils.DAY_IN_MILLIS);
		
		// hours
		int date_diff_hours = (int)Math.floor(millis_left / DateUtils.HOUR_IN_MILLIS);
		millis_left -= (date_diff_hours * DateUtils.HOUR_IN_MILLIS);
		
		// minutes
		int date_diff_minutes = (int)Math.floor(millis_left / DateUtils.MINUTE_IN_MILLIS);
		
		
		// GET TEXTS //
		// days
		String countdown_segment_days = Integer.toString(date_diff_days);
		
		// hours
		String countdown_segment_hours = "";
		if(fill_up_with_zeroes && date_diff_hours < 10){
			countdown_segment_hours += "0";
		}
		countdown_segment_hours += Integer.toString(date_diff_hours);
		
		// minutes
		String countdown_segment_minutes = "";
		if(fill_up_with_zeroes && date_diff_minutes < 10){
			countdown_segment_minutes += "0";
		}
		countdown_segment_minutes += Integer.toString(date_diff_minutes);
		
		
		// RETURN //
		return new String[]{countdown_segment_days, countdown_segment_hours, countdown_segment_minutes};
	}
	
	/*
		TOOL: Get most significant nonnull countdown segment
	*/
	private String[] get_most_significant_segment(String[] countdown){
		if(countdown[0].replace("0", "").length() > 0) {
			return new String[]{countdown[0], "d"};
		}
		if(countdown[1].replace("0", "").length() > 0){
			return new String[]{countdown[1], "h"};
		}
		
		return new String[]{countdown[2], "m"};
	}
	
	/*
		TOOL: Format number as ordinal string
	*/
	private static String format_ordinal(int number){
		String[] suffix_list = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
		switch(number % 100){
			case 11:
			case 12:
			case 13:
				return number + "th";
			default:
				return number + suffix_list[number % 10];
		}
	}
}