/*
 * Project: mybudget2-mobile-android
 * File: MainActivity.kt
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

package it.italiancoders.mybudget.activity.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import com.whiteelephant.monthpicker.MonthPickerDialog
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.SessionData
import it.italiancoders.mybudget.activity.BaseActivity
import it.italiancoders.mybudget.activity.categories.CategoriesActivity
import it.italiancoders.mybudget.activity.login.LoginActivity
import it.italiancoders.mybudget.activity.main.chart.CategoryPieChartManager
import it.italiancoders.mybudget.activity.main.view.lastmovements.LastMovementsView
import it.italiancoders.mybudget.activity.settings.SettingsActivity
import it.italiancoders.mybudget.databinding.ActivityMainBinding
import it.italiancoders.mybudget.manager.AuthManager
import it.italiancoders.mybudget.manager.movements.MovementsManager
import it.italiancoders.mybudget.tutorial.TutorialMainActivity
import it.italiancoders.mybudget.utils.NetworkChecker


class MainActivity : BaseActivity<ActivityMainBinding>(), NavigationView.OnNavigationItemSelectedListener {

    private val movementsManager by lazy { MovementsManager(this) }

    private var mBottomSheetBehavior: BottomSheetBehavior<LastMovementsView?>? = null

    override fun getLayoutResID(): Int = R.layout.activity_main

    override fun getMenuItemsIconColor(): Int = android.R.color.black

    override fun createTutorial() = TutorialMainActivity(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        SessionData.session = AuthManager(this.applicationContext).getLastSession()
        NetworkChecker().isNetworkAvailable(this)

        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        binding.model = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.contentMain.lastMovementsView.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)

        if (SessionData.session == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        CategoryPieChartManager.configure(binding.contentMain.categoriesPieChart)
        binding.model?.categoryOverview?.observe(this, Observer {
            CategoryPieChartManager.setData(binding.contentMain.categoriesPieChart, it)
        })

        binding.model?.loadExpenseSummary(movementsManager)

        binding.contentMain.lastMovementsButton.setOnClickListener {
            mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }

        initLastMovementsSlidingPanel()
    }

    override fun onBackPressed() {
        when {
            binding.drawerLayout.isDrawerOpen(GravityCompat.START) -> binding.drawerLayout.closeDrawer(GravityCompat.START)
            mBottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED -> mBottomSheetBehavior?.state =
                BottomSheetBehavior.STATE_COLLAPSED
            else -> super.onBackPressed()
        }
    }

    fun changePeriod(view: View) {
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED

        MonthPickerDialog.Builder(
            this,
            MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear ->
                binding.model?.year?.postValue(selectedYear)
                binding.model?.month?.postValue(selectedMonth)
                binding.model?.loadExpenseSummary(movementsManager)
            },
            binding.model?.year?.value!!,
            binding.model?.month?.value!!
        ).build().show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            R.id.nav_categories -> {
                startActivity(Intent(this, CategoriesActivity::class.java))
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initLastMovementsSlidingPanel() {
        BottomSheetBehavior.from(binding.contentMain.lastMovementsView)?.let { bsb ->
            bsb.state = BottomSheetBehavior.STATE_HIDDEN

            binding.contentMain.lastMovementsButton.setOnClickListener {
                bsb.state = BottomSheetBehavior.STATE_EXPANDED
            }

            mBottomSheetBehavior = bsb
        }
    }

}
