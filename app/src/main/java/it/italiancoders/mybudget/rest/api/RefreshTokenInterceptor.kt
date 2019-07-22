/*
 * Project: mybudget2-mobile-android
 * File: RefreshTokenInterceptor.kt
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

package it.italiancoders.mybudget.rest.api

import android.content.Context
import it.italiancoders.mybudget.SessionData
import it.italiancoders.mybudget.manager.AuthManager
import it.italiancoders.mybudget.rest.api.services.SessionRestService
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author fattazzo
 *         <p/>
 *         date: 19/07/19
 */
class RefreshTokenInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val initialResponse = chain.proceed(originalRequest)

        when {
            initialResponse.code() == 498 -> {
                val responseNewTokenLoginModel = RetrofitBuilder.client.create(SessionRestService::class.java)
                    .refresh(SessionData.session?.refreshToken.orEmpty()).execute()

                return when {
                    responseNewTokenLoginModel.code() != 200 -> {
                        SessionData.session = null
                        initialResponse
                    }
                    else -> {
                        SessionData.session = responseNewTokenLoginModel.body()
                        AuthManager(context).setSession(responseNewTokenLoginModel.body())

                        val newAuthenticationRequest = originalRequest.newBuilder()
                            .addHeader("x-access-token", SessionData.session?.accessToken.orEmpty())
                            .build()
                        chain.proceed(newAuthenticationRequest)
                    }
                }
            }
            else -> return initialResponse
        }
    }
}