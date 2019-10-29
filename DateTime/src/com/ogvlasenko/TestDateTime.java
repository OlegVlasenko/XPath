package com.ogvlasenko;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TestDateTime {

    public static void main(String[] args){

        Date currentDate = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(formatter.format(currentDate));

        Date date2 = new Date(converLocalTimeToUtcTime(currentDate.toInstant().toEpochMilli()));

        System.out.println(formatter.format(date2));

        Date date3 = convertLocalTimestamp(currentDate.toInstant().toEpochMilli());

        System.out.println(formatter.format(date3));
    }

    public static long getLocalToUtcDelta() {
        Calendar local = Calendar.getInstance();
        local.clear();
        local.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
        return local.getTimeInMillis();
    }

    public static long converLocalTimeToUtcTime(long timeSinceLocalEpoch) {
        return timeSinceLocalEpoch + getLocalToUtcDelta();
    }

    public static Date convertLocalTimestamp(long millis)
    {
        TimeZone tz = TimeZone.getDefault();
        Calendar c = Calendar.getInstance(tz);
        long localMillis = millis;
        int offset, time;

        c.set(1970, Calendar.JANUARY, 1, 0, 0, 0);

        // Add milliseconds
        while (localMillis > Integer.MAX_VALUE)
        {
            c.add(Calendar.MILLISECOND, Integer.MAX_VALUE);
            localMillis -= Integer.MAX_VALUE;
        }
        c.add(Calendar.MILLISECOND, (int)localMillis);

        // Stupidly, the Calendar will give us the wrong result if we use getTime() directly.
        // Instead, we calculate the offset and do the math ourselves.
        time = c.get(Calendar.MILLISECOND);
        time += c.get(Calendar.SECOND) * 1000;
        time += c.get(Calendar.MINUTE) * 60 * 1000;
        time += c.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000;
        offset = tz.getOffset(c.get(Calendar.ERA), c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.DAY_OF_WEEK), time);

        return new Date(millis - offset);
    }
}
