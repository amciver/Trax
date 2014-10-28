package com.experiment.trax.utils;

import android.util.Log;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 12/16/12
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class String {

    public static java.lang.String trim(java.lang.String s) {
        return s.replace(java.lang.String.valueOf((char) 160), " ").trim();
    }

    public static java.lang.String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static java.lang.String formatPhoneNumber(java.lang.String s, java.lang.String seperator) {
        if (s != null && !s.isEmpty()) {
            if (s.length() == 10) {
                java.lang.String part1 = s.substring(0, 3);
                java.lang.String part2 = s.substring(3, 6);
                java.lang.String part3 = s.substring(6, 10);
                return part1 + seperator + part2 + seperator + part3;
            } else if (s.length() == 7) {
                java.lang.String part1 = s.substring(0, 3);
                java.lang.String part2 = s.substring(3, 7);
                return part1 + seperator + part2;
            }
        }
        return s;
    }

    /* Takes a String in HHmm format and returns a String in h:mm aa format  */
    public static java.lang.String formatHours(java.lang.String s) {
        if (s != null && s.length() > 0) {
            try {
                DateTime open = DateTime.parse(
                        DateTime.now().getDayOfMonth() + "-" +
                                DateTime.now().getMonthOfYear() + "-" +
                                DateTime.now().getYear() + " " + s,
                        DateTimeFormat.forPattern("dd-MM-yyyy HHmm"));

                return open.toString(DateTimeFormat.forPattern("h:mm aa"));
            } catch (Exception e) {
                Log.e("String", "Failure when trying to format [" + s + "] as h:mm aa");
            }
        }
        return "";
    }
}
