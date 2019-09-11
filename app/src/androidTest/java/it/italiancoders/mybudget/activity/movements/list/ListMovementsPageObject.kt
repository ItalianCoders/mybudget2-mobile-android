/*
 * Project: mybudget2-mobile-android
 * File: ListMovementsPageObject.kt
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

package it.italiancoders.mybudget.activity.movements.list

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.RecyclerViewItemCountAssertion.Companion.withItemCount
import it.italiancoders.mybudget.activity.main.view.lastmovements.MovementsDataAdapter

/**
 * @author fattazzo
 *         <p/>
 *         date: 11/09/19
 */
class ListMovementsPageObject {

    fun checkMovementsListChildCount(count: Int) {
        onView(withId(R.id.movements_recycler_view)).check(withItemCount(count))
    }

    fun clickMovementItem(position: Int) {
        onView(withId(R.id.movements_recycler_view)).perform(
            actionOnItemAtPosition<MovementsDataAdapter.MovementViewHolder>(
                position,
                click()
            )
        )
    }

    fun swipeRightMovementItem(position: Int) {
        onView(withId(R.id.movements_recycler_view)).perform(
            actionOnItemAtPosition<MovementsDataAdapter.MovementViewHolder>(
                position,
                swipeRight()
            )
        )
    }

    fun checkMovementDeletedSnackBarVisible() {
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.movement_deleted)))
    }
}