package com.example.wifiringtone;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final String PREF_NAME = "WIFI_RINGTONE_PREFS";
    private SharedPreferences ringtone_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.ringtone_settings = getApplicationContext().getSharedPreferences(
                this.PREF_NAME,
                0
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    protected void populateSaved() {
        Map<String, ?> saved = this.ringtone_settings.getAll();

        for(String wifi_network : saved.keySet()) {

        }
    }
}
