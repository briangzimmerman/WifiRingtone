package com.example.wifiringtone;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        setNewListener();
    }

    protected void populateSaved() {
        Map<String, ?> saved = this.ringtone_settings.getAll();
        LinearLayout container = ((LinearLayout) findViewById(R.id.saved_settings));

        for(final String wifi_network : saved.keySet()) {
            Log.d("Network", wifi_network);

            String ringtone_path = saved.get(wifi_network).toString();
            String ringtone_name;

            //Get Ringtone name
            if(ringtone_path == null || ringtone_path.isEmpty()) {
                ringtone_name = "None";
            } else {
                Ringtone ringtone = RingtoneManager.getRingtone(this, Uri.parse(ringtone_path));
                ringtone_name = ringtone.getTitle(this);
            }

            //Create Card
            CardView card = new CardView(getApplicationContext());

            CardView.LayoutParams params = new CardView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    250
            );
            params.setMargins(5, 5, 5, 20);

            card.setLayoutParams(params);
            card.setRadius(15);
            card.setContentPadding(20, 40, 20, 40);
            card.setElevation(10);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    Bundle b = new Bundle();
                    b.putString("wifi_network", wifi_network);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });

            //Create linearlayout
            LinearLayout setting_holder = new LinearLayout(getApplicationContext());
            setting_holder.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            ));
            setting_holder.setOrientation(LinearLayout.VERTICAL);

            //Create title
            TextView network = new TextView(getApplicationContext());
            network.setText(wifi_network);
            network.setTextSize(18);
            network.setTextColor(Color.BLACK);

            setting_holder.addView(network);

            //Create subtitle - ringtone
            TextView ringtone = new TextView(getApplicationContext());
            ringtone.setText(ringtone_name);
            ringtone.setTextSize(15);


            setting_holder.addView(ringtone);

            card.addView(setting_holder);
            container.addView(card);
        }
    }

    protected void setNewListener() {
        ((FloatingActionButton) findViewById(R.id.add_new_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void setEditListener() {
        ;//(())
    }
}
