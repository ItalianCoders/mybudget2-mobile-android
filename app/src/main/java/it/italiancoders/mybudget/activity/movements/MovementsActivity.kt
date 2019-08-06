/*
 * Project: mybudget2-mobile-android
 * File: MovementsActivity.kt
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

package it.italiancoders.mybudget.activity.movements

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.BaseActivity
import it.italiancoders.mybudget.activity.main.view.lastmovements.MovementsDataAdapter
import it.italiancoders.mybudget.activity.movements.model.MovementsViewModel
import it.italiancoders.mybudget.activity.movements.search.SearchMovementsView
import it.italiancoders.mybudget.databinding.ActivityMovementsBinding
import it.italiancoders.mybudget.manager.movements.MovementsManager
import it.italiancoders.mybudget.rest.models.Movement
import kotlinx.android.synthetic.main.content_movements.view.*
import kotlinx.android.synthetic.main.view_search_movements.view.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 02/08/19
 */
class MovementsActivity : BaseActivity<ActivityMovementsBinding>() {

    private var mBottomSheetBehavior: BottomSheetBehavior<SearchMovementsView?>? = null

    val movementsDataAdapter = MovementsDataAdapter()

    private val movementsManager by lazy { MovementsManager(this) }

    override fun getLayoutResID(): Int = R.layout.activity_movements

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Fix: style.xml don't work
        val typeface = ResourcesCompat.getFont(this, R.font.os_regular)
        binding.toolbarLayout.setExpandedTitleTypeface(typeface)
        binding.toolbarLayout.setCollapsedTitleTypeface(typeface)

        binding.model = ViewModelProviders.of(this).get(MovementsViewModel::class.java)

        binding.searchMovementsView.lifecycleOwner = this

        // recycler view
        binding.contentLayout.movements_recycler_view.layoutManager = LinearLayoutManager(this)
        binding.contentLayout.movements_recycler_view.setHasFixedSize(true)
        binding.contentLayout.movements_recycler_view.adapter = movementsDataAdapter
        binding.model?.movements?.observe(this, Observer<List<Movement>> {
            movementsDataAdapter.setMovements(it)
        })
        movementsDataAdapter.movementRecyclerViewAdapterListener = object :
            MovementsDataAdapter.MovementRecyclerViewAdapterListener {
            override fun onListItemSelected(movement: Movement) {

            }
        }

        // Show option menu when appbar is collapsed
        binding.appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true
                    showOption(R.id.action_add)
                } else if (isShow) {
                    isShow = false
                    hideOption(R.id.action_add)
                }
            }
        })

        initSearchMovementsSlidingPanel()
    }

    fun search(view: View) {
        if (binding.searchMovementsView.isParametersValid()) {
            mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.model?.search(movementsManager, true)
        } else {
            Toast.makeText(this, R.string.movements_search_parameters_data_required, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initSearchMovementsSlidingPanel() {
        BottomSheetBehavior.from(binding.searchMovementsView)?.let { bsb ->
            bsb.state = BottomSheetBehavior.STATE_EXPANDED

            binding.searchMovementsView.title_view.setOnClickListener { toggleSearchView() }

            mBottomSheetBehavior = bsb
        }
    }

    private fun toggleSearchView() {
        mBottomSheetBehavior?.state =
            if (mBottomSheetBehavior?.state == BottomSheetBehavior.STATE_COLLAPSED)
                BottomSheetBehavior.STATE_EXPANDED else
                BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onBackPressed() {
        when {
            mBottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED -> mBottomSheetBehavior?.state =
                BottomSheetBehavior.STATE_COLLAPSED
            else -> super.onBackPressed()
        }
    }
}