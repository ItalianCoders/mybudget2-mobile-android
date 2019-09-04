/*
 * Project: mybudget2-mobile-android
 * File: MainViewModel.kt
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

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.italiancoders.mybudget.manager.expensesummary.ExpenseSummaryManager
import it.italiancoders.mybudget.manager.movements.ParametriRicerca
import it.italiancoders.mybudget.rest.models.CategoryMovementOverview
import it.italiancoders.mybudget.rest.models.MovementListPage
import it.italiancoders.mybudget.utils.ioJob
import java.math.BigDecimal
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 27/07/19
 */
open class MainViewModel(var expenseSummaryManager: ExpenseSummaryManager) : ViewModel() {

    var year: MutableLiveData<Int> = MutableLiveData(Calendar.getInstance().get(Calendar.YEAR))
    var month: MutableLiveData<Int> = MutableLiveData(Calendar.getInstance().get(Calendar.MONTH))
    var day: MutableLiveData<Int> =
        MutableLiveData(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))

    var periodType: MutableLiveData<PeriodType> = MutableLiveData(PeriodType.MONTH)

    val periodDescription: MediatorLiveData<String> = MediatorLiveData<String>().apply {
        addSource(year) { setValue(formatDate()) }
        addSource(month) { setValue(formatDate()) }
        addSource(day) { setValue(formatDate()) }
        addSource(periodType) { setValue(formatDate()) }
    }.also { it.observeForever { /* empty */ } }

    private fun formatDate(): String {
        val date = parseDate()
        return periodType.value?.formatDate(date) ?: ""
    }

    private fun parseDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year.value!!, month.value!!, day.value!!)
        return calendar.time
    }

    var total: MutableLiveData<BigDecimal> = MutableLiveData(BigDecimal.ZERO)
    var lastMovements: MutableLiveData<MovementListPage> = MutableLiveData(MovementListPage())
    var categoryOverview: MutableLiveData<List<CategoryMovementOverview>> =
        MutableLiveData(listOf())

    var loadingData: ObservableBoolean = ObservableBoolean(false)

    /**
     * Load expense summary for the next period type
     */
    fun loadNextPeriodTypeExpenseSummary() {
        val nextPeriodoType = periodType.value?.nextType() ?: PeriodType.MONTH
        periodType.postValue(nextPeriodoType)

        loadExpenseSummary(buildParametriRicerca(nextPeriodoType))
    }

    /**
     * Load expense summary whit the current parameters
     */
    fun loadExpenseSummary() {
        val periodoType = periodType.value ?: PeriodType.MONTH
        loadExpenseSummary(buildParametriRicerca(periodoType))
    }

    /**
     * Load expense summary for the current period type and the new year, month and day values
     *
     * @param year year
     * @param month month
     * @param day day, used PeriodType.DAY or PeriodType.WEEK for determinate the days interval
     */
    fun loadExpenseSummary(year: Int, month: Int, day: Int) {

        this.year.postValue(year)
        this.month.postValue(month)
        this.day.postValue(day)

        loadExpenseSummary(buildParametriRicerca(periodType.value!!, year, month, day))
    }

    private fun loadExpenseSummary(parametriRicerca: ParametriRicerca) {
        loadingData.set(true)

        ioJob {
            val summary = expenseSummaryManager.getExpenseSummary(parametriRicerca)

            total.postValue(summary.totalAmount?.toBigDecimal() ?: BigDecimal.ZERO)
            categoryOverview.postValue(summary.categoryOverview?.toList() ?: listOf())
            lastMovements.postValue(summary.lastMovements)

            loadingData.set(false)
        }
    }

    private fun buildParametriRicerca(
        periodType: PeriodType,
        year: Int,
        month: Int,
        day: Int
    ): ParametriRicerca {

        return when (periodType) {
            PeriodType.MONTH -> {
                ParametriRicerca(year, month + 1)
            }
            PeriodType.WEEK -> {
                val calDate = Calendar.getInstance()
                calDate.time = parseDate()
                ParametriRicerca(year, month + 1, calDate.get(Calendar.WEEK_OF_MONTH))
            }
            PeriodType.DAY -> {
                ParametriRicerca(year, month + 1, day, null, null)
            }
        }
    }

    private fun buildParametriRicerca(periodType: PeriodType): ParametriRicerca {
        return buildParametriRicerca(periodType, year.value!!, month.value!!, day.value!!)
    }
}