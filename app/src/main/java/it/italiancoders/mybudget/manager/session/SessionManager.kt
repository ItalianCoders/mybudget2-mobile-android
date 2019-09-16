/*
 * Project: mybudget2-mobile-android
 * File: SessionManager.kt
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

package it.italiancoders.mybudget.manager.session

import android.content.Context
import com.beust.klaxon.Klaxon
import it.italiancoders.mybudget.SessionData
import it.italiancoders.mybudget.manager.AbstractRestManager
import it.italiancoders.mybudget.rest.api.RetrofitBuilder
import it.italiancoders.mybudget.rest.api.services.SessionRestService
import it.italiancoders.mybudget.rest.models.LoginRequest
import it.italiancoders.mybudget.rest.models.Session
import it.italiancoders.mybudget.utils.OpenForTesting

/**
 * @author fattazzo
 *         <p/>
 *         date: 17/07/19
 */
@OpenForTesting
class SessionManager(context: Context) : AbstractRestManager(context) {

    fun login(username: String, password: String, locale: String): LoginResult {

        val loginRequest = LoginRequest(username, password, locale)

        val sessionService = RetrofitBuilder.client.create(SessionRestService::class.java)

        var errorCodeResult: Int? = null

        val sessionResult: Session? = try {
            val response = sessionService.login(loginRequest).execute()
            if (response.isSuccessful) {
                response.body()
            } else {
                errorCodeResult = response.code()
                null
            }
        } catch (e: Exception) {
            errorCodeResult = 400
            null
        }

        sessionResult?.let { setSession(it) }

        return LoginResult(sessionResult, errorCodeResult)
    }

    fun getLastSession(): Session? {

        val lastSessionJson =
            context.getSharedPreferences(AUTH_PREF_FILE, Context.MODE_PRIVATE).getString(
                ACCESS_TOKEN_KEY, ""
            )

        return try {
            Klaxon().parse<Session>(lastSessionJson!!)
        } catch (e: Exception) {
            null
        }
    }

    fun setSession(session: Session?) {

        val sessionJson = Klaxon().toJsonString(session ?: "")

        context.getSharedPreferences(AUTH_PREF_FILE, Context.MODE_PRIVATE).edit()
            .putString(ACCESS_TOKEN_KEY, sessionJson)
            .apply()
        SessionData.session = session
    }

    fun removeSession() {

        context.getSharedPreferences(AUTH_PREF_FILE, Context.MODE_PRIVATE).edit().clear()
            .apply()

        SessionData.session = null
    }

    companion object {

        private const val AUTH_PREF_FILE = "auth_prefs"

        private const val ACCESS_TOKEN_KEY = "accessToken"
    }
}