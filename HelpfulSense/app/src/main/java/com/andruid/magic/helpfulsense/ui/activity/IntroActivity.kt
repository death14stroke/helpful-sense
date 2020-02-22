package com.andruid.magic.helpfulsense.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.andruid.magic.helpfulsense.R
import com.andruid.magic.helpfulsense.util.firstTimeDone
import com.andruid.magic.library.color
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import com.github.paolorotolo.appintro.model.SliderPagerBuilder

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

        askForPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 2)
        askForPermissions(arrayOf(Manifest.permission.SEND_SMS), 3)
        askForPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 4)
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        goToMainActivity()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        goToMainActivity()
    }

    private fun goToMainActivity() {
        firstTimeDone()
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun buildSliderPage(title: String, desc: String, image: Int, bgColor: Int): SliderPagerBuilder {
        return SliderPagerBuilder()
                .title(title)
                .description(desc)
                .imageDrawable(image)
                .bgColor(color(bgColor))
                .titleColor(color(android.R.color.white))
                .descColor(color(android.R.color.white))
    }
}