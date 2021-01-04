package com.lofrus.githubuserappv3

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import java.util.*

class SplashScreenActivity : AppCompatActivity() {

    private val delayTimes: Long = 3500
    private lateinit var locale: Locale

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            run {
                val sh = PreferenceManager.getDefaultSharedPreferences(this)
                val lang =
                    sh.getString(getString(R.string.key_language), getString(R.string.ln_english))
                if (lang != getString(R.string.ln_english) && !lang.isNullOrBlank()) {
                    locale = Locale(lang)
                    val res = resources
                    val dm = res.displayMetrics
                    val conf = res.configuration
                    conf.setLocale(locale)
                    res.updateConfiguration(conf, dm)
                }
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }, delayTimes)
    }

}