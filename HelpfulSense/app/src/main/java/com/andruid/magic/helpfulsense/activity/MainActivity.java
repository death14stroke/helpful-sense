package com.andruid.magic.helpfulsense.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.andruid.magic.helpfulsense.R;
import com.andruid.magic.helpfulsense.adapter.ViewPagerAdapter;
import com.andruid.magic.helpfulsense.databinding.ActivityMainBinding;
import com.andruid.magic.helpfulsense.eventbus.ContactsEvent;
import com.andruid.magic.helpfulsense.service.SensorService;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import timber.log.Timber;

import static com.andruid.magic.helpfulsense.data.Constants.CONTACTS_PICKER_REQUEST;
import static com.andruid.magic.helpfulsense.data.Constants.NO_OF_TABS;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "contactslog";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.senseBtn.setOnClickListener(v ->
                MainActivityPermissionsDispatcher.startSensorServiceWithPermissionCheck(this)
        );
        setBottomNav();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CONTACTS_PICKER_REQUEST){
            if(resultCode == RESULT_OK){
                List<ContactResult> results = MultiContactPicker.obtainResult(data);
                Timber.tag(TAG).d("in activity: %d selected", results.size());
                EventBus.getDefault().post(new ContactsEvent(results));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void setBottomNav() {
        binding.viewPager.setOffscreenPageLimit(NO_OF_TABS);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(pagerAdapter);
        binding.bottomNav.setActiveColor(R.color.colorPrimary)
                .setInActiveColor("#FFFFFF")
                .setBarBackgroundColor("#ECECEC")
                .setMode(BottomNavigationBar.MODE_SHIFTING)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
                .addItem(new BottomNavigationItem(R.drawable.ic_alert, getString(R.string.alert))
                        .setActiveColorResource(R.color.colorTab1))
                .addItem(new BottomNavigationItem(R.drawable.ic_message, getString(R.string.message))
                        .setActiveColorResource(R.color.colorTab2))
                .addItem(new BottomNavigationItem(R.drawable.ic_contacts, getString(R.string.contacts))
                        .setActiveColorResource(R.color.colorTab3))
                .addItem(new BottomNavigationItem(R.drawable.ic_settings, getString(R.string.settings))
                        .setActiveColorResource(R.color.colorTab4))
                .setFirstSelectedPosition(0)
                .initialise();
        binding.bottomNav.setAutoHideEnabled(true);
        binding.bottomNav.setFab(binding.senseBtn);
        binding.bottomNav.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                binding.viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {}

            @Override
            public void onTabReselected(int position) {}
        });
    }

    @NeedsPermission({Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION})
    public void startSensorService() {
        Intent intent = new Intent(MainActivity.this, SensorService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForegroundService(intent);
        else
            startService(intent);
    }

    @OnShowRationale({Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION})
    public void showRationale(PermissionRequest request){
        new AlertDialog.Builder(this)
                .setMessage("Send your location to your trusted contacts in case of emergency. " +
                        "Grant SMS and location permissions for the same")
                .setPositiveButton("Allow", (dialog, which) -> request.proceed())
                .setNegativeButton("Deny", (dialog, which) -> request.cancel())
                .show();
    }

    @OnPermissionDenied({Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION})
    public void showDenied(){
        Toast.makeText(getApplicationContext(), "Permissions denied", Toast.LENGTH_SHORT).show();
    }
}