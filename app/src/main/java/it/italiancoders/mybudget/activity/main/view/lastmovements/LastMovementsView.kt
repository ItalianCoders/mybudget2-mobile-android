/*
 * Project: mybudget2-mobile-android
 * File: LastMovementsView.kt
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

package it.italiancoders.mybudget.activity.main.view.lastmovements

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.main.MainActivity
import it.italiancoders.mybudget.activity.main.MainViewModel
import it.italiancoders.mybudget.activity.movements.list.ListMovementsFragment
import it.italiancoders.mybudget.databinding.ViewLastMovementsBinding
import it.italiancoders.mybudget.rest.models.MovementListPage


/**
 * @author fattazzo
 *         <p/>
 *         date: 29/07/19
 */
class LastMovementsView : LinearLayout {

    val binding: ViewLastMovementsBinding by lazy {
        DataBindingUtil.inflate(
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
            R.layout.view_last_movements,
            this as LinearLayout,
            false
        ) as ViewLastMovementsBinding
    }

    val movementsDataAdapter = MovementsDataAdapter(mutableListOf())

    var lifecycleOwner: LifecycleOwner? = null

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
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

        // recycler view
        /**
        binding.lastMovementsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.lastMovementsRecyclerView.setHasFixedSize(true)
        binding.lastMovementsRecyclerView.adapter = movementsDataAdapter
        movementsDataAdapter.movementRecyclerViewAdapterListener = object :
        MovementsDataAdapter.MovementRecyclerViewAdapterListener {
        override fun onListItemSelected(movement: Movement) {

        }
        }
         **/
    }

    fun setModel(model: MainViewModel) {
        binding.model = model
        binding.model?.lastMovements?.observe(lifecycleOwner!!, Observer<MovementListPage> { page ->
            //movementsDataAdapter.setMovements(it)
            val listMovementsFragment =
                (context as MainActivity).supportFragmentManager.findFragmentById(R.id.last_movements_fragment) as ListMovementsFragment?

            listMovementsFragment?.let {
                val year = binding.model?.year?.value!!
                val month = binding.model?.month?.value!!+1
                val day = null
                val categoryId = null

                it.setParams(year, month, day, categoryId, page)
            }
        })
    }
}