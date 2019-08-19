/*
 * Project: mybudget2-mobile-android
 * File: CategoryPieChartManager.kt
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

import android.graphics.Color
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import it.italiancoders.mybudget.rest.models.CategoryMovementOverview

/**
 * @author fattazzo
 *         <p/>
 *         date: 27/07/19
 */
object CategoryPieChartManager {

    /**
     * Configure chart ui props
     */
    fun configure(chart: PieChart) {
        chart.setUsePercentValues(true)
        chart.description.isEnabled = false
        chart.setExtraOffsets(15f, 15f, 15f, 15f)

        chart.dragDecelerationFrictionCoef = 0.95f

        chart.setDrawEntryLabels(true)

        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.WHITE)

        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)

        chart.holeRadius = 58f
        chart.transparentCircleRadius = 61f

        chart.setDrawCenterText(true)

        chart.rotationAngle = 45f
        chart.isRotationEnabled = true
        chart.isHighlightPerTapEnabled = true

        chart.animateY(1400, Easing.EaseInOutQuad)

        chart.legend.isEnabled = false

        // entry label styling
        chart.setEntryLabelColor(Color.BLACK)
        chart.setEntryLabelTextSize(12f)
    }

    /**
     * Create and set dataset
     */
    fun setData(chart: PieChart, categoryOverview: List<CategoryMovementOverview>) {

        val entries = buildEntries(categoryOverview)

        val dataSet = PieDataSet(entries, "Test")

        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(-0f, 0f)
        dataSet.selectionShift = 5f
        dataSet.setDrawIcons(true)

        dataSet.valueLinePart1OffsetPercentage = 80f
        dataSet.valueLinePart1Length = 0.2f
        dataSet.valueLinePart2Length = 0.4f
        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

        dataSet.colors = ColorTemplate.MATERIAL_COLORS.asList()

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(chart))
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)
        chart.data = data

        // undo all highlights
        chart.highlightValues(null)

        chart.invalidate()
    }

    /**
     * Build all chart entries
     */
    private fun buildEntries(categoryOverview: List<CategoryMovementOverview>): List<PieEntry> {

        return categoryOverview.map {
            PieEntry(it.totalAmount?.toFloat() ?: 0f, it.category.name)
        }
    }
}