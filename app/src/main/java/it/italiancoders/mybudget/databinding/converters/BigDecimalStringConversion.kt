/*
 * Project: mybudget2-mobile-android
 * File: BigDecimalStringConversion.kt
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

package it.italiancoders.mybudget.databinding.converters

import android.widget.TextView
import androidx.databinding.InverseMethod
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException

object BigDecimalStringConversion {

    @JvmStatic
    @InverseMethod("toBigDecimal")
    fun toString(view: TextView, oldValue: BigDecimal, value: BigDecimal): String {
        try {
            // Don't return a different value if the parsed value// doesn't change
            val inView = view.text.toString().replace('.', ',')
            val parsed = BigDecimal(getNumberFormat().parse(inView)!!.toDouble())

            if (parsed.compareTo(value) == 0) {
                return view.text.toString()
            }
        } catch (e: ParseException) {
            // Old number was broken
        }

        return getNumberFormat().format(value)
    }

    @JvmStatic
    fun toBigDecimal(view: TextView, oldValue: BigDecimal, value: String): BigDecimal {
        return try {
            BigDecimal(getNumberFormat().parse(value.replace('.', ','))!!.toDouble())
        } catch (e: ParseException) {
            oldValue
        }
    }

    private fun getNumberFormat(): NumberFormat {
        val format = DecimalFormat("0.00")
        format.isGroupingUsed = false
        return format
    }
}