package com.example.wifiringtone;

import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
    private Map<String, String> ringtones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.ringtones = getRingtones();
        Log.d("TESTING", this.ringtones.toString());


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        makeRingtoneList(this.ringtones);
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

    protected void makeRingtoneList(Map<String, String> ringtones) {
        RadioGroup ringtoneRadios = new RadioGroup(this);
        ringtoneRadios.setOrientation(RadioGroup.VERTICAL);

        int idx = 1;
        for(String ringtone : ringtones.keySet()) {
            RadioButton radio = new RadioButton(this);
            radio.setId(idx++);
            radio.setText(ringtone);
            ringtoneRadios.addView(radio);
        }

        ((ViewGroup) findViewById(R.id.ringtone_group)).addView(ringtoneRadios);
    }
}
