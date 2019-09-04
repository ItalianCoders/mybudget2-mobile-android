/*
 * Project: mybudget2-mobile-android
 * File: MainViewModelTest.kt
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

import it.italiancoders.mybudget.AbstractViewModelTest
import it.italiancoders.mybudget.manager.expensesummary.ExpenseSummaryManager
import it.italiancoders.mybudget.mocks.config.ExpenseSummaryConfig
import it.italiancoders.mybudget.mocks.data.ExpenseSummaryMockData
import it.italiancoders.mybudget.rest.models.ExpenseSummary
import it.italiancoders.mybudget.rest.models.MovementListPage
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mock
import java.math.BigDecimal
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 29/08/19
 */
class MainViewModelTest : AbstractViewModelTest<MainViewModel>() {

    @Mock
    lateinit var expenseSummaryManager: ExpenseSummaryManager

    override fun createViewModel(): MainViewModel = MainViewModel(expenseSummaryManager)

    @Test
    override fun initialValues() {
        assertThat(viewModel.year.value, `is`(Calendar.getInstance().get(Calendar.YEAR)))
        assertThat(viewModel.month.value, `is`(Calendar.getInstance().get(Calendar.MONTH)))
        assertThat(viewModel.day.value, `is`(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)))

        assertThat(viewModel.periodType.value, `is`(PeriodType.MONTH))

        assertThat(viewModel.total.value, `is`(BigDecimal.ZERO))

        assertThat(viewModel.lastMovements.value, `is`(MovementListPage()))

        assertThat(viewModel.categoryOverview.value, `is`(listOf()))

