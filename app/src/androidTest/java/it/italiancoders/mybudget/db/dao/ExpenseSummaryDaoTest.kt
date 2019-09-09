/*
 * Project: mybudget2-mobile-android
 * File: ExpenseSummaryDaoTest.kt
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

import it.italiancoders.mybudget.db.AbstractDbTest
import it.italiancoders.mybudget.manager.movements.ParametriRicerca
import it.italiancoders.mybudget.mocks.config.ExpenseSummaryConfig
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.junit.Test

/**
 * @author fattazzo
 *         <p/>
 *         date: 05/09/19
 */
class ExpenseSummaryDaoTest : AbstractDbTest() {

    @Test
    fun loadAll() {

        val expenseSummary = appDb.expenseSummaryDao().loadAll()
        assertThat(expenseSummary, `is`(notNullValue()))
        assertThat(expenseSummary.size, `is`(5))
    }

    @Test
    fun search() {

        var expenseSummary = appDb.expenseSummaryDao().search(ParametriRicerca(2019,7))
        assertThat(expenseSummary, `is`(notNullValue()))
        assertThat(
            expenseSummary!!.toModel()!!.totalAmount,
            `is`(expensesummaryMap[ExpenseSummaryConfig.DATA_2019_07_OK]!!.totalAmount)
        )
        assertThat(
            expenseSummary.toModel()!!.categoryOverview?.size,
            `is`(expensesummaryMap[ExpenseSummaryConfig.DATA_2019_07_OK]!!.categoryOverview?.size)
        )

        expenseSummary = appDb.expenseSummaryDao().search(ParametriRicerca(2019,8))
        assertThat(expenseSummary, `is`(notNullValue()))
        assertThat(
            expenseSummary!!.toModel()!!.totalAmount,
            `is`(expensesummaryMap[ExpenseSummaryConfig.DATA_2019_08_OK]!!.totalAmount)
        )
        assertThat(
            expenseSummary.toModel()!!.categoryOverview?.size,
            `is`(expensesummaryMap[ExpenseSummaryConfig.DATA_2019_08_OK]!!.categoryOverview?.size)
        )

        expenseSummary = appDb.expenseSummaryDao().search(ParametriRicerca(2019,8,6,null,null))
        assertThat(expenseSummary, `is`(notNullValue()))
        assertThat(
            expenseSummary!!.toModel()!!.totalAmount,
            `is`(expensesummaryMap[ExpenseSummaryConfig.DATA_2019_08_DAY_06_OK]!!.totalAmount)
        )
        assertThat(
            expenseSummary.toModel()!!.categoryOverview?.size,
            `is`(expensesummaryMap[ExpenseSummaryConfig.DATA_2019_08_DAY_06_OK]!!.categoryOverview?.size)
        )

        expenseSummary = appDb.expenseSummaryDao().search(ParametriRicerca(2019,8,2))
        assertThat(expenseSummary, `is`(notNullValue()))
        assertThat(
            expenseSummary!!.toModel()!!.totalAmount,
            `is`(expensesummaryMap[ExpenseSummaryConfig.DATA_2019_08_WEEK_2_OK]!!.totalAmount)
        )
        assertThat(
            expenseSummary.toModel()!!.categoryOverview?.size,
            `is`(expensesummaryMap[ExpenseSummaryConfig.DATA_2019_08_WEEK_2_OK]!!.categoryOverview?.size)
        )

        expenseSummary = appDb.expenseSummaryDao().search(ParametriRicerca(2019,9))
        assertThat(expenseSummary, `is`(notNullValue()))
        assertThat(
            expenseSummary!!.toModel()!!.totalAmount,
            `is`(expensesummaryMap[ExpenseSummaryConfig.DATA_OK_EMPTY]!!.totalAmount)
        )
        assertThat(
            expenseSummary.toModel()!!.categoryOverview?.size,
            `is`(expensesummaryMap[ExpenseSummaryConfig.DATA_OK_EMPTY]!!.categoryOverview?.size)
        )

        expenseSummary = appDb.expenseSummaryDao().search(ParametriRicerca(2019,10))
        assertThat(expenseSummary, `is`(nullValue()))
    }

    @Test
    fun delete() {

        var expenseSummary = appDb.expenseSummaryDao().loadAll()
        assertThat(expenseSummary.size, `is`(5))

        appDb.expenseSummaryDao().delete(ParametriRicerca(2019,8))

        expenseSummary = appDb.expenseSummaryDao().loadAll()
        assertThat(expenseSummary.size, `is`(4))

        appDb.expenseSummaryDao().delete(ParametriRicerca(2019,8,6,null,null))

        expenseSummary = appDb.expenseSummaryDao().loadAll()
        assertThat(expenseSummary.size, `is`(3))

        appDb.expenseSummaryDao().delete(ParametriRicerca(2019,8,2))

        expenseSummary = appDb.expenseSummaryDao().loadAll()
        assertThat(expenseSummary.size, `is`(2))

        appDb.expenseSummaryDao().delete(ParametriRicerca(2019,10))

        expenseSummary = appDb.expenseSummaryDao().loadAll()
        assertThat(expenseSummary.size, `is`(2))
    }

    @Test
    fun insert() {

        var allExpenseSummary = appDb.expenseSummaryDao().loadAll()
        assertThat(allExpenseSummary.size, `is`(5))

        var expenseSummary = appDb.expenseSummaryDao().search(ParametriRicerca(2019,7))!!
        assertThat(
            expenseSummary.toModel()!!.totalAmount,
            `is`(expensesummaryMap[ExpenseSummaryConfig.DATA_2019_07_OK]!!.totalAmount)
        )

        val expenseToInsert =
            expensesummaryMap[ExpenseSummaryConfig.DATA_2019_07_OK]!!.copy(totalAmount = Double.MAX_VALUE)
        appDb.expenseSummaryDao().insert(expenseToInsert, ParametriRicerca(2019,7))

        // DB already contains the expense with this parameters
        // Old data will be deleted and new data added
        allExpenseSummary = appDb.expenseSummaryDao().loadAll()
        assertThat(allExpenseSummary.size, `is`(5))

        expenseSummary = appDb.expenseSummaryDao().search(ParametriRicerca(2019,7))!!
        assertThat(expenseSummary.toModel()!!.totalAmount, `is`(Double.MAX_VALUE))
    }

    @Test
    fun verifyRowLimit() {

        var rows = appDb.expenseSummaryDao().loadAll().size
        assertThat(rows, greaterThan(0))

        val expenseToInsert = expensesummaryMap[ExpenseSummaryConfig.DATA_2019_07_OK]!!.copy()
        var expectedEntries = rows

        for (i in 1..ExpenseSummaryDao.MAX_ROW) {

            if (expectedEntries < 20) {
                expectedEntries++
            }

            appDb.expenseSummaryDao().insert(expenseToInsert, ParametriRicerca(i,7))

            rows = appDb.expenseSummaryDao().loadAll().size
            assertThat(rows, `is`(expectedEntries))
        }
    }
}