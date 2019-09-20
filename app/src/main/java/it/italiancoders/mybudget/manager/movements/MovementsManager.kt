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
import it.italiancoders.mybudget.rest.models.Movement
import it.italiancoders.mybudget.rest.models.MovementListPage
import it.italiancoders.mybudget.utils.OpenForTesting
import it.italiancoders.mybudget.utils.connection.NetworkChecker
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 26/07/19
 */
@OpenForTesting
class MovementsManager(context: Context) : AbstractRestManager(context) {

    private val movementService =
        RetrofitBuilder.getSecureClient(context).create(MovementRestService::class.java)

    private val movementCache = MovementCache(context)
    private var expenseSummaryCache = ExpenseSummaryCache(context)

    fun load(id: Int): Movement? {
        val networkAvailable = NetworkChecker().isInternetAvailable(context)

        val movementCached = movementCache.get(id)

        return if (networkAvailable && (movementCached == null)) {
            val response = movementService.load(id)

            val movement = processResponse(response)

            movement?.let {
                movementCache.remove(id)
                movementCache.addAll(listOf(it))
            }

            return movement
        } else {
            movementCached
        }
    }

    fun search(parametri: ParametriRicerca, forceReload: Boolean = false): MovementListPage {

        val networkAvailable = NetworkChecker().isInternetAvailable(context)

        val movementsCached = movementCache.get(parametri)

        return if (networkAvailable && (movementsCached.contents.isNullOrEmpty() || forceReload)) {
            val response = movementService.query(
                parametri.year,
                parametri.month,
                parametri.day,
                parametri.categoryId?.toInt(),
                parametri.page,
                parametri.size,
                null
            )

            val result = processResponse(response)
            result?.let {
                movementCache.remove(parametri)
                movementCache.addAll(it.contents)
            }

            result ?: MovementListPage()
        } else {
            movementsCached
        }

    }

    fun delete(movement: Movement): Boolean {

        val response = movementService.delete(movement.id!!.toInt())

        val success = processVoidResponse(response)

        if (success) {
            movementCache.remove(movement.id.toInt())

            val parametri = createParametriRicerca(movement)
            expenseSummaryCache.remove(parametri)
        }

        return success
    }

    fun update(id: Int, movement: Movement): Boolean {

        val response = movementService.update(id, movement)

        val success = processVoidResponse(response)

        if (success) {
            movementCache.remove(id)
            movementCache.addAll(listOf(movement))

            val parametri = createParametriRicerca(movement)
            expenseSummaryCache.remove(parametri)
        }

        return success
    }

    fun create(movement: Movement): Boolean {

        val response = movementService.create(movement)

        val success = processVoidResponse(response)

        if (success) {
            val parametri = createParametriRicerca(movement)

            movementCache.remove(parametri)
            expenseSummaryCache.remove(parametri)
        }

        return success
    }

    private fun createParametriRicerca(movement: Movement): ParametriRicerca {
        val cal = Calendar.getInstance()
        cal.time = movement.executedAtDate!!

        return ParametriRicerca(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1)
    }
}