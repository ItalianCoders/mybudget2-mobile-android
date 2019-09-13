/*
 * Project: mybudget2-mobile-android
 * File: RegistrationUserInfoPageObject.kt
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

package it.italiancoders.mybudget.activity.registration

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.afollestad.materialdialogs.internal.main.DialogLayout
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.ViewActions.setTextInTextView
import org.hamcrest.CoreMatchers

/**
 * @author fattazzo
 *         <p/>
 *         date: 11/09/19
 */
class RegistrationUserInfoPageObject {

    fun checkSignUpButtonVisible() {
        onView(withId(R.id.sign_up_button)).check(matches(isDisplayed()))
    }

    fun clickSignUpButtonVisible() {
        onView(withId(R.id.sign_up_button)).perform(click())
    }

    fun checkSignUpButtonIsClickable(clickable: Boolean) {

        if (clickable) {
            onView(withId(R.id.sign_up_button)).check(matches(isClickable()))
            onView(
                CoreMatchers.allOf(
                    withId(R.id.sign_up_button),
                    hasBackground(R.drawable.box_round_green),
                    isDisplayed()
                )
            )
        } else {
            onView(withId(R.id.sign_up_button)).check(matches(CoreMatchers.not(isClickable())))
            onView(
                CoreMatchers.allOf(
                    withId(R.id.sign_up_button),
                    hasBackground(R.drawable.box_round_gray),
                    isDisplayed()
                )
            )
        }
    }

    fun checkFirstNameText(text: String) {
        onView(withId(R.id.firstname_ET)).check(matches(withText(text)))
    }

    fun checkLastNameText(text: String) {
        onView(withId(R.id.lastname_ET)).check(matches(withText(text)))
    }

    fun checkUserNameText(text: String) {
        onView(withId(R.id.username_ET)).check(matches(withText(text)))
    }

    fun checkPasswordText(text: String) {
        onView(withId(R.id.password_ET)).check(matches(withText(text)))
    }

    fun checkPasswordConfirmText(text: String) {
        onView(withId(R.id.password_confirm_ET)).check(matches(withText(text)))
    }

    fun checkEmailText(text: String) {
        onView(withId(R.id.email_ET)).check(matches(withText(text)))
    }

    fun setFirstNameText(text: String) {
        onView(withId(R.id.firstname_ET)).perform(setTextInTextView(text))
    }

    fun setLastNameText(text: String) {
        onView(withId(R.id.lastname_ET)).perform(setTextInTextView(text))
    }

    fun setUserNameText(text: String) {
        onView(withId(R.id.username_ET)).perform(setTextInTextView(text))
    }

    fun setPasswordText(text: String) {
        onView(withId(R.id.password_ET)).perform(setTextInTextView(text))
    }

    fun setPasswordConfirmText(text: String) {
        onView(withId(R.id.password_confirm_ET)).perform(setTextInTextView(text))
    }

    fun setEmailText(text: String) {
        onView(withId(R.id.email_ET)).perform(setTextInTextView(text))
    }

    fun checkFirstNameHasErrorText(context: Context) {
        onView(withId(R.id.firstname_ET)).check(matches(hasErrorText(context.resources.getString(R.string.error_firstname_invalid))))
    }

    fun checkLastNameHasErrorText(context: Context) {
        onView(withId(R.id.lastname_ET)).check(matches(hasErrorText(context.resources.getString(R.string.error_lastname_invalid))))
    }

    fun checkUserNameHasErrorText(context: Context) {
        onView(withId(R.id.username_ET)).check(matches(hasErrorText(context.resources.getString(R.string.error_username_invalid))))
    }

    fun checkPasswordHasErrorText(context: Context) {
        onView(withId(R.id.password_ET)).check(matches(hasErrorText(context.resources.getString(R.string.error_password_invalid))))
    }

    fun checkPasswordConfirmHasErrorText(context: Context) {
        onView(withId(R.id.password_confirm_ET)).check(matches(hasErrorText(context.resources.getString(R.string.error_password_invalid))))
    }

    fun checkEmailHasErrorText(context: Context) {
        onView(withId(R.id.email_ET)).check(matches(hasErrorText(context.resources.getString(R.string.error_email_invalid))))
    }

    fun checkRegistrationCreatedView() {
        onView(CoreMatchers.instanceOf(DialogLayout::class.java)).check(matches(isDisplayed()))
        onView(withId(R.id.md_text_title)).check(matches(withText(R.string.registration_created_title)))
    }

    fun checkRegistrationFailedView() {
        onView(CoreMatchers.instanceOf(DialogLayout::class.java)).check(matches(isDisplayed()))
        onView(withId(R.id.md_text_title)).check(matches(withText(R.string.registration_created_failure_title)))
    }
}