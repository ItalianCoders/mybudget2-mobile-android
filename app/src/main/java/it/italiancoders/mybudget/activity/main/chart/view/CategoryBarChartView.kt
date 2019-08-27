/*
 * Project: mybudget2-mobile-android
 * File: CategoryBarChartView.kt
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

package it.italiancoders.mybudget.activity.main.chart.view

import android.content.Context
import android.util.AttributeSet
import com.github.mikephil.charting.charts.BarChart
import it.italiancoders.mybudget.activity.main.chart.manager.CategoryBarChartManager
import it.italiancoders.mybudget.rest.models.CategoryMovementOverview
import java.math.BigDecimal

/**
 * @author fattazzo
 *         <p/>
 *         date: 26/08/19
 */
class CategoryBarChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ChartView<BarChart>(context, attrs, defStyleAttr) {

    override fun create(): BarChart = BarChart(context)

    override fun configure(chart: BarChart) {
        CategoryBarChartManager.configure(chart)
    }

    override fun updateChart(categoryOverview: List<CategoryMovementOverview>, total: BigDecimal) {
        CategoryBarChartManager.setData(chart, categoryOverview, total)
    }
}