/*
 * Project: mybudget2-mobile-android
 * File: ActivationActivityTest.kt
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

package it.italiancoders.mybudget.activity.activation

import android.content.Intent
import android.os.SystemClock
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import it.italiancoders.mybudget.activity.BaseActivityTest
import org.junit.Rule
import org.junit.Test


/**
 * @author fattazzo
 *         <p/>
 *         date: 03/10/19
 */
class ActivationActivityTest : BaseActivityTest() {

    @Rule
    @JvmField
    var rule: ActivityTestRule<ActivationActivity> =
        ActivityTestRule(ActivationActivity::class.java, true, false)

    private val activationPageObject = ActivationPageObject()

    override fun getActivityTutorialKey(): String? = null

    override fun isTutorialAlreadyShow(): Boolean = true

    @Test
    fun initialState() {

        rule.launchActivity(null)

        activationPageObject.checkUserNameText("")
        activationPageObject.checkCodiceText("")
        activationPageObject.checkConfirmButtonIsClickable(false)
    }

    @Test
    fun initialStateWithExtra() {

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(context, ActivationActivity::class.java)
        intent.putExtra(ActivationActivity.USERNAME_EXTRA, "testUsername")

        rule.launchActivity(intent)

        activationPageObject.checkUserNameText("testUsername")
        activationPageObject.checkCodiceText("")
        activationPageObject.checkConfirmButtonIsClickable(false)
    }

    @Test
    fun confirmationAvailability() {

        rule.launchActivity(null)

        activationPageObject.checkConfirmButtonIsClickable(false)

        // Only username
        activationPageObject.setUserNameText("test")
        SystemClock.sleep(200)
        activationPageObject.checkConfirmButtonIsClickable(false)

        // wrong code (<5)
        activationPageObject.setCodiceText("11")
        SystemClock.sleep(200)
        activationPageObject.checkConfirmButtonIsClickable(false)

        // Ok code
        activationPageObject.setCodiceText("11111")
        SystemClock.sleep(200)
        activationPageObject.checkConfirmButtonIsClickable(true)

        // Wrong code (>5)
        activationPageObject.setCodiceText("111111111")
        SystemClock.sleep(200)
        activationPageObject.checkConfirmButtonIsClickable(false)

        // Ok code and no username
        activationPageObject.setUserNameText("")
        activationPageObject.setCodiceText("12345")
        SystemClock.sleep(200)
        activationPageObject.checkConfirmButtonIsClickable(false)

    }
}