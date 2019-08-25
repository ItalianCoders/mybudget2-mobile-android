/*
 * Project: mybudget2-mobile-android
 * File: PeriodType.kt
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

package it.italiancoders.mybudget.activity.main

import android.annotation.SuppressLint
import it.italiancoders.mybudget.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 23/08/19
 */
enum class PeriodType {

    MONTH {
        override fun getImageRes(): Int = R.drawable.ic_month_calendar

        override fun nextType(): PeriodType = WEEK

        @SuppressLint("ConstantLocale")
        override fun formatDate(date: Date): String {
            return try {
                SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(date)
            } catch (e: Exception) {
                ""
            }
        }
    },
    WEEK {
        override fun getImageRes(): Int = R.drawable.ic_weekly_calendar

        override fun nextType(): PeriodType = DAY

        @SuppressLint("ConstantLocale")
        override fun formatDate(date: Date): String {

            return try {
                val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())

                // first day of week
                val calFirstDay = Calendar.getInstance()
                calFirstDay.time = date
                calFirstDay.set(Calendar.DAY_OF_WEEK, calFirstDay.firstDayOfWeek)

                // last day of week
                val calLastDay = Calendar.getInstance()
                calLastDay.time = calFirstDay.time
                calFirstDay.add(Calendar.DATE, 6)

                "${dateFormat.format(calLastDay.time)} - ${dateFormat.format(calFirstDay.time)}"
            } catch (e: Exception) {
                ""
            }
        }
    },
    DAY {
        override fun getImageRes(): Int = R.drawable.ic_day_calendar

        override fun nextType(): PeriodType = MONTH

        @SuppressLint("ConstantLocale")
        override fun formatDate(date: Date): String {
            return try {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
            } catch (e: Exception) {
                ""
            }
        }
    };


    abstract fun formatDate(date: Date): String

    abstract fun nextType(): PeriodType

    abstract fun getImageRes(): Int
}