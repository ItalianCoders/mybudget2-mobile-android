/*
 * Project: mybudget2-mobile-android
 * File: CurrentMonthExpenseSummaryService.kt
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

package it.italiancoders.mybudget.widget.providers.expensesummary.currentmonth

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.JobIntentService
import com.crashlytics.android.Crashlytics
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.SessionData
import it.italiancoders.mybudget.app.MyBudgetApplication
import it.italiancoders.mybudget.databinding.converters.AmountStringConversion
import it.italiancoders.mybudget.manager.AppPreferenceManager
import it.italiancoders.mybudget.manager.expensesummary.ExpenseSummaryManager
import it.italiancoders.mybudget.manager.movements.ParametriRicerca
import it.italiancoders.mybudget.manager.session.SessionManager
import it.italiancoders.mybudget.rest.models.ExpenseSummary
import it.italiancoders.mybudget.widget.providers.AbstractWidgetService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


/**
 * @author fattazzo
 *         <p/>
 *         date: 17/09/19
 */
class CurrentMonthExpenseSummaryService : JobIntentService(), AbstractWidgetService {

    companion object {

        private const val JOB_ID = 1

        private val TAG = CurrentMonthExpenseSummaryService::class.simpleName

        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, CurrentMonthExpenseSummaryService::class.java, JOB_ID, work)
        }
    }

    @Inject
    lateinit var expenseSummaryManager: ExpenseSummaryManager

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onHandleWork(intent: Intent) {
        Log.d(TAG, "Enter onStartCommand")

        (applicationContext as MyBudgetApplication).appComponent.inject(this)

        val appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)

        appWidgetIds?.forEach {

            SessionData.session = sessionManager.getLastSession()

            when {
                SessionData.session == null -> updateViewsForNoSession(this, it)
                SessionData.session != null -> updateViewsWithSummary(this, it)
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun updateViewsWithSummary(context: Context, appWidgetId: Int) {
        val cal = Calendar.getInstance()
        val parametriRicerca =
            ParametriRicerca(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1)

        Log.d(TAG, "Parametri: $parametriRicerca")

        /**
         * When the widget is added, if the application is not started in the background, the API call generates a SocketTimeoutException.
         *
         * See <a href="https://github.com/ItalianCoders/mybudget2-mobile-android/issues/12">on github repository</a>
         *
         */
        val result = expenseSummaryManager.getExpenseSummary(parametriRicerca, false)
        Log.d(TAG, "Summary in result: ${result.value != null}")

        when {
            result.exception is SocketTimeoutException -> {
                Crashlytics.log(Log.ERROR, TAG, "Timeout exception in getExpenseSummary")
                updateViewsForOpenAppForInitialize(context, appWidgetId)
            }
            result.value != null -> {
                val expenseSummary = result.value
                Log.d(TAG, "create chart")
                val chart = createChart(context, expenseSummary)
                Log.d(TAG, "chart created")

                val b = Bitmap.createBitmap(
                    chart.measuredWidth,
                    chart.measuredHeight,
                    Bitmap.Config.ARGB_8888
                )
                val c = Canvas(b)
                chart.layout(0, 0, chart.measuredWidth, chart.measuredHeight)
                chart.draw(c)
                Log.d(TAG, "Bitmap created")

                // Get the layout for the App Widget
                val views = RemoteViews(packageName, R.layout.widget_current_month_expense_summary)
                Log.d(TAG, "Remote view layout ok")

                val monthDescription =
                    SimpleDateFormat("MMMM", Locale.getDefault()).format(cal.time).capitalize()
                val amountString = AmountStringConversion.toString(
                    context,
                    (expenseSummary.totalAmount ?: 0.0).toBigDecimal()
                )
                Log.d(TAG, "Title: $monthDescription: $amountString")
                views.setTextViewText(R.id.title_view, "$monthDescription: $amountString")

                views.setBitmap(R.id.chart_image, "setImageBitmap", b)
                Log.d(TAG, "setImageBitmap to image view")
                views.setOnClickPendingIntent(R.id.chart_image, createOpenAppIntent(context))
                Log.d(TAG, "set open app intent to image view")

                // attach an on-click listener to the widget sync view
                views.setOnClickPendingIntent(
                    R.id.widget_sync,
                    createRefreshIntent(context, appWidgetId)
                )
                Log.d(TAG, "set refresh intent to widget sync view")

                val appWidgetManager = AppWidgetManager.getInstance(context)
                appWidgetManager.updateAppWidget(appWidgetId, views)
                Log.d(TAG, "updateAppWidget send")
            }
        }
    }

    private fun createChart(context: Context, expenseSummary: ExpenseSummary): BarChart {

        val chart: BarChart = runBlocking(Dispatchers.Main) {
            BarChart(context)
        }

        // Add default measure
        chart.minimumWidth = 600
        chart.minimumHeight = 432
        chart.viewPortHandler.setChartDimens(600f, 432f)
        chart.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        chart.layout(0, 0, chart.measuredWidth, chart.measuredHeight)

        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)
        chart.description.isEnabled = false
        chart.setMaxVisibleValueCount(60)
        chart.setPinchZoom(false)
        chart.setDrawGridBackground(false)

        val leftAxis = chart.axisLeft
        leftAxis.setLabelCount(5, false)
        leftAxis.valueFormatter = DefaultAxisValueFormatter(0)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 5f
        leftAxis.axisMinimum = 0f
        leftAxis.textSize = 8f
        leftAxis.textColor = Color.WHITE

        chart.xAxis.isEnabled = false
        chart.axisRight.isEnabled = false

        val l = chart.legend
        l.isEnabled = true
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.textSize = 8f
        l.xEntrySpace = 4f
        l.textColor = Color.WHITE
        l.isWordWrapEnabled = true

        // Data
        setChartData(chart, expenseSummary)

        return chart
    }

    private fun setChartData(
        chart: BarChart,
        expenseSummary: ExpenseSummary
    ) {
        val dataSets = mutableListOf<BarDataSet>()

        val colors = AppPreferenceManager.getChartColorTheme(chart.context).colors.toList()

        expenseSummary.categoryOverview.orEmpty().mapIndexed { index, overview ->
            val entry = BarEntry(
                index.toFloat(),
                overview.totalAmount?.toFloat()!!, overview.category.name
            )

            val set1 = BarDataSet(listOf(entry), overview.category.name)
            set1.setDrawIcons(false)
            set1.setDrawValues(true)

            set1.colors = if (index <= colors.size) listOf(colors[index]) else listOf(colors[0])
            dataSets.add(set1)
        }


        val data = BarData(dataSets.toList())
        data.setValueTextSize(10f)
        data.setDrawValues(false)
        data.barWidth = 0.9f
        data.isHighlightEnabled = false

        chart.data = data


        chart.invalidate()
    }
}