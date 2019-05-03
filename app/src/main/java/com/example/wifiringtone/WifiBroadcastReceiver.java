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
import android.net.Uri;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import static android.content.Context.NOTIFICATION_SERVICE;

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
        Ringtone defaultRingtone = RingtoneManager.getRingtone(this.context, defaultRingtoneUri);
        this.current_ringtone = defaultRingtone.getTitle(this.context);

        Log.d("Current Ringtone:", this.current_ringtone);

        showNotification();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION .equals(action)) {
            SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
            if (SupplicantState.isValidState(state)
                    && state == SupplicantState.COMPLETED) {

                String setRingtone = ringtoneToSet();
                this.current_ringtone = setRingtone;
            }
        }
    }

    private String ringtoneToSet() {
        String ringtone = "";

        WifiManager wifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifi = wifiManager.getConnectionInfo();

        if (wifi != null) {
            // get current router Mac address
            ringtone = wifi.getBSSID();

            Log.d("Ringtone to set", ringtone);
        }

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
