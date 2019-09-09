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

import android.os.SystemClock
import android.widget.DatePicker
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.mikephil.charting.charts.Chart
import com.google.android.material.bottomsheet.BottomSheetBehavior
import it.italiancoders.mybudget.Matchers.atPosition
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.main.view.lastmovements.MovementsDataAdapter
import it.italiancoders.mybudget.rest.models.Movement
import org.awaitility.Awaitility.await
import org.hamcrest.CoreMatchers.*
import java.time.Duration
import java.util.*


/**
 * @author fattazzo
 *         <p/>
 *         date: 28/08/19
 */
class MainPageObject {

    fun clickAddMovememntView() {
        onView(withId(R.id.add_movement_button)).perform(click())
    }

    fun checkActivityNewMovementVisible() {
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(R.string.movement_new))))
    }

    fun checkActivityEditMovementVisible() {
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(R.string.movement_edit))))
    }

    fun pressBack() {
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())
    }

    fun checkActivityMainVisible() {
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(R.string.app_name))))
    }

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
        onView(withClassName(containsString("com.whiteelephant.monthpicker.MonthPickerView"))).check(
            matches(isDisplayed())
        )
    }

    fun checkDatePickeDialogVisible() {
        onView(instanceOf(DatePicker::class.java)).check(matches(isDisplayed()))
    }

    fun changeDatePickerDate(year: Int, month: Int, day: Int) {
        // Change the date of the DatePicker. Don't use "withId" as at runtime Android shares the DatePicker id between several sub-elements
        onView(instanceOf(DatePicker::class.java)).perform(PickerActions.setDate(year, month, day))
        // Click on the "OK" button to confirm and close the dialog
        onView(withText(android.R.string.ok)).perform(click())
    }

    fun clickLastMovementHeader(expand: Boolean, mainActivity: MainActivity) {
        onView(withId(R.id.title_view)).perform(click())

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val stateToWait =
                if (expand) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_COLLAPSED

            await().atMost(Duration.ofSeconds(1))
                .until { mainActivity.mBottomSheetBehavior!!.state == stateToWait }
        } else {
            SystemClock.sleep(600)
        }
    }

    fun checkLastMovemementVisible(movement: Movement) {

        onView(withId(R.id.movements_recycler_view))
            .check(
                matches(
                    atPosition(
                        0,
                        hasDescendant(
                            allOf(
                                withId(R.id.category_text_view),
                                withText(movement.category.name)
                            )
                        )
                    )
                )
            )
    }

    fun clickLastMovemement(mainActivity: MainActivity, position: Int) {
        onView(withId(R.id.movements_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition<MovementsDataAdapter.MovementViewHolder>(position,click()))
    }

    fun clickRefreshOptionMenu() {
        onView(withId(R.id.action_sync)).perform(click())
    }
}