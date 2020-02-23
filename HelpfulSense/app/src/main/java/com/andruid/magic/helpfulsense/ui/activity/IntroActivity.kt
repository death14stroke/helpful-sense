package com.andruid.magic.helpfulsense.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.util.firstTimeDone
import com.andruid.magic.library.color
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import com.github.paolorotolo.appintro.model.SliderPagerBuilder

/**
 * Intro screen of the application
 */
class IntroActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showSkipButton(true)
        setVibrate(true)
        setVibrateIntensity(30)

        addSlide(AppIntroFragment.newInstance(buildSliderPage(getString(R.string.quick_actions_title),
                getString(R.string.quick_actions_desc), R.drawable.ic_alert, R.color.colorAccent).build()))
        addSlide(AppIntroFragment.newInstance(buildSliderPage(getString(R.string.autostart_title),
                getString(R.string.autostart_desc), R.drawable.ic_low_battery, R.color.colorTab1).build()))
        addSlide(AppIntroFragment.newInstance(buildSliderPage(getString(R.string.quick_gestures_title),
                getString(R.string.quick_gestures_desc), R.drawable.ic_message, R.color.colorTab2).build()))
        addSlide(AppIntroFragment.newInstance(buildSliderPage(getString(R.string.emergency_contacts_title),
                getString(R.string.emergency_contacts_desc), R.drawable.ic_contacts, R.color.colorTab3).build()))

        askForPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE), 2)
        askForPermissions(arrayOf(Manifest.permission.SEND_SMS), 3)
        askForPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 4)
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        goToHomeActivity()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        goToHomeActivity()
    }

    /**
     * Launch [HomeActivity] when intro is finished/skipped
     */
    private fun goToHomeActivity() {
        firstTimeDone()
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    /**
     * Build a intro page instance for a feature
     * @param title title of the feature
     * @param desc description of the feature
     * @param image image resource of the feature
     * @param bgColor background color of the feature intro
     * @return intro slider for the feature
     */
    private fun buildSliderPage(title: String, desc: String, @DrawableRes image: Int, @ColorRes bgColor: Int)
            : SliderPagerBuilder {
        return SliderPagerBuilder()
                .title(title)
                .description(desc)
                .imageDrawable(image)
                .bgColor(color(bgColor))
                .titleColor(color(android.R.color.white))
                .descColor(color(android.R.color.white))
    }
}