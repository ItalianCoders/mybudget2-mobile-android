/*
 * Project: mybudget2-mobile-android
 * File: ChartsCategoryOverviewAdapter.kt
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

package it.italiancoders.mybudget.activity.main.chart

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.italiancoders.mybudget.activity.main.chart.view.CategoryBarChartView
import it.italiancoders.mybudget.activity.main.chart.view.CategoryPieChartView
import it.italiancoders.mybudget.activity.main.chart.view.CategoryRadarChartView
import it.italiancoders.mybudget.activity.main.chart.view.ChartView
import it.italiancoders.mybudget.rest.models.CategoryMovementOverview
import java.math.BigDecimal

/**
 * @author fattazzo
 *         <p/>
 *         date: 26/08/19
 */
class ChartsCategoryOverviewAdapter(context: Context) :
    RecyclerView.Adapter<ChartsCategoryOverviewAdapter.ViewHolder>() {

    private var chartViews: List<ChartView<*>> = listOf(
        CategoryPieChartView(context),
        CategoryBarChartView(context),
        CategoryRadarChartView(context)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(chartViews[viewType])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = chartViews.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    fun updateCharts(categoryOverview: List<CategoryMovementOverview>, total: BigDecimal) {
        chartViews.forEach { it.update(categoryOverview, total) }
    }

    inner class ViewHolder(view: ChartView<*>) : RecyclerView.ViewHolder(view)
}