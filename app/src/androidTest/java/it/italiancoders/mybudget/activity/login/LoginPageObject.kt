/*
 * Project: mybudget2-mobile-android
 * File: LoginPageObject.kt
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

import android.app.Instrumentation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.ActivityResultMatchers.hasResultCode
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import com.afollestad.materialdialogs.internal.main.DialogLayout
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.ViewActions.setTextInTextView
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert

/**
 * @author fattazzo
 *         <p/>
 *         date: 11/09/19
 */
class LoginPageObject {

    fun checkUserNameText(text: String) {
        onView(withId(R.id.usernameET)).check(matches(withText(text)))
    }

    fun checkPasswordText(text: String) {
        onView(withId(R.id.passwordET)).check(matches(withText(text)))
    }

    fun checkPolicyCheckBocChecked(checked: Boolean) {

        if(checked) {
            onView(withId(R.id.checkBox)).check(matches(isChecked()))
            onView(allOf(withId(R.id.privacyPolicyContainer),hasBackground(R.drawable.box_round_green), isDisplayed()))
        } else {
            onView(withId(R.id.checkBox)).check(matches(isNotChecked()))
            onView(allOf(withId(R.id.privacyPolicyContainer),hasBackground(R.drawable.box_round_empty), isDisplayed()))
        }
    }

    fun checkLoginButtonIsClickable(clickable: Boolean) {

        if (clickable) {
            onView(withId(R.id.signInButton)).check(matches(isClickable()))
            onView(allOf(withId(R.id.signInButton),hasBackground(R.drawable.box_round_green), isDisplayed()))
        } else {
            onView(withId(R.id.signInButton)).check(matches(not(isClickable())))
            onView(allOf(withId(R.id.signInButton),hasBackground(R.drawable.box_round_gray), isDisplayed()))
        }
    }

    fun setUserNameText(text: String) {
        onView(withId(R.id.usernameET)).perform(setTextInTextView(text))
    }

    fun setPasswordText(text: String) {
        onView(withId(R.id.passwordET)).perform(setTextInTextView(text))
    }

    fun clickPrivacycheckBox() {
        onView(withId(R.id.checkBox)).perform(click())
    }

    fun clickLoginButton() {
        onView(withId(R.id.signInButton)).perform(click())
    }

    fun checkLoginErrorViewDisplayed(activity: LoginActivity) {
        onView(withText(R.string.login_error)).inRoot(withDecorView(not(activity.window.decorView))).check(matches(isDisplayed()))
    }

    fun checkUsetNotActivatedView() {
        onView(CoreMatchers.instanceOf(DialogLayout::class.java)).check(matches(isDisplayed()))
        onView(withId(R.id.md_text_title)).check(matches(withText(R.string.login_user_not_activated_title)))
    }

    fun checkActivityResult(activityResult: Instrumentation.ActivityResult, expectedResult: Int) {
        MatcherAssert.assertThat(activityResult,hasResultCode(expectedResult))
    }

    fun clickNewUserRegistration() {
        onView(withId(R.id.registration_create_text_view)).perform(click())
    }

    fun clickResendActivationEmail() {
        onView(withId(R.id.registration_resend_text_view)).perform(click())
    }

    fun checkResendActivationDialogVisible() {
        onView(CoreMatchers.instanceOf(DialogLayout::class.java)).check(matches(isDisplayed()))
        onView(withId(R.id.md_text_title)).check(matches(withText(R.string.login_username_hint)))
    }
}