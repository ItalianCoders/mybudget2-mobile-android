/*
 * Project: mybudget2-mobile-android
 * File: ListMovementsViewModel.kt
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

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.italiancoders.mybudget.AppConstants
import it.italiancoders.mybudget.manager.movements.MovementsManager
import it.italiancoders.mybudget.manager.movements.ParametriRicerca
import it.italiancoders.mybudget.rest.models.MovementListPage
import java.util.*

class ListMovementsViewModel : ViewModel() {

    val year = MutableLiveData<Int?>(Calendar.getInstance().get(Calendar.YEAR))
    val month = MutableLiveData<Int?>(Calendar.getInstance().get(Calendar.MONTH))
    val day: MutableLiveData<Int?> = MutableLiveData(null)

    val categoryId: MutableLiveData<Long?> = MutableLiveData(null)

    val page: MutableLiveData<MovementListPage?> = MutableLiveData(null)

    val loadingData: ObservableBoolean = ObservableBoolean(false)

    fun search(
        movementsManager: MovementsManager?,
        forceReload: Boolean = false
    ) {
        loadingData.set(true)
        movementsManager?.search(
            buildParameters(true),
            { pageResult ->
                page.postValue(pageResult ?: MovementListPage())
                loadingData.set(false)
            },
            {
                page.postValue(MovementListPage())
                loadingData.set(false)
            },
            forceReload
        )
    }

    fun loadNextPage(
        movementsManager: MovementsManager?,
        forceReload: Boolean = false
    ) {
        if (isLastPage()) return

        loadingData.set(true)

        movementsManager?.search(
            buildParameters(false),
            { pageResult ->
                page.postValue(pageResult ?: MovementListPage())
                loadingData.set(false)
            },
            {
                page.postValue(MovementListPage())
                loadingData.set(false)
            },
            forceReload
        )
    }

    /**
     * Return true if the parameters are ok for parform a search
     */
    fun isValidParams(): Boolean = year.value != null && month.value != null

    fun isLastPage(): Boolean = page.value?.last ?: true

    private fun buildParameters(fromFirstPage: Boolean): ParametriRicerca {

        // Build default parameters
        var parametri = ParametriRicerca(year.value!!, month.value!!, day.value, null, categoryId.value)

        // If a page exist build the parameters from it
        page.value?.let {
            parametri = ParametriRicerca(
                year.value!!,
                month.value!!,
                day.value,
                null,
                categoryId.value,
                if (fromFirstPage) 0 else (it.number ?: 0).plus(1),
                it.size ?: AppConstants.DEFAULT_PAGE_SIZE,
                null
            )
        }

        return parametri
    }
}
