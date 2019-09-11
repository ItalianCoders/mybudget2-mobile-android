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

import androidx.test.rule.ActivityTestRule
import it.italiancoders.mybudget.activity.BaseActivityTest
import it.italiancoders.mybudget.manager.movements.ParametriRicerca
import it.italiancoders.mybudget.mocks.config.ExpenseSummaryConfig
import it.italiancoders.mybudget.mocks.data.ExpenseSummaryMockData
import it.italiancoders.mybudget.rest.models.ExpenseSummary
import it.italiancoders.mybudget.tutorial.TutorialMainActivity
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
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

    override fun getActivityTutorialKey(): String? = TutorialMainActivity.KEY

    override fun isTutorialAlreadyShow(): Boolean = true

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
        assertThat(viewModel.periodType.value!!,`is`(equalTo(PeriodType.MONTH)))

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

        val expectedSummary = ExpenseSummaryMockData.fromJsonFile<ExpenseSummary>(ExpenseSummaryConfig.DATA_2019_07_OK,ExpenseSummary::class)
        assertThat(viewModel.total.value!!.toDouble(), `is`(expectedSummary!!.totalAmount))

        // Open last movement view
        mainPageObject.clickLastMovementHeader(true,rule.activity)
        mainPageObject.checkLastMovemementVisible(expectedSummary.lastMovements.contents[0])
        mainPageObject.clickLastMovementHeader(false,rule.activity)

        // Load empty period
        viewModel.loadExpenseSummary(2019, 9, 1)

        // Check empty chart
        // Check no data label displayed
        mainPageObject.checkChartViewChartVisibility(false)
        mainPageObject.checkChartViewNoDataVisibility(true)


    }

    @Test
    fun changeWeekPeriod() {

        val cal = Calendar.getInstance()
        ExpenseSummaryMockData.mockData(
            expenseSummaryManager,
            ParametriRicerca(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1),
            ExpenseSummaryConfig.DATA_2019_08_OK
        )
        ExpenseSummaryMockData.mock2019_08_week_2_dataOk(expenseSummaryManager)
        ExpenseSummaryMockData.mock2019_08_week_3_dataOk(expenseSummaryManager)


        rule.launchActivity(null)

        val viewModel = rule.activity.binding.model!!

        assertThat(viewModel.periodType.value!!, `is`(equalTo(PeriodType.MONTH)))

        viewModel.year.postValue(2019)
        viewModel.month.postValue(7)
        viewModel.day.postValue(6)
        drain()

        // Change to PeriodType.WEEK
        mainPageObject.clickNextPeriodType()

        var expectedExpense = ExpenseSummaryMockData.fromJsonFile<ExpenseSummary>(ExpenseSummaryConfig.DATA_2019_08_WEEK_2_OK,ExpenseSummary::class)
        assertThat(viewModel.total.value!!.toDouble(), `is`(expectedExpense!!.totalAmount))

        assertThat(viewModel.periodType.value!!, `is`(equalTo(PeriodType.WEEK)))

        // Open last movement view
        mainPageObject.clickLastMovementHeader(true,rule.activity)
        mainPageObject.checkLastMovemementVisible(expectedExpense.lastMovements.contents[0])
        mainPageObject.clickLastMovementHeader(false,rule.activity)

        // Open DatePickerDialog
        mainPageObject.clickPeriodDescription()
        mainPageObject.checkDatePickeDialogVisible()

        mainPageObject.changeDatePickerDate(2019,8,14)

        assertThat(viewModel.periodType.value!!, `is`(equalTo(PeriodType.WEEK)))
        assertThat(viewModel.year.value!!, `is`(2019))
        assertThat(viewModel.month.value!!, `is`(7))
        assertThat(viewModel.day.value!!, `is`(14))

        expectedExpense = ExpenseSummaryMockData.fromJsonFile<ExpenseSummary>(ExpenseSummaryConfig.DATA_2019_08_WEEK_3_OK,ExpenseSummary::class)
        assertThat(viewModel.total.value!!.toDouble(), `is`(expectedExpense!!.totalAmount))

        // Open last movement view
        mainPageObject.clickLastMovementHeader(true,rule.activity)
        mainPageObject.checkLastMovemementVisible(expectedExpense.lastMovements.contents[0])
        mainPageObject.clickLastMovementHeader(false,rule.activity)
    }

    @Test
    fun changeDayPeriod() {

        val cal = Calendar.getInstance()
        ExpenseSummaryMockData.mockData(
            expenseSummaryManager,
            ParametriRicerca(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1),
            ExpenseSummaryConfig.DATA_2019_08_OK
        )
        ExpenseSummaryMockData.mock2019_08_week_2_dataOk(expenseSummaryManager)
        ExpenseSummaryMockData.mock2019_08_day_06_dataOk(expenseSummaryManager)
        ExpenseSummaryMockData.mock2019_08_day_14_dataOk(expenseSummaryManager)


        rule.launchActivity(null)

        val viewModel = rule.activity.binding.model!!

        assertThat(viewModel.periodType.value!!, `is`(equalTo(PeriodType.MONTH)))

        viewModel.year.postValue(2019)
        viewModel.month.postValue(7)
        viewModel.day.postValue(6)
        drain()

        // Change to PeriodType.WEEK and next PeriodType.DAY
        mainPageObject.clickNextPeriodType()
        mainPageObject.clickNextPeriodType()

        var expectedExpense = ExpenseSummaryMockData.fromJsonFile<ExpenseSummary>(ExpenseSummaryConfig.DATA_2019_08_DAY_06_OK,ExpenseSummary::class)
        assertThat(viewModel.total.value!!.toDouble(), `is`(expectedExpense!!.totalAmount))

        assertThat(viewModel.periodType.value!!, `is`(equalTo(PeriodType.DAY)))

        // Open last movement view
        mainPageObject.clickLastMovementHeader(true,rule.activity)
        mainPageObject.checkLastMovemementVisible(expectedExpense.lastMovements.contents[0])
        mainPageObject.clickLastMovementHeader(false,rule.activity)

        // Open DatePickerDialog
        mainPageObject.clickPeriodDescription()
        mainPageObject.checkDatePickeDialogVisible()

        mainPageObject.changeDatePickerDate(2019,8,14)

        assertThat(viewModel.periodType.value!!, `is`(equalTo(PeriodType.DAY)))
        assertThat(viewModel.year.value!!, `is`(2019))
        assertThat(viewModel.month.value!!, `is`(7))
        assertThat(viewModel.day.value!!, `is`(14))

        expectedExpense = ExpenseSummaryMockData.fromJsonFile<ExpenseSummary>(ExpenseSummaryConfig.DATA_2019_08_DAY_14_OK,ExpenseSummary::class)
        assertThat(viewModel.total.value!!.toDouble(), `is`(expectedExpense!!.totalAmount))

        // Open last movement view
        mainPageObject.clickLastMovementHeader(true,rule.activity)
        mainPageObject.checkLastMovemementVisible(expectedExpense.lastMovements.contents[0])
        mainPageObject.clickLastMovementHeader(false,rule.activity)
    }
}