package com.example.wifiringtone;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.provider.Settings;

public class RingtoneService extends Service {
    private WifiBroadcastReceiver wbr;

    @Override
    public void onCreate() {

        this.wbr = new WifiBroadcastReceiver(this, (NotificationManager)getSystemService(NOTIFICATION_SERVICE));

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(this.wbr, intentFilter);
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
