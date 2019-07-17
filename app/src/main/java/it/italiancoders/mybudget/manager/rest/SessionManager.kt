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

package it.italiancoders.mybudget.manager.rest

import android.content.Context
import android.widget.Toast
import it.italiancoders.mybudget.manager.AuthManager
import it.italiancoders.mybudget.rest.api.RetrofitBuilder
import it.italiancoders.mybudget.rest.api.services.SessionRestService
import it.italiancoders.mybudget.rest.models.LoginRequest
import it.italiancoders.mybudget.rest.models.Session
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author fattazzo
 *         <p/>
 *         date: 17/07/19
 */
class SessionManager(val context: Context) {

    fun login(username: String, password: String, locale: String, onSuccessAction: ()->Unit, onFailureAction: ()->Unit) {

        val loginRequest = LoginRequest(username, username, locale)

        val sessionService = RetrofitBuilder.client.create(SessionRestService::class.java)
        sessionService.login(loginRequest).enqueue(object : Callback<Session> {
            override fun onResponse(call: Call<Session>, response: Response<Session>) {

                if (response.isSuccessful) {
                    AuthManager(context).setSession(response.body())
                    onSuccessAction.invoke()
                } else {
                    Toast.makeText(context, "ERRORE!", Toast.LENGTH_SHORT).show()
                    onFailureAction.invoke()
                }
            }

            override fun onFailure(call: Call<Session>?, t: Throwable?) {
                Toast.makeText(context, "ERRORE!!", Toast.LENGTH_SHORT).show()
                onFailureAction.invoke()
            }
        })
    }
}