/*
 * Project: mybudget2-mobile-android
 * File: RegistrationUserInfoActivityTest.kt
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

import androidx.test.rule.ActivityTestRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import it.italiancoders.mybudget.activity.BaseActivityTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`

/**
 * @author fattazzo
 *         <p/>
 *         date: 12/09/19
 */
class RegistrationUserInfoActivityTest : BaseActivityTest() {

    @Rule
    @JvmField
    var rule: ActivityTestRule<RegistrationUserInfoActivity> =
        ActivityTestRule(RegistrationUserInfoActivity::class.java, true, false)

    private val registrationUserInfoPageObject =
        RegistrationUserInfoPageObject()

    override fun getActivityTutorialKey(): String? = null

    override fun isTutorialAlreadyShow(): Boolean = true

    @Test
    fun initialState() {

        rule.launchActivity(null)

        registrationUserInfoPageObject.checkFirstNameText("")
        registrationUserInfoPageObject.checkLastNameText("")
        registrationUserInfoPageObject.checkUserNameText("")
        registrationUserInfoPageObject.checkPasswordText("")
        registrationUserInfoPageObject.checkPasswordConfirmText("")
        registrationUserInfoPageObject.checkEmailText("")

        registrationUserInfoPageObject.checkSignUpButtonIsClickable(false)
    }

    @Test
    fun checkValidationErrorsVisible() {

        rule.launchActivity(null)

        registrationUserInfoPageObject.setFirstNameText("a")
        registrationUserInfoPageObject.setLastNameText("a")
        registrationUserInfoPageObject.setUserNameText("a")
        registrationUserInfoPageObject.setPasswordText("a")
        registrationUserInfoPageObject.setPasswordConfirmText("a")
        registrationUserInfoPageObject.setEmailText("a")

        registrationUserInfoPageObject.checkFirstNameHasErrorText(rule.activity)
        registrationUserInfoPageObject.checkLastNameHasErrorText(rule.activity)
        registrationUserInfoPageObject.checkUserNameHasErrorText(rule.activity)
        registrationUserInfoPageObject.checkPasswordHasErrorText(rule.activity)
        registrationUserInfoPageObject.checkPasswordConfirmHasErrorText(rule.activity)
        registrationUserInfoPageObject.checkEmailHasErrorText(rule.activity)
    }

    @Test
    fun createSuccessfullyRegistration() {

        rule.launchActivity(null)
        val viewModel = rule.activity.binding.model!!

        registrationUserInfoPageObject.setFirstNameText("userFirstName")
        registrationUserInfoPageObject.setLastNameText("userLastName")
        registrationUserInfoPageObject.setUserNameText("userUserName")
        registrationUserInfoPageObject.setPasswordText("userPassword1")
        registrationUserInfoPageObject.setPasswordConfirmText("userPassword1")
        registrationUserInfoPageObject.setEmailText("user@email.com")

        registrationUserInfoPageObject.checkSignUpButtonIsClickable(true)

        assertThat(viewModel.firstname.value, `is`("userFirstName"))
        assertThat(viewModel.lastname.value, `is`("userLastName"))
        assertThat(viewModel.username.value, `is`("userUserName"))
        assertThat(viewModel.password.value, `is`("userPassword1"))
        assertThat(viewModel.passwordConfirm.value, `is`("userPassword1"))
        assertThat(viewModel.email.value, `is`("user@email.com"))
        assertThat(viewModel.dataValid.value, `is`(true))

        `when`(registrationUserInfoManager.create(any())).doReturn(true)

        registrationUserInfoPageObject.clickSignUpButtonVisible()

        registrationUserInfoPageObject.checkRegistrationCreatedView()
    }

    @Test
    fun createFailureRegistration() {

        rule.launchActivity(null)
        val viewModel = rule.activity.binding.model!!

        registrationUserInfoPageObject.setFirstNameText("userFirstName")
        registrationUserInfoPageObject.setLastNameText("userLastName")
        registrationUserInfoPageObject.setUserNameText("userUserName")
        registrationUserInfoPageObject.setPasswordText("userPassword1")
        registrationUserInfoPageObject.setPasswordConfirmText("userPassword1")
        registrationUserInfoPageObject.setEmailText("user@email.com")

        registrationUserInfoPageObject.checkSignUpButtonIsClickable(true)

        assertThat(viewModel.firstname.value, `is`("userFirstName"))
        assertThat(viewModel.lastname.value, `is`("userLastName"))
        assertThat(viewModel.username.value, `is`("userUserName"))
        assertThat(viewModel.password.value, `is`("userPassword1"))
        assertThat(viewModel.passwordConfirm.value, `is`("userPassword1"))
        assertThat(viewModel.email.value, `is`("user@email.com"))
        assertThat(viewModel.dataValid.value, `is`(true))

        `when`(registrationUserInfoManager.create(any())).doReturn(false)

        registrationUserInfoPageObject.clickSignUpButtonVisible()

        registrationUserInfoPageObject.checkRegistrationFailedView()
    }
}