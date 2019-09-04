/*
 * Project: mybudget2-mobile-android
 * File: ExpenseSummaryTest.kt
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

import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import it.italiancoders.mybudget.activity.BaseActivityTest
import it.italiancoders.mybudget.manager.movements.ParametriRicerca
import it.italiancoders.mybudget.mocks.config.ExpenseSummaryConfig
import it.italiancoders.mybudget.mocks.data.ExpenseSummaryMockData
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import java.util.*


/**
 * @author fattazzo
 *         <p/>
 *         date: 29/08/19
 */
class ExpenseSummaryTest : BaseActivityTest() {

    private val mainPageObject = MainPageObject()

    @Rule
    @JvmField
    var rule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java, true, false)


    @Test
    fun changeMonthPeriod() {

        val cal = Calendar.getInstance()
        ExpenseSummaryMockData.mockData(
            expenseSummaryManager,
            ParametriRicerca(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1),
            ExpenseSummaryConfig.DATA_2019_08_OK
        )
        ExpenseSummaryMockData.mock2019_07_dataOk(expenseSummaryManager)
        ExpenseSummaryMockData.mock2019_10_dataOk_empty(expenseSummaryManager)

        rule.launchActivity(null)

        val viewModel = rule.activity.binding.model!!

        // Check if MONTH is a default type
        ViewMatchers.assertThat(
            viewModel.periodType.value!!,
            Matchers.`is`(Matchers.equalTo(PeriodType.MONTH))
        )

        // Check if current month period is not 2019-07
        val calendar = Calendar.getInstance()
        calendar.set(2019, 6, 1)
        mainPageObject.checkPeriodoText(PeriodType.MONTH, calendar.time, false)

        // No view interaction for PeriodType.MONTH because of the library MonthAndYearPicker
        viewModel.loadExpenseSummary(2019, 6, 1)

        // Check period description update
        mainPageObject.checkPeriodoText(PeriodType.MONTH, calendar.time)
        // Check empty chart
        // Check no data label displayed
        mainPageObject.checkChartViewChartVisibility(true)
        mainPageObject.checkChartViewNoDataVisibility(false)

        // Load empty period
        viewModel.loadExpenseSummary(2019, 9, 1)

        // Check empty chart
        // Check no data label displayed
        mainPageObject.checkChartViewChartVisibility(false)
        mainPageObject.checkChartViewNoDataVisibility(true)
    }
}