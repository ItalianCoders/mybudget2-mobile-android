/*
 * Project: mybudget2-mobile-android
 * File: ParametriRicerca.kt
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

package it.italiancoders.mybudget.manager.movements

import it.italiancoders.mybudget.app.AppConstants

/**
 * @author fattazzo
 *         <p/>
 *         date: 26/07/19
 */
data class ParametriRicerca(
    val year: Int,
    val month: Int,
    val day: Int?,
    val week: Int?,
    val categoryId: Long?,
    val page: Int,
    val size: Int,
    val sort: Array<String>?
) {

    constructor(year: Int,month: Int,day: Int?,week: Int?,categoryId: Long?): this(year,month,day,week,categoryId,0,
        AppConstants.DEFAULT_PAGE_SIZE,null)

    constructor(year: Int,month: Int): this(year,month,null,null,null,0,
        AppConstants.DEFAULT_PAGE_SIZE,null)

    constructor(year: Int,month: Int,week: Int): this(year,month,null,week,null,0,
        AppConstants.DEFAULT_PAGE_SIZE,null)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ParametriRicerca) return false

        if (year != other.year) return false
        if (month != other.month) return false
        if (day != other.day) return false
        if (week != other.week) return false
        if (categoryId != other.categoryId) return false
        if (page != other.page) return false
        if (size != other.size) return false
        if (sort != null) {
            if (other.sort == null) return false
            if (!sort.contentEquals(other.sort)) return false
        } else if (other.sort != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = year
        result = 31 * result + month
        result = 31 * result + (day ?: 0)
        result = 31 * result + (week ?: 0)
        result = 31 * result + (categoryId?.hashCode() ?: 0)
        result = 31 * result + page
        result = 31 * result + size
        result = 31 * result + (sort?.contentHashCode() ?: 0)
        return result
    }
}