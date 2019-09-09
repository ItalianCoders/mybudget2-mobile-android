/*
 * Project: mybudget2-mobile-android
 * File: ExpenseSummaryDao.kt
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

package it.italiancoders.mybudget.db.dao

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.beust.klaxon.Klaxon
import it.italiancoders.mybudget.db.entity.ExpenseSummary
import it.italiancoders.mybudget.manager.movements.ParametriRicerca
import it.italiancoders.mybudget.rest.models.MovementListPage


/**
 * @author fattazzo
 *         <p/>
 *         date: 01/08/19
 */
@Dao
abstract class ExpenseSummaryDao {

    companion object {

        const val MAX_ROW = 20
    }

    fun search(parametriRicerca: ParametriRicerca): ExpenseSummary? {

        val params: MutableList<Any> = mutableListOf()

        val query = StringBuilder()
        query.append("SELECT * FROM expense_summary WHERE year = ? and month = ? ")
        params.add(parametriRicerca.year)
        params.add(parametriRicerca.month)

        if (parametriRicerca.day != null) {
            query.append("and day = ? ")
            params.add(parametriRicerca.day)
        } else {
            query.append("and day is null ")
        }

        if (parametriRicerca.week != null) {
            query.append("and week = ? ")
            params.add(parametriRicerca.week)
        } else {
            query.append("and week is null ")
        }

        if (parametriRicerca.categoryId != null) {
            query.append("and categoryId = ? ")
            params.add(parametriRicerca.categoryId)
        } else {
            query.append("and categoryId is null ")
        }

        return performQuery(SimpleSQLiteQuery(query.toString(), params.toTypedArray()))
    }

    @RawQuery
    protected abstract fun performQuery(query: SupportSQLiteQuery): ExpenseSummary?

    fun delete(parametriRicerca: ParametriRicerca) {
        val params: MutableList<Any> = mutableListOf()

        val query = StringBuilder()
        query.append("DELETE FROM expense_summary WHERE year = ? and month = ? ")
        params.add(parametriRicerca.year)
        params.add(parametriRicerca.month)

        if (parametriRicerca.day != null) {
            query.append("and day = ? ")
            params.add(parametriRicerca.day)
        } else {
            query.append("and day is null ")
        }

        if (parametriRicerca.week != null) {
            query.append("and week = ? ")
            params.add(parametriRicerca.week)
        } else {
            query.append("and week is null ")
        }

        if (parametriRicerca.categoryId != null) {
            query.append("and categoryId = ? ")
            params.add(parametriRicerca.categoryId)
        } else {
            query.append("and categoryId is null ")
        }

        performQuery(SimpleSQLiteQuery(query.toString(), params.toTypedArray()))
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insert(expenseSummary: ExpenseSummary)

    @Query("DELETE FROM expense_summary where id NOT IN (SELECT id from expense_summary ORDER BY id DESC LIMIT 20)")
    protected abstract fun truncateToRowsLimit()

    @Transaction
    open fun insert(
        expenseSummary: it.italiancoders.mybudget.rest.models.ExpenseSummary,
        parametriRicerca: ParametriRicerca
    ) {

        delete(parametriRicerca)

        expenseSummary.lastMovements = MovementListPage()
        val expenseSummaryEntity = ExpenseSummary(
            null,
            parametriRicerca.year,
            parametriRicerca.month,
            parametriRicerca.day,
            parametriRicerca.week,
            parametriRicerca.categoryId?.toInt(),
            Klaxon().toJsonString(expenseSummary)
        )

        insert(expenseSummaryEntity)

        truncateToRowsLimit()
    }

    @Query("SELECT * from expense_summary")
    abstract fun loadAll(): List<ExpenseSummary>
}