/*
 * Project: mybudget2-mobile-android
 * File: MovementCache.kt
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
import it.italiancoders.mybudget.rest.models.Movement
import it.italiancoders.mybudget.rest.models.MovementListPage

/**
 * @author fattazzo
 *         <p/>
 *         date: 26/07/19
 */
class MovementCache(val context: Context) {

    private val movementDao = AppDatabase(context).movementDao()

    fun get(id: Int): Movement? = movementDao.load(id.toLong())?.toModel()

    fun get(parametri: ParametriRicerca): MovementListPage {

        val dateFilter = createDateFiler(parametri)

        val count = movementDao.count(dateFilter)
        val movements = movementDao.search(dateFilter, parametri.page, parametri.size)
        val totalPages = count / parametri.size

        return MovementListPage().apply {
            totalElements = count
            size = movements.size
            number = parametri.page
            contents = movements.map { it.toModel() }
            last = totalPages == parametri.page
            first = parametri.page == 0
        }
    }

    fun remove(parametri: ParametriRicerca) = movementDao.delete(createDateFiler(parametri))

    fun remove(id: Int) = movementDao.delete(id.toLong())

    fun addAll(movements: List<Movement>) = movementDao.insertAll(*movements.map { it.toEntity() }.toTypedArray())

    private fun createDateFiler(parametri: ParametriRicerca): String {
        var dateFilter = "${parametri.year}-${parametri.month.toString().padStart(2, '0')}"
        if (parametri.day != null) {
            dateFilter += "-${parametri.day.toString().padStart(2, '0')}"
        }
        return dateFilter
    }
}