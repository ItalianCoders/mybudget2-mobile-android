/*
 * Project: mybudget2-mobile-android
 * File: AuthManager.kt
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

package it.italiancoders.mybudget.manager

import android.content.Context
import com.beust.klaxon.Klaxon
import it.italiancoders.mybudget.SessionData
import it.italiancoders.mybudget.rest.models.Session

/**
 * @author fattazzo
 *         <p/>
 *         date: 16/07/19
 */
class AuthManager(private val context: Context) {

    fun getLastSession(): Session? {

        val lastSessionJson = context.getSharedPreferences(AUTH_PREF_FILE, Context.MODE_PRIVATE).getString(
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

        context.getSharedPreferences(AUTH_PREF_FILE, Context.MODE_PRIVATE).edit().putString(ACCESS_TOKEN_KEY, sessionJson)
            .apply()
        SessionData.session = session
    }

    companion object {

        private const val AUTH_PREF_FILE = "auth_prefs"

        private const val ACCESS_TOKEN_KEY = "accessToken"
    }
}