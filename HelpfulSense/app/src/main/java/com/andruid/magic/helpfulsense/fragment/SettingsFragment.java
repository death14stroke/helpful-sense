package com.andruid.magic.helpfulsense.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.andruid.magic.helpfulsense.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    public static SettingsFragment newInstance(){
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_preferences);
    }
}