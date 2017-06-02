package com.example.secret.booklist60.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Secret on 2016/12/3.
 * 格式化时间
 */

public class TimeFormat {
    public static final String Format_YearMonDay_Time = "yyyy-MM-dd HH:mm";
    public static final String Format_YearMonDay = "yyyy-MM-dd";
    public static final String Format_MonDay_Time = "MM-dd HH:mm";
    public static final String Format_MonDay = "MM-dd";
    public static final String Format_Time = "HH:mm";

    public static String getChatTime(long time){
        String result = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat(Format_YearMonDay);
        String today1 = sdf1.format(new Date(System.currentTimeMillis()));
        String otherday1 = sdf1.format(new Date(time));
        int y = Integer.valueOf(today1.substring(0,3)) - Integer.valueOf(otherday1.substring(0,3));
        if (y == 0){
            int m = Integer.valueOf(today1.substring(5,6)) - Integer.valueOf(otherday1.substring(5,6));
            if (m==0){
                int d = Integer.valueOf(today1.substring(8,9)) - Integer.valueOf(today1.substring(8,9));
                switch (d){
                    case 0:
                        result = getHour_Min(time);
                        break;
                    case 1:
                        result = "昨天 "+getHour_Min(time);
                        break;
                    case 2:
                        result = "前天 "+getHour_Min(time);
                        break;
                }
            }
            else {
                result = getFormat_MonDay_Time(time);
            }

        }
        else {
            result = getFormat_YearMonDay_Time(time);
        }

        return result;

    }
    public static String getMessageTime(long time){
        String result = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat(Format_YearMonDay);
        String today1 = sdf1.format(new Date(System.currentTimeMillis()));
        String otherday1 = sdf1.format(new Date(time));
        int y = Integer.valueOf(today1.substring(0,3)) - Integer.valueOf(otherday1.substring(0,3));
        if (y == 0){
            int m = Integer.valueOf(today1.substring(5,6)) - Integer.valueOf(otherday1.substring(5,6));
            if (m==0){
                int d = Integer.valueOf(today1.substring(8,9)) - Integer.valueOf(today1.substring(8,9));
                switch (d){
                    case 0:
                        result = getHour_Min(time);
                        break;
                    case 1:
                        result = "昨天 "+getHour_Min(time);
                        break;
                    case 2:
                        result = "前天 "+getHour_Min(time);
                        break;
                }
            }
            else {
                result = getFormat_MonDay(time);
            }

        }
        else {
            result = getFormat_YearMonDay(time);
        }

        return result;
    }

    public static String getHour_Min(long time){
        SimpleDateFormat sdf = new SimpleDateFormat(Format_Time);
        return sdf.format(new Date(time));
    }
    public static String getFormat_MonDay_Time(long time){
        SimpleDateFormat sdf = new SimpleDateFormat(Format_MonDay_Time);
        return sdf.format(new Date(time));
    }
    public static String getFormat_YearMonDay_Time(long time){
        SimpleDateFormat sdf = new SimpleDateFormat(Format_YearMonDay_Time);
        return sdf.format(new Date(time));
    }
    public static String getFormat_YearMonDay(long time){
        SimpleDateFormat sdf = new SimpleDateFormat(Format_YearMonDay);
        return sdf.format(new Date(time));
    }
    public static String getFormat_MonDay(long time){
        SimpleDateFormat sdf = new SimpleDateFormat(Format_MonDay);
        return sdf.format(new Date(time));
    }
}
