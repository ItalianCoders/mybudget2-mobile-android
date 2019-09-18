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

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import com.whiteelephant.monthpicker.MonthPickerDialog
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.SessionData
import it.italiancoders.mybudget.activity.BaseActivity
import it.italiancoders.mybudget.activity.SessionHandler
import it.italiancoders.mybudget.activity.categories.CategoriesActivity
import it.italiancoders.mybudget.activity.login.LoginActivity
import it.italiancoders.mybudget.activity.main.chart.ChartsCategoryOverviewAdapter
import it.italiancoders.mybudget.activity.main.view.lastmovements.LastMovementsView
import it.italiancoders.mybudget.activity.movements.MovementsActivity
import it.italiancoders.mybudget.activity.movements.edit.MovementActivity
import it.italiancoders.mybudget.activity.settings.SettingsActivity
import it.italiancoders.mybudget.app.MyBudgetApplication
import it.italiancoders.mybudget.app.module.viewModel.DaggerViewModelFactory
import it.italiancoders.mybudget.databinding.ActivityMainBinding
import it.italiancoders.mybudget.tutorial.TutorialMainActivity
import it.italiancoders.mybudget.utils.LinePagerIndicatorDecoration
import it.italiancoders.mybudget.utils.NetworkChecker
import java.math.BigDecimal
import javax.inject.Inject


class MainActivity : BaseActivity<ActivityMainBinding>(),
    NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var mBottomSheetBehavior: BottomSheetBehavior<LastMovementsView?>? = null

    override fun getLayoutResID(): Int = R.layout.activity_main

    override fun getMenuItemsIconColor(): Int = android.R.color.black

    override fun createTutorial() = TutorialMainActivity(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        SessionHandler().setAppLastSession(this,false)

        (application as MyBudgetApplication).appComponent.inject(this)

        NetworkChecker().isNetworkAvailable(this)

        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        binding.model = ViewModelProvider(this, viewModelFactory)
            .get(MainViewModel::class.java)
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

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.contentMain.chartsRecyclerView)

        binding.contentMain.chartsRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.contentMain.chartsRecyclerView.adapter = ChartsCategoryOverviewAdapter(this)
        binding.contentMain.chartsRecyclerView.addItemDecoration(LinePagerIndicatorDecoration())

        binding.model?.categoryOverview?.observe(this, Observer {
            (binding.contentMain.chartsRecyclerView.adapter as ChartsCategoryOverviewAdapter).updateCharts(
                it,
                binding.model?.total?.value ?: BigDecimal.ZERO
            )
        })

        if (SessionData.session != null) {
            binding.model?.loadExpenseSummary()
        }

        binding.contentMain.addMovementButton.setOnClickListener {
            startActivityForResult(
                Intent(this@MainActivity, MovementActivity::class.java),
                MovementActivity.REQUEST_CODE_MOVEMENT
            )
        }

        initLastMovementsSlidingPanel()
    }

    override fun onBackPressed() {
        when {
            binding.drawerLayout.isDrawerOpen(GravityCompat.START) -> binding.drawerLayout.closeDrawer(
                GravityCompat.START
            )
            mBottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED -> mBottomSheetBehavior?.state =
                BottomSheetBehavior.STATE_COLLAPSED
            else -> {
                MaterialDialog(this).show {
                    icon(R.drawable.menu_logout)
                    title(R.string.app_name)
                    message(R.string.exit_confirmation_message)
                    negativeButton(android.R.string.no)
                    positiveButton(android.R.string.yes) { super.onBackPressed() }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MovementActivity.REQUEST_CODE_MOVEMENT && resultCode == Activity.RESULT_OK) {
            binding.model?.loadExpenseSummary()
        }
    }

    override fun onUserLoggedIn() {
        binding.model?.loadExpenseSummary()
    }

    fun nextPeriodType(view: View?) {
        binding.contentMain.periodoTextView.alpha = 0f

        val flip = ObjectAnimator.ofFloat(
            binding.contentMain.periodoContainer,
            "rotationX",
            0f,
            360f
        ).apply { duration = 1000 }

        val alpha1TextAnimator =
            ObjectAnimator.ofFloat(binding.contentMain.periodoTextView, "alpha", 0f, 1f)
                .apply { duration = 1000 }


        runOnUiThread {
            val set = AnimatorSet()
            set.play(flip).with(alpha1TextAnimator)
            set.start()
        }

        binding.model?.loadNextPeriodTypeExpenseSummary()
    }

    fun changePeriod(view: View) {
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED

        if (binding.model?.periodType?.value == PeriodType.MONTH) {
            MonthPickerDialog.Builder(
                this,
                MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear ->
                    binding.model?.loadExpenseSummary(selectedYear, selectedMonth, 1)
                },
                binding.model?.year?.value!!,
                binding.model?.month?.value!!
            ).build().show()
        } else {
            DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                    binding.model?.loadExpenseSummary(selectedYear, selectedMonth, selectedDay)
                },
                binding.model?.year?.value!!,
                binding.model?.month?.value!!,
                binding.model?.day?.value!!
            ).show()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_settings -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.nav_categories -> startActivity(Intent(this, CategoriesActivity::class.java))
            R.id.nav_movements -> startActivity(Intent(this, MovementsActivity::class.java))
            R.id.nav_logout -> {
                sessionManager.removeSession()
                startActivity(Intent(this.applicationContext, LoginActivity::class.java))
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initLastMovementsSlidingPanel() {
        BottomSheetBehavior.from(binding.contentMain.lastMovementsView)?.let { bsb ->
            bsb.state = BottomSheetBehavior.STATE_HIDDEN

            binding.contentMain.lastMovementsView.binding.titleView.setOnClickListener { toggleLastMovementsView() }

            mBottomSheetBehavior = bsb
        }
    }

    private fun toggleLastMovementsView() {
        mBottomSheetBehavior?.state =
            if (mBottomSheetBehavior?.state == BottomSheetBehavior.STATE_COLLAPSED)
                BottomSheetBehavior.STATE_EXPANDED else
                BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sync -> {
                binding.model?.loadExpenseSummary()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
