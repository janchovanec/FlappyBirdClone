package com.example.flappybirdclone.activities;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

    private AnimatorSet animatorSet;

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
            startActivityForResult(intent, 100);
        });

        cowboyBtn.setOnClickListener(v -> {
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.cowboy);
            preferenceManager.setBirdSkinPath(uri.toString(), "cowboy");
            animateSelectedSkin();
        });

        flappyBtn.setOnClickListener(v -> {
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.flappybird);
            preferenceManager.setBirdSkinPath(uri.toString(), "flappybird");
            animateSelectedSkin();
        });

        japanBtn.setOnClickListener(v -> {
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.japan);
            preferenceManager.setBirdSkinPath(uri.toString(), "japan");
            animateSelectedSkin();
        });

        customBtn.setOnClickListener(v -> {
            Uri uri;
            if (preferenceManager.getCustomSkinPath() == null) {
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.custom_skin);
            } else {
                uri = Uri.parse(preferenceManager.getCustomSkinPath());
            }
            preferenceManager.setBirdSkinPath(uri.toString(), "custom");
            if (preferenceManager.getCustomSkinPath() != null) {
                customBtn.setImageURI(Uri.parse(preferenceManager.getCustomSkinPath()));
            }
            animateSelectedSkin();
        });

        if (preferenceManager.getCustomSkinPath() != null) {
            customBtn.setImageURI(Uri.parse(preferenceManager.getCustomSkinPath()));
        }
        animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.oscillate);
        animateSelectedSkin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (preferenceManager.getCustomSkinPath() != null) {
                customBtn.setImageURI(Uri.parse(preferenceManager.getCustomSkinPath()));
            }
        }
    }

    private void animateSelectedSkin() {
        String chosenSkin = preferenceManager.getChosenSkinName();
        if (chosenSkin != null) {
            ImageButton selectedSkinButton = findViewById(R.id.flappySkin);
            if (chosenSkin.compareTo("cowboy") == 0) {
                selectedSkinButton = findViewById(R.id.cowboySkin);
            } else if (chosenSkin.compareTo("flappybird") == 0) {
                selectedSkinButton = findViewById(R.id.flappySkin);
            } else if (chosenSkin.compareTo("japan") == 0) {
                selectedSkinButton = findViewById(R.id.japanSkin);
            } else if (chosenSkin.compareTo("custom") == 0) {
                selectedSkinButton = findViewById(R.id.customSkin);
            } else {
                return;
            }
            animatorSet.end();
            animatorSet.setTarget(selectedSkinButton);
            animatorSet.start();
        }

    }

}
