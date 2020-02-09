package com.andruid.magic.helpfulsense.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.andruid.magic.helpfulsense.data.NO_OF_TABS
import com.andruid.magic.helpfulsense.ui.fragment.AlertFragment
import com.andruid.magic.helpfulsense.ui.fragment.ContactsFragment
import com.andruid.magic.helpfulsense.ui.fragment.MessageFragment
import com.andruid.magic.helpfulsense.ui.fragment.SettingsFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    companion object {
        private const val POS_ALERT = 0
        private const val POS_MESSAGE = 1
        private const val POS_CONTACTS = 2
    }

    override fun getItemCount() = NO_OF_TABS

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            POS_ALERT -> AlertFragment.newInstance()
            POS_MESSAGE -> MessageFragment.newInstance()
            POS_CONTACTS -> ContactsFragment.newInstance()
            else -> SettingsFragment.newInstance()
        }
    }
}