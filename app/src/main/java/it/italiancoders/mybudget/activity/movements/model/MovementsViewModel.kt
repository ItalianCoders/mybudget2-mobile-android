/*
 * Project: mybudget2-mobile-android
 * File: MovementsViewModel.kt
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

package it.italiancoders.mybudget.activity.movements.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.italiancoders.mybudget.manager.movements.MovementsManager
import it.italiancoders.mybudget.manager.movements.ParametriRicerca
import it.italiancoders.mybudget.rest.models.Category
import it.italiancoders.mybudget.rest.models.Movement
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 18/07/19
 */
class MovementsViewModel : ViewModel() {

    val year = MutableLiveData<Int?>(Calendar.getInstance().get(Calendar.YEAR))
    val month = MutableLiveData<Int?>(Calendar.getInstance().get(Calendar.MONTH))
    val day: MutableLiveData<Int?> = MutableLiveData(null)

    val category: MutableLiveData<Category?> = MutableLiveData(null)

    var movements: MutableLiveData<List<Movement>> = MutableLiveData(listOf())

    fun reset() {
        year.value = Calendar.getInstance().get(Calendar.YEAR)
        month.value = Calendar.getInstance().get(Calendar.MONTH)
        day.value = null
        category.value = null
    }

    fun search(movementsManager: MovementsManager, forceReload: Boolean = false) {

        val parametri = ParametriRicerca(year.value!!, month.value!!, day.value, category.value, 0, 50, null)

        movementsManager.search(
            parametri,
            { page -> movements.postValue(page?.contents ?: listOf()) },
            { movements.postValue(listOf()) },
            forceReload
        )
    }
}