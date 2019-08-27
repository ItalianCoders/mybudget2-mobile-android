/*
 * Project: mybudget2-mobile-android
 * File: CategoryBarChartManager.kt
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

package it.italiancoders.mybudget.activity.main.chart.manager

import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import it.italiancoders.mybudget.activity.main.chart.marker.CategoryMarkerView
import it.italiancoders.mybudget.databinding.converters.AmountStringConversion
import it.italiancoders.mybudget.manager.AppPreferenceManager
import it.italiancoders.mybudget.rest.models.CategoryMovementOverview
import java.math.BigDecimal


/**
 * @author fattazzo
 *         <p/>
 *         date: 27/07/19
 */
object CategoryBarChartManager {

    /**
     * Configure chart ui props
     */
    fun configure(chart: BarChart) {
        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)

        chart.description.isEnabled = false

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60)

        chart.setDrawGridBackground(false)

        val xAxis = chart.xAxis
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.labelCount = 7

        val leftAxis = chart.axisLeft
        leftAxis.setLabelCount(8, false)
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f

        chart.xAxis.isEnabled = false
        chart.axisRight.isEnabled = false
        chart.legend.isEnabled = false

        val mv = CategoryMarkerView(chart.context)
        mv.chartView = chart // For bounds control
        chart.marker = mv // Set the marker to the chart
    }

    /**
     * Create and set dataset
     */
    fun setData(
        chart: BarChart,
        categoryOverview: List<CategoryMovementOverview>,
        total: BigDecimal
    ) {
        val values = categoryOverview.mapIndexed { index, overview ->
            BarEntry(
                index.toFloat(),
                overview.totalAmount?.toFloat()!!, overview.category
            )
        }.toList()

        val set1: BarDataSet

        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as BarDataSet
            set1.values = values
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()

        } else {
            set1 = BarDataSet(values, AmountStringConversion.toString(chart.context, total))
            set1.setDrawIcons(false)
            set1.setDrawValues(true)

            set1.colors = AppPreferenceManager.getChartColorTheme(chart.context).colors.toList()

            val dataSets = listOf(set1)

            val data = BarData(dataSets)
            data.setValueTextSize(10f)
            data.setDrawValues(false)
            data.barWidth = 0.9f
            data.isHighlightEnabled = true

            chart.data = data
        }

        chart.invalidate()

        chart.animateY(1400, Easing.EaseInOutQuad)
    }
}