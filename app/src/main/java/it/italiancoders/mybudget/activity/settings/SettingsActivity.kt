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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.settings.preferences.*

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

        private val preferencesList = mutableListOf<PreferenceHandler>()

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            initPreferencesHandler()

            // Update all summary
            context?.let { preferencesList.forEach { pref -> pref.updatesummary(it) } }
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            context?.let {
                preferencesList.firstOrNull { handler -> handler.getKey() == preference?.key }
                    ?.handle(it)
            }
            return super.onPreferenceTreeClick(preference)
        }

        private fun initPreferencesHandler() {
            val prefPrivacy: Preference? =
                findPreference(resources.getString(R.string.pref_key_privacy))
            prefPrivacy?.let { preferencesList.add(PrefPrivacyHandler(it)) }

            val prefVersion: Preference? =
                findPreference(resources.getString(R.string.pref_key_version))
            prefVersion?.let { preferencesList.add(PrefAppVersionHandler(it)) }

            val prefClearData: Preference? =
                findPreference(resources.getString(R.string.pref_key_clear_data))
            prefClearData?.let { preferencesList.add(PrefClearDataHandler(it)) }

            val prefResetTutorial: Preference? =
                findPreference(resources.getString(R.string.pref_key_tutorial))
            prefResetTutorial?.let { preferencesList.add(PrefResetTutorialHandler(it)) }

            val prefCurrencySymbol: Preference? =
                findPreference(resources.getString(R.string.pref_key_currency_symbol))
            prefCurrencySymbol?.let { preferencesList.add(PrefCurrencySymbolHandler(it)) }
        }
    }
}