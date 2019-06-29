package com.andruid.magic.helpfulsense;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector;

public class SensorService extends Service implements ShakeDetector.ShakeListener {
    private static final String TAG = "senselog";

    @Override
    public void onCreate() {
        super.onCreate();
        Sensey.getInstance().init(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Sensey.getInstance().startShakeDetection(this);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Sensey.getInstance().stopShakeDetection(this);
        super.onDestroy();
        Sensey.getInstance().stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onShakeDetected() {
        
    }

    @Override
    public void onShakeStopped() {
        Log.d(TAG, "onShakeStopped: ");
    }
}