/*
 * Project: mybudget2-mobile-android
 * File: PrefClearDataHandler.kt
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

import android.content.Context
import androidx.preference.Preference
import com.afollestad.materialdialogs.MaterialDialog
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.db.AppDatabase
import java.text.DecimalFormat

/**
 * @author fattazzo
 *         <p/>
 *         date: 26/08/19
 */
class PrefClearDataHandler(preference: Preference) : PreferenceHandler(preference) {

    override fun handle(context: Context) {
        AppDatabase.clearAllData(context)

        MaterialDialog(context).show {
            title(R.string.settings_cleared_data_title)
            message(R.string.settings_cleared_data_message)
            icon(R.drawable.ic_storage)
            positiveButton(android.R.string.ok)
        }
        updatesummary(context)
    }

    override fun updatesummary(context: Context) {
        val dbSize = AppDatabase.getSize(context)
        if (dbSize != null) {
            preference.summary =
                "${context.getString(R.string.size)}: ${DecimalFormat("0.00").format(dbSize)} MB"
        } else {
            preference.summary = "Nd."
        }
    }
}