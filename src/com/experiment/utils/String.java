package com.experiment.utils;

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

}
