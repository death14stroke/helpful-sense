package com.andruid.magic.helpfulsense.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.andruid.magic.helpfulsense.R;
import com.andruid.magic.helpfulsense.adapter.ViewPagerAdapter;
import com.andruid.magic.helpfulsense.databinding.ActivityMainBinding;
import com.andruid.magic.helpfulsense.eventbus.ContactsEvent;
import com.andruid.magic.helpfulsense.service.SensorService;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import timber.log.Timber;

import static com.andruid.magic.helpfulsense.data.Constants.CONTACTS_PICKER_REQUEST;
import static com.andruid.magic.helpfulsense.data.Constants.NO_OF_TABS;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "contactslog";
    private ActivityMainBinding binding;
    private ViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.senseBtn.setOnClickListener(v ->
                startSensorService()
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


    private void setBottomNav() {
        binding.viewPager.setOffscreenPageLimit(NO_OF_TABS);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(pagerAdapter);
        binding.bottomNav.setActiveColor(R.color.colorPrimary)
                .setInActiveColor("#FFFFFF")
                .setBarBackgroundColor("#ECECEC")
                .setMode(BottomNavigationBar.MODE_SHIFTING)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
                .addItem(new BottomNavigationItem(R.drawable.ic_alert, getString(R.string.alert)))
                .addItem(new BottomNavigationItem(R.drawable.ic_message, getString(R.string.message)))
                .addItem(new BottomNavigationItem(R.drawable.ic_contacts, getString(R.string.contacts)))
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

    private void startSensorService() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.SEND_SMS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        startService(new Intent(MainActivity.this, SensorService.class));
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this, response.getPermissionName()+
                                " denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
}