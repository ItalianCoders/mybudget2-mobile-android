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

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.italiancoders.mybudget.manager.movements.MovementsManager
import it.italiancoders.mybudget.manager.movements.ParametriRicerca
import it.italiancoders.mybudget.rest.models.CategoryMovementOverview
import it.italiancoders.mybudget.rest.models.MovementListPage
import java.math.BigDecimal
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 27/07/19
 */
class MainViewModel : ViewModel() {

    var year: MutableLiveData<Int> = MutableLiveData(Calendar.getInstance().get(Calendar.YEAR))
    var month: MutableLiveData<Int> = MutableLiveData(Calendar.getInstance().get(Calendar.MONTH))
    var day: MutableLiveData<Int> =
        MutableLiveData(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))

    var periodType: MutableLiveData<PeriodType> = MutableLiveData(PeriodType.MONTH)

    var total: MutableLiveData<BigDecimal> = MutableLiveData(BigDecimal.ZERO)

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

    var lastMovements: MutableLiveData<MovementListPage> = MutableLiveData(MovementListPage())
    var categoryOverview: MutableLiveData<List<CategoryMovementOverview>> =
        MutableLiveData(listOf())

    var loadingData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(categoryOverview) { setValue(false) }
    }

    fun loadExpenseSummary(movementsManager: MovementsManager, forceReload: Boolean = false) {
        loadingData.value = true

        movementsManager.getExpenseSummary(
            buildParametriRicerca(),
            { summary ->
                total.postValue(summary?.totalAmount?.toBigDecimal() ?: BigDecimal.ZERO)
                categoryOverview.postValue(summary?.categoryOverview?.toList() ?: listOf())
                lastMovements.postValue(summary?.lastMovements ?: MovementListPage())
            },
            {
                total.postValue(BigDecimal.ZERO)
                categoryOverview.postValue(listOf())
                lastMovements.postValue(MovementListPage())
            },
            forceReload
        )
    }

    private fun buildParametriRicerca(): ParametriRicerca {

        return when (periodType.value ?: PeriodType.MONTH) {
            PeriodType.MONTH -> {
                ParametriRicerca(year.value!!, month.value!!+1, null, null, null)
            }
            PeriodType.WEEK -> {
                val calDate = Calendar.getInstance()
                calDate.time = parseDate()
                ParametriRicerca(
                    year.value!!,
                    month.value!!+1,
                    null,
                    calDate.get(Calendar.WEEK_OF_MONTH),
                    null
                )
            }
            PeriodType.DAY -> {
                ParametriRicerca(year.value!!, month.value!!+1, day.value!!, null, null)
            }
        }
    }
}