/*
 * Project: mybudget2-mobile-android
 * File: SessionHandler.kt
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

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import it.italiancoders.mybudget.SessionData
import it.italiancoders.mybudget.activity.login.LoginActivity
import it.italiancoders.mybudget.app.MyBudgetApplication
import it.italiancoders.mybudget.manager.session.SessionManager
import javax.inject.Inject

/**
 * @author fattazzo
 *         <p/>
 *         date: 16/09/19
 */
class SessionHandler {

    @Inject
    lateinit var sessionManager: SessionManager

    fun setAppLastSession(activity: AppCompatActivity, openLoginActivity: Boolean) {

        (activity.application as MyBudgetApplication).appComponent.inject(this)

        SessionData.session = sessionManager.getLastSession()
        if (SessionData.session == null && openLoginActivity) {
            val intent = Intent(activity.applicationContext, LoginActivity::class.java)
            activity.startActivityForResult(intent, LoginActivity.REQUEST_CODE_LOGIN)
        }
    }
}