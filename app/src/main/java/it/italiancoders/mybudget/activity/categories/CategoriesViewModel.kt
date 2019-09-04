/*
 * Project: mybudget2-mobile-android
 * File: CategoriesViewModel.kt
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

package it.italiancoders.mybudget.activity.categories

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.italiancoders.mybudget.manager.categories.CategoriesManager
import it.italiancoders.mybudget.rest.models.Category
import it.italiancoders.mybudget.utils.ioJob

/**
 * @author fattazzo
 *         <p/>
 *         date: 18/07/19
 */
class CategoriesViewModel(private val categoriesManager: CategoriesManager) : ViewModel() {

    val categories = MutableLiveData<List<Category>>().apply { postValue(listOf()) }

    val loadingData: ObservableBoolean = ObservableBoolean(false)

    fun loadAll(forceRefresh: Boolean = false) {
        loadingData.set(true)

        ioJob {
            try {
                val categoriesLoaded = categoriesManager.loadAll(forceRefresh)

                categories.postValue(categoriesLoaded)
            } finally {
                loadingData.set(false)
            }
        }
    }

    fun delete(categoryId: Int) {

        ioJob {
            categoriesManager.delete(categoryId)
            loadAll(true)
        }
    }

    fun create(category: Category) {

        ioJob {
            categoriesManager.create(category)
            loadAll(true)
        }
    }

    fun update(id: Int, category: Category) {

        ioJob {
            categoriesManager.update(id, category)
            loadAll(true)
        }
    }
}