package com.example.wifiringtone;


import android.app.ActionBar;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

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

        populateSaved();
    }

    protected void populateSaved() {
        Map<String, ?> saved = this.ringtone_settings.getAll();
        LinearLayout container = ((LinearLayout) findViewById(R.id.saved_settings));

        for(String wifi_network : saved.keySet()) {
            CardView card = new CardView(getApplicationContext());

            CardView.LayoutParams params = new CardView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    100
            );

            card.setLayoutParams(params);
            card.setRadius(5);
            card.setCardBackgroundColor(Color.parseColor("#FFC6D6C3"));

            container.addView(card);
        }
    }
}