        assertThat(viewModel.loadingData.get(), `is`(false))
    }

    @Test
    fun periodDescriptionTypeMonth() {

        val calendar = Calendar.getInstance()

        val initialDesc = viewModel.periodDescription.value

        assertThat(
            viewModel.periodDescription.value,
            `is`(PeriodType.MONTH.formatDate(calendar.time))
        )

        assertThat(viewModel.day.value, `is`(calendar.get(Calendar.DAY_OF_MONTH)))
        viewModel.day.postValue(2)
        assertThat(viewModel.day.value, `is`(2))

        assertThat(viewModel.periodDescription.value, `is`(initialDesc))
    }

    @Test
    fun periodDescriptionTypeDay() {

        val calendar = Calendar.getInstance()

        viewModel.periodType.postValue(PeriodType.DAY)
        assertThat(viewModel.periodType.value, `is`(PeriodType.DAY))

        val initialDesc = viewModel.periodDescription.value

        assertThat(
            viewModel.periodDescription.value,
            `is`(PeriodType.DAY.formatDate(calendar.time))
        )
        assertThat(viewModel.day.value, `is`(calendar.get(Calendar.DAY_OF_MONTH)))

        val newDay = if (viewModel.day.value!! > 1) 1 else 2
        viewModel.day.postValue(newDay)
        assertThat(viewModel.day.value, `is`(newDay))

        calendar.set(Calendar.DATE, newDay)
        assertThat(
            viewModel.periodDescription.value,
            `is`(PeriodType.DAY.formatDate(calendar.time))
        )

        assertThat(viewModel.periodDescription.value, not(`is`(initialDesc)))
    }

    @Test
    fun periodDescriptionTypeWeek() {

        val calendar = Calendar.getInstance()

        viewModel.periodType.postValue(PeriodType.WEEK)
        assertThat(viewModel.periodType.value, `is`(PeriodType.WEEK))
        assertThat(
            viewModel.periodDescription.value,
            `is`(PeriodType.WEEK.formatDate(calendar.time))
        )

        val initialDesc = viewModel.periodDescription.value

        viewModel.day.postValue(10)
        viewModel.month.postValue(2) // Month java based Jan=0 Dec=11
        viewModel.year.postValue(2018)

        assertThat(viewModel.periodDescription.value, `is`("05/03 - 11/03"))

        assertThat(viewModel.periodDescription.value, not(`is`(initialDesc)))
    }

    /**
     * Load expense summary whit the current parameters
     */
    @Test
    fun loadExpenseSummary() {

        ExpenseSummaryMockData.mock2019_07_dataOk(expenseSummaryManager)

        viewModel.year.postValue(2019)
        viewModel.month.postValue(6)
        viewModel.day.postValue(1)
        viewModel.periodType.postValue(PeriodType.MONTH)

        assertThat(viewModel.month.value, `is`(6))

        viewModel.loadExpenseSummary()
        val expenseSummaryExpected = ExpenseSummaryMockData.fromJsonFile<ExpenseSummary>(
            ExpenseSummaryConfig.DATA_2019_07_OK,
            ExpenseSummary::class
        )
        checkExpenseSummaryData(expenseSummaryExpected)
    }

    /**
     * Load expense summary for the next period type
     */
    @Test
    fun loadNextPeriodTypeExpenseSummary() {

        ExpenseSummaryMockData.mock2019_08_dataOk(expenseSummaryManager)
        ExpenseSummaryMockData.mock2019_08_week_2_dataOk(expenseSummaryManager)
        ExpenseSummaryMockData.mock2019_08_day_06_dataOk(expenseSummaryManager)

        viewModel.year.postValue(2019)
        viewModel.month.postValue(7)
        viewModel.day.postValue(6)
        viewModel.periodType.postValue(PeriodType.MONTH)

        assertThat(viewModel.month.value, `is`(7))

        viewModel.loadExpenseSummary()
        var expenseSummaryExpected = ExpenseSummaryMockData.fromJsonFile<ExpenseSummary>(
            ExpenseSummaryConfig.DATA_2019_08_OK,
            ExpenseSummary::class
        )
        checkExpenseSummaryData(expenseSummaryExpected)

        viewModel.loadNextPeriodTypeExpenseSummary()

        expenseSummaryExpected = ExpenseSummaryMockData.fromJsonFile<ExpenseSummary>(
            ExpenseSummaryConfig.DATA_2019_08_WEEK_2_OK,
            ExpenseSummary::class
        )
        checkExpenseSummaryData(expenseSummaryExpected)

        viewModel.loadNextPeriodTypeExpenseSummary()

        expenseSummaryExpected = ExpenseSummaryMockData.fromJsonFile<ExpenseSummary>(
            ExpenseSummaryConfig.DATA_2019_08_DAY_06_OK,
            ExpenseSummary::class
        )
        checkExpenseSummaryData(expenseSummaryExpected)


    }

    /**
     * Load expense summary for the current period type and the new year, month and day values
     *
     */
    @Test
    fun loadExpenseSummaryWithPeriod() {

        ExpenseSummaryMockData.mock2019_07_dataOk(expenseSummaryManager)
        ExpenseSummaryMockData.mock2019_08_dataOk(expenseSummaryManager)
        ExpenseSummaryMockData.mock2019_10_dataOk_empty(expenseSummaryManager)

        viewModel.year.postValue(2019)
        viewModel.month.postValue(4)
        viewModel.periodType.postValue(PeriodType.MONTH)

        assertThat(viewModel.month.value, `is`(4))

        viewModel.loadExpenseSummary(2019, 6, 1)
        var expenseSummaryExpected = ExpenseSummaryMockData.fromJsonFile<ExpenseSummary>(
            ExpenseSummaryConfig.DATA_2019_07_OK,
            ExpenseSummary::class
        )
        checkExpenseSummaryData(expenseSummaryExpected)

        viewModel.year.postValue(2019)
        viewModel.month.postValue(4)
        viewModel.periodType.postValue(PeriodType.MONTH)

        assertThat(viewModel.month.value, `is`(4))

        viewModel.loadExpenseSummary(2019, 7, 1)
        expenseSummaryExpected = ExpenseSummaryMockData.fromJsonFile<ExpenseSummary>(
            ExpenseSummaryConfig.DATA_2019_08_OK,
            ExpenseSummary::class
        )
        checkExpenseSummaryData(expenseSummaryExpected)

        viewModel.year.postValue(2019)
        viewModel.month.postValue(4)
        viewModel.periodType.postValue(PeriodType.MONTH)

        assertThat(viewModel.month.value, `is`(4))

        viewModel.loadExpenseSummary(2019, 9, 1)
        expenseSummaryExpected = ExpenseSummaryMockData.fromJsonFile<ExpenseSummary>(
            ExpenseSummaryConfig.DATA_OK_EMPTY,
            ExpenseSummary::class
        )
        checkExpenseSummaryData(expenseSummaryExpected)
    }

    private fun checkExpenseSummaryData(expenseSummaryExpected: ExpenseSummary?) {

        assertThat(expenseSummaryExpected, `is`(notNullValue()))

        expenseSummaryExpected?.let {
            // Total
            assertThat(viewModel.total.value, `is`(notNullValue()))
            assertThat(viewModel.total.value!!.toLong(), `is`(it.totalAmount?.toLong()))

            // Cateogries
            assertThat(viewModel.categoryOverview.value?.size, `is`(it.categoryOverview?.size))
            it.categoryOverview?.forEachIndexed { index, cat ->
                val modelCat = viewModel.categoryOverview.value?.get(index)
                assertThat(modelCat?.totalAmount, `is`(cat.totalAmount))
                assertThat(modelCat?.category?.id, `is`(cat.category.id))
            }

            // Last movements
            assertThat(
                viewModel.lastMovements.value?.contents?.size,
                `is`(it.lastMovements.contents.size)
            )
            assertThat(viewModel.lastMovements.value?.first, `is`(it.lastMovements.first))
            assertThat(viewModel.lastMovements.value?.last, `is`(it.lastMovements.last))
            assertThat(viewModel.lastMovements.value?.number, `is`(it.lastMovements.number))
            assertThat(viewModel.lastMovements.value?.size, `is`(it.lastMovements.size))
            assertThat(
                viewModel.lastMovements.value?.totalElements,
                `is`(it.lastMovements.totalElements)
            )
            assertThat(viewModel.lastMovements.value?.totalPages, `is`(it.lastMovements.totalPages))
            it.lastMovements.contents.forEachIndexed { index, mov ->
                val modelMov = viewModel.lastMovements.value?.contents?.get(index)
                assertThat(modelMov?.amount, `is`(mov.amount))
                assertThat(modelMov?.category?.id, `is`(mov.category.id))
                assertThat(modelMov?.executedAt, `is`(mov.executedAt))
                assertThat(modelMov?.id, `is`(mov.id))
            }
        }
    }
}