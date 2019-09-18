/*
 * Project: mybudget2-mobile-android
 * File: ExpenseSummaryManager.kt
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

package it.italiancoders.mybudget.manager.expensesummary

import android.content.Context
import it.italiancoders.mybudget.cache.ExpenseSummaryCache
import it.italiancoders.mybudget.manager.AbstractRestManager
import it.italiancoders.mybudget.manager.Result
import it.italiancoders.mybudget.manager.movements.ParametriRicerca
import it.italiancoders.mybudget.rest.api.RetrofitBuilder
import it.italiancoders.mybudget.rest.api.services.ExpenseSummaryRestService
import it.italiancoders.mybudget.rest.models.ExpenseSummary
import it.italiancoders.mybudget.rest.models.MovementListPage
import it.italiancoders.mybudget.utils.OpenForTesting

/**
 * @author fattazzo
 *         <p/>
 *         date: 30/08/19
 */
@OpenForTesting
class ExpenseSummaryManager(context: Context) : AbstractRestManager(context) {

    private val expenseSummaryRestService =
        RetrofitBuilder.getSecureClient(context).create(ExpenseSummaryRestService::class.java)

    private val expenseSummaryCache = ExpenseSummaryCache(context)

    fun getExpenseSummary(parametri: ParametriRicerca, showError: Boolean): Result<ExpenseSummary> {

        expenseSummaryCache.get(parametri)

        val response = expenseSummaryRestService.getExpenseSummary(
            parametri.year,
            parametri.month,
            parametri.day,
            parametri.week,
            null
        )

        return processResponseWithResult(response, showError)
    }

    fun getExpenseSummary(parametri: ParametriRicerca): ExpenseSummary =
        getExpenseSummary(parametri, true).value ?: ExpenseSummary(
            0.0,
            arrayOf(),
            MovementListPage()
        )
}