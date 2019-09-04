/*
 * Project: mybudget2-mobile-android
 * File: BigDecimalStringConversionTest.kt
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

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Test
import java.math.BigDecimal

/**
 * @author fattazzo
 *         <p/>
 *         date: 04/09/19
 */
class BigDecimalStringConversionTest {

    @Test
    fun convertToString() {

        val currentText = "12.3"

        val oldValue = BigDecimal(12.3)
        val newValue = BigDecimal(134.6)

        val stringValue = BigDecimalStringConversion.toString(currentText,oldValue,newValue)
        MatcherAssert.assertThat(stringValue, `is`(BigDecimalStringConversion.getNumberFormat().format(newValue)))
    }

    @Test
    fun convertToStringNullValue() {

        val currentText = "12.3"

        val oldValue = BigDecimal(12.3)
        val newValue = null

        val stringValue = BigDecimalStringConversion.toString(currentText,oldValue,newValue)
        MatcherAssert.assertThat(stringValue, `is`(""))
    }

    @Test
    fun convertToBigDecimal() {

        val bigDecimalValue = BigDecimalStringConversion.toBigDecimal(null,null,"134.6")

        MatcherAssert.assertThat(bigDecimalValue?.toLong(), `is`(BigDecimalStringConversion.getNumberFormat().parse("134.6")?.toLong()))
    }
}