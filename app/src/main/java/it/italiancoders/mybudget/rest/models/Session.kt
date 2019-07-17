/*
 * Project: mybudget2-mobile-android
 * File: Session.kt
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
 * Session
 */


class Session : Serializable {

    var userInfo: User? = null

    var locale: String? = null

    var environment: String? = null

    var accessToken: String? = null

    var refreshToken: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val session = other as Session?
        return this.userInfo == session!!.userInfo &&
                this.locale == session.locale &&
                this.environment == session.environment &&
                this.accessToken == session.accessToken &&
                this.refreshToken == session.refreshToken
    }

    override fun hashCode(): Int {
        return Objects.hash(userInfo, locale, environment, accessToken, refreshToken)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("class Session {\n")

        sb.append("    userInfo: ").append(toIndentedString(userInfo)).append("\n")
        sb.append("    locale: ").append(toIndentedString(locale)).append("\n")
        sb.append("    environment: ").append(toIndentedString(environment)).append("\n")
        sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n")
        sb.append("    refreshToken: ").append(toIndentedString(refreshToken)).append("\n")
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

