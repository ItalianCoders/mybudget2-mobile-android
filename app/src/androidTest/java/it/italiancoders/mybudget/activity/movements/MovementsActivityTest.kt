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

import android.os.SystemClock
import androidx.test.rule.ActivityTestRule
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nhaarman.mockitokotlin2.doReturn
import it.italiancoders.mybudget.activity.BaseActivityTest
import it.italiancoders.mybudget.activity.movements.edit.MovementPageObject
import it.italiancoders.mybudget.activity.movements.list.ListMovementsPageObject
import it.italiancoders.mybudget.activity.movements.search.SearchMovementPageObject
import it.italiancoders.mybudget.mocks.config.MovementsConfig
import it.italiancoders.mybudget.mocks.data.MovementsMockData
import it.italiancoders.mybudget.rest.models.Movement
import it.italiancoders.mybudget.rest.models.MovementListPage
import it.italiancoders.mybudget.tutorial.TutorialMovementsActivity
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

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
    private val searchMovementPageObject = SearchMovementPageObject()
    private val listMovementsPageObject = ListMovementsPageObject()

    override fun getActivityTutorialKey(): String? = TutorialMovementsActivity.KEY

    override fun isTutorialAlreadyShow(): Boolean = true

    @Test
    fun initialState() {

        rule.launchActivity(null)

        movementsPageObject.checkNewMovementFabVisible()
        listMovementsPageObject.checkMovementsListChildCount(0)
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

        MovementsMockData.mock2019_08(movementsManager)
        MovementsMockData.mock_id_00014(movementsManager)

        rule.launchActivity(null)

        searchMovementPageObject.setTextInDayTextView("")
        searchMovementPageObject.setTextInMonthTextView("8")
        searchMovementPageObject.setTextInYearTextView("2019")
        searchMovementPageObject.clickSearchButton()

        listMovementsPageObject.checkMovementsListChildCount(14)

        val movementPage = MovementsMockData.fromJsonFile<MovementListPage>(
            MovementsConfig.DATA_2019_08_OK,
            MovementListPage::class
        )
        val editMovement14Position = movementPage!!.contents.indexOfFirst { it.id!! == 14L }

        listMovementsPageObject.clickMovementItem(editMovement14Position)

        movementPageObject.checkMovement(movementPage.contents[editMovement14Position])
    }

    @Test
    fun deleteMovement() {

        MovementsMockData.mock2019_08(movementsManager)
        MovementsMockData.mock_id_00014(movementsManager)

        rule.launchActivity(null)

        searchMovementPageObject.setTextInDayTextView("")
        searchMovementPageObject.setTextInMonthTextView("8")
        searchMovementPageObject.setTextInYearTextView("2019")
        searchMovementPageObject.clickSearchButton()

        listMovementsPageObject.checkMovementsListChildCount(14)

        val movementPage = MovementsMockData.fromJsonFile<MovementListPage>(
            MovementsConfig.DATA_2019_08_OK,
            MovementListPage::class
        )
        val editMovement14Position = movementPage!!.contents.indexOfFirst { it.id!! == 14L }

        listMovementsPageObject.clickMovementItem(editMovement14Position)

        movementPageObject.checkMovement(movementPage.contents[editMovement14Position])

        val mov14 = MovementsMockData.fromJsonFile<Movement>(MovementsConfig.DATA_ID_00014_OK,Movement::class)!!

        Mockito.`when`(movementsManager.delete(mov14)).doReturn(true)
        MovementsMockData.mock2019_08_no_id_00014(movementsManager)
        movementPageObject.clickDeleteButton()

        listMovementsPageObject.checkMovementsListChildCount(13)
    }

    @Test
    fun deleteMovementFromSwipe() {

        MovementsMockData.mock2019_08(movementsManager)
        MovementsMockData.mock_id_00014(movementsManager)

        rule.launchActivity(null)

        searchMovementPageObject.setTextInDayTextView("")
        searchMovementPageObject.setTextInMonthTextView("8")
        searchMovementPageObject.setTextInYearTextView("2019")
        searchMovementPageObject.clickSearchButton()

        listMovementsPageObject.checkMovementsListChildCount(14)

        val movementPage = MovementsMockData.fromJsonFile<MovementListPage>(
            MovementsConfig.DATA_2019_08_OK,
            MovementListPage::class
        )
        val editMovement14Position = movementPage!!.contents.indexOfFirst { it.id!! == 14L }

        val mov14 = MovementsMockData.fromJsonFile<Movement>(MovementsConfig.DATA_ID_00014_OK,Movement::class)!!

        // Error on delete
        Mockito.`when`(movementsManager.delete(mov14)).doReturn(false)
        listMovementsPageObject.swipeRightMovementItem(editMovement14Position)

        listMovementsPageObject.checkMovementDeletedSnackBarVisible()

        SystemClock.sleep(5000)
        listMovementsPageObject.checkMovementsListChildCount(14)

        // Delete ok
        Mockito.`when`(movementsManager.delete(mov14)).doReturn(true)
        listMovementsPageObject.swipeRightMovementItem(editMovement14Position)

        listMovementsPageObject.checkMovementDeletedSnackBarVisible()

        SystemClock.sleep(5000)
        listMovementsPageObject.checkMovementsListChildCount(13)
    }
}