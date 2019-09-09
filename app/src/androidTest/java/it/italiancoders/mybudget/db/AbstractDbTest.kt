/*
 * Project: mybudget2-mobile-android
 * File: AbstractDbTest.kt
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

package it.italiancoders.mybudget.db

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import it.italiancoders.mybudget.db.entity.Category
import it.italiancoders.mybudget.db.entity.Movement
import it.italiancoders.mybudget.manager.movements.ParametriRicerca
import it.italiancoders.mybudget.mocks.config.ExpenseSummaryConfig
import it.italiancoders.mybudget.mocks.data.ExpenseSummaryMockData
import it.italiancoders.mybudget.rest.models.ExpenseSummary
import org.junit.After
import org.junit.Before
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 05/09/19
 */
abstract class AbstractDbTest {

    protected lateinit var appDb: AppDatabase

    companion object {

        const val DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val DATE_PATTERN_YEAR = "yyyy"
        const val DATE_PATTERN_MONTH = "yyyy-MM"
        const val DATE_PATTERN_DAY = "yyyy-MM-dd"
    }

    protected val expensesummaryMap: MutableMap<String, ExpenseSummary> = mutableMapOf()

    @Before
    fun setUp() {
        appDb = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        ).build()
        initDefaultData()
    }

    @After
    fun tearDown() {
        appDb.close()
    }

    private fun initDefaultData() {

        // Default category
        for (i in 1..10) {
            val category = Category(i.toLong(), "name $i", "desc $i", false)
            appDb.categoryDao().insert(category)
        }

        // Default movements
        var movId = 1.toLong()
        val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
        val calendar = Calendar.getInstance()
        for (i in 1..10) {
            val category = appDb.categoryDao().load(i.toLong())!!
            val movDate = dateFormat.format(calendar.time)
            val movAmount = BigDecimal(Math.random() * 100).setScale(2, RoundingMode.HALF_UP)
            for (j in 1..10) {
                val movement = Movement(movId++, movAmount, category, movDate)
                appDb.movementDao().insertAll(movement)
            }
            calendar.add(Calendar.DATE, 1)
        }

        // Default expense summary (cached for performance)
        if (expensesummaryMap.isEmpty()) {
            expensesummaryMap[ExpenseSummaryConfig.DATA_2019_07_OK] =
                ExpenseSummaryMockData.fromJsonFile<ExpenseSummary>(
                    ExpenseSummaryConfig.DATA_2019_07_OK,
                    ExpenseSummary::class
                )!!

            expensesummaryMap[ExpenseSummaryConfig.DATA_2019_08_OK] =
                ExpenseSummaryMockData.fromJsonFile<ExpenseSummary>(
                    ExpenseSummaryConfig.DATA_2019_08_OK,
                    ExpenseSummary::class
                )!!

            expensesummaryMap[ExpenseSummaryConfig.DATA_2019_08_DAY_06_OK] =
                ExpenseSummaryMockData.fromJsonFile<ExpenseSummary>(
                    ExpenseSummaryConfig.DATA_2019_08_DAY_06_OK,
                    ExpenseSummary::class
                )!!

            expensesummaryMap[ExpenseSummaryConfig.DATA_2019_08_WEEK_2_OK] =
                ExpenseSummaryMockData.fromJsonFile<ExpenseSummary>(
                    ExpenseSummaryConfig.DATA_2019_08_WEEK_2_OK,
                    ExpenseSummary::class
                )!!

            expensesummaryMap[ExpenseSummaryConfig.DATA_OK_EMPTY] =
                ExpenseSummaryMockData.fromJsonFile<ExpenseSummary>(
                    ExpenseSummaryConfig.DATA_OK_EMPTY,
                    ExpenseSummary::class
                )!!
        }

        appDb.expenseSummaryDao().insert(
            expensesummaryMap[ExpenseSummaryConfig.DATA_2019_07_OK]!!,
            ParametriRicerca(2019, 7)
        )
        appDb.expenseSummaryDao().insert(
            expensesummaryMap[ExpenseSummaryConfig.DATA_2019_08_OK]!!,
            ParametriRicerca(2019, 8)
        )
        appDb.expenseSummaryDao().insert(
            expensesummaryMap[ExpenseSummaryConfig.DATA_2019_08_DAY_06_OK]!!,
            ParametriRicerca(2019, 8, 6, null, null)
        )
        appDb.expenseSummaryDao().insert(
            expensesummaryMap[ExpenseSummaryConfig.DATA_2019_08_WEEK_2_OK]!!,
            ParametriRicerca(2019, 8, 2)
        )
        appDb.expenseSummaryDao().insert(
            expensesummaryMap[ExpenseSummaryConfig.DATA_OK_EMPTY]!!,
            ParametriRicerca(2019, 9)
        )
    }
}