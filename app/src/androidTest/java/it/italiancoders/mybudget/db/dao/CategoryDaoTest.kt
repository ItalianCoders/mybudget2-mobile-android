/*
 * Project: mybudget2-mobile-android
 * File: CategoryDaoTest.kt
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
import it.italiancoders.mybudget.db.entity.Category
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author fattazzo
 *         <p/>
 *         date: 05/09/19
 */
@RunWith(AndroidJUnit4::class)
class CategoryDaoTest : AbstractDbTest() {

    @Test
    fun loadAll() {

        val categories = appDb.categoryDao().loadAll()
        assertThat(categories.size, `is`(10))
    }

    @Test
    fun deleteAll() {

        var categories = appDb.categoryDao().loadAll()
        assertThat(categories.size, `is`(10))

        appDb.categoryDao().deleteAll()

        categories = appDb.categoryDao().loadAll()
        assertThat(categories.size, `is`(0))
    }

    @Test
    fun delete() {

        var categories = appDb.categoryDao().loadAll()
        assertThat(categories.size, `is`(10))

        appDb.categoryDao().delete(5L)

        categories = appDb.categoryDao().loadAll()
        assertThat(categories.size, `is`(9))

        categories.forEach {
            if (it.id == 5L) {
                assertThat("Category with id 5 still present after delete!", false)
            }
        }

        // Trying to delete a category that does not exist
        appDb.categoryDao().delete(Long.MAX_VALUE)
        categories = appDb.categoryDao().loadAll()
        assertThat(categories.size, `is`(9))
    }

    @Test
    fun insertAll() {

        var categories = appDb.categoryDao().loadAll()
        assertThat(categories.size, `is`(10))

        val categoriesToInsert = mutableListOf<Category>()
        for (i in 1..10) {
            val category = Category(i.toLong(), "name $i", "desc $i", false)
            categoriesToInsert.add(category)
        }
        appDb.categoryDao().insertAll(*categoriesToInsert.toTypedArray())

        // Insert on CategoryDao marked as onConflict = OnConflictStrategy.REPLACE.
        // By default the DB contains 10 categories with id from 1 to 10 so the previous insert override all of them
        categories = appDb.categoryDao().loadAll()
        assertThat(categories.size, `is`(10))

        categoriesToInsert.clear()
        for (i in 11..20) {
            val category = Category(i.toLong(), "name $i", "desc $i", false)
            categoriesToInsert.add(category)
        }
        appDb.categoryDao().insertAll(*categoriesToInsert.toTypedArray())

        categories = appDb.categoryDao().loadAll()
        assertThat(categories.size, `is`(20))
    }

    @Test
    fun insert() {

        var categories = appDb.categoryDao().loadAll()
        assertThat(categories.size, `is`(10))

        var category = Category(1L, "name 1", "desc 1", false)
        appDb.categoryDao().insert(category)

        // Insert on CategoryDao marked as onConflict = OnConflictStrategy.REPLACE.
        // By default the DB contains 10 categories with id from 1 to 10 so the previous insert override all of them
        categories = appDb.categoryDao().loadAll()
        assertThat(categories.size, `is`(10))

        category = Category(999L, "name 999", "desc 999", false)
        appDb.categoryDao().insert(category)

        categories = appDb.categoryDao().loadAll()
        assertThat(categories.size, `is`(11))
    }
}