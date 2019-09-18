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
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.main.view.lastmovements.MovementsDataAdapter
import it.italiancoders.mybudget.activity.movements.edit.MovementActivity
import it.italiancoders.mybudget.app.MyBudgetApplication
import it.italiancoders.mybudget.app.module.viewModel.DaggerViewModelFactory
import it.italiancoders.mybudget.databinding.ListMovementsFragmentBinding
import it.italiancoders.mybudget.rest.models.Movement
import it.italiancoders.mybudget.rest.models.MovementListPage
import it.italiancoders.mybudget.utils.recyclerview.SwipeToDeleteCallback
import javax.inject.Inject


class ListMovementsFragment : Fragment() {

    lateinit var binding: ListMovementsFragmentBinding

    @Inject
    lateinit var listMovementsViewModelFactory: DaggerViewModelFactory

    private val movementsDataAdapter = MovementsDataAdapter(mutableListOf())

    var layoutManager: LinearLayoutManager? = null

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

        activity?.let {
            (it.application as MyBudgetApplication).appComponent.inject(this)
        }

        binding.model =
            ViewModelProvider(this, listMovementsViewModelFactory).get(
                ListMovementsViewModel::class.java
            )

        layoutManager = LinearLayoutManager(binding.root.context)

        binding.movementsRecyclerView.setHasFixedSize(true)
        binding.movementsRecyclerView.layoutManager = layoutManager
        binding.movementsRecyclerView.adapter = movementsDataAdapter
        binding.model?.page?.observe(this, Observer {
            if (it?.number ?: 0 == 0) {
                movementsDataAdapter.setMovements(it?.contents.orEmpty())
            } else {
                movementsDataAdapter.addMovements(it?.contents.orEmpty())
            }
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

        binding.movementsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    if (binding.model?.loadingData?.get() != true && binding.model?.isLastPage() != true) {
                        binding.model?.loadNextPage(true)
                    }
                } else if (binding.model?.isLastPage() != true && layoutManager?.findLastVisibleItemPosition() != -1 &&
                    layoutManager?.findLastVisibleItemPosition() == (layoutManager?.itemCount
                        ?: 0) - 1
                ) {
                    binding.model?.loadNextPage(true)
                }
            }
        })

        val itemTouchHelper =
            ItemTouchHelper(SwipeToDeleteCallback(binding.root.context) { deleteItemOnSwipe(it) })
        itemTouchHelper.attachToRecyclerView(binding.movementsRecyclerView)
    }

    private fun deleteItemOnSwipe(position: Int) {
        val movement = movementsDataAdapter.getMovement(position)
        if (movement.id == null) return

        movementsDataAdapter.removeMovement(movement)
        movementsDataAdapter.notifyItemRemoved(position)

        var confirmDelete = true
        var deleteProcessed = false

        val snackbar = Snackbar.make(
            binding.root, R.string.movement_deleted,
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction(R.string.undo) {
            confirmDelete = false
            snackbar.dismiss()
        }
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            .setTextColor(ContextCompat.getColor(binding.root.context, android.R.color.white))
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                if (deleteProcessed) return

                deleteProcessed = true
                if (confirmDelete) {
                    binding.model?.delete(movement) {
                        if (!it) {
                            Toast.makeText(
                                binding.root.context,
                                R.string.movement_delete_error,
                                Toast.LENGTH_SHORT
                            ).show()
                            movementsDataAdapter.addMovement(movement, position)
                        }
                    }
                } else {
                    movementsDataAdapter.addMovement(movement, position)
                }
            }
        })
        snackbar.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode != MovementActivity.REQUEST_CODE_MOVEMENT -> return
            resultCode == Activity.RESULT_OK -> search()
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

    fun search() {
        binding.model?.isValidParams()?.let {
            binding.model?.search(true)
        }
    }
}
