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
import it.italiancoders.mybudget.db.entity.CategoryMovementOverview
import it.italiancoders.mybudget.db.pojo.ExpenseSummaryWithCategoriesOverview
import it.italiancoders.mybudget.rest.models.ExpenseSummary

/**
 * @author fattazzo
 *         <p/>
 *         date: 01/08/19
 */
@Dao
abstract class ExpenseSummaryDao {

    @Query("SELECT * FROM expense_summary WHERE year = :year and month = :month limit 1")
    abstract fun search(year: Int, month: Int): ExpenseSummaryWithCategoriesOverview?

    @Query("DELETE FROM expense_summary WHERE year = :year and month = :month")
    abstract fun delete(year: Int, month: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(expenseSummary: it.italiancoders.mybudget.db.entity.ExpenseSummary): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(expenseSummary: CategoryMovementOverview)

    @Transaction
    open fun insert(expenseSummary: ExpenseSummary, year: Int, month: Int) {

        val expenseSummaryEntity =
            it.italiancoders.mybudget.db.entity.ExpenseSummary(null, expenseSummary.totalAmount ?: 0.0, year, month)

        val expenseSummaryId = insert(expenseSummaryEntity)

        expenseSummary.categoryOverview?.forEach {

            val categoryOverviewEntity =
                CategoryMovementOverview(null, expenseSummaryId, it.category.toEntity(), it.totalAmount ?: 0.0)
            insert(categoryOverviewEntity)
        }
    }
}