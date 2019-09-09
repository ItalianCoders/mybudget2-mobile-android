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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.BaseActivity
import it.italiancoders.mybudget.activity.categories.edit.EditCategoryDialogBuilder
import it.italiancoders.mybudget.app.component.AppComponent
import it.italiancoders.mybudget.databinding.ActivityCategoriesBinding
import it.italiancoders.mybudget.manager.categories.CategoriesManager
import it.italiancoders.mybudget.rest.models.Category
import it.italiancoders.mybudget.tutorial.AbstractTutorialActivity
import it.italiancoders.mybudget.tutorial.TutorialCategoriesActivity
import javax.inject.Inject


class CategoriesActivity : BaseActivity<ActivityCategoriesBinding>() {

    private val categoriesDataAdapter: CategoriesDataAdapter = CategoriesDataAdapter()

    @Inject
    lateinit var categoriesManager: CategoriesManager

    lateinit var categoriesViewModel: CategoriesViewModel

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

        categoriesViewModel =
            ViewModelProvider(this, CategoriesViewModelFactory(categoriesManager)).get(
                CategoriesViewModel::class.java
            )
        binding.model = categoriesViewModel

        // swipe refresh layout
        binding.contentLayout.swipeRefreshContainer.setOnRefreshListener {
            categoriesViewModel.loadAll(true)
        }

        // recycler view
        binding.contentLayout.categoriesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.contentLayout.categoriesRecyclerView.setHasFixedSize(true)
        binding.contentLayout.categoriesRecyclerView.adapter = categoriesDataAdapter
        categoriesDataAdapter.categoryRecyclerViewAdapterListener = object :
            CategoriesDataAdapter.CategoryRecyclerViewAdapterListener {
            override fun onListItemSelected(category: Category) {
                EditCategoryDialogBuilder(categoriesViewModel).build(
                    this@CategoriesActivity,
                    category
                ).show()
            }
        }

        // Observe categories live data and update recycler view items
        categoriesViewModel.categories.observe(this,
            Observer<List<Category>> { t ->
                categoriesDataAdapter.setCategories(t.orEmpty())
                binding.contentLayout.categoriesRecyclerView.scheduleLayoutAnimation()
            })

        binding.fab.setOnClickListener {
            EditCategoryDialogBuilder(categoriesViewModel).build(this@CategoriesActivity).show()
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

        categoriesViewModel.loadAll()
    }

    override fun injectComponent(appComponent: AppComponent) {
        appComponent.inject(this)
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
                EditCategoryDialogBuilder(categoriesViewModel).build(this@CategoriesActivity).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
