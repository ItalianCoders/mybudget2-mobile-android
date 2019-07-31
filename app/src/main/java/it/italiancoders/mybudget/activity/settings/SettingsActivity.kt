/*
 * Project: mybudget2-mobile-android
 * File: SettingsActivity.kt
 *
 * Created by fattazzo
 * Copyright © 2019 Gianluca Fattarsi. All rights reserved.
 *
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package it.italiancoders.mybudget.activity.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.crashlytics.android.Crashlytics
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.tutorial.AbstractTutorialActivity
import it.italiancoders.mybudget.utils.PrivacyPolicyManager

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        private var prefKeyPrivacy: Preference? = null
        private var prefVersion: Preference? = null
        private var prefResetTutorial: Preference? = null

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            prefKeyPrivacy = findPreference(resources.getString(R.string.pref_key_privacy))
            prefVersion = findPreference(resources.getString(R.string.pref_key_version))
            prefResetTutorial = findPreference(resources.getString(R.string.pref_key_tutorial))

            try {
                prefVersion?.summary = this.context!!.packageManager.getPackageInfo(
                    this.context!!.packageName,
                    0
                ).versionName
            } catch (ex: PackageManager.NameNotFoundException) {
                Crashlytics.logException(ex)
                Log.e("Preferences", ex.message.orEmpty())
                prefVersion?.summary = "Nd."
            }
        }

        @SuppressLint("ApplySharedPref")
        override fun onPreferenceTreeClick(preference: Preference?): Boolean {

            context?.let {
                when (preference?.key.orEmpty()) {
                    prefKeyPrivacy?.key.orEmpty() -> PrivacyPolicyManager.showContent(it)
                    prefResetTutorial?.key.orEmpty() -> {
                        it.getSharedPreferences(AbstractTutorialActivity.TUTORIAL_PREF_FILE, Context.MODE_PRIVATE)
                            .edit().clear().commit()
                        MaterialDialog(it).show {
                            title(R.string.settings_tutorial_reset)
                            message(R.string.reset_tutorial_complete_message)
                            icon(R.drawable.ic_live_help)
                            cancelOnTouchOutside(true)
                            cancelable(true)
                        }
                    }
                    else -> {
                        /** Non used **/
                    }
                }
            }

            return super.onPreferenceTreeClick(preference)
        }
    }
}