package com.starschina.sdk.demo.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/10/19.
 */
public class DateUtils {
    /**
     * 获取今天之前的时间
     *
     * @param i
     * @return
     */
    public static  String getFormerlyTimeString(int i) {
        String result = getFormerlyTime(i);
        StringBuilder sb = new StringBuilder();
        String[] sp = result.split("/");
        if (sp[1].startsWith("0")) {
            sb.append(sp[1].substring(1));
        } else {
            sb.append(sp[1]);
        }
        sb.append("月");
        if (sp[2].startsWith("0")) {
            sb.append(sp[2].substring(1));
        } else {
            sb.append(sp[2]);
        }
        sb.append("日");
        return sb.toString();
    }

    public static String getFormerlyTime(int i) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd ");
        Calendar cal = Calendar.getInstance();
        TimeZone localZone = cal.getTimeZone();
        //设定SDF的时区为本地
        simpleDateFormat.setTimeZone(localZone);
        cal.add(Calendar.DATE, -i);
//        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String date = simpleDateFormat.format(cal.getTime());
        String result = date.substring(2, date.length());
        return result;
    }

    public static String getHourAndMin(long m) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(TimeZone.getDefault());
        String dateString = formatter.format(m);
        return dateString;
    }
}
