/*
 * Project: mybudget2-mobile-android
 * File: MyBudgetApplication.kt
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

package it.italiancoders.mybudget.app

import android.app.Application
import it.italiancoders.mybudget.SessionData
import it.italiancoders.mybudget.app.component.AppComponent
import it.italiancoders.mybudget.app.component.DaggerAppComponent
import it.italiancoders.mybudget.app.module.AppModule
import it.italiancoders.mybudget.app.module.CategoriesModule
import it.italiancoders.mybudget.app.module.MovementsModule
import it.italiancoders.mybudget.app.module.SessionModule
import it.italiancoders.mybudget.utils.connection.ConnectivityLiveData

/**
 * @author fattazzo
 *         <p/>
 *         date: 16/07/19
 */
open class MyBudgetApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        initAppComponent()

        SessionData.networkAvailable = ConnectivityLiveData(this) //.observeForever { SessionData.networkAvailable.postValue(it) }
    }


    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .categoriesModule(CategoriesModule())
            .movementsModule(MovementsModule())
            .sessionModule(SessionModule())
            .build()
    }
}