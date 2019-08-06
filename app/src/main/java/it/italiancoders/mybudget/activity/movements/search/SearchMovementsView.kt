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
import androidx.lifecycle.LifecycleOwner
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.movements.model.MovementsViewModel
import it.italiancoders.mybudget.databinding.ViewSearchMovementsBinding
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

    var lifecycleOwner: LifecycleOwner? = null

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
        binding.lifecycleOwner = lifecycleOwner

        binding.datePickerButton.setOnClickListener { showDatePickerDialog() }
        binding.clearButton.setOnClickListener {
            binding.searchModel?.reset()
            binding.invalidateAll()
        }
    }

    fun setSearchModel(searchModel: MovementsViewModel) {
        binding.searchModel = searchModel
    }

    private fun showDatePickerDialog() {

        val newCalendar = Calendar.getInstance()

        DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                binding.searchModel?.year?.value = year
                binding.searchModel?.month?.value = month
                binding.searchModel?.day?.value = dayOfMonth
                binding.invalidateAll()
            },
            newCalendar.get(Calendar.YEAR),
            newCalendar.get(Calendar.MONTH),
            newCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun isParametersValid() : Boolean = binding.searchModel?.year?.value != null && binding.searchModel?.month?.value != null
}