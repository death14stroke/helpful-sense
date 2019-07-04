package com.andruid.magic.helpfulsense.fragment;

import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.andruid.magic.helpfulsense.R;

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat implements
        EditTextPreference.OnBindEditTextListener, Preference.OnPreferenceChangeListener {

    public static SettingsFragment newInstance(){
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_preferences);
        EditTextPreference threshPref = Objects.requireNonNull(findPreference(getString(R.string.pref_threshold)));
        threshPref.setOnBindEditTextListener(this);
        threshPref.setOnPreferenceChangeListener(this);
        threshPref.callChangeListener(PreferenceManager.getDefaultSharedPreferences(
                Objects.requireNonNull(getContext())).getString(getString(R.string.pref_threshold),
                getString(R.string.def_threshold)));

        EditTextPreference timesPref = Objects.requireNonNull(findPreference(getString(R.string.pref_time_stop)));
        timesPref.setOnBindEditTextListener(this);
        timesPref.setOnPreferenceChangeListener(this);
        timesPref.callChangeListener(PreferenceManager.getDefaultSharedPreferences(
                Objects.requireNonNull(getContext())).getString(getString(R.string.pref_time_stop),
                getString(R.string.def_time_stop)));
    }

    @Override
    public void onBindEditText(@NonNull EditText editText) {
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        if(key.equals(getString(R.string.pref_threshold)) || key.equals(getString(R.string.pref_time_stop)))
            preference.setSummary((String)newValue);
        return true;
    }
}