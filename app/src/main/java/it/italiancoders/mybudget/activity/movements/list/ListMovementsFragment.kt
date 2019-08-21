/*
 * Project: mybudget2-mobile-android
 * File: ListMovementsFragment.kt
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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.main.view.lastmovements.MovementsDataAdapter
import it.italiancoders.mybudget.activity.movements.edit.MovementActivity
import it.italiancoders.mybudget.databinding.ListMovementsFragmentBinding
import it.italiancoders.mybudget.manager.movements.MovementsManager
import it.italiancoders.mybudget.rest.models.Movement
import it.italiancoders.mybudget.rest.models.MovementListPage

class ListMovementsFragment : Fragment() {

    lateinit var binding: ListMovementsFragmentBinding

    var movementsManager: MovementsManager? = null

    val movementsDataAdapter = MovementsDataAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.list_movements_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.model = ViewModelProvider(this).get(ListMovementsViewModel::class.java)

        activity?.let {
            movementsManager = MovementsManager(it)
            binding.movementsRecyclerView.layoutManager = LinearLayoutManager(it)
        }

        binding.movementsRecyclerView.setHasFixedSize(true)
        binding.movementsRecyclerView.adapter = movementsDataAdapter
        binding.model?.page?.observe(this, Observer {
            movementsDataAdapter.setMovements(it?.contents.orEmpty())
        })

        movementsDataAdapter.movementRecyclerViewAdapterListener = object :
            MovementsDataAdapter.MovementRecyclerViewAdapterListener {
            override fun onListItemSelected(movement: Movement) {
                context?.let {
                    val intent = Intent(it, MovementActivity::class.java)
                    intent.putExtra(MovementActivity.EXTRA_MOVEMENT_ID, movement.id)

                    startActivityForResult(intent, MovementActivity.REQUEST_CODE_MOVEMENT)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode != MovementActivity.REQUEST_CODE_MOVEMENT -> return
            resultCode == Activity.RESULT_OK -> search(true)
        }
    }

    fun setParams(
        year: Int,
        month: Int,
        day: Int?,
        categoryId: Long?,
        page: MovementListPage? = null
    ) {
        binding.model?.year?.value = year
        binding.model?.month?.value = month
        binding.model?.day?.value = day
        binding.model?.categoryId?.value = categoryId
        binding.model?.page?.value = page
    }

    fun search(fromFirstPage: Boolean) {
        binding.model?.isValidParams()?.let {
            binding.model?.search(movementsManager, true,fromFirstPage)
        }
    }
}
