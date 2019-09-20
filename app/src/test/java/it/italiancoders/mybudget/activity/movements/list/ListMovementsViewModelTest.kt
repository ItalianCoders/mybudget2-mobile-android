/*
 * Project: mybudget2-mobile-android
 * File: ListMovementsViewModelTest.kt
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

package it.italiancoders.mybudget.activity.movements.list

import it.italiancoders.mybudget.AbstractViewModelTest
import it.italiancoders.mybudget.app.AppConstants
import it.italiancoders.mybudget.manager.movements.MovementsManager
import it.italiancoders.mybudget.mocks.config.MovementsConfig
import it.italiancoders.mybudget.mocks.data.MovementsMockData
import it.italiancoders.mybudget.rest.models.MovementListPage
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mockito.Mock
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 29/08/19
 */
class ListMovementsViewModelTest : AbstractViewModelTest<ListMovementsViewModel>() {

    @Mock
    lateinit var movementsManager: MovementsManager

    override fun createViewModel(): ListMovementsViewModel =
        ListMovementsViewModel(movementsManager)

    @Test
    override fun initialValues() {
        assertThat(viewModel.year.value, `is`(Calendar.getInstance().get(Calendar.YEAR)))
        assertThat(viewModel.month.value, `is`(Calendar.getInstance().get(Calendar.MONTH)+1))
        assertThat(viewModel.day.value, `is`(nullValue()))

        assertThat(viewModel.categoryId.value, `is`(nullValue()))

        assertThat(viewModel.page.value, `is`(nullValue()))

        assertThat(viewModel.loadingData.get(), `is`(false))

        assertThat(viewModel.isValidParams(), `is`(true))

        assertThat(viewModel.isLastPage(), `is`(true))
    }

    @Test
    fun search() {
        MovementsMockData.mock2019_08_page0(movementsManager)

        viewModel.year.postValue(2019)
        viewModel.month.postValue(8)
        viewModel.day.postValue(null)

        viewModel.search()
        assertThat(viewModel.page.value, `is`(notNullValue()))
        assertThat(viewModel.page.value?.first, `is`(true))
        assertThat(viewModel.page.value?.last, `is`(false))
        assertThat(viewModel.isLastPage(), `is`(false))
        assertThat(viewModel.page.value?.number, `is`(0))

        val expectedPage0 = MovementsMockData.fromJsonFile<MovementListPage>(MovementsConfig.DATA_2019_08_PAGE_0_OK,MovementListPage::class)
        checkPageDataWithViewModel(expectedPage0)

        // Search with the same parameters
        viewModel.search()
        assertThat(viewModel.page.value, `is`(notNullValue()))
        assertThat(viewModel.page.value?.first, `is`(true))
        assertThat(viewModel.page.value?.last, `is`(false))
        assertThat(viewModel.isLastPage(), `is`(false))
        assertThat(viewModel.page.value?.number, `is`(0))
        checkPageDataWithViewModel(expectedPage0)
    }

    @Test
    fun loadNextPage() {

        MovementsMockData.mock2019_08_page0(movementsManager)
        MovementsMockData.mock2019_08_page1(movementsManager)

        viewModel.year.postValue(2019)
        viewModel.month.postValue(8)
        viewModel.day.postValue(null)

        viewModel.search()
        assertThat(viewModel.page.value, `is`(notNullValue()))
        assertThat(viewModel.page.value?.first, `is`(true))
        assertThat(viewModel.page.value?.last, `is`(false))
        assertThat(viewModel.isLastPage(), `is`(false))
        assertThat(viewModel.page.value?.number, `is`(0))

        viewModel.loadNextPage()
        assertThat(viewModel.page.value, `is`(notNullValue()))
        assertThat(viewModel.page.value?.first, `is`(false))
        assertThat(viewModel.page.value?.last, `is`(true))
        assertThat(viewModel.isLastPage(), `is`(true))
        assertThat(viewModel.page.value?.number, `is`(1))
        val expectedPage1 = MovementsMockData.fromJsonFile<MovementListPage>(MovementsConfig.DATA_2019_08_PAGE_1_OK,MovementListPage::class)
        checkPageDataWithViewModel(expectedPage1)
    }

    @Test
    fun isValidParams() {

        assertThat(viewModel.isValidParams(), `is`(true))

        viewModel.year.postValue(null)
        assertThat(viewModel.isValidParams(), `is`(false))

        viewModel.year.postValue(2019)
        assertThat(viewModel.isValidParams(), `is`(true))

        viewModel.month.postValue(null)
        assertThat(viewModel.isValidParams(), `is`(false))

        viewModel.month.postValue(1)
        assertThat(viewModel.isValidParams(), `is`(true))

        viewModel.year.postValue(null)
        viewModel.month.postValue(null)
        assertThat(viewModel.isValidParams(), `is`(false))
    }

    @Test
    fun isLastPage() {

        assertThat(viewModel.isLastPage(), `is`(true))

        viewModel.page.postValue(MovementListPage().apply { last = true})
        assertThat(viewModel.isLastPage(), `is`(true))

        viewModel.page.postValue(MovementListPage().apply { last = false})
        assertThat(viewModel.isLastPage(), `is`(false))
    }

    @Test
    fun buildDefaultParameters() {

        // By default build default parameter
        val parameters = viewModel.buildParameters(false)
        assertThat(parameters, `is`(notNullValue()))
        assertThat(parameters.year, `is`(viewModel.year.value))
        assertThat(parameters.month, `is`(viewModel.month.value))
        assertThat(parameters.day, `is`(viewModel.day.value))
        assertThat(parameters.categoryId, `is`(viewModel.categoryId.value))
        assertThat(parameters.page, `is`(0))
        assertThat(parameters.size, `is`(AppConstants.DEFAULT_PAGE_SIZE))

        // Parameters must be the same if viewModel.page is null
        var newParameters = viewModel.buildParameters(true)
        assertThat(parameters, `is`(newParameters))

        newParameters = viewModel.buildParameters(true)
        assertThat(parameters, `is`(newParameters))
    }

    @Test
    fun buildPageParameters() {

        val defaultParameters = viewModel.buildParameters(false)

        // Set page
        viewModel.page.postValue(MovementListPage().apply {
            number = 3
            size = 9999
        })

        // with fromFirstPage = false build parameters for the next page
        var newParameters = viewModel.buildParameters(false)
        assertThat(newParameters, not(`is`(defaultParameters)))
        assertThat(newParameters.page, `is`((viewModel.page.value?.number ?: 0) + 1))
        assertThat(newParameters.size, `is`(viewModel.page.value?.size))

        // with fromFirstPage = true build parameters for the first page
        newParameters = viewModel.buildParameters(true)
        assertThat(newParameters, not(`is`(defaultParameters)))
        assertThat(newParameters.page, `is`(0))
        assertThat(newParameters.size, `is`(viewModel.page.value?.size))
    }

    private fun checkPageDataWithViewModel(expectedData: MovementListPage?) {

        assertThat(expectedData, `is`(notNullValue()))

        expectedData?.let {
            assertThat(viewModel.page.value?.number, `is`(it.number))
            assertThat(viewModel.page.value?.last, `is`(it.last))
            assertThat(viewModel.page.value?.first, `is`(it.first))
            assertThat(viewModel.page.value?.totalPages, `is`(it.totalPages))
            assertThat(viewModel.page.value?.totalElements, `is`(it.totalElements))
            assertThat(viewModel.page.value?.size, `is`(it.size))
            assertThat(viewModel.page.value?.contents?.size, `is`(it.contents.size))
        }
    }
}