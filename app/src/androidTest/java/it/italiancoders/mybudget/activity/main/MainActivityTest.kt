/*
 * Project: mybudget2-mobile-android
 * File: MainActivityTest.kt
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

import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.rule.ActivityTestRule
import it.italiancoders.mybudget.activity.BaseActivityTest
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import java.util.*


/**
 * @author fattazzo
 *         <p/>
 *         date: 27/08/19
 */
class MainActivityTest : BaseActivityTest() {

    @Rule
    @JvmField
    var rule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java, true, false)

    private val mainPageObject = MainPageObject()



    @Test
    fun testPeriodTypeDescription() {
        //RESTMockServer.whenGET(pathContains(ExpenseSummaryConfig.PATH_GENERIC_OK))
        //    .thenReturnFile(ExpenseSummaryConfig.DATA_2019_08_OK)

        rule.launchActivity(null)
        val viewModel = rule.activity.binding.model!!

        // Initial type: PeriodType.MONTH date: now
        val periodTypeInitial = PeriodType.MONTH
        assertThat(viewModel.periodType.value!!, `is`(equalTo(periodTypeInitial)))
        mainPageObject.checkPeriodoText(PeriodType.MONTH, Calendar.getInstance().time)

        repeat(PeriodType.values().count()) {

            // change period type
            mainPageObject.clickNextPeriodType()

            drain()

            // check if period description is valid
            mainPageObject.checkPeriodoText(viewModel.periodType.value, Calendar.getInstance().time)
        }

        // PeriodType is a "state machine" so after repeat the value return to the initial (periodTypeInitial)
        assertThat(viewModel.periodType.value!!, `is`(equalTo(periodTypeInitial)))
        mainPageObject.checkPeriodoText(PeriodType.MONTH, Calendar.getInstance().time)
    }

    @Test
    fun testDialogsPeriodType() {

        //RESTMockServer.whenGET(pathContains(ExpenseSummaryConfig.PATH_GENERIC_OK))
        //    .thenReturnFile(ExpenseSummaryConfig.DATA_2019_08_OK)

        rule.launchActivity(null)
        val viewModel = rule.activity.binding.model!!

        repeat(PeriodType.values().count()) {

            // change period type
            mainPageObject.clickNextPeriodType()

            // open selector
            mainPageObject.clickPeriodDescription()

            drain()

            // check if a correct dialog is displayed according with current PeriodType
            assertThat(viewModel.periodType.value, `is`(notNullValue()))
            when (viewModel.periodType.value) {
                PeriodType.MONTH -> mainPageObject.checkMonthPickeDialogVisible()
                PeriodType.WEEK -> mainPageObject.checkDatePickeDialogVisible()
                PeriodType.DAY -> mainPageObject.checkDatePickeDialogVisible()
            }
            Espresso.pressBackUnconditionally()
        }
    }
}