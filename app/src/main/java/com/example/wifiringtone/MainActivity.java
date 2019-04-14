package com.example.wifiringtone;

import android.database.Cursor;
import android.media.RingtoneManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

        makeRingtoneList();
        saveOptionsListener();
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
        RadioGroup ringtoneRadios = new RadioGroup(this);
        ringtoneRadios.setOrientation(RadioGroup.VERTICAL);

        RadioButton radio = new RadioButton(this);
        radio.setId(1);
        radio.setText("None");
        ringtoneRadios.addView(radio);


        int idx = 2;
        for(String ringtone : this.ringtones.keySet()) {
            radio = new RadioButton(this);
            radio.setId(idx++);
            radio.setText(ringtone);
            ringtoneRadios.addView(radio);
        }

        ((ViewGroup) findViewById(R.id.ringtone_group)).addView(ringtoneRadios);
    }

    protected void saveOptionsListener() {
        ((Button) findViewById(R.id.submit_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText wifi_input = (EditText) findViewById(R.id.wifi_network);

                RadioGroup radios = (RadioGroup) findViewById(R.id.ringtone_group);
                int ringtone_id = radios.getCheckedRadioButtonId();
                Log.d("ringtone ID", "Value: " + ringtone_id);
                RadioButton selected_radio = (RadioButton) findViewById(ringtone_id);

//                Log.d("Ringtone", selected_radio.getText().toString());
                Log.d("Wifi", wifi_input.getText().toString());
            }
        });
    }
}
