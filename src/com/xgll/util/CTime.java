package com.xgll.util;

import java.util.*;

/**
 * 获得格式化的时间
 * @author ywh
 *
 */
public class CTime
{
    /**
     * 取得本地系统的时间，时间格式由参数决定
     * 
     * @param format 时间格式由常量决定
     * @return String 具有format格式的字符串
     */
    public synchronized static String getTime(int format)
    {
        StringBuffer cTime = new StringBuffer(10);
        Calendar time = Calendar.getInstance();
        int miltime = time.get(Calendar.MILLISECOND);
        int second = time.get(Calendar.SECOND);
        int minute = time.get(Calendar.MINUTE);
        int hour = time.get(Calendar.HOUR_OF_DAY);
        int day = time.get(Calendar.DAY_OF_MONTH);
        int month = time.get(Calendar.MONTH) + 1;
        int year = time.get(Calendar.YEAR);
        if (format != 14)
        {
            if (year >= 2000)
            {
                year = year - 2000;
            }
            else
            {
                year = year - 1900;
            }
        }
        if (format >= 2)
        {
            if (format == 14)
            {
                cTime.append(year);
            }
            else
            {
                cTime.append(getFormatTime(year, 2));
            }
        }
        if (format >= 4)
        {
            cTime.append(getFormatTime(month, 2));
        }
        if (format >= 6)
        {
            cTime.append(getFormatTime(day, 2));
        }
        if (format >= 8)
        {
            cTime.append(getFormatTime(hour, 2));
        }
        if (format >= 10)
        {
            cTime.append(getFormatTime(minute, 2));
        }
        if (format >= 12)
        {
            cTime.append(getFormatTime(second, 2));
        }
        if (format >= 15)
        {
            cTime.append(getFormatTime(miltime, 3));
        }
        return cTime.toString();
    }

    /**
     * 产生任意位的字符串
     * 
     * @param time 要转换格式的时间
     * @param format 转换的格式
     * @return String 转换的时间
     */
    private synchronized static String getFormatTime(int time, int format)
    {
        StringBuffer numm = new StringBuffer();
        int length = String.valueOf(time).length();

        if (format < length)
        {
            return null;
        }

        for (int i = 0; i < format - length; i++)
        {
            numm.append("0");
        }
        numm.append(time);
        return numm.toString().trim();
    }
    
    public static final int YYMMDDhhmmssxxx = 15;

    public static final int YYYYMMDDhhmmss = 14;

    public static final int YYMMDDhhmmss = 12;

    public static final int YYMMDDhhmm = 10;

    public static final int YYMMDDhh = 8;
}
