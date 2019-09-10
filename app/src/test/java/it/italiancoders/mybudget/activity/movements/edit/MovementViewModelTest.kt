/*
 * Project: mybudget2-mobile-android
 * File: MovementViewModelTest.kt
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

package it.italiancoders.mybudget.activity.movements.edit

import it.italiancoders.mybudget.AbstractViewModelTest
import it.italiancoders.mybudget.manager.categories.CategoriesManager
import it.italiancoders.mybudget.manager.movements.MovementsManager
import it.italiancoders.mybudget.mocks.data.CategoriesMockData
import it.italiancoders.mybudget.rest.models.Category
import it.italiancoders.mybudget.rest.models.Movement
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mockito.Mock
import java.math.BigDecimal
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 04/09/19
 */
class MovementViewModelTest : AbstractViewModelTest<MovementViewModel>() {

    @Mock
    lateinit var categoriesManager: CategoriesManager

    @Mock
    lateinit var movementsManager: MovementsManager

    override fun createViewModel(): MovementViewModel =
        MovementViewModel(categoriesManager, movementsManager)

    @Test
    override fun initialValues() {

        val calToday = Calendar.getInstance()
        val calViewModel = Calendar.getInstance()
        calViewModel.time = viewModel.date.value!!

        assertThat(viewModel.amount.value?.toLong(), `is`(BigDecimal.ZERO.toLong()))
        assertThat(viewModel.note.value.orEmpty(), `is`(""))
        assertThat(viewModel.category.value, `is`(notNullValue()))
        assertThat(calViewModel.get(Calendar.YEAR), `is`(calToday.get(Calendar.YEAR)))
        assertThat(calViewModel.get(Calendar.MONTH), `is`(calToday.get(Calendar.MONTH)))
        assertThat(
            calViewModel.get(Calendar.DAY_OF_MONTH),
            `is`(calToday.get(Calendar.DAY_OF_MONTH))
        )
        assertThat(viewModel.categories.value, `is`(listOf()))
    }

    @Test
    fun init() {

        val movement = Movement().apply {
            amount = BigDecimal.TEN
            category = Category(1L, "name", "desc", false)
            executedAt = "2019-08-30T13:59:23.617Z"
            note = "note"
        }

        viewModel.init(movement)

        assertThat(viewModel.amount.value?.toLong(), `is`(movement.amount.toLong()))
        assertThat(viewModel.category.value, `is`(movement.category))
        assertThat(viewModel.date.value, `is`(movement.executedAtDate))
        assertThat(viewModel.note.value, `is`(movement.note))
    }

    @Test
    fun isNewMovement() {

        assertThat(viewModel.isNewMovement(), `is`(true))

        viewModel.init(Movement())
        assertThat(viewModel.isNewMovement(), `is`(true))

        val movement = Movement(
            1L,
            BigDecimal.TEN,
            Category(1L, "name", "desc", false),
            "2019-08-30T13:59:23.617Z", null
        )
        viewModel.init(movement)
        assertThat(viewModel.isNewMovement(), `is`(false))
    }

    @Test
    fun isMovementValid() {

        assertThat(viewModel.isMovementValid(), `is`(false))

        viewModel.init(Movement())
        assertThat(viewModel.isMovementValid(), `is`(false))

        var movement = Movement(
            1L,
            BigDecimal.TEN,
            Category(1L, "name", "desc", false),
            "2019-08-30T13:59:23.617Z", null
        )
        viewModel.init(movement)
        assertThat(viewModel.isMovementValid(), `is`(true))

        movement = Movement(
            1L,
            BigDecimal.TEN,
            Category(),
            "2019-08-30T13:59:23.617Z", null
        )
        viewModel.init(movement)
        assertThat(viewModel.isMovementValid(), `is`(false))

        movement = Movement(
            1L,
            BigDecimal.ZERO,
            Category(1L, "name", "desc", false),
            "2019-08-30T13:59:23.617Z", null
        )
        viewModel.init(movement)
        assertThat(viewModel.isMovementValid(), `is`(false))

        movement = Movement(
            1L,
            BigDecimal(-10),
            Category(1L, "name", "desc", false),
            "2019-08-30T13:59:23.617Z", null
        )
        viewModel.init(movement)
        assertThat(viewModel.isMovementValid(), `is`(false))
    }

    @Test
    fun loadCategories() {

        CategoriesMockData.mockLoadAll_dataOk(categoriesManager)

        assertThat(viewModel.categories.value, `is`(listOf()))

        viewModel.loadCategories()

        assertThat(viewModel.categories.value!!.size, not(`is`(0)))
    }
}