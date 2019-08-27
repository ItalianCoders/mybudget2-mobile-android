/*
 * Project: mybudget2-mobile-android
 * File: ChartView.kt
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
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.github.mikephil.charting.charts.Chart
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.rest.models.CategoryMovementOverview
import java.math.BigDecimal

/**
 * @author fattazzo
 *         <p/>
 *         date: 26/08/19
 */
abstract class ChartView<T : Chart<*>> : LinearLayout {

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView()
    }

    val chart: T by lazy { create() }

    val noDataLabel: TextView by lazy { TextView(context) }

    private fun initView() {

        configure(chart)

        addView(chart, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        addView(noDataLabel, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))

        chart.visibility = View.GONE
        noDataLabel.gravity = Gravity.CENTER
        noDataLabel.text = context.resources.getString(R.string.no_data_for_period)

        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    fun update(categoryOverview: List<CategoryMovementOverview>, total: BigDecimal) {

        updateChart(categoryOverview, total)

        chart.visibility = if (categoryOverview.isEmpty()) View.GONE else View.VISIBLE
        noDataLabel.visibility = if (categoryOverview.isEmpty()) View.VISIBLE else View.GONE
    }

    abstract fun updateChart(categoryOverview: List<CategoryMovementOverview>, total: BigDecimal)

    abstract fun create(): T

    abstract fun configure(chart: T)
}