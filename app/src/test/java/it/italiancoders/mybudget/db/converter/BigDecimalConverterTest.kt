/*
 * Project: mybudget2-mobile-android
 * File: BigDecimalConverterTest.kt
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

package it.italiancoders.mybudget.db.converter

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.math.BigDecimal

/**
 * @author fattazzo
 *         <p/>
 *         date: 04/09/19
 */
class BigDecimalConverterTest {

    @Test
    fun fromLong() {

        val converter = BigDecimalConverter()

        assertThat(converter.fromLong(12345)!!.toLong(), `is`(123.45.toLong()))
        assertThat(converter.fromLong(12)!!.toLong(), `is`(0.12.toLong()))
        assertThat(converter.fromLong(null), `is`(nullValue()))

        assertThat(converter.fromLong(-12345)!!.toLong(), `is`((-123.45).toLong()))
        assertThat(converter.fromLong(-12)!!.toLong(), `is`((-0.12).toLong()))
    }

    @Test
    fun fromBigDecimal() {

        val converter = BigDecimalConverter()

        assertThat(converter.fromBigDecimal(BigDecimal(123.45)), `is`(12345.toLong()))
        assertThat(converter.fromBigDecimal(BigDecimal(123.456)), `is`(12345.toLong()))
        assertThat(converter.fromBigDecimal(BigDecimal(123)), `is`(12300.toLong()))
        assertThat(converter.fromBigDecimal(BigDecimal(123.4)), `is`(12340.toLong()))
        assertThat(converter.fromBigDecimal(null), `is`(nullValue()))

        assertThat(converter.fromBigDecimal(BigDecimal(-123.45)), `is`((-12345).toLong()))
        assertThat(converter.fromBigDecimal(BigDecimal(-123.456)), `is`((-12345).toLong()))
        assertThat(converter.fromBigDecimal(BigDecimal(-123)), `is`((-12300).toLong()))
        assertThat(converter.fromBigDecimal(BigDecimal(-123.4)), `is`((-12340).toLong()))
    }
}