/*
 * Project: mybudget2-mobile-android
 * File: CategoriesDataAdapter.kt
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

package it.italiancoders.mybudget.activity.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.databinding.ListItemCategoryBinding
import it.italiancoders.mybudget.rest.models.Category
import kotlinx.android.synthetic.main.list_item_category.view.*

class CategoriesDataAdapter : RecyclerView.Adapter<CategoriesDataAdapter.CategoryViewHolder>() {

    var categoryRecyclerViewAdapterListener: CategoryRecyclerViewAdapterListener? = null

    private var categories: MutableList<Category>? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CategoryViewHolder {
        val listItemCategoryBinding = DataBindingUtil.inflate<ListItemCategoryBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.list_item_category, viewGroup, false
        )
        return CategoryViewHolder(listItemCategoryBinding)
    }

    override fun onBindViewHolder(categoryViewHolder: CategoryViewHolder, i: Int) {
        categoryViewHolder.listItemCategoryBinding.category = categories!![i]
    }

    override fun getItemCount(): Int = categories?.size ?: 0

    fun setCategories(categories: List<Category>) {
        this.categories = categories.toMutableList()
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(var listItemCategoryBinding: ListItemCategoryBinding) :
        RecyclerView.ViewHolder(listItemCategoryBinding.root) {

        init {
            listItemCategoryBinding.root.rootLayout.setOnClickListener {
                categoryRecyclerViewAdapterListener?.onListItemSelected(categories!![layoutPosition])
            }
        }
    }

    interface CategoryRecyclerViewAdapterListener {
        fun onListItemSelected(category: Category)
    }
}