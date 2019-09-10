/*
 * Project: mybudget2-mobile-android
 * File: MovementsPageObject.kt
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

package it.italiancoders.mybudget.activity.movements

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import it.italiancoders.mybudget.R

/**
 * @author fattazzo
 *         <p/>
 *         date: 10/09/19
 */
class MovementsPageObject {

    fun checkMovementsListChildCount(count: Int) {
        onView(withId(R.id.movements_recycler_view)).check(matches(hasChildCount(count)))
    }

    fun checkNewMovementFabVisible() {
        onView(withId(R.id.new_movement_fab)).check(matches(isDisplayed()))
    }

    fun checkNewMovementFabNotVisible() {
        onView(withId(R.id.new_movement_fab)).check(doesNotExist())
    }

    fun clickNewMovementFab() {
        onView(withId(R.id.new_movement_fab)).perform(click())
    }

    fun clickNewMovementFromMenu() {
        onView(withId(R.id.action_add)).perform(click())
    }

    fun pressBack() {
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())
    }

    fun checkActivityNewMovementVisible() {
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(R.string.movement_new))))
    }

    fun checkActivityMovementsVisible() {
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(R.string.activity_movements_title))))
    }

    fun swipeUpMovements() {
        onView(withId(R.id.movements_recycler_view)).perform(swipeUp())
    }
}