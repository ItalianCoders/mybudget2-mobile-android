/*
 * Project: mybudget2-mobile-android
 * File: AmountStringConversionTest.kt
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
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert
import org.junit.Test
import java.math.BigDecimal
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 04/09/19
 */
class AmountStringConversionTest {

    @Test
    fun convertToString() {
        Locale.setDefault(Locale.ITALY)
        var string = AmountStringConversion.toString(null, BigDecimal.TEN)
        MatcherAssert.assertThat(string, containsString("€"))

        Locale.setDefault(Locale.US)
        string = AmountStringConversion.toString(null, BigDecimal.TEN)
        MatcherAssert.assertThat(string, containsString("$"))

        AmountStringConversion.currentSymbol = "AA"
        string = AmountStringConversion.toString(null, BigDecimal.TEN)
        MatcherAssert.assertThat(string, containsString("AA"))
    }

    @Test
    fun convertToStringNullValue() {

        Locale.setDefault(Locale.ITALY)
        val string = AmountStringConversion.toString(null, null)
        MatcherAssert.assertThat(string, `is`(""))
    }
}