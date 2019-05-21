package com.opencdk.appwidget.widget;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.widget.RemoteViewsService;

import com.opencdk.appwidget.utils.Utils;

/**
 * @author 笨鸟不乖
 * @version 1.0.0
 * @email benniaobuguai@gmail.com
 * @Modify 2016-3-22
 * @since 2016-3-22
 */
@SuppressLint("NewApi")
public class NewsWidgetService extends RemoteViewsService implements Runnable {

    private static final String TAG = "NewsWidgetService";

    private final static int HOURS_PERIOD_ALARM = 12;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new NewsListRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(this).start();
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void run() {

        final String pref_key = "appwidget_news_refresh_time";
        final long updatePeriod = 10 * 60 * 1000;
        final long lastRefreshTime = Utils.getLong(this, pref_key, 0);
        final long now = System.currentTimeMillis();
        if (now - lastRefreshTime >= updatePeriod) {

            Utils.putLong(this, pref_key, now);

            Intent refreshNowIntent = new Intent(this, NewsAppWidgetProvider.class);
            refreshNowIntent.setAction(NewsAppWidgetProvider.ACTION_REFRESH_AUTO);
            sendBroadcast(refreshNowIntent);
        }

        Intent autoRefreshIntent = new Intent(this, NewsAppWidgetProvider.class);
        autoRefreshIntent.setAction(NewsAppWidgetProvider.ACTION_REFRESH_AUTO);
        PendingIntent pending = PendingIntent.getBroadcast(NewsWidgetService.this, 0, autoRefreshIntent, 0);

        // 1*60
        final long updateTime = HOURS_PERIOD_ALARM * 60 * 1000;
        Time time = new Time();
        long nowMillis = System.currentTimeMillis();
        time.set(nowMillis + updateTime);
        long updateTimes = time.toMillis(true);

        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        // Log.d(TAG, "request next update at " + updateTimes);
        // Log.d(TAG, "refresh time: " + sdf.format(new Date()));

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, updateTimes, pending);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
        // return super.onStartCommand(intent, flags, startId);
    }

}