import android.content.Context
import androidx.preference.PreferenceManager
import com.andruid.magic.locationsms.R

fun Context.getShakeThreshold(): Int {
    return PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.pref_threshold),
            resources.getInteger(R.integer.def_threshold).toString())!!.toInt()
}

fun Context.getShakeStopTime(): Int {
    return PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.pref_time_stop),
            resources.getInteger(R.integer.def_time_stop).toString())!!.toInt()
}