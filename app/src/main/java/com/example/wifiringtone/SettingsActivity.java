package com.example.wifiringtone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SettingsActivity extends AppCompatActivity {
    private Map<String, String> ringtones;
    private final String PREF_NAME = "WIFI_RINGTONE_PREFS";
    private SharedPreferences ringtone_settings;
    private MediaPlayer ringtone;
    private String wifi_network = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.ringtones = getRingtones();
        Log.d("TESTING", this.ringtones.toString());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            this.wifi_network = b.getString("wifi_network");
            ((EditText) findViewById(R.id.wifi_network)).setText(this.wifi_network);
        }

        this.ringtone_settings = getApplicationContext().getSharedPreferences(
                this.PREF_NAME,
                0
        );

        makeRingtoneList();
        setRingtoneChangeListener();
        saveOptionsListener();
        deleteSettingListener();
    }

    protected Map<String, String> getRingtones() {
        RingtoneManager ringtoneMgr = new RingtoneManager(this);
        ringtoneMgr.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor ringtoneCursor = ringtoneMgr.getCursor();
        int ringtoneCount = ringtoneCursor.getCount();

        if (ringtoneCount == 0 && !ringtoneCursor.moveToFirst()) {
            return null;
        }

        Map<String, String> ringtones = new HashMap<>();
        while(!ringtoneCursor.isAfterLast() && ringtoneCursor.moveToNext()) {
            String title = ringtoneCursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String uri = ringtoneMgr.getRingtoneUri(ringtoneCursor.getPosition()).toString();

            ringtones.put(title, uri);
        }

        ringtoneCursor.close();

        return new TreeMap<>(ringtones);
    }

    protected void makeRingtoneList() {
        RadioGroup ringtoneRadios = (RadioGroup) findViewById(R.id.ringtone_group);
        ringtoneRadios.setOrientation(RadioGroup.VERTICAL);
        String saved_ringtone = this.ringtone_settings.getString(this.wifi_network, "");

        int id = 1;

        RadioButton radio = new RadioButton(this);
        radio.setId(id++);
        radio.setText("None");
        radio.setChecked(saved_ringtone.equals(""));
        ringtoneRadios.addView(radio);

        for(Map.Entry<String, String> entry : this.ringtones.entrySet()) {
            radio = new RadioButton(this);
            radio.setId(id++);
            radio.setText(entry.getKey());
            radio.setChecked(entry.getValue().equals(saved_ringtone));
            ringtoneRadios.addView(radio);
        }
    }

    protected void setRingtoneChangeListener() {
        RadioGroup ringtoneRadios = (RadioGroup) findViewById(R.id.ringtone_group);
        ringtoneRadios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == 1 || checkedId == -1) { return; }

                String ringtone_file = SettingsActivity.this.ringtones.get(
                        ((RadioButton) findViewById(checkedId)).getText().toString()
                );

                playRingtone(ringtone_file);
            }
        });
    }

    protected void saveOptionsListener() {
        ((Button) findViewById(R.id.submit_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText wifi_input = (EditText) findViewById(R.id.wifi_network);
                String wifi_network = wifi_input.getText().toString();

                RadioGroup radios = (RadioGroup) findViewById(R.id.ringtone_group);
                int ringtone_id = radios.getCheckedRadioButtonId();
                RadioButton selected_radio = (RadioButton) findViewById(ringtone_id);
                String ringtone_file;

                if(wifi_network.equals("")) {
                    return; //Error no network
                } else if(ringtone_id == -1) {
                    return; //Error no ringtone
                } else if(!SettingsActivity.this.wifi_network.equals(wifi_network) &&
                          SettingsActivity.this.ringtone_settings.getString(wifi_network, null) != null) {
                    return; //Error double wifi rule
                } else if(ringtone_id == 1) {
                    ringtone_file = "";
                } else {
                    ringtone_file = SettingsActivity.this.ringtones.get(selected_radio.getText().toString());
                }

                SettingsActivity.this.saveOptions(wifi_network, ringtone_file);

                if(SettingsActivity.this.ringtone != null && SettingsActivity.this.ringtone.isPlaying()) {
                    SettingsActivity.this.ringtone.stop();
                    SettingsActivity.this.ringtone.release();
                }

                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void deleteSettingListener() {
        ((Button) findViewById(R.id.delete_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor setting_edit = SettingsActivity.this.ringtone_settings.edit();

                setting_edit.remove(SettingsActivity.this.wifi_network);

                setting_edit.apply();

                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void saveOptions(String wifi_network, String ringtone_file) {
        Log.d("Wifi network", wifi_network);
        Log.d("Ringtone path", ringtone_file);

        SharedPreferences.Editor setting_edit = this.ringtone_settings.edit();

        if(!this.wifi_network.equals(wifi_network)) {
            setting_edit.remove(this.wifi_network);
        }

        setting_edit.putString(wifi_network, ringtone_file);

        setting_edit.apply();

        showInfo("Settings Saved");
    }

    protected void showInfo(String message) {
        Log.d("Info Message", message);
        return;
    }

    protected void playRingtone(String ringtone_file) {
        if(this.ringtone != null && this.ringtone.isPlaying()) {
            this.ringtone.stop();
            this.ringtone.release();
        }

        Log.d("Playing ringtone", ringtone_file);
        this.ringtone = MediaPlayer.create(getApplicationContext(), Uri.parse(ringtone_file));
        this.ringtone.start();
        this.ringtone.setLooping(false);
        this.ringtone.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                mp.stop();
                mp.release();
                SettingsActivity.this.ringtone = null;
            }
        });
    }
}
