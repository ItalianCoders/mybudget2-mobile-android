/*
 * Project: mybudget2-mobile-android
 * File: MovementDaoTest.kt
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

import androidx.test.ext.junit.runners.AndroidJUnit4
import it.italiancoders.mybudget.db.AbstractDbTest
import it.italiancoders.mybudget.db.entity.Movement
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

/**
 * @author fattazzo
 *         <p/>
 *         date: 05/09/19
 */
@RunWith(AndroidJUnit4::class)
class MovementDaoTest : AbstractDbTest() {

    @Test
    fun search() {

        // Search by day
        val cal = Calendar.getInstance()

        val dayFilter = SimpleDateFormat(DATE_PATTERN_DAY, Locale.getDefault()).format(cal.time)

        var movements = appDb.movementDao().search(dayFilter,0,200)
        assertThat(movements.size, `is`(10))

        movements = appDb.movementDao().search(dayFilter,0,3)
        assertThat(movements.size, `is`(3))

        movements = appDb.movementDao().search(dayFilter,1,3)
        assertThat(movements.size, `is`(3))

        movements = appDb.movementDao().search(dayFilter,2,3)
        assertThat(movements.size, `is`(3))

        movements = appDb.movementDao().search(dayFilter,3,3)
        assertThat(movements.size, `is`(1))

        // Search global
        movements = appDb.movementDao().search("",0,200)
        assertThat(movements.size, `is`(100))

        movements = appDb.movementDao().search("",0,56)
        assertThat(movements.size, `is`(56))

        movements = appDb.movementDao().search("",1,51)
        assertThat(movements.size, `is`(49))

        movements = appDb.movementDao().search("",2,51)
        assertThat(movements.size, `is`(0))

    }

    @Test
    fun count() {

        val cal = Calendar.getInstance()
        val startDay = cal.get(Calendar.DAY_OF_MONTH)
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)

        val yearFilter = SimpleDateFormat(DATE_PATTERN_YEAR, Locale.getDefault()).format(cal.time)
        val monthFilter = SimpleDateFormat(DATE_PATTERN_MONTH, Locale.getDefault()).format(cal.time)
        val dayFilter = SimpleDateFormat(DATE_PATTERN_DAY, Locale.getDefault()).format(cal.time)

        val calLastDayOfMonth = Calendar.getInstance()
        calLastDayOfMonth.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE))
        val lastDayOfMonth = calLastDayOfMonth.get(Calendar.DAY_OF_MONTH)

        val calLastDayOfYear = Calendar.getInstance()
        calLastDayOfYear.set(cal.get(Calendar.MONTH), 11, 31)
        val day = calLastDayOfYear.get(Calendar.DAY_OF_YEAR)

        var count = appDb.movementDao().count(yearFilter)
        assertThat(count, `is`(min(10, (day - dayOfYear) + 1) * 10))

        count = appDb.movementDao().count(monthFilter)
        assertThat(count, `is`(min(10, (lastDayOfMonth - startDay) + 1) * 10))

        count = appDb.movementDao().count(dayFilter)
        assertThat(count, `is`(10))

        count = appDb.movementDao().count("2000")
        assertThat(count, `is`(0))
    }

    @Test
    fun insertAll() {

        var movements = appDb.movementDao().loadAll()
        assertThat(movements.size, `is`(100))

        val category = appDb.categoryDao().load(1L)!!
        val movDate =
            SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(Calendar.getInstance().time)

        val movementsToInsert = mutableListOf<Movement>()
        for (i in 1..10) {
            val movement = Movement(i.toLong(), BigDecimal(Math.random()), category, movDate,"")
            movementsToInsert.add(movement)
        }
        appDb.movementDao().insertAll(*movementsToInsert.toTypedArray())

        // Insert on MovementDao marked as onConflict = OnConflictStrategy.REPLACE.
        // By default the DB contains 100 movement with id from 1 to 100 so the previous insert override all of them
        movements = appDb.movementDao().loadAll()
        assertThat(movements.size, `is`(100))

        movementsToInsert.clear()
        for (i in 101..120) {
            val movement = Movement(i.toLong(), BigDecimal(Math.random()), category, movDate,"")
            movementsToInsert.add(movement)
        }
        appDb.movementDao().insertAll(*movementsToInsert.toTypedArray())

        movements = appDb.movementDao().loadAll()
        assertThat(movements.size, `is`(120))
    }

    @Test
    fun deleteAll() {

        var movements = appDb.movementDao().loadAll()
        assertThat(movements.size, `is`(100))

        appDb.movementDao().deleteAll()

        movements = appDb.movementDao().loadAll()
        assertThat(movements.size, `is`(0))
    }

    @Test
    fun deleteByDayFilter() {

        val cal = Calendar.getInstance()

        var movements = appDb.movementDao().loadAll()
        assertThat(movements.size, `is`(100))

        val dayFilter = SimpleDateFormat(DATE_PATTERN_DAY, Locale.getDefault()).format(cal.time)
        appDb.movementDao().delete(dayFilter)
        movements = appDb.movementDao().loadAll()
        assertThat(movements.size, `is`(90))
    }

    @Test
    fun deleteByMonthFilter() {

        val cal = Calendar.getInstance()
        val startDay = cal.get(Calendar.DAY_OF_MONTH)

        var movements = appDb.movementDao().loadAll()
        assertThat(movements.size, `is`(100))

        val monthFilter = SimpleDateFormat(DATE_PATTERN_MONTH, Locale.getDefault()).format(cal.time)
        appDb.movementDao().delete(monthFilter)

        val calLastDayOfMonth = Calendar.getInstance()
        calLastDayOfMonth.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE))
        val lastDayOfMonth = calLastDayOfMonth.get(Calendar.DAY_OF_MONTH)

        movements = appDb.movementDao().loadAll()
        assertThat(movements.size, `is`(100- min(10, (lastDayOfMonth - startDay) + 1) * 10))
    }

    @Test
    fun deleteByYearFilter() {

        val cal = Calendar.getInstance()
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)

        var movements = appDb.movementDao().loadAll()
        assertThat(movements.size, `is`(100))

        val yearFilter = SimpleDateFormat(DATE_PATTERN_YEAR, Locale.getDefault()).format(cal.time)
        appDb.movementDao().delete(yearFilter)

        val calLastDayOfYear = Calendar.getInstance()
        calLastDayOfYear.set(cal.get(Calendar.MONTH), 11, 31)
        val day = calLastDayOfYear.get(Calendar.DAY_OF_YEAR)

        movements = appDb.movementDao().loadAll()
        assertThat(movements.size, `is`(100-min(10, (day - dayOfYear) + 1) * 10))
    }

    @Test
    fun deleteById() {

        var movements = appDb.movementDao().loadAll()
        assertThat(movements.size, `is`(100))

        appDb.movementDao().delete(5L)

        movements = appDb.movementDao().loadAll()
        assertThat(movements.size, `is`(99))

        movements.forEach {
            if (it.id == 5L) {
                assertThat("Movement with id 5 still present after delete!", false)
            }
        }

        // Trying to delete a movement that does not exist
        appDb.movementDao().delete(Long.MAX_VALUE)
        movements = appDb.movementDao().loadAll()
        assertThat(movements.size, `is`(99))
    }

    @Test
    fun load() {

        var movement = appDb.movementDao().load(2L)
        assertThat(movement,`is`(notNullValue()))
        assertThat(movement!!.id,`is`(2L))

        movement = appDb.movementDao().load(Long.MAX_VALUE)
        assertThat(movement,`is`(nullValue()))
    }
}