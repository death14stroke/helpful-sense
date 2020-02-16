package com.andruid.magic.helpfulsense.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andruid.magic.helpfulsense.util.isFirstTime

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, when (isFirstTime()) {
            true -> IntroActivity::class.java
            false -> HomeActivity::class.java
        }))
        finish()
    }
}