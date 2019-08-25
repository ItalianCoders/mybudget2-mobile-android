/*
 * Project: mybudget2-mobile-android
 * File: UserValidationRules.kt
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

package it.italiancoders.mybudget.activity.login

import android.text.Editable
import android.util.Patterns

object UserValidationRules {

    @JvmStatic
    var FIRSTNAME: Rule = object : Rule {
        override fun isValid(s: Editable?): Boolean {
            // Check length
            return s?.toString().orEmpty().length >= 5

        }
    }

    @JvmStatic
    var LASTNAME: Rule = object : Rule {
        override fun isValid(s: Editable?): Boolean {
            // Check length
            return s?.toString().orEmpty().length >= 5

        }
    }

    @JvmStatic
    var USERNAME: Rule = object : Rule {
        override fun isValid(s: Editable?): Boolean {
            // Check length
            return s?.toString().orEmpty().length >= 5

        }
    }

    @JvmStatic
    var PASSWORD: Rule = object : Rule {
        override fun isValid(s: Editable?): Boolean {
            // Check if contains at least one upper case char
            val upperCaseChar = """.*[A-Z].*""".toRegex().containsMatchIn(s?.toString().orEmpty())
            // Check if contains at least one digit char
            val digitChar = """.*[0-9].*""".toRegex().containsMatchIn(s?.toString().orEmpty())
            // Check length
            val length = s?.toString().orEmpty().length >= 8
            return length && upperCaseChar && digitChar
        }
    }

    @JvmStatic
    var EMAIL: Rule = object : Rule {
        override fun isValid(s: Editable?): Boolean {
            // Check with pattern
            return Patterns.EMAIL_ADDRESS.matcher(s?.toString().orEmpty()).matches()

        }
    }

    interface Rule {
        fun isValid(s: Editable?): Boolean
    }
}