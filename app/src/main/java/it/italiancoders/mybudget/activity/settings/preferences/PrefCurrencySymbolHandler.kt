/*
 * Project: mybudget2-mobile-android
 * File: PrefCurrencySymbolHandler.kt
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

package it.italiancoders.mybudget.activity.settings.preferences

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputType
import android.util.Log
import androidx.preference.Preference
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.crashlytics.android.Crashlytics
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.manager.AppPreferenceManager

/**
 * @author fattazzo
 *         <p/>
 *         date: 26/08/19
 */
class PrefCurrencySymbolHandler(preference: Preference) : PreferenceHandler(preference) {

    @SuppressLint("ApplySharedPref")
    override fun handle(context: Context) {
        MaterialDialog(context).show {
            input(inputType = InputType.TYPE_CLASS_TEXT)
            title(R.string.settings_currency_symbol)
            positiveButton(android.R.string.ok) {
                AppPreferenceManager.setCurrentSymbol(
                    context,
                    it.getInputField().editableText.toString()
                )
                updatesummary(context)
            }
            negativeButton(android.R.string.cancel)
        }
    }

    override fun updatesummary(context: Context) {
        try {
            val currentSymbol = AppPreferenceManager.getCurrencySymbol(context)
            preference.summary = "${context.getString(R.string.current)}: $currentSymbol"
        } catch (ex: Exception) {
            Crashlytics.logException(ex)
            Log.e("Preferences", ex.message.orEmpty())
            preference.summary = "Nd."
        }
    }
}