/*
 * Project: mybudget2-mobile-android
 * File: ActivationPageObject.kt
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

package it.italiancoders.mybudget.activity.activation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.ViewActions.setTextInTextView
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not

/**
 * @author fattazzo
 *         <p/>
 *         date: 11/09/19
 */
class ActivationPageObject {

    fun checkUserNameText(text: String) {
        onView(withId(R.id.username_ET)).check(matches(withText(text)))
    }

    fun checkCodiceText(text: String) {
        onView(withId(R.id.codice_ET)).check(matches(withText(text)))
    }

    fun checkConfirmButtonIsClickable(clickable: Boolean) {

        if (clickable) {
            onView(withId(R.id.confirmButton)).check(matches(isClickable()))
            onView(allOf(withId(R.id.confirmButton),hasBackground(R.drawable.box_round_green), isDisplayed()))
        } else {
            onView(withId(R.id.confirmButton)).check(matches(not(isClickable())))
            onView(allOf(withId(R.id.confirmButton),hasBackground(R.drawable.box_round_gray), isDisplayed()))
        }
    }

    fun setUserNameText(text: String) {
        onView(withId(R.id.username_ET)).perform(setTextInTextView(text))
    }

    fun setCodiceText(text: String) {
        onView(withId(R.id.codice_ET)).perform(setTextInTextView(text))
    }

    fun clickLoginButton() {
        onView(withId(R.id.confirmButton)).perform(click())
    }
}