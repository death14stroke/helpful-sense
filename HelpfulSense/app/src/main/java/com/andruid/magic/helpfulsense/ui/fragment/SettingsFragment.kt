package com.andruid.magic.helpfulsense.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceFragmentCompat
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.ui.util.buildInfoDialog
import com.andruid.magic.helpfulsense.ui.util.buildSettingsDialog
import com.andruid.magic.helpfulsense.util.getSimCards
import com.andruid.magic.locationsms.util.getSelectedSimSlot
import com.andruid.magic.locationsms.util.getShakeStopTime
import com.andruid.magic.locationsms.util.getShakeThreshold
import com.andruid.magic.locationsms.util.toast
import permissions.dispatcher.*

/**
 * Fragment to change settings
 */
@RuntimePermissions
class SettingsFragment : PreferenceFragmentCompat(), OnBindEditTextListener, OnPreferenceChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_preferences)

        findPreference<EditTextPreference>(getString(R.string.pref_threshold))?.let {
            it.setOnBindEditTextListener(this)
            it.onPreferenceChangeListener = this
            it.callChangeListener(getShakeThreshold().toString())
        }
        findPreference<EditTextPreference>(getString(R.string.pref_time_stop))?.let {
            it.setOnBindEditTextListener(this)
            it.onPreferenceChangeListener = this
            it.callChangeListener(getShakeStopTime().toString())
        }
        initSimPreferenceWithPermissionCheck()
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
        when (preference.key) {
            getString(R.string.pref_threshold), getString(R.string.pref_time_stop) ->
                preference.summary = (newValue as String)
            getString(R.string.pref_sms_sim) -> {
                val listPreference = preference as ListPreference
                preference.summary = listPreference.entries[(newValue as String).toInt()]
            }
        }
        return true
    }

    /**
     * Initialize the SIM selection [ListPreference]
     */
    @SuppressLint("MissingPermission")
    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    fun initSimPreference() {
        findPreference<ListPreference>(getString(R.string.pref_sms_sim))?.let {
            val simCards = getSimCards()
            val entries = simCards.map { sim -> sim.displayName }
            val values = simCards.map { sim -> sim.simSlotIndex.toString() }
            it.entries = entries.toTypedArray()
            it.entryValues = values.toTypedArray()
            it.onPreferenceChangeListener = this
            it.callChangeListener(getSelectedSimSlot().toString())
        }
    }

    @OnShowRationale(Manifest.permission.READ_PHONE_STATE)
    fun showRationale(request: PermissionRequest) {
        buildInfoDialog(R.string.phone_state_permission, request).show()
    }

    @OnPermissionDenied(Manifest.permission.READ_PHONE_STATE)
    fun showDenied() {
        toast("Denied permission")
    }

    @OnNeverAskAgain(Manifest.permission.READ_PHONE_STATE)
    fun showSettingsDialog() {
        buildSettingsDialog(R.string.phone_state_permission).show()
    }
}