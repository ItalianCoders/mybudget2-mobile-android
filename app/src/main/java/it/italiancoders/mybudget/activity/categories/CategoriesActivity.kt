/*
 * Project: mybudget2-mobile-android
 * File: CategoriesActivity.kt
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

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.google.android.material.appbar.AppBarLayout
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.BaseActivity
import it.italiancoders.mybudget.activity.categories.edit.EditCategoryDialogBuilder
import it.italiancoders.mybudget.databinding.ActivityCategoriesBinding
import it.italiancoders.mybudget.manager.categories.CategoriesManager
import it.italiancoders.mybudget.rest.models.Category
import it.italiancoders.mybudget.tutorial.AbstractTutorialActivity
import it.italiancoders.mybudget.tutorial.TutorialCategoriesActivity
import kotlinx.android.synthetic.main.content_categories.view.*


class CategoriesActivity : BaseActivity<ActivityCategoriesBinding>() {

    private val categoriesDataAdapter: CategoriesDataAdapter = CategoriesDataAdapter()

    private val categoriesViewModel by lazy { ViewModelProviders.of(this).get(CategoriesViewModel::class.java) }

    override fun getLayoutResID(): Int = R.layout.activity_categories

    override fun createTutorial(): AbstractTutorialActivity<ActivityCategoriesBinding>? =
        TutorialCategoriesActivity(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Fix: style.xml don't work
        val typeface = ResourcesCompat.getFont(this, R.font.os_regular)
        binding.toolbarLayout.setExpandedTitleTypeface(typeface)
        binding.toolbarLayout.setCollapsedTitleTypeface(typeface)

        // swipe refresh layout
        binding.contentLayout.swipeRefreshContainer.setOnRefreshListener { loadAllCategories(true) }

        // recycler view
        binding.contentLayout.categoriesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.contentLayout.categoriesRecyclerView.setHasFixedSize(true)
        binding.contentLayout.categoriesRecyclerView.adapter = categoriesDataAdapter
        categoriesDataAdapter.categoryRecyclerViewAdapterListener = object :
            CategoriesDataAdapter.CategoryRecyclerViewAdapterListener {
            override fun onListItemSelected(category: Category) {
                EditCategoryDialogBuilder().build(this@CategoriesActivity, category).show {
                    onDismiss { loadAllCategories(true) }
                }
            }
        }

        // Observe categories live data and update recycler view items
        categoriesViewModel.categories.observe(this,
            Observer<List<Category>> { t ->
                categoriesDataAdapter.setCategories(t.orEmpty())
                binding.contentLayout.categoriesRecyclerView.scheduleLayoutAnimation()
            })

        binding.fab.setOnClickListener { view ->
            EditCategoryDialogBuilder().build(this@CategoriesActivity).show {
                onDismiss { loadAllCategories(true) }
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

        loadAllCategories()
    }

    private fun loadAllCategories(forceRefresh: Boolean = false) {
        if (forceRefresh || categoriesViewModel.categories.value.isNullOrEmpty()) {
            binding.contentLayout.swipeRefreshContainer.isRefreshing = true

            CategoriesManager(this).loadAll(
                {
                    categoriesViewModel.categories.postValue(it)
                    binding.contentLayout.swipeRefreshContainer.isRefreshing = false
                },
                { binding.contentLayout.swipeRefreshContainer.isRefreshing = false }, forceRefresh
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.categories, menu)
        hideOption(R.id.action_add)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                EditCategoryDialogBuilder().build(this@CategoriesActivity).show {
                    onDismiss { loadAllCategories(true) }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}