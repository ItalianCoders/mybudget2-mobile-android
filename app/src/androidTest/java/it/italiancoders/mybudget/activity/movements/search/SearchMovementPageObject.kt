/*
 * Project: mybudget2-mobile-android
 * File: SearchMovementPageObject.kt
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

package it.italiancoders.mybudget.activity.movements.search

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.ViewActions.setTextInTextView

/**
 * @author fattazzo
 *         <p/>
 *         date: 11/09/19
 */
class SearchMovementPageObject {

    fun checkMovementsListChildCount(count: Int) {
        Espresso.onView(ViewMatchers.withId(R.id.movements_recycler_view))
            .check(ViewAssertions.matches(ViewMatchers.hasChildCount(count)))
    }

    fun setTextInDayTextView(text: String) {
        onView(withId(R.id.day_TIET)).perform(setTextInTextView(text))
    }

    fun setTextInMonthTextView(text: String) {
        onView(withId(R.id.month_TIET)).perform(setTextInTextView(text))
    }

    fun setTextInYearTextView(text: String) {
        onView(withId(R.id.year_TIET)).perform(setTextInTextView(text))
    }

    fun clickSearchButton() {
        onView(withId(R.id.search_button)).perform(click())
    }
}