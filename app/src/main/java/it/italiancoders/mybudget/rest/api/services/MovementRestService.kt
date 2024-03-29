/*
 * Project: mybudget2-mobile-android
 * File: MovementRestService.kt
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

import it.italiancoders.mybudget.rest.models.Movement
import it.italiancoders.mybudget.rest.models.MovementListPage
import retrofit2.Call
import retrofit2.http.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 16/07/19
 */
interface MovementRestService {

    /**
     * Create a new movement
     *
     * @param movement The Movement to create
     * @return The Movement object created
     */
    @POST("movements")
    fun create(@Body movement: Movement): Call<Void>

    /**
     * Returns paged list of Movements
     *
     * @param year The year of movement
     * @param month The month of movement
     * @param day The day of movement
     * @param categoryId The id of category
     * @param page The page number (zero-based index)
     * @param size The number of records per page
     * @param sort The fields to sort (example sort=field1,desc&sort=field2,asc)
     * @return The paged Movement list object
     */
    @GET("movements")
    fun query(
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int?,
        @Query("category") categoryId: Int?,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: Array<String>?
    ): Call<MovementListPage>

    /**
     * Obtain the movement
     *
     * @param id movement id to load
     * @return The Movement loaded
     */
    @GET("movements/{id}")
    fun load(@Path("id") id: Int): Call<Movement>

    /**
     * Update an existing movement
     *
     * @param id movement id to update
     * @param movement new Movement data
     */
    @PUT("movements/{id}")
    fun update(@Path("id") id: Int, @Body movement: Movement): Call<Void>

    /**
     * Delete an existing movement
     *
     * @param id movement id to delete
     */
    @DELETE("movements/{id}")
    fun delete(@Path("id") id: Int): Call<Void>
}