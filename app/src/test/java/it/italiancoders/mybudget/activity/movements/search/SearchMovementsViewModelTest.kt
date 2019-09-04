/*
 * Project: mybudget2-mobile-android
 * File: SearchMovementsViewModelTest.kt
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

package it.italiancoders.mybudget.activity.movements.search

import it.italiancoders.mybudget.AbstractViewModelTest
import it.italiancoders.mybudget.manager.categories.CategoriesManager
import it.italiancoders.mybudget.mocks.data.CategoriesMockData
import it.italiancoders.mybudget.rest.models.Category
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mockito.Mock
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 04/09/19
 */
class SearchMovementsViewModelTest : AbstractViewModelTest<SearchMovementsViewModel>() {

    @Mock
    lateinit var categoriesManager: CategoriesManager

    override fun createViewModel(): SearchMovementsViewModel =
        SearchMovementsViewModel(categoriesManager)

    @Test
    override fun initialValues() {

        assertThat(viewModel.year.value, `is`(Calendar.getInstance().get(Calendar.YEAR)))
        assertThat(viewModel.month.value, `is`(Calendar.getInstance().get(Calendar.MONTH) + 1))
        assertThat(viewModel.day.value, `is`(nullValue()))
        assertThat(viewModel.category.value, `is`(nullValue()))

        assertThat(viewModel.categories.value, `is`(listOf()))
    }

    @Test
    fun reset() {

        viewModel.year.postValue(Int.MAX_VALUE)
        viewModel.month.postValue(Int.MAX_VALUE)
        viewModel.day.postValue(Int.MAX_VALUE)
        viewModel.category.postValue(Category())

        assertThat(viewModel.year.value, `is`(Int.MAX_VALUE))
        assertThat(viewModel.month.value, `is`(Int.MAX_VALUE))
        assertThat(viewModel.day.value, `is`(Int.MAX_VALUE))
        assertThat(viewModel.category.value, `is`(notNullValue()))

        viewModel.reset()

        assertThat(viewModel.year.value, `is`(Calendar.getInstance().get(Calendar.YEAR)))
        assertThat(viewModel.month.value, `is`(Calendar.getInstance().get(Calendar.MONTH) + 1))
        assertThat(viewModel.day.value, `is`(nullValue()))
        assertThat(viewModel.category.value, `is`(nullValue()))
    }

    @Test
    fun loadCategories() {

        CategoriesMockData.mockLoadAll_dataOk(categoriesManager)

        assertThat(viewModel.categories.value, `is`(listOf()))

        viewModel.loadCategories()

        assertThat(viewModel.categories.value!!.size, not(`is`(0)))
    }
}