package com.andruid.magic.helpfulsense.ui.fragment

import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.preference.EditTextPreference
import androidx.preference.EditTextPreference.OnBindEditTextListener
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.util.getShakeStopTime
import com.andruid.magic.helpfulsense.util.getShakeThreshold

class SettingsFragment : PreferenceFragmentCompat(), OnBindEditTextListener, Preference.OnPreferenceChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_preferences)

        findPreference<EditTextPreference>(getString(R.string.pref_threshold))?.let {
            it.setOnBindEditTextListener(this)
            it.onPreferenceChangeListener = this
            it.callChangeListener(requireContext().getShakeThreshold().toString())
        }
        findPreference<EditTextPreference>(getString(R.string.pref_time_stop))?.let {
            it.setOnBindEditTextListener(this)
            it.onPreferenceChangeListener = this
            it.callChangeListener(requireContext().getShakeStopTime().toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_help, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController()
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onBindEditText(editText: EditText) {
        editText.inputType = InputType.TYPE_CLASS_NUMBER
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        val key = preference.key
        if (key == getString(R.string.pref_threshold) || key == getString(R.string.pref_time_stop))
            preference.summary = (newValue as String)
        return true
    }
}