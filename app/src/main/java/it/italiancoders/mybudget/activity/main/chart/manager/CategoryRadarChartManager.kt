/*
 * Project: mybudget2-mobile-android
 * File: CategoryRadarChartManager.kt
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

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.rest.models.CategoryMovementOverview
import java.math.BigDecimal


/**
 * @author fattazzo
 *         <p/>
 *         date: 27/07/19
 */
object CategoryRadarChartManager {

    /**
     * Configure chart ui props
     */
    fun configure(chart: RadarChart) {
        chart.description.isEnabled = false

        chart.webLineWidth = 1f
        chart.webColor = Color.LTGRAY
        chart.webLineWidthInner = 1f
        chart.webColorInner = Color.LTGRAY
        chart.webAlpha = 100

        val xAxis = chart.xAxis
        xAxis.textSize = 9f
        xAxis.yOffset = 0f
        xAxis.xOffset = 0f
        xAxis.setDrawLabels(true)

        xAxis.textColor = Color.BLACK

        val yAxis = chart.yAxis
        yAxis.setLabelCount(5, false)
        yAxis.textSize = 9f
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = 80f
        yAxis.setDrawLabels(false)

        chart.legend.isEnabled = false
    }

    /**
     * Create and set dataset
     */
    fun setData(
        chart: RadarChart,
        categoryOverview: List<CategoryMovementOverview>,
        total: BigDecimal
    ) {

        val maxValue = categoryOverview.maxBy { it.totalAmount ?: 0.0 }?.totalAmount ?: 0.0
        chart.yAxis.mAxisMaximum = maxValue.toFloat()

        val categories = categoryOverview.map { it.category }
        chart.xAxis.valueFormatter = object : ValueFormatter() {

            override fun getFormattedValue(value: Float): String {
                return if (categories.isNotEmpty() && categories.size >= (value.toInt() % categories.size)) {
                    categories[value.toInt() % categories.size].name
                } else {
                    ""
                }
            }
        }

        val values =
            categoryOverview.map { RadarEntry(it.totalAmount?.toFloat()!!, it.category) }.toList()

        val set1 = RadarDataSet(values, "Categories")
        set1.color = ContextCompat.getColor(chart.context, R.color.primaryDarkColor)
        set1.fillColor = ContextCompat.getColor(chart.context, R.color.primaryColor)
        set1.setDrawFilled(true)
        set1.fillAlpha = 80
        set1.lineWidth = 2f
        set1.isDrawHighlightCircleEnabled = true
        set1.setDrawHighlightIndicators(false)

        val sets = listOf(set1)

        val data = RadarData(sets)
        data.setValueFormatter(DefaultValueFormatter(2))
        data.setValueTextSize(8f)
        data.setDrawValues(true)
        data.setValueTextColor(Color.BLACK)

        chart.data = data
        chart.invalidate()

        chart.animateY(1400, Easing.EaseInOutQuad)
    }
}