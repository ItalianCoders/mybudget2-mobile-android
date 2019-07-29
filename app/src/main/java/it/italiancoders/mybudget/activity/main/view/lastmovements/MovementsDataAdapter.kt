/*
 * Project: mybudget2-mobile-android
 * File: MovementsDataAdapter.kt
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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.databinding.ListItemMovementBinding
import it.italiancoders.mybudget.rest.models.Movement
import kotlinx.android.synthetic.main.list_item_category.view.*

class MovementsDataAdapter : RecyclerView.Adapter<MovementsDataAdapter.MovementViewHolder>() {

    var movementRecyclerViewAdapterListener: MovementRecyclerViewAdapterListener? = null

    private var movements: MutableList<Movement>? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MovementViewHolder {
        val listItemMovementBinding = DataBindingUtil.inflate<ListItemMovementBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.list_item_movement, viewGroup, false
        )
        return MovementViewHolder(listItemMovementBinding)
    }

    override fun onBindViewHolder(movementViewHolder: MovementViewHolder, i: Int) {
        movementViewHolder.listItemMovementBinding.movement = movements!![i]
    }

    override fun getItemCount(): Int = movements?.size ?: 0

    fun setMovements(movements: List<Movement>) {
        this.movements = movements.toMutableList()
        notifyDataSetChanged()
    }

    inner class MovementViewHolder(var listItemMovementBinding: ListItemMovementBinding) :
        RecyclerView.ViewHolder(listItemMovementBinding.root) {

        init {
            listItemMovementBinding.root.rootLayout.setOnClickListener {
                movementRecyclerViewAdapterListener?.onListItemSelected(movements!![layoutPosition])
            }
        }
    }

    interface MovementRecyclerViewAdapterListener {
        fun onListItemSelected(movement: Movement)
    }
}