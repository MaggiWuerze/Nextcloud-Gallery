package de.maggiwuerze.nextcloudgallery.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.preference.PreferenceManager
import com.jakewharton.threetenabp.AndroidThreeTen
import de.maggiwuerze.nextcloudgallery.R


// SplashActivity.kt

class Splash : Activity() {

    private val SPLASH_TIME_OUT = 300
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        AndroidThreeTen.init(this)
        var onboardingComplete = PreferenceManager.getDefaultSharedPreferences(this)
            .getString(getString(R.string.onboarding_complete), "false")

        if (onboardingComplete == "true") {
            switchToMain()
        } else {
            Handler().postDelayed(
                {
                    switchToOnboarding()
                }, SPLASH_TIME_OUT.toLong()
            )
        }
    }

    private fun switchToMain() {
        var intent = Intent(this, Main::class.java)
        startActivity(intent)
        finish()
    }

    private fun switchToOnboarding() {
        startActivity(Intent(this, Onboarding::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()

    }

}

