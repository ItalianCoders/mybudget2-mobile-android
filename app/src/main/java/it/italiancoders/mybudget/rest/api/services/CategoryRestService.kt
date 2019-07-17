/*
 * Project: mybudget2-mobile-android
 * File: CategoryRestService.kt
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

import it.italiancoders.mybudget.rest.models.Category
import retrofit2.Call
import retrofit2.http.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 16/07/19
 */
interface CategoryRestService {

    /**
     * Create a new category
     *
     * @param category The category to create
     * @return The Category object created
     */
    @POST("/categories")
    fun create(@Body category: Category): Call<Category>

    /**
     * Obtain the categories to use to perform new movement
     *
     * @return All categories loaded
     */
    @GET("/categories")
    fun loadAll(): Call<List<Category>>

    /**
     * Obtain the categoriy to use to perform new movement
     *
     * @param id category id to load
     * @return The category loaded
     */
    @GET("/categories/{id}")
    fun load(@Path("id") id: Int): Call<Category>

    /**
     * Update an existing category
     *
     * @param id id to update
     * @param category new Category data
     */
    @PUT("/categories/{id}")
    fun update(@Path("id") id: Int, @Body category: Category)

    /**
     * Delete an existing category
     *
     * @param category id to delete
     */
    @DELETE("/categories/{id}")
    fun delete(@Path("id") id: Int)
}