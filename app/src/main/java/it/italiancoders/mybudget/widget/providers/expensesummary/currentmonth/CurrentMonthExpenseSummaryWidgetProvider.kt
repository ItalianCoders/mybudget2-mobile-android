/*
 * Project: mybudget2-mobile-android
 * File: CurrentMonthExpenseSummaryWidgetProvider.kt
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

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import it.italiancoders.mybudget.widget.MyBudgetWidgetManager


/**
 * @author fattazzo
 *         <p/>
 *         date: 16/09/19
 */
class CurrentMonthExpenseSummaryWidgetProvider : AppWidgetProvider() {

    private val TAG = CurrentMonthExpenseSummaryWidgetProvider::class.simpleName

    override fun onReceive(context: Context?, intent: Intent?) {

        if (context != null && intent?.action == MyBudgetWidgetManager.ACTION_APPWIDGET_FORCE_REFRESH) {
            val intentUpdate = Intent(context, CurrentMonthExpenseSummaryService::class.java)
            intentUpdate.putExtra(
                AppWidgetManager.EXTRA_APPWIDGET_IDS,
                intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)
            )
            intentUpdate.putExtra(CurrentMonthExpenseSummaryService.EXTRA_FORCE_REFRESH, true)
            CurrentMonthExpenseSummaryService.enqueueWork(context, intentUpdate)
        } else {
            super.onReceive(context, intent)
        }
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        Log.d(TAG, "Widget update")

        context?.let {
            // To prevent any ANR timeouts, we perform the update in a service
            val intent = Intent(it, CurrentMonthExpenseSummaryService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
            intent.putExtra(CurrentMonthExpenseSummaryService.EXTRA_FORCE_REFRESH, false)
            CurrentMonthExpenseSummaryService.enqueueWork(it, intent)
        }
    }
}