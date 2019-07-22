/*
 * Project: mybudget2-mobile-android
 * File: CategoriesManager.kt
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

package it.italiancoders.mybudget.manager

import android.content.Context
import it.italiancoders.mybudget.rest.api.RetrofitBuilder
import it.italiancoders.mybudget.rest.api.services.CategoryRestService
import it.italiancoders.mybudget.rest.models.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author fattazzo
 *         <p/>
 *         date: 22/07/19
 */
class CategoriesManager(context: Context) : AbstractRestManager(context) {

    val categoriesService = RetrofitBuilder.getSecureClient(context).create(CategoryRestService::class.java)

    fun loadAll(onSuccessAction: (List<Category>?) -> Unit, onFailureAction: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = categoriesService.loadAll()
            withContext(Dispatchers.Main) {
                processResponse(response, onSuccessAction, onFailureAction)
            }
        }
    }

    fun create(category: Category, onSuccessAction: ((Category?) -> Unit)? = null, onFailureAction: (() -> Unit)? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = categoriesService.create(category)
            withContext(Dispatchers.Main) {
                processResponse(response, onSuccessAction, onFailureAction)
            }
        }
    }

    fun load(id: Int, onSuccessAction: (Category?) -> Unit, onFailureAction: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = categoriesService.load(id)
            withContext(Dispatchers.Main) {
                processResponse(response, onSuccessAction, onFailureAction)
            }
        }
    }

    fun update(id: Int, category: Category, onSuccessAction: ((Void?) -> Unit)? = null, onFailureAction: (() -> Unit)? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = categoriesService.update(id, category)
            withContext(Dispatchers.Main) {
                processResponse(response, onSuccessAction, onFailureAction)
            }
        }
    }

    fun delete(id: Int, onSuccessAction: ((Void?) -> Unit)? = null, onFailureAction: (() -> Unit?)? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = categoriesService.delete(id)
            withContext(Dispatchers.Main) {
                processResponse(response, onSuccessAction, onFailureAction)
            }
        }
    }
}