/*
 * Project: mybudget2-mobile-android
 * File: Page.kt
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

import java.io.Serializable
import java.util.*

/**
 * Page
 */

class Page : Serializable {

    var isFirst: Boolean? = null

    var isLast: Boolean? = null

    var size: Int? = null

    var totalElements: Int? = null

    var totalPages: Int? = null

    var number: Int? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val page = other as Page?
        return this.isFirst == page!!.isFirst &&
                this.isLast == page.isLast &&
                this.size == page.size &&
                this.totalElements == page.totalElements &&
                this.totalPages == page.totalPages &&
                this.number == page.number
    }

    override fun hashCode(): Int {
        return Objects.hash(isFirst, isLast, size, totalElements, totalPages, number)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("class Page {\n")

        sb.append("    first: ").append(toIndentedString(isFirst)).append("\n")
        sb.append("    last: ").append(toIndentedString(isLast)).append("\n")
        sb.append("    size: ").append(toIndentedString(size)).append("\n")
        sb.append("    totalElements: ").append(toIndentedString(totalElements)).append("\n")
        sb.append("    totalPages: ").append(toIndentedString(totalPages)).append("\n")
        sb.append("    number: ").append(toIndentedString(number)).append("\n")
        sb.append("}")
        return sb.toString()
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private fun toIndentedString(o: Any?): String {
        return o?.toString()?.replace("\n", "\n    ") ?: "null"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}

