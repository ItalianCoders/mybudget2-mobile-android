/*
 * Project: mybudget2-mobile-android
 * File: RegistrationUserInfoRestService.kt
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

import it.italiancoders.mybudget.rest.models.UserRegistrationInfo
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 23/08/19
 */
interface RegistrationUserInfoRestService {

    /**
     * Registrate anew User
     *
     * @param userRegistrationInfo user
     */
    @POST("registration-user")
    fun create(@Body userRegistrationInfo: UserRegistrationInfo): Call<Void>

    /**
     * Resend confirm registration mail
     *
     * @param username username of user
     */
    @POST("resend-confirm-registration-mail")
    suspend fun resend(@Query("username") username: String): Response<Void>

    /**
     * Confirm Registration
     *
     * @param token token for confirmation
     */
    @GET("confirm-registration/{token}")
    suspend fun confirm(@Path("token") token: String): Response<Void>
}