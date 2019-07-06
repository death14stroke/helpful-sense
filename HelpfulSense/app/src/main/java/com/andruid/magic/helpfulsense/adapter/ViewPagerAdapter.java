package com.andruid.magic.helpfulsense.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.andruid.magic.helpfulsense.fragment.AlertFragment;
import com.andruid.magic.helpfulsense.fragment.ContactsFragment;
import com.andruid.magic.helpfulsense.fragment.MessageFragment;
import com.andruid.magic.helpfulsense.fragment.SettingsFragment;

import static com.andruid.magic.helpfulsense.data.Constants.NO_OF_TABS;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static final int POS_ALERT = 0, POS_MESSAGE = 1, POS_CONTACTS = 2;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
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
            default:
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