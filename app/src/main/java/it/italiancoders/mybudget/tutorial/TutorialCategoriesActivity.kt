/*
 * Project: mybudget2-mobile-android
 * File: TutorialCategoriesActivity.kt
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

import com.takusemba.spotlight.shape.Circle
import com.takusemba.spotlight.target.SimpleTarget
import com.takusemba.spotlight.target.Target
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.BaseActivity
import it.italiancoders.mybudget.databinding.ActivityCategoriesBinding
import it.italiancoders.mybudget.tutorial.shape.ArrowDownShape

/**
 * @author fattazzo
 *         <p/>
 *         date: 01/08/19
 */
class TutorialCategoriesActivity(activity: BaseActivity<ActivityCategoriesBinding>) :
    AbstractTutorialActivity<ActivityCategoriesBinding>(activity) {

    override fun getTutorialPreferenceKey(): String = "categories_activity_show"

    override fun getTargets(): ArrayList<Target> {
        return arrayListOf(createNewTarget(), createRefreshTarget(), createEditDeleteTarget())
    }

    private fun createNewTarget(): Target {
        val view = activity.binding.fab
        val location = getCenterLocation(view)
        val pointX = location[0]
        val pointY = location[1]
        val radius = 100f

        return SimpleTarget.Builder(activity)
            .setPoint(pointX, pointY)
            .setShape(Circle(radius))
            .setTitle(activity.getString(R.string.tutorial_categories_new_title))
            .setDescription(activity.getString(R.string.tutorial_categories_new_description))
            .setOverlayPoint(50f, pointY + radius + 100f)
            .build()
    }

    private fun createRefreshTarget(): Target {
        val view = activity.binding.contentLayout.root
        val location = IntArray(2)
        view.getLocationInWindow(location)
        val locationCenter = getCenterLocation(view)
        val pointX = locationCenter[0]
        val pointY = location[1].toFloat()
        val radius = 200f

        return SimpleTarget.Builder(activity)
            .setPoint(pointX, pointY + 100)
            .setShape(ArrowDownShape(activity, radius))
            .setTitle(activity.getString(R.string.tutorial_categories_refresh_title))
            .setDescription(activity.getString(R.string.tutorial_categories_refresh_description))
            .setOverlayPoint(50f, pointY + radius + 100f)
            .build()
    }

    private fun createEditDeleteTarget(): Target {
        val view = activity.binding.contentLayout.root
        val location = IntArray(2)
        view.getLocationInWindow(location)
        val pointY = location[1].toFloat()
        val radius = 100f

        return SimpleTarget.Builder(activity)
            .setPoint(100f, pointY + 100)
            .setShape(Circle(radius))
            .setTitle(activity.getString(R.string.tutorial_categories_update_delete_title))
            .setDescription(activity.getString(R.string.tutorial_categories_update_delete_description))
            .setOverlayPoint(50f, pointY + radius + 100f)
            .build()
    }
}