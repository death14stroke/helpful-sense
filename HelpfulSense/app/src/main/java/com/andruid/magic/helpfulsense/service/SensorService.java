package com.andruid.magic.helpfulsense.service;

import android.Manifest;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.andruid.magic.helpfulsense.util.NotificationUtil;
import com.andruid.magic.helpfulsense.util.SmsUtil;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector;
import com.github.nisrulz.sensey.WristTwistDetector;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Objects;

import timber.log.Timber;

import static com.andruid.magic.helpfulsense.data.Constants.INTENT_LOC_SMS;
import static com.andruid.magic.helpfulsense.data.Constants.INTENT_SERVICE_STOP;
import static com.andruid.magic.helpfulsense.data.Constants.INTENT_SMS_SENT;
import static com.andruid.magic.helpfulsense.data.Constants.KEY_MESSAGE;
import static com.andruid.magic.helpfulsense.data.Constants.NOTI_ID;
import static com.andruid.magic.helpfulsense.data.Constants.SHAKE_THRESHOLD;

public class SensorService extends Service implements GoogleApiClient.ConnectionCallbacks,
        ShakeDetector.ShakeListener, GoogleApiClient.OnConnectionFailedListener, WristTwistDetector.WristTwistListener {
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private MyLocationCallback locationCallback;
    private boolean apiConnected = false;
    private String message = "";
    private static final String TAG = "servicelog";

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationCompat.Builder builder = NotificationUtil.buildProgressNotification(
                getApplicationContext());
        startForeground(NOTI_ID, Objects.requireNonNull(builder).build());
        Timber.tag(TAG).d("start foreground done");
        Sensey.getInstance().init(getApplicationContext());
        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
        locationCallback = new MyLocationCallback();
        Sensey.getInstance().startShakeDetection(SHAKE_THRESHOLD ,0,
                this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null)
            return START_STICKY;
        if(INTENT_LOC_SMS.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            if(extras != null)
                message = extras.getString(KEY_MESSAGE);
            startLocationReq();
        }
        else if(INTENT_SERVICE_STOP.equals(intent.getAction())) {
            stopForeground(true);
            stopSelf();
        }
        else if(INTENT_SMS_SENT.equals(intent.getAction())){
            Toast.makeText(getApplicationContext(), "sms sent", Toast.LENGTH_SHORT).show();
        }
        return START_STICKY;
    }

    private void startLocationReq(){
        if (!apiConnected || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        LocationServices.getFusedLocationProviderClient(getApplicationContext())
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onDestroy() {
        Sensey.getInstance().stopShakeDetection(this);
        super.onDestroy();
        Sensey.getInstance().stop();
        googleApiClient.disconnect();
        LocationServices.getFusedLocationProviderClient(getApplicationContext())
                .removeLocationUpdates(locationCallback);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        apiConnected = true;
        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setFastestInterval(1000)
                .setNumUpdates(1);
        NotificationCompat.Builder builder = NotificationUtil.buildNotification(getApplicationContext());
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTI_ID, Objects.requireNonNull(builder).build());
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), connectionResult.getErrorMessage(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShakeDetected() {}

    @Override
    public void onShakeStopped() {
        Timber.tag(TAG).d("onShakeStopped: ");
        message = "Shook phone. Sensing danger.";
        startLocationReq();
    }

    @Override
    public void onWristTwist() {
        Timber.tag(TAG).d("on wrist twist");
    }

    private class MyLocationCallback extends LocationCallback {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location loc = locationResult.getLocations().get(0);
            SmsUtil.sendSMS(getApplicationContext(), loc, message);
        }
    }
}