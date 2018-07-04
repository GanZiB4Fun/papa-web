/*
 * Copyright 2012 by FBD Corporation.
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * FBD Corporation ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with FBD.
 *
 */
package com.ganzib.papa.support.date;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.Date;


/**
 * <pre>
 * 日期工具类
 * </pre>
 * <p>
 * <br>
 *
 * @author
 * @version 1.0
 * @see
 * @since 1.0
 */

public class DateUtils {


    public static enum DatePattern {
        DEFAULT("yyyy-MM-dd HH:mm:ss"),
        PATTERN_ALL("yyyy-MM-dd HH:mm:ss.S"),
        PATTERN_ALL_NOSECOND("yyyy-MM-dd HH:mm"),
        PATTERN_ALL_SLASH("yyyy/MM/dd HH:mm:ss"),
        PATTERN_ALL_NOSPLIT("yyyyMMddHHmmss"),
        PATTERN_ALL_NOSPLIT_EXTENDS("yyyyMMddHHmmssSSS"),
        PATTERN_TIME("HH:mm:ss"),
        PATTERN_TIME_NOSECOND("HH:mm"),
        PATTERN_TIME_NOSPLIT("HHmmss"),
        PATTERN_TIME_ONLYSECOND(":ss"),
        PATTERN_DATE("yyyy-MM-dd"),
        PATTERN_DATE_SLASH("yyyy/MM/dd"),
        PATTERN_DATE_NOYEAR("MM-dd"),
        PATTERN_DATE_NODAY("yyyy-MM"),
        PATTERN_DATE_EXCEPTYEAR("MM月dd日 HH:mm"),
        PATTERN_DATE_YDMHMS("yyyy-MM-dd HH:mm:ss"),
        PATTERN_DATE_YMD_POINT_HM("yyyy.MM.dd HH:mm"),
        PATTERN_DATA_YMDHM("yyyyMMddHHmm");

        private DatePattern(String pattern) {

            this.pattern = pattern;
        }

        private String pattern;

        public String getPattern() {
            return pattern;
        }

        public String getDateStr(Date date) {
            if (date == null)
                return "";
            DateTime dateTime = new DateTime(date);
            return dateTime.toString(this.getPattern());
        }

        public Date getDate(String dateStr) {
            DateTimeFormatter format = DateTimeFormat.forPattern(this.getPattern());
            return DateTime.parse(dateStr, format).toDate();
        }
    }

