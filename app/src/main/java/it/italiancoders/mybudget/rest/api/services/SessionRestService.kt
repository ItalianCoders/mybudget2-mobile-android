/*
 * Project: mybudget2-mobile-android
 * File: SessionRestService.kt
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

package it.italiancoders.mybudget.rest.api.services

import it.italiancoders.mybudget.rest.models.LoginRequest
import it.italiancoders.mybudget.rest.models.Session
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path


/**
 * @author fattazzo
 *         <p/>
 *         date: 16/07/19
 */
interface SessionRestService {

    /**
     * Obtain AccessToken, RefreshToken and the user session
     *
     * @param loginRequest The login request data
     * @return The session object
     */
    @POST("session")
    fun login(@Body loginRequest: LoginRequest): Call<Session>

    /**
     * Obtain a new AccessToken and user session using RefreshToken
     *
     * @param refreshToken The refresh token
     * @return The session object
     */
    @POST("session/refresh/{refreshToken}")
    fun refresh(@Path("refreshToken") refreshToken: String): Call<Session>
}