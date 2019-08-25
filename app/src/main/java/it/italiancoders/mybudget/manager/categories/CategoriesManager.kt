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

package it.italiancoders.mybudget.manager.categories

import android.content.Context
import it.italiancoders.mybudget.cache.CategoryCache
import it.italiancoders.mybudget.manager.AbstractRestManager
import it.italiancoders.mybudget.rest.api.RetrofitBuilder
import it.italiancoders.mybudget.rest.api.services.CategoryRestService
import it.italiancoders.mybudget.rest.models.Category
import it.italiancoders.mybudget.utils.NetworkChecker
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

    private val categoriesService =
        RetrofitBuilder.getSecureClient(context).create(CategoryRestService::class.java)

    private val categoryCache = CategoryCache(context)

    fun loadAll(
        onSuccessAction: (List<Category>?) -> Unit,
        onFailureAction: (Int?) -> Unit,
        forceReload: Boolean = false
    ) {
        val networkAvailable = NetworkChecker().isNetworkAvailable(context)
        CoroutineScope(Dispatchers.IO).launch {

            val cachedCategories = categoryCache.getAll()

            if (networkAvailable && (cachedCategories.isNullOrEmpty() || forceReload)) {
                val response = categoriesService.loadAll()
                categoryCache.removeAll()
                withContext(Dispatchers.Main) {
                    val onLoadAllSuccessAction: ((List<Category>?) -> Unit)? =
                        { categoriesResponse ->
                            onSuccessAction.invoke(categoriesResponse)
                            CoroutineScope(Dispatchers.IO).launch {
                                categoryCache.addAll(categoriesResponse.orEmpty())
                            }
                        }
                    processResponse(response, onLoadAllSuccessAction, onFailureAction)
                }
            } else {
                withContext(Dispatchers.Main) {
                    onSuccessAction.invoke(cachedCategories)
                }
            }
        }
    }

    fun create(
        category: Category,
        onSuccessAction: ((Category?) -> Unit)? = null,
        onFailureAction: ((Int?) -> Unit)? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = categoriesService.create(category)
            categoryCache.removeAll()
            withContext(Dispatchers.Main) {
                processResponse(response, onSuccessAction, onFailureAction)
            }
        }
    }

    fun load(id: Int, onSuccessAction: (Category?) -> Unit, onFailureAction: (Int?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = categoriesService.load(id)
            withContext(Dispatchers.Main) {
                processResponse(response, onSuccessAction, onFailureAction)
            }
        }
    }

    fun update(
        id: Int,
        category: Category,
        onSuccessAction: ((Void?) -> Unit)? = null,
        onFailureAction: ((Int?) -> Unit)? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = categoriesService.update(id, category)
            categoryCache.add(category)
            withContext(Dispatchers.Main) {
                processResponse(response, onSuccessAction, onFailureAction)
            }
        }
    }

    fun delete(
        id: Int,
        onSuccessAction: ((Void?) -> Unit)? = null,
        onFailureAction: ((Int?) -> Unit?)? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = categoriesService.delete(id)
            categoryCache.remove(id)
            withContext(Dispatchers.Main) {
                processResponse(response, onSuccessAction, onFailureAction)
            }
        }
    }
}