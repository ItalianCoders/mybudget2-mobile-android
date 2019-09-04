/*
 * Project: mybudget2-mobile-android
 * File: ArrowDownShape.kt
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

package it.italiancoders.mybudget.tutorial.shape

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import com.takusemba.spotlight.shape.Shape
import it.italiancoders.mybudget.R

/**
 * @author fattazzo
 *         <p/>
 *         date: 02/08/19
 */
class ArrowDownShape(private val context: Context, private val radius: Float) : Shape {
    override fun draw(canvas: Canvas?, point: PointF, value: Float, paint: Paint) {
        val halfWidth = radius / 2 //* value
        val halfHeight = radius / 2 //* value
        val left = point.x - halfWidth
        val top = point.y - halfHeight
        val right = point.x + halfWidth
        val bottom = point.y + halfHeight

        val drawable = context.resources.getDrawable(R.drawable.ic_arrow_down_tutorial, null)
        drawable.setBounds(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())

        drawable.draw(canvas!!)
    }
}

