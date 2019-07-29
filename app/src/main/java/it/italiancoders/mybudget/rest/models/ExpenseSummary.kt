/*
 * Project: mybudget2-mobile-android
 * File: ExpenseSummary.kt
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

package it.italiancoders.mybudget.rest.models


/**
 *
 * @param totalAmount
 * @param categoryOverview
 * @param lastMovements
 */
data class ExpenseSummary(
    val totalAmount: Double? = null,
    val categoryOverview: Array<CategoryMovementOverview>?,
    val lastMovements: Array<Movement>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ExpenseSummary) return false

        if (totalAmount != other.totalAmount) return false
        if (categoryOverview != null) {
            if (other.categoryOverview == null) return false
            if (!categoryOverview.contentEquals(other.categoryOverview)) return false
        } else if (other.categoryOverview != null) return false
        if (!lastMovements.contentEquals(other.lastMovements)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = totalAmount?.hashCode() ?: 0
        result = 31 * result + (categoryOverview?.contentHashCode() ?: 0)
        result = 31 * result + lastMovements.contentHashCode()
        return result
    }

}

