package com.example.wifiringtone;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RingtoneService extends Service {
    private WifiBroadcastReceiver wbr;

    @Override
    public void onCreate() {
        this.wbr = new WifiBroadcastReceiver(this, (NotificationManager)getSystemService(NOTIFICATION_SERVICE));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.wbr.showNotification();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        this.wbr.destroyNotification();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
