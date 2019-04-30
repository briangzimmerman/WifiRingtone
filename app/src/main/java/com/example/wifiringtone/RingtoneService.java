package com.example.wifiringtone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class RingtoneService extends Service {
    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "ringtone_channel_id";
    private NotificationManager nm;

    @Override
    public void onCreate() {
        this.nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showNotification();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        this.nm.cancel(this.NOTIF_ID);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void showNotification() {
        //Open the app when notification is clicked
        PendingIntent appIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

//        Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(RingtoneManager.TYPE_RINGTONE);
//        Ringtone defaultRingtone = RingtoneManager.getRingtone(getActivity(), defaultRingtoneUri);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setOngoing(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Current Ringtone: ")
                .setContentIntent(appIntent)
                .build();

        this.nm.notify(this.NOTIF_ID, notification);
    }
}
