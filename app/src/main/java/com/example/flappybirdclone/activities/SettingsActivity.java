package com.example.flappybirdclone.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;

import com.example.flappybirdclone.R;
import com.example.flappybirdclone.utils.PreferenceManager;

public class SettingsActivity extends Activity {
    private SeekBar volumeBar;
    private Switch vibrationSwitch;
    private ImageButton cowboyBtn, flappyBtn, japanBtn, customBtn;

    private Button cameraBtn;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        preferenceManager = PreferenceManager.getInstance(this);

        cowboyBtn = findViewById(R.id.cowboySkin);
        flappyBtn = findViewById(R.id.flappySkin);
        japanBtn = findViewById(R.id.japanSkin);
        customBtn = findViewById(R.id.customSkin);

        cameraBtn = findViewById(R.id.cameraSkin);

        volumeBar = findViewById(R.id.volumeBar);
        vibrationSwitch = findViewById(R.id.vibrationSwitch);

        volumeBar.setProgress(preferenceManager.getVolume());
        vibrationSwitch.setChecked(preferenceManager.isVibrationEnabled());


        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                preferenceManager.setVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        vibrationSwitch.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            preferenceManager.setVibrationEnabled(isChecked);
        }));

        cameraBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, CameraActivity.class);
            startActivity(intent);
        });



    }
}
