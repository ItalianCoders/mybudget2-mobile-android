/*
 * Project: mybudget2-mobile-android
 * File: Movement.kt
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

import it.italiancoders.mybudget.db.entity.Movement
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

/**
 * Movement
 */
data class Movement(
    val id: Long?,
    var amount: BigDecimal,
    var category: Category,
    var executedAt: String,
    var note: String?
) {

    var executedAtDate: Date? = null
        get() =
            try {
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(
                    executedAt
                )
            } catch (e: Exception) {
                null
            }
        set(value) {
            field = value
            executedAt = try {
                SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                    Locale.getDefault()
                ).format(value!!)
            } catch (e: Exception) {
                ""
            }
        }


    constructor() : this(null, BigDecimal.ZERO, Category(), "", null)

    fun toEntity(): Movement =
        Movement(
            this@Movement.id!!,
            this@Movement.amount,
            this@Movement.category.toEntity(),
            this@Movement.executedAt,
            this@Movement.note
        )
}

