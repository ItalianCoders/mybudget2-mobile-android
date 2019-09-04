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
import it.italiancoders.mybudget.utils.OpenForTesting

/**
 * @author fattazzo
 *         <p/>
 *         date: 22/07/19
 */
@OpenForTesting
class CategoriesManager(context: Context) : AbstractRestManager(context) {

    private val categoriesService =
        RetrofitBuilder.getSecureClient(context).create(CategoryRestService::class.java)

    private val categoryCache = CategoryCache(context)

    fun loadAll(forceReload: Boolean = false): List<Category> {

        val networkAvailable = NetworkChecker().isNetworkAvailable(context)

        val cachedCategories = categoryCache.getAll()

        return if (networkAvailable && (cachedCategories.isNullOrEmpty() || forceReload)) {

            val response = categoriesService.loadAll()

            val result = processResponse(response)
            result?.let {
                categoryCache.removeAll()
                categoryCache.addAll(result.orEmpty())
            }

            result ?: listOf()
        } else {
            cachedCategories
        }
    }

    fun create(category: Category): Category? {

        val response = categoriesService.create(category)

        val result = processResponse(response)
        result?.let { categoryCache.removeAll() }

        return result
    }

    fun load(id: Int): Category? {
        val response = categoriesService.load(id)
        return processResponse(response)
    }

    fun update(id: Int, category: Category) {
        val response = categoriesService.update(id, category)
        processResponse(response)
        categoryCache.add(category)
    }

    fun delete(id: Int) {
        val response = categoriesService.delete(id)
        processResponse(response)
        categoryCache.remove(id)
    }
}