/*
 * Project: mybudget2-mobile-android
 * File: ExpenseSummaryRestService.kt
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

import it.italiancoders.mybudget.rest.models.ExpenseSummary
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author fattazzo
 *         <p/>
 *         date: 30/08/19
 */
interface ExpenseSummaryRestService {

    /**
     * Return the expense summary
     *
     * @param year The year of movement
     * @param month The month of movement
     * @param day The day of movement
     * @param week The week number
     * @param category The id of category
     */
    @GET("expense-summary")
    fun getExpenseSummary(
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int?,
        @Query("week") week: Int?,
        @Query("category") category: Int?
    ): Call<ExpenseSummary>
}