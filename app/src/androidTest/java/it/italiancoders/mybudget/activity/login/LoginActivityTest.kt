/*
 * Project: mybudget2-mobile-android
 * File: LoginActivityTest.kt
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

import android.app.Activity
import androidx.test.rule.ActivityTestRule
import it.italiancoders.mybudget.activity.BaseActivityTest
import it.italiancoders.mybudget.mocks.data.SessionMockData
import org.junit.Rule
import org.junit.Test
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 11/09/19
 */
class LoginActivityTest : BaseActivityTest() {

    @Rule
    @JvmField
    var rule: ActivityTestRule<LoginActivity> =
        ActivityTestRule(LoginActivity::class.java, true, false)

    private val loginPageObject = LoginPageObject()

    override fun getActivityTutorialKey(): String? = null

    override fun isTutorialAlreadyShow(): Boolean = true

    @Test
    fun initialState() {

        rule.launchActivity(null)

        loginPageObject.checkUserNameText("")
        loginPageObject.checkPasswordText("")
        loginPageObject.checkPolicyCheckBocChecked(false)
        loginPageObject.checkLoginButtonIsClickable(false)
    }

    @Test
    fun loginInvalid() {

        rule.launchActivity(null)

        loginPageObject.setUserNameText("testUserName")
        loginPageObject.setPasswordText("testPassword")
        loginPageObject.clickPrivacycheckBox()

        SessionMockData.mock_login_invalid_ok(sessionManager,"testUserName","testPassword", Locale.getDefault())

        loginPageObject.clickLoginButton()

        loginPageObject.checkLoginErrorViewDisplayed(rule.activity)
    }

    @Test
    fun loginUserNonActivated() {

        rule.launchActivity(null)

        loginPageObject.setUserNameText("testUserName")
        loginPageObject.setPasswordText("testPassword")
        loginPageObject.clickPrivacycheckBox()

        SessionMockData.mock_login_not_activated_ok(sessionManager,"testUserName","testPassword", Locale.getDefault())

        loginPageObject.clickLoginButton()

        loginPageObject.checkUsetNotActivatedView()
    }

    @Test
    fun loginOk() {

        rule.launchActivity(null)

        loginPageObject.setUserNameText("testUserName")
        loginPageObject.setPasswordText("testPassword")
        loginPageObject.clickPrivacycheckBox()

        SessionMockData.mock_login_ok(sessionManager,"testUserName","testPassword", Locale.getDefault())

        loginPageObject.clickLoginButton()

        loginPageObject.checkActivityResult(rule.activityResult,Activity.RESULT_OK)
    }
}