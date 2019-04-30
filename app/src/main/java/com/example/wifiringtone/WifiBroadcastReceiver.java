package com.example.wifiringtone;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiBroadcastReceiver extends BroadcastReceiver {
    private final String PREF_NAME = "WIFI_RINGTONE_PREFS";
    private SharedPreferences ringtone_settings;
    private Context context;
    private String current_ringtone;

    public WifiBroadcastReceiver(Context context) {
        this.context = context;
        this.ringtone_settings = context.getSharedPreferences(this.PREF_NAME, 0);
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
}
