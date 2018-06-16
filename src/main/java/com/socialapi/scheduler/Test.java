package com.socialapi.scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Test {
	public static void main(String[] args) {
		String timeStamp = "2014-12-14T18:23:17+0000";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			System.out.println("Unix timestamp: " + dateFormat.parse(timeStamp).getTime());
		System.out.println(dateFormat2.format(new Date(dateFormat.parse(timeStamp).getTime())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
