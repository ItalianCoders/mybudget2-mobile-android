/*
 * Project: mybudget2-mobile-android
 * File: SearchMovementsView.kt
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

import android.app.DatePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapters.CategoryAdapter
import it.italiancoders.mybudget.databinding.ViewSearchMovementsBinding
import it.italiancoders.mybudget.manager.categories.CategoriesManager
import it.italiancoders.mybudget.rest.models.Category
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 02/08/19
 */
class SearchMovementsView : LinearLayout {

    val binding: ViewSearchMovementsBinding by lazy {
        DataBindingUtil.inflate(
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
            R.layout.view_search_movements,
            this as LinearLayout,
            false
        ) as ViewSearchMovementsBinding
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context) : super(context) {
        initView()
    }

    private fun initView() {
        addView(binding.root)

        binding.model = SearchMovementsViewModel()
        binding.datePickerButton.setOnClickListener { showDatePickerDialog() }
        binding.clearButton.setOnClickListener {
            binding.model?.reset()
            binding.invalidateAll()
        }

        val allCategory = Category().apply {
            name = context.getString(R.string.category_all)
        }

        CategoriesManager(context).loadAll({
            val categoryAdapter = CategoryAdapter(context, it ?: listOf(), allCategory)
            binding.categorySpinner.adapter = categoryAdapter
        }, {
            val categoryAdapter = CategoryAdapter(context, listOf(), allCategory)
            binding.categorySpinner.adapter = categoryAdapter
        })
    }

    private fun showDatePickerDialog() {

        val newCalendar = Calendar.getInstance()

        DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                binding.model?.year?.value = year
                binding.model?.month?.value = month + 1
                binding.model?.day?.value = dayOfMonth
                binding.invalidateAll()
            },
            newCalendar.get(Calendar.YEAR),
            newCalendar.get(Calendar.MONTH),
            newCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun isParametersValid(): Boolean = binding.model?.year?.value != null && binding.model?.month?.value != null

    fun getYear(): Int? = binding.model?.year?.value
    fun getMonth(): Int? = binding.model?.month?.value
    fun getDay(): Int? = binding.model?.day?.value
    fun getCategory(): Category? {
        val category: Category? = binding.categorySpinner.selectedItem as Category?
        return if (category?.id == null) null else category
    }
}