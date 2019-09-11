/*
 * Project: mybudget2-mobile-android
 * File: SessionMockData.kt
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

package it.italiancoders.mybudget.mocks.data

import com.nhaarman.mockitokotlin2.doReturn
import it.italiancoders.mybudget.manager.session.LoginResult
import it.italiancoders.mybudget.manager.session.SessionManager
import it.italiancoders.mybudget.mocks.config.SessionConfig
import it.italiancoders.mybudget.rest.models.Session
import org.mockito.Mockito.`when`
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 11/09/19
 */
object SessionMockData : AbstractMockData() {

    fun mock_login_ok(
        sessionManager: SessionManager,
        username: String,
        password: String,
        locale: Locale
    ) {

        `when`(sessionManager.login(username, password, locale.language))
            .doReturn(
                LoginResult(
                    fromJsonFile(SessionConfig.DATA_SESSION_OK, Session::class),
                    null
                )
            )
    }

    fun mock_login_invalid_ok(
        sessionManager: SessionManager,
        username: String,
        password: String,
        locale: Locale
    ) {

        `when`(sessionManager.login(username, password, locale.language))
            .doReturn(LoginResult(null,400))
    }

    fun mock_login_not_activated_ok(
        sessionManager: SessionManager,
        username: String,
        password: String,
        locale: Locale
    ) {

        `when`(sessionManager.login(username, password, locale.language))
            .doReturn(LoginResult(null,403))
    }
}