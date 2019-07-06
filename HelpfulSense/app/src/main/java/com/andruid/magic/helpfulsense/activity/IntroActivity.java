package com.andruid.magic.helpfulsense.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.andruid.magic.helpfulsense.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPagerBuilder;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showSkipButton(true);
        setVibrate(true);
        setVibrateIntensity(30);
        addSlide(AppIntroFragment.newInstance(buildSliderPage("Quick actions",
                "Save custom messages to send in case of emergency with location at one tap",
                R.drawable.ic_alert, R.color.colorAccent).build()));
        addSlide(AppIntroFragment.newInstance(buildSliderPage("Battery and boot",
                "Start listening for sensors when device is booted up. Send message when " +
                        "battery low so your loved ones don't worry",
                R.drawable.ic_low_battery, R.color.colorTab1).build()));
        addSlide(AppIntroFragment.newInstance(buildSliderPage("Quick gestures",
                "Shake your phone to send emergency SMS to trusted contacts",
                R.drawable.ic_message, R.color.colorTab2).build()));
        addSlide(AppIntroFragment.newInstance(buildSliderPage("Select contacts",
                "Select contacts you trust to send your location and message to get help",
                R.drawable.ic_contacts, R.color.colorTab3).build()));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        goToMainActivity();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        goToMainActivity();
    }

    private void goToMainActivity() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean(getString(R.string.pref_first), false)
                .apply();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private SliderPagerBuilder buildSliderPage(String title, String desc, int image, int bgColor){
        return new SliderPagerBuilder()
                .title(title)
                .description(desc)
                .imageDrawable(image)
                .bgColor(ContextCompat.getColor(this, bgColor))
                .titleColor(ContextCompat.getColor(this, android.R.color.white))
                .descColor(ContextCompat.getColor(this, android.R.color.white));
    }
}