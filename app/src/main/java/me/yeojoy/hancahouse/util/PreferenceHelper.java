package me.yeojoy.hancahouse.util;

import android.content.Context;

import me.yeojoy.hancahouse.app.Constants;

/**
 * PreferenceHelper는 좀더 business 적인 접근으로 만듬.
 * PreferenceUtil과 차이를 둬서,
 * KEY와 DEFAULT VALUE를 wrapping해서 좀 더 편하게 사용할 수 있도록 추가함.
 */
public class PreferenceHelper implements Constants {

    public static boolean isCrawlerStatusOn(Context context) {
        return PreferenceUtil.getInstance(context).getInt(KEY_CRAWLER_STATUS, CRAWLER_STATUS_OFF)
                == CRAWLER_STATUS_ON;
    }

    public static void setCrawlerStatusOn(Context context) {
        PreferenceUtil.getInstance(context).putInt(KEY_CRAWLER_STATUS, CRAWLER_STATUS_ON);
    }

    public static void setCrawlerStatusOff(Context context) {
        PreferenceUtil.getInstance(context).putInt(KEY_CRAWLER_STATUS, CRAWLER_STATUS_OFF);
    }

    public static int getCrawlerDurationTime(Context context) {
        return PreferenceUtil.getInstance(context).getInt(KEY_TIMER_DURATION, TIMER_DEFAULT_HOUR);
    }

    public static void setCrawlerDurationTime(Context context, int hour) {
        PreferenceUtil.getInstance(context).putInt(KEY_TIMER_DURATION, hour);
    }

    public static boolean isFirstLaunch(Context context) {
        return PreferenceUtil.getInstance(context)
                .getInt(Constants.KEY_INITIALIZE_PAGE, -1) != 1;
    }

    public static void setFirstLaunchFlag(Context context, boolean isAfterFirstLaunch) {
        PreferenceUtil.getInstance(context).putInt(Constants.KEY_INITIALIZE_PAGE, isAfterFirstLaunch ? 1 : -1);
    }
}
