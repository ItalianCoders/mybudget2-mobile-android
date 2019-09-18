/*
 * Project: mybudget2-mobile-android
 * File: AbstractWidgetService.kt
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

package it.italiancoders.mybudget.widget.providers

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.main.MainActivity
import it.italiancoders.mybudget.widget.providers.expensesummary.currentmonth.CurrentMonthExpenseSummaryWidgetProvider

/**
 * @author fattazzo
 *         <p/>
 *         date: 17/09/19
 */
interface AbstractWidgetService {

    /**
     * Create intent for widget refresh action
     */
    fun createRefreshIntent(context: Context, appWidgetId: Int): PendingIntent? {
        val intent = Intent(context, CurrentMonthExpenseSummaryWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(appWidgetId))
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    /**
     * Create intent for open application action
     */
    fun createOpenAppIntent(context: Context): PendingIntent? {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun updateViewsForNoSession(context: Context, appWidgetId: Int) {

        val views = RemoteViews(context.packageName, R.layout.widget_error)

        views.setTextViewText(
            R.id.title_view,
            "Sessione utente scaduta o non presente.\nClicca qui per eseguire il login"
        )
        views.setOnClickPendingIntent(R.id.title_view, createOpenAppIntent(context))

        views.setViewVisibility(R.id.widget_sync, View.GONE)

        val appWidgetManager = AppWidgetManager.getInstance(context)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    fun updateViewsForOpenAppForInitialize(context: Context, appWidgetId: Int) {

        val views = RemoteViews(context.packageName, R.layout.widget_error)

        views.setTextViewText(
            R.id.title_view,
            "E' necessario lanciare l'app per inizializzare il widget\nSe necessario eseguire successivamente un refresh"
        )
        views.setOnClickPendingIntent(R.id.title_view, createOpenAppIntent(context))

        views.setOnClickPendingIntent(R.id.widget_sync, createRefreshIntent(context, appWidgetId))

        val appWidgetManager = AppWidgetManager.getInstance(context)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}