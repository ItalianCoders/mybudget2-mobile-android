/*
 * Project: mybudget2-mobile-android
 * File: MovementViewModel.kt
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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.italiancoders.mybudget.manager.categories.CategoriesManager
import it.italiancoders.mybudget.rest.models.Category
import it.italiancoders.mybudget.rest.models.Movement
import it.italiancoders.mybudget.utils.ioJob
import java.math.BigDecimal
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 06/08/19
 */
class MovementViewModel(private val categoriesManager: CategoriesManager) : ViewModel() {

    var initialized = false

    val amount = MutableLiveData(BigDecimal.ZERO)
    val category = MutableLiveData(Category())
    val date = MutableLiveData(Calendar.getInstance().time)

    val categories = MutableLiveData<List<Category>>(listOf())

    private var movement = Movement()

    fun init(movement: Movement) {
        amount.postValue(movement.amount)
        category.value = movement.category
        date.postValue(movement.executedAtDate ?: Calendar.getInstance().time)
        this.movement = movement
    }

    fun isNewMovement(): Boolean = movement.id == null

    fun isMovementValid(): Boolean =
        amount.value != null && category.value != null && category.value?.id != null && date.value != null

    fun getMovement(): Movement? {
        return if (isMovementValid()) {
            movement.amount = amount.value!!
            movement.category = category.value!!
            movement.executedAtDate = date.value
            movement
        } else {
            null
        }
    }

    fun loadCategories() {

        ioJob {
            val categoriesLoaded = categoriesManager.loadAll()
            categories.postValue(categoriesLoaded)
        }
    }
}