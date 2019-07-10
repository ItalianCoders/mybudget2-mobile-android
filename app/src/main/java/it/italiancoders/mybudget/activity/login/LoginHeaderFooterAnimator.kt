/*
 * Project: mybudget2-mobile-android
 * File: LoginHeaderFooterAnimator.kt
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

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout

/**
 * @author fattazzo
 *         <p/>
 *         date: 09/07/19
 */
object LoginHeaderFooterAnimator {

    /**
     * Esegue l'animazione di slide variando i margini ( top in caso di slide down, bottom in caso di slide up ) della view.
     * Il valore del margine viene impostato a inizio animazione pari all'altezza della view in negativo per poi essere
     * diminuito ad ogni step successivo fino a portarlo al valore fine pari a 0.
     */
    fun start(view: ViewGroup, slideDown: Boolean = true) {

        val viewHeight = view.layoutParams.height

        val slideAnimator = ValueAnimator
            .ofInt(0, viewHeight)
            .setDuration(1000)


        slideAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            val marginParams = ViewGroup.MarginLayoutParams(view.layoutParams)

            val actualMargin = viewHeight * -1 + value
            if (slideDown) {
                marginParams.setMargins(0, actualMargin, 0, 0)
            } else {
                marginParams.setMargins(0, 0, 0, actualMargin)
            }
            val layoutParams = LinearLayout.LayoutParams(marginParams)
            view.layoutParams = layoutParams
            view.requestLayout()
        }

        val set = AnimatorSet()

        set.play(slideAnimator)

        set.interpolator = AccelerateDecelerateInterpolator()

        set.start()
    }
}