/*
 * Project: mybudget2-mobile-android
 * File: AppPreferenceManager.kt
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

package it.italiancoders.mybudget.manager

import android.annotation.SuppressLint
import android.content.Context
import androidx.preference.PreferenceManager
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.preferences.chart.ChartColorTheme
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 26/08/19
 */
object AppPreferenceManager {

    private var currencySymbol: String? = null

    private var chartColorTheme: ChartColorTheme? = null

    // --- Chart theme ------------------------------------------------------------------
    fun getChartColorTheme(context: Context): ChartColorTheme {
        if (chartColorTheme == null) {
            val themeIndex = PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(
                    context.resources.getString(R.string.pref_key_chart_color_theme),
                    ChartColorTheme.COLORFUL.ordinal
                )
            chartColorTheme = ChartColorTheme.values()[themeIndex]
        }
        return chartColorTheme ?: ChartColorTheme.COLORFUL
    }

    @SuppressLint("ApplySharedPref")
    fun setChartColorTheme(context: Context, themeIndex: Int) {
        val prefKey = context.resources.getString(R.string.pref_key_chart_color_theme)

        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putInt(prefKey, themeIndex)
            .commit()

        chartColorTheme = ChartColorTheme.values()[themeIndex]
    }

    // --- Currency Symbol --------------------------------------------------------------
    fun getCurrencySymbol(context: Context?): String? {
        if (currencySymbol == null && context != null) {
            val currency = Currency.getInstance(Locale.getDefault())
            currencySymbol = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(
                    context.resources.getString(R.string.pref_key_currency_symbol),
                    currency.symbol
                )
        }
        return currencySymbol
    }

    @SuppressLint("ApplySharedPref")
    fun setCurrentSymbol(context: Context, newValue: String?) {
        val prefKey = context.resources.getString(R.string.pref_key_currency_symbol)

        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(prefKey, newValue)
            .commit()

        currencySymbol = null
    }
}