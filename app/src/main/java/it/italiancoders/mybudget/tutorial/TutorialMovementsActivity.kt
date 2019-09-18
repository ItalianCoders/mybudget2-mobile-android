/*
 * Project: mybudget2-mobile-android
 * File: TutorialMovementsActivity.kt
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

import android.widget.TextView
import com.takusemba.spotlight.shape.Circle
import com.takusemba.spotlight.shape.RoundedRectangle
import com.takusemba.spotlight.target.SimpleTarget
import com.takusemba.spotlight.target.Target
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.BaseActivity
import it.italiancoders.mybudget.databinding.ActivityMovementsBinding
import it.italiancoders.mybudget.tutorial.shape.ArrowRightShape

/**
 * @author fattazzo
 *         <p/>
 *         date: 27/08/19
 */
class TutorialMovementsActivity(activity: BaseActivity<ActivityMovementsBinding>) :
    AbstractTutorialActivity<ActivityMovementsBinding>(activity) {

    companion object {
        const val KEY = "movements_activity_show"
    }

    override fun getTutorialPreferenceKey(): String = KEY

    override fun getTargets(): ArrayList<Target> {
        return arrayListOf(
            createParamsTarget(),
            createEditMovementTarget(),
            createNewMovementsTarget(),
            createSwipeDeleteMovementTarget()
        )
    }

    private fun createParamsTarget(): Target {
        val view = activity.binding.searchMovementsView.findViewById<TextView>(R.id.search_button)
        val location = getCenterLocation(view)
        val pointX = location[0]
        val pointY = location[1]
        val radius = 200f

        return SimpleTarget.Builder(activity)
            .setPoint(pointX, pointY)
            .setShape(
                RoundedRectangle(
                    view.layout.height.toFloat() + 100,
                    view.layout.width.toFloat() + 120,
                    15f
                )
            )
            .setTitle(activity.getString(R.string.tutorial_movements_params_title))
            .setDescription(activity.getString(R.string.tutorial_movements_params_description))
            .setOverlayPoint(100f, pointY - radius - 300f)
            .build()
    }

    private fun createEditMovementTarget(): Target {
        val view = activity.binding.contentLayout
        val location = IntArray(2)
        view.getLocationInWindow(location)
        val locationCenter = getCenterLocation(view)
        val pointX = locationCenter[0]
        val pointY = location[1].toFloat()
        val radius = 150f

        return SimpleTarget.Builder(activity)
            .setPoint(pointX, pointY + 200)
            .setShape(Circle(radius))
            .setTitle(activity.getString(R.string.tutorial_movements_edit_title))
            .setDescription(activity.getString(R.string.tutorial_movements_edit_description))
            .setOverlayPoint(50f, pointY + radius + 300f)
            .build()
    }

    private fun createNewMovementsTarget(): Target {
        val view = activity.binding.newMovementFab
        val location = getCenterLocation(view)
        val pointX = location[0]
        val pointY = location[1]
        val oneRadius = 100f

        return SimpleTarget.Builder(activity)
            .setPoint(pointX, pointY)
            .setShape(Circle(oneRadius))
            .setTitle(activity.getString(R.string.tutorial_main_add_movements_title))
            .setDescription(activity.getString(R.string.tutorial_main_add_movements_description))
            .setOverlayPoint(100f, pointY + oneRadius + 100f)
            .build()
    }

    private fun createSwipeDeleteMovementTarget(): Target {
        val view = activity.binding.contentLayout
        val location = IntArray(2)
        view.getLocationInWindow(location)
        val locationCenter = getCenterLocation(view)
        val pointX = locationCenter[0]
        val pointY = location[1].toFloat()
        val radius = 150f

        return SimpleTarget.Builder(activity)
            .setPoint(pointX, pointY + 200)
            .setShape(ArrowRightShape(activity, radius))
            .setTitle(activity.getString(R.string.tutorial_movements_swipe_delete_title))
            .setDescription(activity.getString(R.string.tutorial_movements_swipe_delete_description))
            .setOverlayPoint(50f, pointY + radius + 300f)
            .build()
    }
}