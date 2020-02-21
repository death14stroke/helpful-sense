package com.andruid.magic.locationsms.service

import android.Manifest
import android.annotation.SuppressLint
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
import splitties.toast.toast
import timber.log.Timber

class SmsService : Service(), ConnectionCallbacks, OnConnectionFailedListener, ShakeListener,
        OnSharedPreferenceChangeListener {
    companion object {
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

    private var apiConnected = false
    private var startIntent: Intent? = null

    private lateinit var message: String
    private lateinit var phoneNumbers: List<String>
    private lateinit var className: String
    @DrawableRes
    private var iconRes = android.R.drawable.sym_def_app_icon

    private val gpsReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            if (LocationManager.PROVIDERS_CHANGED_ACTION == intent.action)
                requestLocation()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            handleIntent(intent)
        }
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
        apiConnected = true
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
        if (apiConnected)
            startLocationReq()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, s: String) {
        if (s == getString(R.string.pref_threshold) || s == getString(R.string.pref_time_stop)) {
            Sensey.getInstance().stopShakeDetection(this)
            initShakeDetection()
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private fun requestLocation() {
        LocationServices.getFusedLocationProviderClient(applicationContext)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    private fun init() {
        startForeground(NOTI_ID, buildProgressNotification(iconRes).build())

        Sensey.getInstance().init(applicationContext, Sensey.SAMPLING_PERIOD_NORMAL)
        googleApiClient.connect()
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)
        initShakeDetection()
    }

    private fun initShakeDetection() {
        val threshold = getShakeThreshold()
        val timeStop = getShakeStopTime()
        Sensey.getInstance().startShakeDetection(threshold.toFloat(), timeStop.toLong(), this)
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            ACTION_LOC_SMS -> {
                intent.extras?.let {
                    message = it.getString(EXTRA_MESSAGE, "")
                    phoneNumbers = it.getStringArray(EXTRA_PHONE_NUMBERS)?.toList() ?: emptyList()
                    className = it.getString(EXTRA_CLASS, "")
                    iconRes = it.getInt(EXTRA_ICON_RES)
                    if (!apiConnected) {
                        startIntent = intent
                        init()
                    } else
                        startLocationReq()
                }
            }
            ACTION_STOP_SERVICE -> {
                stopForeground(true)
                stopSelf()
            }
            ACTION_START_SERVICE -> {
                if (!apiConnected) {
                    intent.extras?.let {
                        phoneNumbers = it.getStringArray(EXTRA_PHONE_NUMBERS)?.toList() ?: emptyList()
                        className = it.getString(EXTRA_CLASS, "")
                    }
                    init()
                }
            }
            ACTION_SMS_SENT -> toast(R.string.sms_sent)
        }
    }

    @SuppressLint("MissingPermission")
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

    private inner class MyLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val loc = locationResult.lastLocation
            Timber.i("onLocationResult: loc obtained")
            if(checkPhoneStatePermission() && hasSmsPermission())
            sendSMS(loc, message, phoneNumbers)
        }
    }
}