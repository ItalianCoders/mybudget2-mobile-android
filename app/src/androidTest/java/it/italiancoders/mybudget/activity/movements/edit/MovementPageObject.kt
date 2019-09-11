/*
 * Project: mybudget2-mobile-android
 * File: MovementPageObject.kt
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

package it.italiancoders.mybudget.activity.movements.edit

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.databinding.converters.BigDecimalStringConversion
import it.italiancoders.mybudget.rest.models.Movement
import java.text.DateFormat

/**
 * @author fattazzo
 *         <p/>
 *         date: 09/09/19
 */
class MovementPageObject {

    fun checkMovement(movement: Movement) {

        // Category
        onView(withId(R.id.categoty_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.categoty_text_view)).check(matches(withText(movement.category.name)))

        // Amount
        onView(withId(R.id.amountTextInputEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.amountTextInputEditText)).check(
            matches(
                withText(
                    BigDecimalStringConversion.toString("", movement.amount, movement.amount)
                )
            )
        )

        // Date
        onView(withId(R.id.date_text_view)).check(matches(isDisplayed()))
        if (movement.id != null) {
            onView(withId(R.id.date_text_view)).check(
                matches(
                    withText(DateFormat.getDateInstance().format(movement.executedAtDate!!))
                )
            )
        }

        // Time
        onView(withId(R.id.time_text_view)).check(matches(isDisplayed()))
        if (movement.id != null) {
            onView(withId(R.id.time_text_view)).check(
                matches(
                    withText(
                        DateFormat.getTimeInstance().format(movement.executedAtDate!!)
                    )
                )
            )
        }
    }

    fun clickDeleteButton() {
        onView(withId(R.id.delete_button)).perform(click())
    }
}