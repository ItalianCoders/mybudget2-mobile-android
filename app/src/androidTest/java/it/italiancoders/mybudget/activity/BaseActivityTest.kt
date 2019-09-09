/*
 * Project: mybudget2-mobile-android
 * File: BaseActivityTest.kt
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

package it.italiancoders.mybudget.activity

import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import it.italiancoders.mybudget.app.TestApplication
import it.italiancoders.mybudget.app.component.TestAppComponent
import it.italiancoders.mybudget.manager.categories.CategoriesManager
import it.italiancoders.mybudget.manager.expensesummary.ExpenseSummaryManager
import it.italiancoders.mybudget.manager.movements.MovementsManager
import it.italiancoders.mybudget.manager.registrationuserinfo.RegistrationUserInfoManager
import it.italiancoders.mybudget.manager.session.SessionManager
import it.italiancoders.mybudget.rest.models.Session
import it.italiancoders.mybudget.rest.models.User
import it.italiancoders.mybudget.utils.background
import it.italiancoders.mybudget.utils.io
import it.italiancoders.mybudget.utils.ui
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito.`when`
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author fattazzo
 *         <p/>
 *         date: 28/08/19
 */
open class BaseActivityTest {

    @Inject
    lateinit var expenseSummaryManager: ExpenseSummaryManager
    @Inject
    lateinit var categoriesManager: CategoriesManager
    @Inject
    lateinit var movementsManager: MovementsManager
    @Inject
    lateinit var sessionManager: SessionManager
    @Inject
    lateinit var registrationUserInfoManager: RegistrationUserInfoManager

    @Rule
    @JvmField
    val countingTaskExecutorRule = CountingTaskExecutorRule()

    fun drain() {
        countingTaskExecutorRule.drainTasks(20, TimeUnit.SECONDS)
    }

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        (getTargetApplication().appComponent as TestAppComponent).inject(this)

        unconfinifyTestScope()

        if (useDefaultSession())
            `when`(sessionManager.getLastSession()).thenReturn(getDefaultSession())
    }

    @ExperimentalCoroutinesApi
    private fun unconfinifyTestScope() {
        ui = Dispatchers.Unconfined
        io = Dispatchers.Unconfined
        background = Dispatchers.Unconfined
    }

    fun useDefaultSession(): Boolean = true

    private fun getTargetApplication(): TestApplication =
        InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestApplication

    private fun getDefaultSession(): Session = Session().apply {
        accessToken = "aaaaaa"
        locale = "it"
        refreshToken = "bbbbbbb"
        userInfo = User().apply {
            username = "defaultUser"
            email = "default@user.com"
        }
    }
}

