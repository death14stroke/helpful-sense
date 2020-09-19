package com.andruid.magic.locationsms.service

import android.Manifest
import android.app.Service
import android.content.*
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresPermission
import androidx.preference.PreferenceManager
import com.andruid.magic.locationsms.R
import com.andruid.magic.locationsms.data.*
import com.andruid.magic.locationsms.util.*
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.ShakeDetector.ShakeListener
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import splitties.systemservices.locationManager
import splitties.systemservices.notificationManager
import timber.log.Timber

/**
 * Foreground service to handle shake detection and send SMS with current location
 */
class SmsService : Service(), ConnectionCallbacks, OnConnectionFailedListener, ShakeListener,
        OnSharedPreferenceChangeListener {
    companion object {
        // notification ID of persistent notification
        private const val NOTI_ID = 1
    }

    private val locationCallback = MyLocationCallback()
    private val googleApiClient by lazy {
        GoogleApiClient.Builder(applicationContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()
    }
    private val locationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 1000
        fastestInterval = 1000
        numUpdates = 1
    }

    // intent delivered to the service when service is not initialized yet
    private var startIntent: Intent? = null

    // Message to be sent with location
    private lateinit var message: String
    /// Selected phone numbers
    private lateinit var phoneNumbers: List<String>
    // Activity to launch on notification click
    private lateinit var className: String
    // Small icon shown in notification
    @DrawableRes
    private var iconRes = android.R.drawable.sym_def_app_icon

    // broadcast receiver for GPS turn on action
    private val gpsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (LocationManager.PROVIDERS_CHANGED_ACTION == intent.action) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && checkLocationPermission())
                    requestLocation()
            }
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        handleIntent(intent)
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this)
        Sensey.getInstance().stopShakeDetection(this)
        Sensey.getInstance().stop()
        googleApiClient.disconnect()
        LocationServices.getFusedLocationProviderClient(applicationContext)
                .removeLocationUpdates(locationCallback)
        try {
            unregisterReceiver(gpsReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onConnected(bundle: Bundle?) {
        notificationManager.notify(NOTI_ID, buildNotification(phoneNumbers, className, iconRes).build())
        startIntent?.let {
            handleIntent(it)
            startIntent = null
        }
    }

    override fun onConnectionSuspended(i: Int) {}

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        toast(connectionResult.errorMessage ?: "Location services error")
    }

    override fun onShakeDetected() {}

    override fun onShakeStopped() {
        Timber.i("onShakeStopped: ")
        message = getString(R.string.shake_msg)
        if (googleApiClient.isConnected && checkLocationPermission())
            startLocationReq()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, s: String) {
        if (s == getString(R.string.pref_threshold) || s == getString(R.string.pref_time_stop)) {
            Sensey.getInstance().stopShakeDetection(this)
            initShakeDetection()
        }
    }

    /**
     * Get one time live location from GPS
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private fun requestLocation() {
        try {
            unregisterReceiver(gpsReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        LocationServices.getFusedLocationProviderClient(applicationContext)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    /**
     * Init [googleApiClient] and [Sensey] for shake detection and location request
     */
    private fun init() {
        startForeground(NOTI_ID, buildProgressNotification(iconRes).build())

        Sensey.getInstance().init(applicationContext, Sensey.SAMPLING_PERIOD_NORMAL)
        googleApiClient.connect()
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)
        initShakeDetection()
    }

    /**
     * Init shake detection with settings set in [SharedPreferences]
     */
    private fun initShakeDetection() {
        val threshold = getShakeThreshold()
        val timeStop = getShakeStopTime()
        Sensey.getInstance().startShakeDetection(threshold.toFloat(), timeStop.toLong(), this)
    }

    /**
     * Process the intent received in [onStartCommand]
     * @param intent which started the service
     */
    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            ACTION_LOC_SMS -> {
                intent.extras?.let {
                    message = it.getString(EXTRA_MESSAGE, "")
                    phoneNumbers = it.getStringArray(EXTRA_PHONE_NUMBERS)?.toList() ?: emptyList()
                    className = it.getString(EXTRA_CLASS, "")
                    iconRes = it.getInt(EXTRA_ICON_RES)
                    if (!googleApiClient.isConnected) {
                        startIntent = intent
                        init()
                    } else {
                        if (checkLocationPermission())
                            startLocationReq()
                    }
                }
            }
            ACTION_STOP_SERVICE -> {
                stopForeground(true)
                stopSelf()
            }
            ACTION_START_SERVICE -> {
                if (!googleApiClient.isConnected) {
                    intent.extras?.let {
                        phoneNumbers = it.getStringArray(EXTRA_PHONE_NUMBERS)?.toList()
                                ?: emptyList()
                        className = it.getString(EXTRA_CLASS, "")
                        iconRes = it.getInt(EXTRA_ICON_RES)
                    }
                    init()
                }
            }
            ACTION_SMS_SENT -> toast(R.string.sms_sent)
        }
    }

    /**
     * Request location if GPS is enabled else take to the settings screen to turn on GPS
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private fun startLocationReq() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            requestLocation()
        else {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            registerReceiver(gpsReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
        }
    }

    /**
     * Callback for when location is received from GPS module
     */
    private inner class MyLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val loc = locationResult.lastLocation
            Timber.i("onLocationResult: loc obtained")
            if (checkPhoneStatePermission() && checkSmsPermission())
                sendSMS(loc, message, phoneNumbers)
        }
    }
}