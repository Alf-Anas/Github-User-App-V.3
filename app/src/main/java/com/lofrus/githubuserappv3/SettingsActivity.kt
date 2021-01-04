package com.lofrus.githubuserappv3

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.lofrus.githubuserappv3.broadcast.AlarmReceiver
import java.util.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.app_name_settings)
    }

    class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {

        private lateinit var keyReminder: String
        private lateinit var keyLanguage: String
        private lateinit var defLanguage: String
        private lateinit var reminderActivePreference: SwitchPreference
        private lateinit var languagePreference: ListPreference
        private lateinit var alarmReceiver: AlarmReceiver
        private lateinit var locale: Locale

        companion object {
            private const val reminderAt9 = "09:00"
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.root_preferences)
            alarmReceiver = AlarmReceiver()
            init()
            setSummaries()
        }

        private fun init() {
            keyReminder = resources.getString(R.string.key_reminder9)
            keyLanguage = resources.getString(R.string.key_language)
            defLanguage = resources.getString(R.string.ln_english)
            reminderActivePreference =
                findPreference<SwitchPreference>(keyReminder) as SwitchPreference
            languagePreference = findPreference<ListPreference>(keyLanguage) as ListPreference
        }

        private fun setSummaries() {
            val sh = preferenceManager.sharedPreferences
            reminderActivePreference.isChecked = sh.getBoolean(keyReminder, false)
            languagePreference.value = sh.getString(keyLanguage, defLanguage)
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences,
            key: String
        ) {
            if (key == keyReminder) {
                if (reminderActivePreference.isChecked) {
                    activity?.let {
                        alarmReceiver.setRepeatingAlarm(
                            it, getString(R.string.reminder_at_9_title),
                            reminderAt9, getString(R.string.reminder_at_9_message)
                        )
                    }
                } else {
                    activity?.let { alarmReceiver.cancelAlarm(it) }
                }
            }
            if (key == keyLanguage) {
                val lang: String = languagePreference.value
                setLocale(lang)
            }
        }

        private fun setLocale(localeName: String) {
            locale = Locale(localeName)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.setLocale(locale)
            res.updateConfiguration(conf, dm)
            val refresh = Intent(
                activity,
                SplashScreenActivity::class.java
            )
            refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            refresh.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            refresh.putExtra("EXIT", true)
            startActivity(refresh)
            activity?.finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}