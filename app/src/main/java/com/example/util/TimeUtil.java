package com.example.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    private static String dates;
    private static long days;
    private static long minutes;
    private static long hours;

    public static void setTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        dates = format.format(date);
    }

    private static long getMinutes(String time1, String time2){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            Date d1 = df.parse(time1);
            Date d2 = df.parse(time2);
            long diff = d1.getTime() - d2.getTime();

            days = diff / (1000 * 60 * 60 * 24);
            hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
            minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }
    public static String getDates(){
        return dates;
    }
    public static long getDay(){
        return days;
    }
    public static long getMinutes(){
        return minutes;
    }
    public static long getHours() {
        return hours;
    }
    public static String getCuttentDay(){
        setTime();
        return dates.substring(0,10);
    }
}

