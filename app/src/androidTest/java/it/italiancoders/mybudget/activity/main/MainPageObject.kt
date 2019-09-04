/*
 * Project: mybudget2-mobile-android
 * File: MainPageObject.kt
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

import android.widget.DatePicker
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.mikephil.charting.charts.Chart
import it.italiancoders.mybudget.Matchers.atPosition
import it.italiancoders.mybudget.R
import org.hamcrest.CoreMatchers.*
import java.util.*


/**
 * @author fattazzo
 *         <p/>
 *         date: 28/08/19
 */
class MainPageObject {

    fun clickNextPeriodType() {
        onView(withId(R.id.change_period_type_fab)).perform(click())
    }

    fun checkPeriodoText(periodType: PeriodType?, date: Date, checkMatchText: Boolean = true) {

        val matchText = withText(periodType?.formatDate(date))
        val notMatchText = not(matchText)

        onView(withId(R.id.periodo_text_view)).check(
            matches(if (checkMatchText) matchText else notMatchText)
        )
    }

    fun clickPeriodDescription() {
        onView(withId(R.id.periodo_text_view)).perform(click())
    }

    fun checkChartViewNoDataVisibility(checkVisible: Boolean) {

        val matchVisible =
            if (checkVisible) isDisplayed() else withEffectiveVisibility(Visibility.GONE)

        onView(withId(R.id.charts_recycler_view))
            .check(
                matches(
                    atPosition(
                        0,
                        hasDescendant(allOf(withText(R.string.no_data_for_period), matchVisible))
                    )
                )
            )
    }

    fun checkChartViewChartVisibility(checkVisible: Boolean) {

        val matchVisible =
            if (checkVisible) isDisplayed() else withEffectiveVisibility(Visibility.GONE)

        onView(withId(R.id.charts_recycler_view))
            .check(
                matches(
                    atPosition(
                        0,
                        hasDescendant(allOf(instanceOf(Chart::class.java), matchVisible))
                    )
                )
            )
    }

    fun checkMonthPickeDialogVisible() {
        onView(withClassName(containsString("com.whiteelephant.monthpicker.MonthPickerView"))).check(matches(isDisplayed()))
    }

    fun checkDatePickeDialogVisible() {
        onView(instanceOf(DatePicker::class.java)).check(matches(isDisplayed()))
    }
}