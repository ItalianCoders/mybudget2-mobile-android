/*
 * Project: mybudget2-mobile-android
 * File: RetrofitBuilder.kt
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
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import it.italiancoders.mybudget.SessionData
import it.italiancoders.mybudget.app.AppConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

object RetrofitBuilder {

    var baseUrlPublic = AppConstants.REST_API_BASE_URL_PUBLIC
    var baseUrl = AppConstants.REST_API_BASE_URL

    var socketFactory: SSLSocketFactory? = null
    var trustManager: X509TrustManager? = null

    val client: Retrofit by lazy {
        Retrofit.Builder().client(httpClientBuilder.build()).baseUrl(baseUrlPublic)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    private val httpClientBuilder: OkHttpClient.Builder
        get() {
            val builder = OkHttpClient.Builder().connectTimeout(50, TimeUnit.SECONDS).writeTimeout(
                50,
                TimeUnit.SECONDS
            ).readTimeout(50, TimeUnit.SECONDS)

            if (socketFactory != null && trustManager != null) {
                builder.sslSocketFactory(socketFactory!!, trustManager!!)
            }

            return builder
        }

    fun getSecureClient(context: Context): Retrofit {

        val httpClient = httpClientBuilder
        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(
                    "x-access-token",
                    SessionData.session?.accessToken.orEmpty()
                )
                .addHeader("Accept-Language", SessionData.session?.locale.orEmpty())
                .build()
            chain.proceed(request)
        }
        httpClient.addInterceptor(RefreshTokenInterceptor(context))

        return Retrofit.Builder().client(httpClient.build()).baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }
}