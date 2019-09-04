/*
 * Project: mybudget2-mobile-android
 * File: TutorialMainActivity.kt
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
import it.italiancoders.mybudget.databinding.ActivityMainBinding
import it.italiancoders.mybudget.tutorial.shape.ArrowRightShape


/**
 * @author fattazzo
 *         <p/>
 *         date: 30/07/19
 */
class TutorialMainActivity(activity: BaseActivity<ActivityMainBinding>) :
    AbstractTutorialActivity<ActivityMainBinding>(activity) {

    override fun getTutorialPreferenceKey(): String = "main_activity_show"

    override fun getTargets(): ArrayList<Target> {
        return arrayListOf(
            createPeriodoTarget(),
            createPeriodoTypeTarget(),
            createChartsTarget(),
            createAddMovementsTarget(),
            createAddScheduledMovementsTarget(),
            createLastMovementsTarget()
        )
    }

    private fun createPeriodoTypeTarget(): Target {
        val view = activity.binding.contentMain.changePeriodTypeFab
        val location = getCenterLocation(view)
        val pointX = location[0]
        val pointY = location[1]
        val oneRadius = 100f

        return SimpleTarget.Builder(activity)
            .setPoint(pointX, pointY)
            .setShape(Circle(oneRadius))
            .setTitle(activity.getString(R.string.tutorial_main_period_type_title))
            .setDescription(activity.getString(R.string.tutorial_main_period_type_description))
            .setOverlayPoint(100f, pointY - oneRadius * 2 + 300f)
            .build()
    }

    private fun createPeriodoTarget(): Target {
        val view = activity.binding.contentMain.periodoTextView
        val location = getCenterLocation(view)
        val pointX = location[0]
        val pointY = location[1]
        val radius = 200f

        return SimpleTarget.Builder(activity)
            .setPoint(pointX, pointY)
            .setShape(
                RoundedRectangle(
                    view.layout.height.toFloat() + 70,
                    view.layout.width.toFloat() + 70,
                    5f
                )
            )
            .setTitle(activity.getString(R.string.tutorial_main_period_title))
            .setDescription(activity.getString(R.string.tutorial_main_period_description))
            .setOverlayPoint(100f, pointY + radius + 100f)
            .build()
    }

    private fun createChartsTarget(): Target {
        val view = activity.binding.contentMain.chartsRecyclerView
        val locationCenter = getCenterLocation(view)
        val pointX = locationCenter[0]
        val pointY = locationCenter[1]
        val radius = 200f

        return SimpleTarget.Builder(activity)
            .setPoint(pointX, pointY + 100)
            .setShape(ArrowRightShape(activity, radius))
            .setTitle(activity.getString(R.string.tutorial_main_charts_title))
            .setDescription(activity.getString(R.string.tutorial_main_charts_description))
            .setOverlayPoint(50f, pointY + radius + 100f)
            .build()
    }

    private fun createAddMovementsTarget(): Target {
        val view = activity.binding.contentMain.addMovementButton
        val location = getCenterLocation(view)
        val pointX = location[0]
        val pointY = location[1]
        val oneRadius = 100f

        return SimpleTarget.Builder(activity)
            .setPoint(pointX, pointY)
            .setShape(Circle(oneRadius))
            .setTitle(activity.getString(R.string.tutorial_main_add_movements_title))
            .setDescription(activity.getString(R.string.tutorial_main_add_movements_description))
            .setOverlayPoint(100f, pointY - oneRadius * 2 - 300f)
            .build()
    }

    private fun createAddScheduledMovementsTarget(): Target {
        val view = activity.binding.contentMain.addScheduledMovementButton
        val location = getCenterLocation(view)
        val pointX = location[0]
        val pointY = location[1]
        val oneRadius = 100f

        return SimpleTarget.Builder(activity)
            .setPoint(pointX, pointY)
            .setShape(Circle(oneRadius))
            .setTitle(activity.getString(R.string.tutorial_main_add_scheduled_movements_title))
            .setDescription(activity.getString(R.string.tutorial_main_add_scheduled_movements_description))
            .setOverlayPoint(100f, pointY - oneRadius * 2 - 300f)
            .build()
    }

    private fun createLastMovementsTarget(): Target {
        val view =
            activity.binding.contentMain.lastMovementsView.findViewById<TextView>(R.id.title_view)
        val location = getCenterLocation(view)
        val pointX = location[0]
        val pointY = location[1]
        val oneRadius = 200f

        return SimpleTarget.Builder(activity)
            .setPoint(pointX, pointY)
            .setShape(
                RoundedRectangle(
                    view.layout.height.toFloat() + 70,
                    view.layout.width.toFloat() + 70,
                    5f
                )
            )
            .setTitle(activity.getString(R.string.tutorial_main_last_movements_button_title))
            .setDescription(activity.getString(R.string.tutorial_main_last_movements_button_description))
            .setOverlayPoint(100f, pointY - oneRadius * 2 - 200f)
            .build()
    }
}