    public static String formatDate(Date date, String pattern) {
        if (date == null)
            return "";
        if (pattern == null) {
            return DatePattern.PATTERN_DATE.getDateStr(date);
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(pattern);
    }


    public static Date now() {
        return (new Date());
    }

    public static Long nowDateStamp() {
        return System.currentTimeMillis();
    }

    public static String parseDateStamp(String dateTime) {
        return DatePattern.DEFAULT.getDateStr(new Date(Long.parseLong(dateTime) * 1000L));
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long s) {
        return DatePattern.DEFAULT.getDateStr(new Date(s));
    }

    public static String lastMonth(int num) {
        DateTime dateTime = new DateTime();
        return DatePattern.PATTERN_DATE_NODAY.getDateStr(dateTime.plusMonths(-num).toDate());
    }

    public static Date parseDateTime(String datetime) {
        if ((datetime == null) || (datetime.equals(""))) {
            return null;
        }
        return DatePattern.DEFAULT.getDate(datetime);
    }

    public static Date parseDate(String date) {
        if ((date == null) || (date.trim().equals(""))) {
            return null;
        }
        return DatePattern.PATTERN_DATE.getDate(date);
    }

    public static Date parseDateCustom(String date, String pattern) {
        DateTimeFormatter format = DateTimeFormat.forPattern(pattern);
        return DateTime.parse(date, format).toDate();
    }

    public static Date parseDateAll(String date) {
        if ((date == null) || (date.trim().equals(""))) {
            return null;
        } else {
            if (date.length() > 10) {
                return DatePattern.DEFAULT.getDate(date);
            } else {
                return DatePattern.PATTERN_DATE.getDate(date);
            }
        }
    }

    public static Date parseDate(Date datetime) {
        if (datetime == null) {
            return null;
        } else {
            return DatePattern.PATTERN_DATE.getDate(DatePattern.PATTERN_DATE.getDateStr(datetime));
        }
    }

    public static Date getDate() {
        return DatePattern.PATTERN_DATE.getDate(DatePattern.PATTERN_DATE.getDateStr(now()));
    }

    public static Date getDateTime() {
        return DatePattern.DEFAULT.getDate(DatePattern.DEFAULT.getDateStr(now()));
    }

    public static Date getTime() {
        return DatePattern.PATTERN_TIME.getDate(DatePattern.PATTERN_TIME.getDateStr(now()));
    }

    @SuppressWarnings("deprecation")
    public static String dateDiff(String startTime, String endTime) {
        DateTime startDateTime = new DateTime(DatePattern.PATTERN_ALL_NOSECOND.getDate(startTime));
        DateTime endDateTime = new DateTime(DatePattern.PATTERN_ALL_NOSECOND.getDate(endTime));
        Duration duration = getDuration(startDateTime.toDate(), endDateTime.toDate());
        System.out.println(duration.getStandardDays());

        if (duration.getStandardSeconds() < 0) {
            return "";
        } else if (duration.getStandardDays() > 0 && (startDateTime.getYear() == endDateTime.getYear())) {
            return startDateTime.toString(DatePattern.PATTERN_DATE.getPattern());
        } else if (duration.getStandardDays() == 0 && duration.getStandardHours() < 1) {
            return duration.getStandardMinutes() + "分钟前";
        } else if (duration.getStandardDays() == 0 && duration.getStandardHours() >= 1) {
            return duration.getStandardHours() + "小时前";
        } else {
            return startDateTime.toString(DatePattern.PATTERN_DATE.getPattern());
        }
    }

    public static String dateSecond(int second) {
        DateTime dateTime = new DateTime();
        return DatePattern.DEFAULT.getDateStr(dateTime.plusSeconds(second).toDate());
    }

    public static String dateBefore(int hour) {
        DateTime dateTime = new DateTime();
        return DatePattern.DEFAULT.getDateStr(dateTime.plusHours(-hour).toDate());
    }

    public static String dateBeforeHm(int hour) {
        DateTime dateTime = new DateTime();
        return DatePattern.PATTERN_ALL_NOSECOND.getDateStr(dateTime.plusHours(-hour).toDate());
    }

    public static long dateMinute(Date startDate, Date endDate) {
        return getDuration(startDate, endDate).getStandardMinutes();
    }

    public static long dateHour(Date startDate, Date endDate) {
        return getDuration(startDate, endDate).getStandardHours();
    }

    private static Duration getDuration(Date startDate, Date endDate) {
        DateTime startDateTime = new DateTime(startDate);
        DateTime endDateTime = new DateTime(endDate);
        return new Duration(startDateTime, endDateTime);
    }

    public static int compare_date(String DATE1, String DATE2) {
        DateTime dateTime1 = new DateTime(DatePattern.DEFAULT.getDate(DATE1));
        DateTime dateTime2 = new DateTime(DatePattern.DEFAULT.getDate(DATE2));
        return (dateTime1.isBefore(dateTime2)) ? 1 : dateTime1.isAfter(dateTime2) ? -1 : 0;
    }

    public static int compare_YMDdate(String DATE1, String DATE2) {
        DateTime dateTime1 = new DateTime(DatePattern.PATTERN_DATE_SLASH.getDate(DATE1));
        DateTime dateTime2 = new DateTime(DatePattern.PATTERN_DATE_SLASH.getDate(DATE2));
        return (dateTime1.isBefore(dateTime2)) ? 1 : dateTime1.isAfter(dateTime2) ? -1 : 0;
    }

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        DateTime dateTime = new DateTime();
        return DatePattern.PATTERN_DATE.getDateStr(dateTime.plusDays(-past).toDate());
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @param seconds
     * @return
     */
    public static String timeStamp2Date(int seconds) {
        return DatePattern.DEFAULT.getDateStr(new Date(Long.valueOf(seconds + "000")));
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDates(String s) {
        long lt = new Long(s);
        return DatePattern.DEFAULT.getDateStr(new Date(lt));
    }

    public static Date getYMDDate(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate();
    }

    public static int getMinuteRegion(Date date) {
        DateTime dateTime = new DateTime(date);
        int minute = dateTime.getMinuteOfHour();
        return (int) minute / 10;
    }

    public static int getDayOfMonth(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.getDayOfMonth();
    }

    public static int getSecondsRegion(Date date) {
        DateTime dateTime = new DateTime(date);
        int seconds = dateTime.getSecondOfMinute();
        return (int) seconds / 5;
    }

    public static int getMinuteOfHour(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.getMinuteOfHour();
    }

    public static int getHourOfDay(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.getHourOfDay();
    }

    public static int getWeekOfYear(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.getWeekOfWeekyear();
    }

    public static int getMonthOfYear(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.getMonthOfYear();
    }

    public static int getYear(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.getYear();
    }

    /**
     * 将时间字符串按所需格式转换
     *
     * @param strDate 时间字符串
     * @param pattern 时间格式
     * @return
     */

    public static Date strToDateByPattern(String strDate, String pattern) {
        DateTimeFormatter format = DateTimeFormat.forPattern(pattern);
        return DateTime.parse(strDate, format).toDate();
    }


    public static Date strToDate(String strDate) {
        return strToDateByPattern(strDate, DatePattern.PATTERN_DATE.getPattern());
    }

    /**
     * 得到当前时间n分钟前/后，以S为秒的时间点
     *
     * @param n
     * @return
     */
    public static Date getOffsetMinDate(int n, int s) {
        DateTime dateTime = new DateTime(new Date());
        return dateTime.plusMinutes(n).withSecondOfMinute(s).toDate();
    }

    /**
     * 得到一个时间延后或前移几天的时间,nowdate为时间,delay为前移或后延的天数
     */

    public static Date getNextNumDay(Date date, Integer delay) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusDays(delay).toDate();
    }

    /**
     * 得到当前时间前N分钟的时间
     */

    public static Date getBeforeNMinuteDate(int n) {
        int number = Math.abs(n);
        return getOffsetMinDate(-number, 0);

    }

    /**
     * 判断给定日期是不是周末
     */
    public static boolean isWeekEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (week == 0 || week == 6) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前时间在不在给定的时间区间内
     */
    public static boolean isOutTime(Integer startHour, Integer endHour) {
        Integer hour = Integer.valueOf(formatDate(new Date(), "HH"));
        if (hour >= startHour && hour < endHour) {
            return true;
        }
        return false;
    }


    public static void main(String[] args) {
        Date date1 = new Date();
        Date date2 = new DateTime().plusHours(22).toDate();
        System.out.println(getPastDate(1));
        System.out.println(dateDiff("2019-03-02 00:00", "2019-03-02 01:00"));
//		String msl="1515413205000";
//	    System.out.println(stampToDates(msl));
//		
//		 int i= compare_date("1995-11-12 15:21", "1999-12-11 09:59");
//	     System.out.println("i=="+i);
//	     Calendar calendar = Calendar.getInstance();
//         calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 48);
//         SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//         System.out.println("48小时前的时间：" + df.format(calendar.getTime()));
//         System.out.println("当前的时间：" + df.format(new Date()));

//		Long s=nowDateStamp();
//		System.out.println(s);
//		System.out.println(parseDateStamp("1474522204"));
//		dateDiff("2016-4-7 13:51", "2016-4-21 14:53");
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		try {
//			Date date = sdf.parse("2016-12-07 11:23");
//			System.out.println(date);
//			System.out.println(parseDateCustom("2016-12-07 11:23", "yyy-MM-dd"));
//			SimpleDateFormat df=new SimpleDateFormat("MM-dd");
//			System.out.println(df.format(parseDateCustom("2016-12-07 11:23", "yyy-MM-dd")));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}


//		String file="http%3A%2F%2Fmashang1.qiniudn.com%2Fnews%2F995050a8979841fe862354d4ad7ef847.jpg";
//		  String output;
//		try {
//			output = URLDecoder.decode(file, "UTF-8");
//			System.out.println(output); 
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		System.out.println(getSpecifiedDayAfter(("2013-11-30")));
//		// 本月的第一天
//		System.out.println(getWeekStartDate(("2013-12-01")));
//		// 本月的最后一天
//		System.out.println(getWeekEndDate(("2013-12-01")));
//
//		// 本月的第一天
//		System.out.println(getMonthStartDate(("2013-12-31")));
//		// 本月的最后一天
//		System.out.println(getMonthEndDate(("2013-12-31")));
//		
//		System.out.println(getWeeksForYear(2011));
//		
//		System.out.println(formatDate(addDay(parseDate("2015-05-27"), -90)));
        //fdfdsfdsfdsfdsfd
//		String stId="";
//		String str="音乐,娱乐";
//		String[] tag=str.split(",");
//		for (String st : tag) {
//			stId+=st+",";
//		}
//		System.out.println(stId.substring(0,stId.lastIndexOf(",")));
//		DecimalFormat    df   = new DecimalFormat("######0.00");  
//		double i = (0.45+Math.random()*(0.60-0.45+0.01));
//		System.out.println(df.format(i));
//		String date="2015-02-11 12:12:24";
//		System.out.println(parseDateTime(date).getTime());
//		long str=parseDateTime(date).getTime();
//		System.out.println(str);

//		  //返回java所支持的全部国家和语言的数组
//		  Locale[] localeList=Locale.getAvailableLocales();
//		  //遍历数组的每个元素,依次获取所支持的国家和语言
//		  for (int i=0;i<localeList.length;i++)
//		  {
//		   //打印出所支持的国家和语言
//		   System.out.println(localeList[i].getDisplayCountry()+"="+localeList[i].getCountry()+"  "+localeList[i].getDisplayLanguage()+"="+localeList[i].getLanguage());
//		  }
//		  System.out.println(new Date());
//		System.out.println(compare_YMDdate(DateUtils.formatDateDTime(), "2017/10/24"));

//		System.out.println(nowDateStamp());
    }
}
