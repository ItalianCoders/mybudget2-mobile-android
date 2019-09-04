/*
 * Project: mybudget2-mobile-android
 * File: SearchMovementsViewModel.kt
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

package it.italiancoders.mybudget.activity.movements.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.italiancoders.mybudget.manager.categories.CategoriesManager
import it.italiancoders.mybudget.rest.models.Category
import it.italiancoders.mybudget.utils.ioJob
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 18/07/19
 */
class SearchMovementsViewModel(private val categoriesManager: CategoriesManager) : ViewModel() {

    val year = MutableLiveData<Int?>(Calendar.getInstance().get(Calendar.YEAR))
    val month = MutableLiveData<Int?>(Calendar.getInstance().get(Calendar.MONTH)+1)
    val day: MutableLiveData<Int?> = MutableLiveData(null)

    val category: MutableLiveData<Category?> = MutableLiveData(null)

    val categories = MutableLiveData<List<Category>>(listOf())

    fun reset() {
        year.value = Calendar.getInstance().get(Calendar.YEAR)
        month.value = Calendar.getInstance().get(Calendar.MONTH)+1
        day.value = null
        category.value = null
    }

    fun loadCategories() {

        ioJob {
            val categoriesLoaded = categoriesManager.loadAll()
            categories.postValue(categoriesLoaded)
        }
    }
}