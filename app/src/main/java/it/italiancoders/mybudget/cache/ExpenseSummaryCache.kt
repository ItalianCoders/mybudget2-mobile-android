/*
 * Project: mybudget2-mobile-android
 * File: ExpenseSummaryCache.kt
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

package it.italiancoders.mybudget.cache

import android.content.Context
import it.italiancoders.mybudget.db.AppDatabase
import it.italiancoders.mybudget.manager.movements.ParametriRicerca
import it.italiancoders.mybudget.rest.models.ExpenseSummary

/**
 * @author fattazzo
 *         <p/>
 *         date: 01/08/19
 */
class ExpenseSummaryCache(private val context: Context) {

    private val expenseSummaryDao = AppDatabase(context).expenseSummaryDao()

    fun get(parametriRicerca: ParametriRicerca): ExpenseSummary? = expenseSummaryDao.search(parametriRicerca)?.toModel()

    fun remove(parametriRicerca: ParametriRicerca) = expenseSummaryDao.delete(parametriRicerca)

    fun removeAll() = expenseSummaryDao.deleteAll()

    fun add(
        expenseSummary: ExpenseSummary,
        parametriRicerca: ParametriRicerca
    ) {
        expenseSummaryDao.insert(expenseSummary, parametriRicerca)
    }
}