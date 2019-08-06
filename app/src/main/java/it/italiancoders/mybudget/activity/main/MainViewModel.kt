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
import it.italiancoders.mybudget.rest.models.CategoryMovementOverview
import it.italiancoders.mybudget.rest.models.Movement
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

    var total: MutableLiveData<BigDecimal> = MutableLiveData(BigDecimal.ZERO)

    val periodDate = MediatorLiveData<Date>().apply {
        addSource(year) { setValue(parseDate()) }
        addSource(month) { setValue(parseDate()) }
    }.also { it.observeForever { /* empty */ } }

    private fun parseDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year.value!!, month.value!!, 1)
        return calendar.time
    }

    var lastMovements: MutableLiveData<List<Movement>> = MutableLiveData(listOf())
    var categoryOverview: MutableLiveData<List<CategoryMovementOverview>> = MutableLiveData(listOf())

    var loadingData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(categoryOverview) { setValue(false) }
    }

    fun loadExpenseSummary(movementsManager: MovementsManager,forceReload: Boolean = false) {
        loadingData.value = true

        movementsManager.getExpenseSummary(
            year.value!!, month.value!!, null,
            { summary ->
                total.postValue(summary?.totalAmount?.toBigDecimal() ?: BigDecimal.ZERO)
                categoryOverview.postValue(summary?.categoryOverview?.toList() ?: listOf())
                lastMovements.postValue(summary?.lastMovements?.toList() ?: listOf())
            },
            {
                total.postValue(BigDecimal.ZERO)
                categoryOverview.postValue(listOf())
                lastMovements.postValue(listOf())
            },
            forceReload
        )
    }
}