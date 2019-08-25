/*
 * Project: mybudget2-mobile-android
 * File: AbstractTutorialActivity.kt
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

package it.italiancoders.mybudget.tutorial

import android.content.Context
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.databinding.ViewDataBinding
import com.takusemba.spotlight.OnSpotlightStateChangedListener
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.target.Target
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.BaseActivity

/**
 * @author fattazzo
 *         <p/>
 *         date: 30/07/19
 */
abstract class AbstractTutorialActivity<T : ViewDataBinding>(protected val activity: BaseActivity<T>) {

    /**
     * Key used to manage preferences
     */
    abstract fun getTutorailPreferenceKey(): String

    /**
     * Check if the tutorial is never show
     */
    open fun isNeverShow(): Boolean {
        val alreadyShow = activity.getSharedPreferences(TUTORIAL_PREF_FILE, Context.MODE_PRIVATE)
            .getBoolean(getTutorailPreferenceKey(), false)

        return !alreadyShow
    }

    /**
     * Start the tutorial
     */
    fun start() {
        Spotlight.with(activity)
            .setOverlayColor(R.color.black_70)
            .setDuration(10L)
            .setAnimation(DecelerateInterpolator(2f))
            .setTargets(getTargets())
            .setClosedOnTouchedOutside(true)
            .setOnSpotlightStateListener(object : OnSpotlightStateChangedListener {
                override fun onStarted() {
                    saveTutorialAsShow()
                }

                override fun onEnded() {}
            })
            .start()
    }

    /**
     * Get all targets for the tutorial
     */
    abstract fun getTargets(): ArrayList<Target>

    private fun saveTutorialAsShow() {
        activity.getSharedPreferences(TUTORIAL_PREF_FILE, Context.MODE_PRIVATE)
            .edit().putBoolean(getTutorailPreferenceKey(), true).apply()
    }

    fun getCenterLocation(view: View): FloatArray {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        val pointX = location[0] + view.width / 2f
        val pointY = location[1] + view.height / 2f
        return floatArrayOf(pointX, pointY)
    }

    companion object {

        const val TUTORIAL_PREF_FILE = "tutorial_prefs"
    }
}