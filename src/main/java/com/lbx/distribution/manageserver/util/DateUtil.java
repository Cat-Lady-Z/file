package com.lbx.distribution.manageserver.util;


import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.common.TimeUnitCode;
import com.lbx.distribution.manageserver.entity.DateEntity;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间戳处理类
 */
public class DateUtil {

    /**
     * 返回当前时间的秒数
     *
     * @return
     */
    public static Long unixTime() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 把表转换为Date
     *
     * @param seconds
     * @return
     */
    public static Date fromUnixTime(Long seconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new Date(seconds * 1000L);
    }

    /**
     * 获取当前时间
     *
     * @return String
     */
    public static String getCurrentDateString() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(System.currentTimeMillis());
    }

    /**
     * 获取当前时间
     *
     * @return Date
     */
    public static Date getCurrentDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(System.currentTimeMillis());
        return sdf.parse(format);
    }

    /**
     * 获取当前时间
     *
     * @return Timestamp
     */
    public static Timestamp getCurrentTimestamp() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(System.currentTimeMillis());
        System.out.println("date:" + date);

        return Timestamp.valueOf(date);
    }

    /**
     * 把string转换为Timstamp
     *
     * @return Timestamp
     */
    public static Timestamp getTimestampByString(String time) throws ParseException {
        if (time == null) {
            return null;
        }
        return Timestamp.valueOf(time);
    }

    /**
     * 判断起始时间是否在一个小时及以内
     * @param startTime
     * @param endTime
     * @return
     */
    public static Boolean isHour(Date startTime, Date endTime) throws ParseException {
        Long cha = getTimeSpace(startTime, endTime);

        double result = cha * 1.0 / (1000 * 60 );
        if(result <= 60){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断起始时间是否在二十四小时及以内
     * @param startTime
     * @param endTime
     * @return
     */
    public static Boolean isDay(Date startTime, Date endTime) throws ParseException {
        Long cha = getTimeSpace(startTime, endTime);

        double result = cha * 1.0 / (1000 * 60 * 60);
        if(result <= 24){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断起始时间是否超过一个月（30天）
     * @param startTime
     * @param endTime
     * @return
     */
    public static Boolean isMonth(Date startTime, Date endTime) throws ParseException {
        Long cha = getTimeSpace(startTime, endTime);

        Long result = cha  / (1000 * 60 * 60 * 24);
        if(result <= 30){
            return true;
        }else{
            if (startTime.getYear() == endTime.getYear() &&startTime.getMonth() == endTime.getMonth()) {
                return true;
            }
            return false;
        }
    }

    /**
     * 判断起始时间是在一年内
     * @param startTime
     * @param endTime
     * @return
     */
    public static Boolean isYear(Date startTime, Date endTime) throws ParseException {
        if (startTime.getYear() == endTime.getYear()) {
            return true;
        }

        Long cha = getTimeSpace(startTime, endTime);
        Long result = cha  / (1000 * 60 * 60 * 24);
        if(result <= 365){
            return true;
        }else {
            return false;
        }
    }

    private static Long getTimeSpace(Date startTime, Date endTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(startTime);
        Date start = sdf.parse(format);
        Date end = sdf.parse(sdf.format(endTime));
        long result = end.getTime() - start.getTime();

        if ( result < 0 ) {
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "起始日期有误！");
        }

        return result;
    }

    /**
     * 获取日期的年月日小时
     * @param date
     * @return
     */
    public static DateEntity getDateEntity(Date date) {

        DateEntity dateEntity = new DateEntity();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);					//放入Date类型数据

        dateEntity.setYear(calendar.get(Calendar.YEAR));					//获取年份
        dateEntity.setMonth(calendar.get(Calendar.MONTH) + 1);					//获取月份
        dateEntity.setDay(calendar.get(Calendar.DATE));					//获取日
        dateEntity.setHour(calendar.get(Calendar.HOUR_OF_DAY));				//时（24小时制）
        dateEntity.setMinute(calendar.get(Calendar.MINUTE));				//分钟
        dateEntity.setMinute(calendar.get(Calendar.SECOND));				//秒

        return dateEntity;
    }

    /**
     * 将日期类型转成long型（1970... + 时分秒[前端数据]）
     * @param time
     */
    public static Long parseLongByDate(String time) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date date = sdf.parse(time);

        long timeTime = date.getTime();

        return timeTime;
    }

    /**
     * 将long型(1970... + 时分秒[前端数据] )转换为时间（时分秒）
     * @param timeLong
     * @return
     */
    public static String parseDateStrByLong(Long timeLong) throws ParseException {

        Date dateOld = new Date(timeLong);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String date = sdf.format(dateOld);

        return date;
    }

    /**
     * 获取today 00:00:00时间
     * @return
     */
    public static Date getTodayStartTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);

        return  todayStart.getTime();
    }

    /**
     * 获取today 23:59:59 999毫秒的最后时间
     * @return
     */
    public static Date getTodayEndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return  todayEnd.getTime() ;
    }

    /**
     * x轴时间点的封装
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> getTimeXAxis(Date startTime, Date endTime) throws ParseException {
        List<String> resultList = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);

        int i = 1;
        if ( isDay(startTime, endTime) ) {
            resultList = new ArrayList<>();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
            String endTimeFormat = sdf.format(endTime);
            String startTimeFormat = sdf.format(startTime);
            Date endParse = sdf.parse(endTimeFormat);
            Date startParse = sdf.parse(startTimeFormat);

            while (startParse.compareTo(endParse) <= 0 ) {
                resultList.add(startTimeFormat);

                calendar.add(Calendar.HOUR_OF_DAY, i);
                startTime = calendar.getTime();
                startTimeFormat = sdf.format(startTime);
                startParse = sdf.parse(startTimeFormat);
            }
        } else if ( isMonth(startTime, endTime) ) {
            resultList = new ArrayList<>();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String endTimeFormat = sdf.format(endTime);
            String startTimeFormat = sdf.format(startTime);
            Date endParse = sdf.parse(endTimeFormat);
            Date startParse = sdf.parse(startTimeFormat);

            while (startParse.compareTo(endParse) <= 0 ) {
                resultList.add(startTimeFormat);

                calendar.add(Calendar.DATE, i);
                startTime = calendar.getTime();
                startTimeFormat = sdf.format(startTime);
                startParse = sdf.parse(startTimeFormat);
            }
        } else if (isYear(startTime, endTime)) {
            //单位为月
            resultList = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String endTimeFormat = sdf.format(endTime);
            String startTimeFormat = sdf.format(startTime);
            Date endParse = sdf.parse(endTimeFormat);
            Date startParse = sdf.parse(startTimeFormat);

            while (startParse.compareTo(endParse) <= 0 ) {
                resultList.add(startTimeFormat);

                calendar.add(Calendar.MONTH, i);
                startTime = calendar.getTime();
                startTimeFormat = sdf.format(startTime);
                startParse = sdf.parse(startTimeFormat);
            }
        } else {
            resultList = new ArrayList<>();
            while ( startTime.getYear() <= endTime.getYear() ) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                String format = sdf.format(startTime);
                resultList.add(format);

                calendar.add(Calendar.YEAR, i);
                startTime = calendar.getTime();
            }
        }

        return resultList;
    }

    /**
     * 将字符串转成日期（按照不同格式 小时/天/月/年）
     * @param time
     * @return
     */
    public static Date getDateByString(String time, Integer unit) throws ParseException {
        Date date = new Date();
        if ( unit == TimeUnitCode.HOUR ) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
            date = sdf.parse(time);
        }
        if (unit == TimeUnitCode.DAY) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(time);
        }

        if (unit == TimeUnitCode.MONTH) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            date = sdf.parse(time);
        }
        if (unit == TimeUnitCode.YEAR) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            date = sdf.parse(time);
        }

        return date;
    }
}
