/*
 * Project: mybudget2-mobile-android
 * File: ChartColorThemePickerPreference.kt
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

package it.italiancoders.mybudget.preferences.chart

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.preference.PreferenceViewHolder
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.customListAdapter
import com.afollestad.materialdialogs.list.getListAdapter
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.manager.AppPreferenceManager

class ChartColorThemePickerPreference(context: Context?, attrs: AttributeSet?) :
    androidx.preference.ListPreference(context, attrs) {
    private var currentIndex = 0
    private var image1: ImageView? = null
    private var image2: ImageView? = null
    private var image3: ImageView? = null
    private var image4: ImageView? = null
    private var image5: ImageView? = null

    private var selectedTheme: ChartColorTheme? = null
    private var summary: TextView? = null

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)

        val view = holder!!.itemView

        view.setPadding(50, 50, 50, 50)

        val iconIV: ImageView? = view.findViewById(R.id.icon)
        iconIV?.setImageDrawable(icon)

        val titleTV: TextView? = view.findViewById(R.id.title)
        titleTV?.text = title

        val themes = ChartColorTheme.values()
        selectedTheme = themes[currentIndex]
        image1 = view.findViewById(R.id.color1)
        image2 = view.findViewById(R.id.color2)
        image3 = view.findViewById(R.id.color3)
        image4 = view.findViewById(R.id.color4)
        image5 = view.findViewById(R.id.color5)
        updateIcons()
        summary = view.findViewById(R.id.summary)
        summary!!.text = themes[currentIndex].name
    }

    override fun onClick() {
        MaterialDialog(context).show {
            title(R.string.charts_color_theme)
            customListAdapter(ChartColorThemeAdapter(context, currentIndex))
            positiveButton(android.R.string.ok) { dialog ->
                val adapter = dialog.getListAdapter() as ChartColorThemeAdapter
                currentIndex = adapter.currentIndex
                persistInt(currentIndex)
                AppPreferenceManager.setChartColorTheme(context, currentIndex)
                selectedTheme = ChartColorTheme.values()[currentIndex]
                updateIcons()
                summary!!.text = ChartColorTheme.values()[currentIndex].name
            }
            negativeButton(android.R.string.cancel)
        }
    }

    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any?) {
        currentIndex = if (restorePersistedValue) {
            this.getPersistedInt(3)
        } else {
            persistInt(3)
            3
        }
    }

    private fun updateIcons() {
        image1!!.setBackgroundColor(selectedTheme!!.colors[0])
        image1!!.tag = selectedTheme
        image2!!.setBackgroundColor(selectedTheme!!.colors[1])
        image2!!.tag = selectedTheme
        image3!!.setBackgroundColor(selectedTheme!!.colors[2])
        image3!!.tag = selectedTheme
        image4!!.setBackgroundColor(selectedTheme!!.colors[3])
        image4!!.tag = selectedTheme
        image5!!.setBackgroundColor(selectedTheme!!.colors[4])
        image5!!.tag = selectedTheme
    }
}