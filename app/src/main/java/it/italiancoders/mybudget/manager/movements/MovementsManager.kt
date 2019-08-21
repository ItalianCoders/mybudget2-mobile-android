/*
 * Project: mybudget2-mobile-android
 * File: MovementsManager.kt
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

package it.italiancoders.mybudget.manager.movements

import android.content.Context
import it.italiancoders.mybudget.cache.ExpenseSummaryCache
import it.italiancoders.mybudget.cache.MovementCache
import it.italiancoders.mybudget.manager.AbstractRestManager
import it.italiancoders.mybudget.rest.api.RetrofitBuilder
import it.italiancoders.mybudget.rest.api.services.MovementRestService
import it.italiancoders.mybudget.rest.models.ExpenseSummary
import it.italiancoders.mybudget.rest.models.Movement
import it.italiancoders.mybudget.rest.models.MovementListPage
import it.italiancoders.mybudget.utils.NetworkChecker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author fattazzo
 *         <p/>
 *         date: 26/07/19
 */
class MovementsManager(context: Context) : AbstractRestManager(context) {

    private val movementService = RetrofitBuilder.getSecureClient(context).create(MovementRestService::class.java)

    private val movementCache = MovementCache(context)
    private val expenseSummaryCache = ExpenseSummaryCache(context)

    fun load(id: Int, onSuccessAction: (Movement?) -> Unit, onFailureAction: () -> Unit) {
        val networkAvailable = NetworkChecker().isNetworkAvailable(context)
        CoroutineScope(Dispatchers.IO).launch {

            val movementCached = movementCache.get(id)

            if (networkAvailable && (movementCached == null)) {
                val response = movementService.load(id)

                movementCache.remove(id)
                withContext(Dispatchers.Main) {
                    val onLoadAllSuccessAction: ((Movement?) -> Unit)? = { loadResponse ->
                        onSuccessAction.invoke(loadResponse)
                        loadResponse?.let {
                            CoroutineScope(Dispatchers.IO).launch {
                                movementCache.addAll(listOf(it))
                            }
                        }
                    }
                    processResponse(response, onLoadAllSuccessAction, onFailureAction)
                }
            } else {
                withContext(Dispatchers.Main) {
                    onSuccessAction.invoke(movementCached)
                }
            }
        }
    }

    fun search(
        parametri: ParametriRicerca,
        onSuccessAction: (MovementListPage?) -> Unit,
        onFailureAction: () -> Unit,
        forceReload: Boolean = false
    ) {
        val networkAvailable = NetworkChecker().isNetworkAvailable(context)
        CoroutineScope(Dispatchers.IO).launch {

            val movementsCached = movementCache.get(parametri)

            if (networkAvailable && (movementsCached.contents.isNullOrEmpty() || forceReload)) {
                val response = movementService.query(
                    parametri.year,
                    parametri.month,
                    parametri.day,
                    parametri.categoryId?.toInt(),
                    parametri.page,
                    parametri.size,
                    null
                )

                movementCache.remove(parametri)
                withContext(Dispatchers.Main) {
                    val onLoadAllSuccessAction: ((MovementListPage?) -> Unit)? = { searchResponse ->
                        onSuccessAction.invoke(searchResponse)
                        CoroutineScope(Dispatchers.IO).launch {
                            movementCache.addAll(searchResponse?.contents.orEmpty())
                        }
                    }
                    processResponse(response, onLoadAllSuccessAction, onFailureAction)
                }
            } else {
                withContext(Dispatchers.Main) {
                    onSuccessAction.invoke(movementsCached)
                }
            }
        }
    }

    fun getExpenseSummary(
        year: Int,
        month: Int,
        day: Int?,
        onSuccessAction: (ExpenseSummary?) -> Unit,
        onFailureAction: () -> Unit,
        forceReload: Boolean = false
    ) {
        val networkAvailable = NetworkChecker().isNetworkAvailable(context)
        CoroutineScope(Dispatchers.IO).launch {

            val expenseSummary = expenseSummaryCache.get(year, month)

            if (networkAvailable && (expenseSummary == null || forceReload)) {
                val response = movementService.getExpenseSummary(year, month, day)

                expenseSummaryCache.remove(year, month)
                val onLoadAllSuccessAction: ((ExpenseSummary?) -> Unit)? = { summary ->
                    onSuccessAction.invoke(summary)
                    summary?.let {
                        CoroutineScope(Dispatchers.IO).launch {
                            expenseSummaryCache.add(it, year, month)
                        }
                    }
                }
                processResponse(response, onLoadAllSuccessAction, onFailureAction)
            } else {
                withContext(Dispatchers.Main) {
                    onSuccessAction.invoke(expenseSummary)
                }
            }
        }
    }

    fun delete(id: Int, onSuccessAction: ((Void?) -> Unit)? = null, onFailureAction: (() -> Unit?)? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = movementService.delete(id)

            val onDeleteSuccessAction: ((Void?) -> Unit)? = { summary ->
                onSuccessAction?.invoke(summary)
                CoroutineScope(Dispatchers.IO).launch {
                    movementCache.remove(id)
                }
            }

            withContext(Dispatchers.Main) {
                processResponse(response, onDeleteSuccessAction, onFailureAction)
            }
        }
    }

    fun update(
        id: Int,
        movement: Movement,
        onSuccessAction: ((Void?) -> Unit)? = null,
        onFailureAction: (() -> Unit)? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = movementService.update(id, movement)

            val onUpdateSuccessAction: ((Void?) -> Unit)? = { summary ->
                onSuccessAction?.invoke(summary)
                CoroutineScope(Dispatchers.IO).launch {
                    movementCache.addAll(listOf(movement))
                }
            }

            withContext(Dispatchers.Main) {
                processResponse(response, onUpdateSuccessAction, onFailureAction)
            }
        }
    }

    fun create(
        movement: Movement,
        onSuccessAction: ((Void?) -> Unit)? = null,
        onFailureAction: (() -> Unit)? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = movementService.create(movement)

            withContext(Dispatchers.Main) {
                processResponse(response, onSuccessAction, onFailureAction)
            }
        }
    }
}