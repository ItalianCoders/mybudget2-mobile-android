/*
 * Project: mybudget2-mobile-android
 * File: CategoriesViewModelTest.kt
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

package it.italiancoders.mybudget.activity.categories

import it.italiancoders.mybudget.AbstractViewModelTest
import it.italiancoders.mybudget.manager.categories.CategoriesManager
import it.italiancoders.mybudget.mocks.config.CategoriesConfig
import it.italiancoders.mybudget.mocks.data.CategoriesMockData
import it.italiancoders.mybudget.rest.models.Category
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mockito.Mock

/**
 * @author fattazzo
 *         <p/>
 *         date: 02/09/19
 */
class CategoriesViewModelTest : AbstractViewModelTest<CategoriesViewModel>() {

    @Mock
    lateinit var categoriesManager: CategoriesManager

    override fun createViewModel(): CategoriesViewModel = CategoriesViewModel(categoriesManager)

    @Test
    override fun initialValues() {
        assertThat(viewModel.categories.value, `is`(listOf()))
        assertThat(viewModel.loadingData.get(), `is`(false))
    }

    @Test
    fun loadAllCategories() {

        CategoriesMockData.mockLoadAll_dataOk(categoriesManager)

        viewModel.loadAll()

        assertThat(viewModel.categories.value?.size, `is`(7))
        val expectedCategories = CategoriesMockData.listFromJsonFile<Category>(
            CategoriesConfig.DATA_ALL_OK,
            Category::class
        )
        expectedCategories.forEachIndexed { index, category ->
            assertThat(viewModel.categories.value?.get(index)?.id, `is`(category.id))
            assertThat(viewModel.categories.value?.get(index)?.name, `is`(category.name))
            assertThat(
                viewModel.categories.value?.get(index)?.description,
                `is`(category.description)
            )
        }
    }

    @Test
    fun deleteCategory() {
        CategoriesMockData.mockLoadAllAndNoId2(categoriesManager)
        viewModel.loadAll()
        assertThat(viewModel.categories.value?.size, `is`(7))

        viewModel.delete(2)
        assertThat(viewModel.categories.value?.size, `is`(6))
        assertThat(viewModel.categories.value?.find { it.id!!.toInt() == 2 }, `is`(nullValue()))
    }

    @Test
    fun createCategory() {
        CategoriesMockData.mockLoadAllAndNewId10(categoriesManager)
        viewModel.loadAll()
        assertThat(viewModel.categories.value?.size, `is`(7))

        viewModel.create(Category(10, "New category", "New category", false))
        assertThat(viewModel.categories.value?.size, `is`(8))
        assertThat(viewModel.categories.value?.find { it.id!!.toInt() == 10 }, `is`(notNullValue()))
    }

    @Test
    fun updateCategory() {
        CategoriesMockData.mockLoadAllAndId2Updated(categoriesManager)
        viewModel.loadAll()
        assertThat(viewModel.categories.value?.size, `is`(7))

        val cat2 = viewModel.categories.value?.find { it.id!!.toInt() == 2 }
        cat2?.description = "${cat2?.description} updated"
        cat2?.name = "${cat2?.name} updated"

        viewModel.update(2,cat2!!)
        assertThat(viewModel.categories.value?.size, `is`(7))
        assertThat(viewModel.categories.value?.find { it.name == cat2.name && it.id!!.toInt() == 2 }, `is`(notNullValue()))
    }
}