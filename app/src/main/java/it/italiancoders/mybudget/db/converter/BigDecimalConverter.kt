/*
 * Project: mybudget2-mobile-android
 * File: BigDecimalConverter.kt
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

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Converter used for store and retrieve Long and BigDecimal values from DB.
 */
class BigDecimalConverter {

    /**
     * Create new BigDecimal from Long value.
     * BigDecimal = (Long / 100) scale 2 ROUND_HALF_UP
     *
     */
    @TypeConverter
    fun fromLong(value: Long?): BigDecimal? {
        return if (value == null) null else BigDecimal(value).divide(
            BigDecimal(100),
            2,
            BigDecimal.ROUND_HALF_UP
        )
    }

    /**
     * Create new Long from BigDecimal value.
     * Long = (BigDecimal * 100) precision 2 ROUND_HALF_UP
     */
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): Long? {

        val multiplier = BigDecimal(100).setScale(0,RoundingMode.UNNECESSARY)

        val result = value?.setScale(2,RoundingMode.DOWN)?.multiply(multiplier)

        return result?.setScale(2,RoundingMode.DOWN)?.longValueExact()
    }
}