/*
 * Project: mybudget2-mobile-android
 * File: MovementsActivityTest.kt
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

package it.italiancoders.mybudget.activity.movements

import androidx.test.rule.ActivityTestRule
import com.google.android.material.bottomsheet.BottomSheetBehavior
import it.italiancoders.mybudget.activity.BaseActivityTest
import it.italiancoders.mybudget.activity.movements.edit.MovementPageObject
import it.italiancoders.mybudget.rest.models.Movement
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

/**
 * @author fattazzo
 *         <p/>
 *         date: 10/09/19
 */
class MovementsActivityTest : BaseActivityTest() {

    @Rule
    @JvmField
    var rule: ActivityTestRule<MovementsActivity> =
        ActivityTestRule(MovementsActivity::class.java, true, false)

    private val movementsPageObject = MovementsPageObject()
    private val movementPageObject = MovementPageObject()

    @Test
    fun initialState() {

        rule.launchActivity(null)

        movementsPageObject.checkNewMovementFabVisible()
        movementsPageObject.checkMovementsListChildCount(0)
        assertThat(
            rule.activity.mBottomSheetBehavior?.state,
            `is`(BottomSheetBehavior.STATE_EXPANDED)
        )
    }

    @Test
    fun createNewMovementFromFab() {

        rule.launchActivity(null)

        movementsPageObject.checkActivityMovementsVisible()

        movementsPageObject.clickNewMovementFab()

        movementsPageObject.checkActivityNewMovementVisible()

        movementPageObject.checkMovement(Movement())

        movementsPageObject.pressBack()

        movementsPageObject.checkActivityMovementsVisible()
    }

    @Test
    fun createNewMovementFromMenu() {

        rule.launchActivity(null)

        movementsPageObject.checkActivityMovementsVisible()

        movementsPageObject.swipeUpMovements()

        movementsPageObject.clickNewMovementFromMenu()

        movementsPageObject.checkNewMovementFabNotVisible()

        movementPageObject.checkMovement(Movement())

        movementsPageObject.pressBack()

        movementsPageObject.checkActivityMovementsVisible()
    }

    @Test
    fun editMovement() {

    }

    @Test
    fun deleteMovement() {

    }
}