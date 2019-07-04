package com.andruid.magic.helpfulsense.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.andruid.magic.helpfulsense.fragment.AlertFragment;
import com.andruid.magic.helpfulsense.fragment.ContactsFragment;
import com.andruid.magic.helpfulsense.fragment.MessageFragment;
import com.andruid.magic.helpfulsense.fragment.SettingsFragment;

import static com.andruid.magic.helpfulsense.data.Constants.NO_OF_TABS;
import static com.andruid.magic.helpfulsense.data.Constants.POS_ALERT;
import static com.andruid.magic.helpfulsense.data.Constants.POS_CONTACTS;
import static com.andruid.magic.helpfulsense.data.Constants.POS_MESSAGE;
import static com.andruid.magic.helpfulsense.data.Constants.POS_SETTINGS;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case POS_ALERT:
                fragment = AlertFragment.newInstance();
                break;
            case POS_MESSAGE:
                fragment = MessageFragment.newInstance();
                break;
            case POS_CONTACTS:
                fragment = ContactsFragment.newInstance();
                break;
            case POS_SETTINGS:
                fragment = SettingsFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return NO_OF_TABS;
    }
}