package com.example.wifiringtone;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;

import java.util.Map;

public class WifiBroadcastReceiver extends BroadcastReceiver {
    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "ringtone_channel_id";
    private final String PREF_NAME = "WIFI_RINGTONE_PREFS";
    private SharedPreferences ringtone_settings;
    private NotificationManager nm;
    private Context context;
    private String current_ringtone = "";

    public WifiBroadcastReceiver(Context context, NotificationManager nm) {
        this.context = context;
        this.nm = nm;
        this.ringtone_settings = context.getSharedPreferences(this.PREF_NAME, 0);

        NotificationChannel nc = new NotificationChannel(this.NOTIF_CHANNEL_ID, "ringtone_channel", nm.IMPORTANCE_HIGH);
        nc.setDescription("Current Ringtone");
        nc.enableLights(false);
        nc.enableVibration(false);
        nc.setShowBadge(false);

        this.nm.createNotificationChannel(nc);

        Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this.context, RingtoneManager.TYPE_RINGTONE);
        if(Uri.EMPTY.equals(defaultRingtoneUri)) {
            this.current_ringtone = "None";
        } else {
            Ringtone defaultRingtone = RingtoneManager.getRingtone(this.context, defaultRingtoneUri);
            this.current_ringtone = defaultRingtone.getTitle(this.context);
        }

        Log.d("Current Ringtone:", this.current_ringtone);

        showNotification();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(info != null && info.isConnected()) {
            String setRingtone = ringtoneToSet();

            if(setRingtone.isEmpty()) {
                this.current_ringtone = "None";
            } else {
                Ringtone ringtone = RingtoneManager.getRingtone(this.context, Uri.parse(setRingtone));
                this.current_ringtone = ringtone.getTitle(this.context);
            }

            RingtoneManager.setActualDefaultRingtoneUri(this.context, RingtoneManager.TYPE_RINGTONE, Uri.parse(setRingtone));

            this.showNotification();
        } else {
            this.current_ringtone = "None";
            RingtoneManager.setActualDefaultRingtoneUri(this.context, RingtoneManager.TYPE_RINGTONE, Uri.parse(""));
            this.showNotification();
        }
    }

    private String ringtoneToSet() {
        String ringtone = "";

        WifiManager wifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifi = wifiManager.getConnectionInfo();

        if (wifi != null) {
            String ssid = wifi.getSSID();
            ssid = ssid.substring(1, ssid.length() - 1);

            Log.d("SSID", ssid);
            ringtone = this.ringtone_settings.getString(ssid, "");
        }

        Log.d("Ringtone To Set:", "Ringtone: " + ringtone);

        return ringtone;
    }

    public void destroyNotification() {
        this.nm.cancel(this.NOTIF_ID);
    }

    public void showNotification() {
        //Open the app when notification is clicked
        PendingIntent appIntent = PendingIntent.getActivity(this.context, 0,
                new Intent(this.context, MainActivity.class), 0);

        Notification notification = new Notification.Builder(this.context, this.NOTIF_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setOngoing(true)
                .setContentTitle(this.context.getString(R.string.app_name))
                .setContentText("Current Ringtone: " + this.current_ringtone)
                .setContentIntent(appIntent)
                .build();

        this.nm.notify(this.NOTIF_ID, notification);
    }
}
